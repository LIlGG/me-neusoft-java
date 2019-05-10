package com.lixingyong.meneusoft.api;

import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.utils.LoginUtil;
import org.apache.pdfbox.jbig2.util.log.Logger;
import org.apache.pdfbox.jbig2.util.log.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.net.InetSocketAddress;
import java.util.*;

public abstract class VPNInterface {
    private static boolean isVPN = true;
    protected static RestTemplate restTemplate = RestConfig.getRestTemplate();
    protected static RedisUtils redisUtils = RestConfig.getRedisUtils();
    protected static Map<String,Object> map;
    private static List<String> cookies;
    protected static Logger logger = LoggerFactory.getLogger(VPNInterface.class);


    /**
     * 修改restTemplate
     */


    /**
     * 获取HttpHeaders
     */
    protected static HttpHeaders httpHeaders() throws WSExcetpion {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.put(HttpHeaders.COOKIE,getCookies());
        return httpHeaders;
    }
    /**
     * 根据是否为VPN登录，设置不同的URL
     * @return
     */
    protected static String URL(String url) throws WSExcetpion{
        return url;
    }

    /**
     * 无请求体的最简单的请求实体
     * @return
     */
    protected static HttpEntity httpEntity() throws WSExcetpion{
        return  new HttpEntity<>(null,httpHeaders());
    }



    protected static boolean isVPN() {
        return isVPN;
    }

    public static void setVPN(boolean isVPN) {
        VPNInterface.isVPN = isVPN;
    }

    public static List<String> getCookies() {
        if(cookies == null){
            cookies =  new LinkedList<>();
        }
        if(!isVPN()){
            return cookies;
        }
        LoginUtil.exeVpnLogin();
        if(!redisUtils.hasKey("SVPNCOOKIE")){
            throw new WSExcetpion("redis中不存在SVPNCOOKIE");
        }
        cookies.add(redisUtils.get("SVPNCOOKIE"));
        return cookies;
    }

    /**
     * 执行有需要验证的cookies的HttpEntity
     * @return
     */
    public static void setCookies(String cookie) throws WSExcetpion {
        if(!redisUtils.hasKey(cookie)){
            throw new WSExcetpion("redis中缺少" + cookie);
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get(cookie));
        setCookies(cookiesList);
    }

    public static void setCookies(List<String> cookies) throws WSExcetpion {
        VPNInterface.cookies = cookies;
    }

    public static Map<String, Object> getMap() {
        if(map == null){
            map =  new HashMap<>();
        }
        if(!isVPN()){
            return map;
        }
        LoginUtil.exeVpnLogin();
        if(!redisUtils.hasKey("SID")){
            throw new WSExcetpion("redis中不存在SID");
        }
        map.put("sid",redisUtils.get("SID"));
        return map;
    }

    public static void setMap(Map<String, Object> map) {
        VPNInterface.map = map;
    }
}
