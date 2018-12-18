package com.lixingyong.meneusoft.api.vpn;

import com.google.gson.Gson;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.JSONUtils;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName WxUtil
 * @Description TODO VPN网络访问工具类
 * @Author lixingyong
 * @Date 2018/11/5 11:51
 * @Version 1.0
 */
@Component
public class  VPNUtil {
    private static String[] userName;
    private static String[] password;
    private static RestTemplate restTemplate;
    private static RedisUtils redisUtils;
    private static Logger logger = LoggerFactory.getLogger(VPNUtil.class);


    public static boolean exeVpnLogin(){
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "8888");
        if(isLogin()){
           return true;
        }
        logger.info("VPN未登录，执行登录流程");
        if(userName.length < 1 || null == userName){
            throw new WSExcetpion("服务器数据错误，请联系管理员!");
        }
        if(password.length < 1 || null == password){
            throw new WSExcetpion("服务器数据错误，请联系管理员!");
        }
        if(password.length != userName.length){
            throw new WSExcetpion("服务器数据错误，请联系管理员!");
        }
        for(int i = 0; i < userName.length; i++){
            synchronized (VPNUtil.class){
                logger.info("使用账号："+userName[i] + "进行登录");
                try {
                    String redir = loginCheck(userName[i], password[i]);
                    getSVpnCookie(redir);
                    getSessionId();
                    logger.info("登录VPN成功");
                    return true;
                }catch (WSExcetpion s) {
                    logger.error("账号"+userName[i] + "登录失败，准备尝试其它账号,错误信息："+s.getMsg());
                }
            }
        }
        logger.error("登录VPN失败，请稍候再试");
        return false;
    }

    /**
     * @Author lixingyong
     * @Description //TODO 验证VPN是否已经登录
     * @Date 2018/12/17
     * @Param []
     * @return boolean
     **/
    public static boolean isLogin(){
        logger.info("检测VPN登录状态");
        //获取Header
        HttpHeaders headers = new HttpHeaders();
        //判断是否存在SVPNSESSION
        /** 获取redis保存的cookies */
        if(!redisUtils.hasKey("SVPNCOOKIE")){
            return false;
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(VPNAPI.ISLOGIN,HttpMethod.GET,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(null != responseEntity.getBody()){
                if(responseEntity.getBody().contains("success")) {
                    logger.info("VPN已登录");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @Author lixingyong
     * @Description //TODO 执行登录，并获取返回信息
     * @Date 2018/11/20
     * @Param []
     * @return java.lang.String
     **/
    private static String loginCheck(String un,String pw) throws WSExcetpion {
        //获取Header
        HttpHeaders headers = new HttpHeaders();
        //设置类型
        headers.setCacheControl("no-store, no-cache, must-revalidate");
        headers.set("Accept","*/*");
        headers.set("If-Modified-Since","Sat, 1 Jan 2000 00:00:00 GMT");
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();//参数放入一个map中，restTemplate不能用hashMap
        param.add("ajax","1");
        param.add("realm","");
        param.add("username",un);
        param.add("credential",pw);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,headers);//将参数和header组成一个请求
        ResponseEntity<String> response = restTemplate.postForEntity(VPNAPI.VPN_LOGIN_CHECK, request, String.class);
        if(response.getStatusCode().is2xxSuccessful()){
            if(response.getBody().contains("ret=1")){
                return response.getBody().substring(response.getBody().indexOf("redir")+6);
            }
        }
        throw new WSExcetpion("登录VPN失败");
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取Cookie信息
     * @Date 2018/11/27
     * @Param [url]
     * @return boolean //TODO 获取成功返回true，否则返回false
     **/
    private static void getSVpnCookie(String url) throws WSExcetpion {
        //获取Header
        HttpHeaders headers = new HttpHeaders();
        //设置类型
        headers.setCacheControl("no-store, no-cache, must-revalidate");
        headers.set("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(VPNAPI.VPN+url,HttpMethod.GET,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(responseEntity.getHeaders().containsKey("Set-Cookie")){
                if(responseEntity.getHeaders().get("Set-Cookie").size() > 0){
                    if(responseEntity.getHeaders().get("Set-Cookie").get(0).startsWith("SVPNCOOKIE")){
                        String[] cookies = responseEntity.getHeaders().get("Set-Cookie").get(0).split(";");
                        String svpnCookie = cookies[0];
                        //将获取到的数据存入redis数据库中并返回
                        redisUtils.set("SVPNCOOKIE",svpnCookie);
                        return;
                    }
                }
            }
        }
        throw new WSExcetpion("获取VPNCOOKIE失败");
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取SID的值
     * @Date 2018/11/27
     * @Param []
     * @return boolean
     **/
    private static void getSessionId() throws WSExcetpion {
        //获取Header
        HttpHeaders headers = new HttpHeaders();
        //设置类型
        headers.set("X-Requested-With","XMLHttpRequest");
        headers.set("Accept","*/*");
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        /** 获取redis保存的cookies */
        if(!redisUtils.hasKey("SVPNCOOKIE")){
            throw new WSExcetpion("redis中不存在SVPNCOOKIE");
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(VPNAPI.GETSID,HttpMethod.GET,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(responseEntity.getBody().indexOf("fgt_sslvpn_sid") > 0){
                String[] bodys = responseEntity.getBody().split(",");
                for(String body : bodys){
                    if(body.indexOf("fgt_sslvpn_sid") > 0){
                        String sessionId = body.split(":")[1].substring(body.split(":")[1].indexOf("\"")+1,body.split(":")[1].lastIndexOf("\""));
                        //将获取到的数据存入redis数据库中并返回
                        redisUtils.set("SID",sessionId);
                        return;
                    }
                }
            }
        }
        throw new WSExcetpion("获取SessionId失败");
    }

    /**
     * @Author lixingyong
     * @Description //TODO 登出VPN
     * @Date 2018/12/7
     * @Param []
     * @return boolean
     **/
    public static boolean logout(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        /** 获取redis保存的cookies */
        if(!redisUtils.hasKey("SVPNCOOKIE")){
            return false;
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(VPNAPI.LOGOUT,HttpMethod.GET,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            redisUtils.delete("SVPNCOOKIE");
            redisUtils.delete("SID");
            return true;
        }
        return false;
    }
    

    @Value("${neusoft.username}")
    private void setUserName(String username){
        this.userName = username.split("&");
    }

    @Value("${neusoft.password}")
    private void setPassword(String password){
        this.password = password.split("&");
    }

    @Autowired(required = true)
    private void setRestTemplate(RestTemplate restTemplate){
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.remove(1);
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converters.add(1,converter);
        /** 设置超时时间 */
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(60 * 1000);
        simpleClientHttpRequestFactory.setReadTimeout(60 * 1000);
        restTemplate.setMessageConverters(converters);
        restTemplate.setRequestFactory(simpleClientHttpRequestFactory);
        this.restTemplate = restTemplate;
    }

    @Autowired(required = true)
    private void setRedisUtils(RedisUtils redisUtils){
        this.redisUtils = redisUtils;
    }
}
