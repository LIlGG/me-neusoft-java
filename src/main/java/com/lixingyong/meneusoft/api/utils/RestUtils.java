package com.lixingyong.meneusoft.api.utils;

import com.lixingyong.meneusoft.common.config.MyRedirectStrategy;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.service.EcardService;
import com.lixingyong.meneusoft.modules.xcx.service.TagService;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import org.apache.http.HttpRequestFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
    private static EcardService ecardService;
    private static UserService userService;
    /** 阿里云API的bucket名称 */
    private static String bucketName;
    /** 阿里云API的文件夹名称 */
    private static String folder;
    /** 阿里云API的文件前缀 */
    private static String codeFolder;
    /** 阿里云API的文件后缀 */
    private static String suffix;
    /** github Token */
    private static String token;

    public static EcardService getEcardService() {
        return ecardService;
    }

    @Autowired(required = true)
    private void setRestTemplate(RestTemplate restTemplate){
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.remove(1);
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converters.add(1,converter);
        /** 设置超时时间 */
//        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
//        simpleClientHttpRequestFactory.setConnectTimeout(60 * 1000);
//        simpleClientHttpRequestFactory.setReadTimeout(60 * 1000);
//
        HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new MyRedirectStrategy())
                .build();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(30 * 1000);
        httpRequestFactory.setReadTimeout(30 * 1000);
        httpRequestFactory.setHttpClient(httpClient);
        restTemplate.setMessageConverters(converters);
        restTemplate.setRequestFactory(httpRequestFactory);
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
    @Autowired
    public void setEcardService(EcardService ecardService) {
        this.ecardService = ecardService;
    }
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public static UserService getUserService() {
        return userService;
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


    @Value("${github.token}")
    public void setToken(String token) {
        this.token = token;
    }

    @Value("${aliyun.oss.bucketName}")
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    @Value("${aliyun.oss.folder}")
    public void setFolder(String folder) {
        this.folder = folder+"/";
    }

    @Value("${aliyun.oss.codefolder}")
    public void setCodeFolder(String codefolder) {
        this.codeFolder = codefolder;
    }

    @Value("${aliyun.oss.suffix}")
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static String getFolder() {
        return folder;
    }

    public static String getCodeFolder() {
        return codeFolder;
    }

    public static String getSuffix() {
        return suffix;
    }

    public static String getToken() {
        return token;
    }
}
