package com.lixingyong.meneusoft.api.neusoft;

import com.lixingyong.meneusoft.api.library.LibraryAPI;
import com.lixingyong.meneusoft.api.utils.RestUtils;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 其他所有类型的教学系统请求
 */
public class NeusoftUtil {
    private static RestTemplate restTemplate = RestUtils.getRestTemplate();
    private static RedisUtils redisUtils = RestUtils.getRedisUtils();
    private static Map<String,Object> map = new HashMap<>();
    /**
     * 获取校历列表请求（需登录VPN）
     */
    public Map<Integer, String> termList(){
        Map<Integer, String> terms = new LinkedHashMap<>();
        // 暂时不使用VPN登录
        HttpEntity<String> request = new HttpEntity<>(null,null);//将参数和header组成一个请求
        ResponseEntity<String> response = restTemplate.exchange(NeusoftAPI.TERM, HttpMethod.POST,request,String.class);
        if(response.getStatusCode().is2xxSuccessful()){
            Document html = Jsoup.parse(Objects.requireNonNull(response.getBody()));
            Elements options = html.select("select[name=sel_xnxq] > option");
            for(Element option : options){
                Integer value = Integer.valueOf(option.attr("value"));
                terms.put(value, option.text());
            }
        }
        return terms;
    }
}
