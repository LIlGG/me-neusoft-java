package com.lixingyong.meneusoft.api.jwc;

import com.aliyun.oss.OSSClient;
import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.MD5Utils;
import com.lixingyong.meneusoft.common.utils.OSSClientUtil;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import org.apache.http.client.methods.HttpHead;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @ClassName JWCUtil
 * @Description TODO    教务处安全接口工具
 * @Author lixingyong
 * @Date 2018/11/29 14:57
 * @Version 1.0
 */

public class JWCUtil extends JWCAbs {
    private static Logger logger = LoggerFactory.getLogger(JWCUtil.class);
    /**
     * @Author lixingyong
     * @Description //TODO 获取教务处的Cookie，当前Cookie针对与每个账号而言
     * @Date 2018/11/29
     * @Param [uid] 当前登录账号的Uid，根据Uid获取对应用户的教务处Cookie
     * @return boolean
     **/
    public static void getJWCCookie(long uid) throws WSExcetpion {
        setCookies(new LinkedList<>());
        HttpEntity request = httpEntity();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.JWCSID), HttpMethod.GET, request, String.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(responseEntity.getHeaders().containsKey("Set-Cookie")){
                if(responseEntity.getHeaders().get("Set-Cookie").size() > 0){
                    if(responseEntity.getHeaders().get("Set-Cookie").get(0).startsWith("ASP")){
                        String[] cookies = responseEntity.getHeaders().get("Set-Cookie").get(0).split(";");
                        String ASPCookie = cookies[0];
                        //将获取到的数据存入redis数据库中并返回
                        redisUtils.set(uid+"ASPCOOKIE",ASPCookie);
                        logger.info("获取教务处Cookie成功");
                        return;
                    }
                }
            }
        }
        throw new WSExcetpion("获取教务处COOKIE失败");
    }


    /***
     * @Author lixingyong
     * @Description //TODO 获取经过处理后的浏览器状态
     * @Date 2018/11/29
     * @Param [uid, studentId, password, center]
     * @return java.util.Map<java.lang.String                                                               ,                                                               java.lang.Object>
     **/
    public static void getJWCInfo(long uid) throws WSExcetpion {

        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey(uid +"ASPCOOKIE")){
            throw new WSExcetpion("redis中缺少ASPCOOKIE");
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get(uid+"ASPCOOKIE"));
        setCookies(cookiesList);
        HttpEntity request = httpEntity();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.LOGIN),HttpMethod.GET,request,String.class, getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(responseEntity.getBody().indexOf("__VIEWSTATE") > 0){
                String body = responseEntity.getBody();
                String viewState = body.substring(body.indexOf("__VIEWSTATE"),body.lastIndexOf("id=\"pcInfo\""));
                viewState = viewState.substring(viewState.indexOf("value")+7,viewState.lastIndexOf("\""));
                // 保存当前viewState至redis数据库中
                redisUtils.set(uid +"VIEWSTATE",viewState);
                logger.info("获取教务处信息成功");
                return;
            }
        }
        throw new WSExcetpion("获取教务处登录信息失败");
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取验证码
     * @Date 2018/12/7
     * @Param [uid]
     * @return void
     **/
    public static String getValidateCode(long uid) throws WSExcetpion{
        setCookies(new LinkedList<>());
        // 判断redis中是否存着对应的Cookie
        setCookies(uid +"ASPCOOKIE");
        HttpEntity request = httpEntity();
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(URL(JWCAPI.CODE),HttpMethod.GET,request,Resource.class,getMap());
        InputStream in = null;
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            try {
                in = responseEntity.getBody().getInputStream();
                OSSClient oss = OSSClientUtil.getOSSClient();
                OSSClientUtil.uploadObject2OSS(oss,in,codeFolder+uid+suffix,bucketName,folder);
                logger.info("获取验证码并上传至OSS成功");
                String url = OSSClientUtil.getUrl(folder + "/" + codeFolder+uid+suffix);
                return url;
            } catch (IOException e) {
                e.printStackTrace();
            }catch (WSExcetpion s){
                s.getMsg();
            }finally {
                if(null != in){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * @Author lixingyong
     * @Description //TODO 执行教务处登录流程
     * @Date 2019/3/7
     * @Param [uid, account, pw, vCode]
     * @return void
     **/
    public static void jwcStudentLogin(long uid, String account, String pw, String vCode) throws WSExcetpion{
        setCookies(new LinkedList<>());
        // 判断redis中是否存着对应的Cookie
        if( !redisUtils.hasKey(uid+"VIEWSTATE")){
            throw new WSExcetpion("redis中缺少VIEWSTATE");
        }
        setCookies(uid +"ASPCOOKIE");
        HttpHeaders httpHeaders = httpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 获取参数
        String viewState = redisUtils.get(uid +"VIEWSTATE");
        String pcInfo = "";
        MultiValueMap<String,String> param = studentLoginEncryption(viewState, pcInfo, account, pw, vCode);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,httpHeaders);//将参数和header组成一个请求
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(URL(JWCAPI.LOGIN), HttpMethod.POST,request,Resource.class, getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return;
        }
        throw new WSExcetpion("获取教务处登录信息失败");
    }

    /**
     * @Author lixingyong
     * @Description //TODO 教务处登录流程执行前进行账号加密措施
     * @Date 2019/3/7
     * @Param [viewState, pcInfo, typeName, account, pw, vCode]
     * @return java.lang.String
     **/
    private static MultiValueMap<String, String> studentLoginEncryption(String viewState, String pcInfo, String account, String pw, String vCode){
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();//参数放入一个map中，restTemplate不能用hashMap
        param.add("__VIEWSTATE",viewState);
        param.add("pcInfo",pcInfo);
        param.add("typeName", "学生");
        param.add("dsdsdsdsdxcxdfgfg",chkPwd(account,pw));
        param.add("fgfggfdgtyuuyyuuckjg",chkVCode(vCode));
        param.add("Sel_Type","STU");
        param.add("txt_asmcdefsddsd",account);
        param.add("txt_pewerwedsdfsdff","");
        param.add("txt_sdertfgsadscxcadsads","");
        return param;
    }


    /** 教务处学生基本学分情况查询 */
    public static void stuXyjzqk(long uid){
        setCookies(new LinkedList<>());;
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey(uid+"VIEWSTATE")){
            throw new WSExcetpion("redis中缺少VIEWSTATE");
        }
        setCookies(uid +"ASPCOOKIE");
        HttpEntity request = httpEntity();
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(URL(JWCAPI.XYJZQK),HttpMethod.GET,request,Resource.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return;
        }
        throw new WSExcetpion("查询学分失败！");
    }
    private static String chkPwd(String account, String pw) {
        return MD5Utils.getMD5String(account + MD5Utils.getMD5String(pw).substring(0,30).toUpperCase()+"13631").substring(0,30).toUpperCase();
    }

    private static String chkVCode(String code){
        return MD5Utils.getMD5String(MD5Utils.getMD5String(code.toUpperCase()).substring(0,30).toUpperCase()+"13631").substring(0,30).toUpperCase();
    }


}
