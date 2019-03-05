package com.lixingyong.meneusoft.api.jwc;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.internal.OSSUtils;
import com.lixingyong.meneusoft.api.jwc.JWCAPI;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.api.vpn.VPNUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
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
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
@Component
public class JWCUtil {
    private static RestTemplate restTemplate;
    private static RedisUtils redisUtils;
    private static Map<String,Object> map = new HashMap<>();
    /** 阿里云API的bucket名称 */
    private static String bucketName;
    /** 阿里云API的文件夹名称 */
    private static String folder;

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
                        redisUtils.set(Long.toString(uid)+"ASPCOOKIE",ASPCookie);
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
        cookiesList.add(redisUtils.get(Long.toString(uid)+"ASPCOOKIE"));
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
//                String pcInfo = body.substring(body.indexOf("Mozilla"));
//                pcInfo = pcInfo.substring(0,pcInfo.indexOf("\""));
//                System.out.println(pcInfo);
            }
        }
        throw new WSExcetpion("获取教务处登录信息失败");
    }
    
    /**
     * @Author lixingyong
     * @Description //TODO 获取验证码（未完善）
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
                String key = OSSClientUtil.uploadObject2OSS(oss,in,"code"+uid+".jpeg",bucketName,folder);
                logger.info("获取验证码并上传至OSS成功");
                String url = OSSClientUtil.getUrl("code"+uid+".jpeg");
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
    @Autowired(required = true)
    public void setRestTemplate(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Autowired(required = true)
    private void setRedisUtils(RedisUtils redisUtils){
        this.redisUtils = redisUtils;
    }

    @Value("${aliyun.oss.bucketName}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Value("${aliyun.oss.codefolder}")
    public void setFolder(String folder) {
        this.folder = folder+"/";
    }
}
