package com.lixingyong.meneusoft.api.jwc;

import com.aliyun.oss.OSSClient;
import com.lixingyong.meneusoft.api.utils.RestUtils;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.MD5Utils;
import com.lixingyong.meneusoft.common.utils.OSSClientUtil;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JWCUtil
 * @Description TODO    教务处安全接口工具
 * @Author lixingyong
 * @Date 2018/11/29 14:57
 * @Version 1.0
 */

public class JWCUtil {
    private static RestTemplate restTemplate = RestUtils.getRestTemplate();
    private static RedisUtils redisUtils = RestUtils.getRedisUtils();
    private static Map<String,Object> map = new HashMap<>();
    /** 阿里云API的bucket名称 */
    private static String bucketName = RestUtils.getBucketName();
    /** 阿里云API的文件夹名称 */
    private static String folder = RestUtils.getFolder();
    /** 阿里云API的文件前缀 */
    private static String codeFolder = RestUtils.getCodeFolder();
    /** 阿里云API的文件后缀 */
    private static String suffix = RestUtils.getSuffix();

    private static Logger logger = LoggerFactory.getLogger(JWCUtil.class);

    /**
     * @Author lixingyong
     * @Description //TODO 获取教务处的Cookie，当前Cookie针对与每个账号而言
     * @Date 2018/11/29
     * @Param [uid] 当前登录账号的Uid，根据Uid获取对应用户的教务处Cookie
     * @return boolean
     **/
    public static void getJWCCookie(long uid) throws WSExcetpion {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        /** 获取redis保存的cookies */
        if(!redisUtils.hasKey("SVPNCOOKIE") || !redisUtils.hasKey("SID")){
            throw new WSExcetpion("redis中不存在SVPNCOOKIE");
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        map.put("sid",redisUtils.get("SID"));
        ResponseEntity<String> responseEntity = restTemplate.exchange(VPNAPI.PROXY+JWCAPI.JWCSID,HttpMethod.GET,request,String.class,map);
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey("SVPNCOOKIE") || !redisUtils.hasKey(Long.toString(uid)+"ASPCOOKIE") || !redisUtils.hasKey("SID")){
            throw new WSExcetpion("redis中数据不完善");
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        cookiesList.add(redisUtils.get(uid+"ASPCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        map.put("sid",redisUtils.get("SID"));
        ResponseEntity<String> responseEntity = restTemplate.exchange(VPNAPI.PROXY+JWCAPI.LOGIN,HttpMethod.GET,request,String.class,map);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(responseEntity.getBody().indexOf("__VIEWSTATE") > 0){
                String body = responseEntity.getBody();
                String viewState = body.substring(body.indexOf("__VIEWSTATE"),body.lastIndexOf("id=\"pcInfo\""));
                viewState = viewState.substring(viewState.indexOf("value")+7,viewState.lastIndexOf("\""));
                // 保存当前viewState至redis数据库中
                redisUtils.set(Long.toString(uid)+"VIEWSTATE",viewState);
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey("SVPNCOOKIE") || !redisUtils.hasKey(Long.toString(uid)+"ASPCOOKIE") || !redisUtils.hasKey("SID")){
            return null;
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        cookiesList.add(redisUtils.get(Long.toString(uid)+"ASPCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        map.put("sid",redisUtils.get("SID"));
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(VPNAPI.PROXY+JWCAPI.CODE,HttpMethod.GET,request,Resource.class,map);
        InputStream in = null;
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            try {
                in = responseEntity.getBody().getInputStream();
                OSSClient oss = OSSClientUtil.getOSSClient();
                String key = OSSClientUtil.uploadObject2OSS(oss,in,codeFolder+uid+suffix,bucketName,folder);
                logger.info("获取验证码并上传至OSS成功");
                String url = OSSClientUtil.getUrl(codeFolder+uid+suffix);
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey("SVPNCOOKIE") || !redisUtils.hasKey(Long.toString(uid)+"ASPCOOKIE") || !redisUtils.hasKey("SID") || !redisUtils.hasKey(Long.toString(uid)+"VIEWSTATE")){
            throw new WSExcetpion("redis中数据不完善");
        }
        headers.set("Content-Type","application/x-www-form-urlencoded;charset=gb2312");
        headers.set("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        headers.set("Accept-Encoding","gzip, deflate, br");
        headers.set("Accept-Language", "zh-CN,zh;q=0.8");

        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        cookiesList.add(" name=value");
        cookiesList.add(redisUtils.get(Long.toString(uid)+"ASPCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header

        // 获取参数
        String viewState = redisUtils.get(Long.toString(uid)+"VIEWSTATE");
        String pcInfo = "";
        MultiValueMap<String,String> param = studentLoginEncryption(viewState, pcInfo, "学生", account, pw, vCode);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,headers);//将参数和header组成一个请求
        map.put("sid",redisUtils.get("SID"));
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(VPNAPI.PROXY+JWCAPI.LOGIN,HttpMethod.POST,request,Resource.class,map);
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
    private static MultiValueMap<String, String> studentLoginEncryption(String viewState, String pcInfo, String typeName, String account, String pw, String vCode){
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();//参数放入一个map中，restTemplate不能用hashMap
        param.add("__VIEWSTATE",viewState);
        param.add("pcInfo",pcInfo);
        param.add("typeName",typeName);
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey("SVPNCOOKIE") || !redisUtils.hasKey(Long.toString(uid)+"ASPCOOKIE") || !redisUtils.hasKey("SID") || !redisUtils.hasKey(Long.toString(uid)+"VIEWSTATE")){
            throw new WSExcetpion("redis中数据不完善");
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        cookiesList.add(redisUtils.get(Long.toString(uid)+"ASPCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        map.put("sid",redisUtils.get("SID"));
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(VPNAPI.PROXY+JWCAPI.XYJZQK,HttpMethod.GET,request,Resource.class,map);
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
