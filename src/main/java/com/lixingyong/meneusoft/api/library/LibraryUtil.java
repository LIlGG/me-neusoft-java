package com.lixingyong.meneusoft.api.library;

import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.api.vpn.VPNUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName com.lixingyong.meneusoft.api.library
 * @Description TODO 图书馆网络请求方式
 * @Author mail@lixingyong.com
 * @Date 2019-03-16 15:23
 */
@Component
public class LibraryUtil {
    private static RestTemplate restTemplate;
    private static RedisUtils redisUtils;
    private static Map<String,Object> map = new HashMap<>();
    private static Logger logger = LoggerFactory.getLogger(VPNUtil.class);
    /**
     * @Author lixingyong
     * @Description //TODO 登录图书馆
     * @Date 2019/3/16
     * @Param [uid]
     * @return void
     **/
    public static void libraryLogin(long uid, String barcode, String password){
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "9999");
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "9999");
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey("SVPNCOOKIE")){
            throw new WSExcetpion("redis中数据不完善");
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("barcode", barcode);
        param.add("password", password);
        param.add("login.x",Integer.toString(0));
        param.add("login.y", Integer.toString(0));
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,headers);//将参数和header组成一个请求
        map.put("sid",redisUtils.get("SID"));
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(VPNAPI.PROXY+LibraryAPI.LIBRARYLOGIN,HttpMethod.POST,request,Resource.class,map);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(responseEntity.getHeaders().containsKey("Set-Cookie")){
                if(responseEntity.getHeaders().get("Set-Cookie").size() > 0){
                    if(responseEntity.getHeaders().get("Set-Cookie").get(0).startsWith("JSESSIONID")){
                        String[] cookies = responseEntity.getHeaders().get("Set-Cookie").get(0).split(";");
                        String JSessionId = cookies[0];
                        //将获取到的数据存入redis数据库中并返回
                        redisUtils.set(Long.toString(uid)+"JSESSIONID",JSessionId);
                        logger.info("获取图书馆登录授权Cookie成功");
                        return;
                    }
                }
            }
        }
        throw new WSExcetpion("获取教务处登录信息失败");
    }

    @Autowired(required = true)
    public void setRestTemplate(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Autowired(required = true)
    private void setRedisUtils(RedisUtils redisUtils){
        this.redisUtils = redisUtils;
    }
}
