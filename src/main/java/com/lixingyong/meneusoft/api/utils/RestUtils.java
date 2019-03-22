package com.lixingyong.meneusoft.api.utils;

import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName com.lixingyong.meneusoft.api.utils
 * @Description TODO
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 19:01
 */
@Component
public class RestUtils {
    private static RestTemplate restTemplate;
    private static RedisUtils redisUtils;
    private static TagService tagService;


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

    @Autowired
    private void setTagService(TagService tagService){
        this.tagService = tagService;
    }

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public static RedisUtils getRedisUtils() {
        return redisUtils;
    }

    public static TagService getTagService() {
        return tagService;
    }
}
