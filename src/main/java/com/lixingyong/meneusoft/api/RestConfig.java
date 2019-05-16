package com.lixingyong.meneusoft.api;

import com.lixingyong.meneusoft.common.config.MyCookieStore;
import com.lixingyong.meneusoft.common.config.MyRedirectStrategy;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.service.*;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @ClassName com.lixingyong.meneusoft.api.utils
 * @Description TODO
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 19:01
 */
@Component
public class RestConfig {
    private static RestTemplate baseRestTemplate;
    private static RestTemplate restTemplate;
    private static RestTemplate restIOSTemplate;
    private static RedisUtils redisUtils;
    private static TagService tagService;
    private static EcardService ecardService;
    private static UserService userService;
    private static ClassroomService classroomService;
    private static CourseService courseService;

    private static CourseScheduleService courseScheduleService;
    /** 阿里云API的bucket名称 */
    private static String bucketName;
    /** 阿里云API的文件夹名称 */
    private static String folder;
    /** 阿里云API的文件前缀 */
    private static String codeFolder;
    /** 阿里云API的文件后缀 */
    private static String suffix;
    private static String qcodeSuffix;
    private static String qcodeFolder;
    /** github Token */
    private static String token;
    /** APPID*/
    private static String appId;
    /** Secret */
    private static String appSecret;
    /** tess4j 文件目录 */
    private static String tess4jData;

    public static EcardService getEcardService() {
        return ecardService;
    }

    @Autowired(required = true)
    private void setRestTemplate(RestTemplate restTemplate){
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.remove(1);
        converters.remove(2);
        converters.remove(3);
        HttpMessageConverter<?> converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));

        FormHttpMessageConverter fc = new FormHttpMessageConverter();
        MappingJackson2HttpMessageConverter mc = new MappingJackson2HttpMessageConverter();
        fc.setCharset(Charset.forName("GBK"));
        converters.add(1,fc);
        converters.add(2,converter);
        converters.add(3,mc);
        /** 设置超时时间 */
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(60 * 1000);
        simpleClientHttpRequestFactory.setReadTimeout(60 * 1000);
        HttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new MyRedirectStrategy())
//                .setProxy(new HttpHost("127.0.0.1",8888))
                .setDefaultCookieStore(new MyCookieStore())
                .build();

        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectTimeout(30 * 1000);
        httpRequestFactory.setReadTimeout(30 * 1000);
        httpRequestFactory.setHttpClient(httpClient);

        restTemplate.setMessageConverters(converters);
        restTemplate.setRequestFactory(httpRequestFactory);
        this.restTemplate = restTemplate;

    }

    public static RestTemplate getRestIOSTemplate() {
        /** 设置超时时间 */
        return restIOSTemplate;
    }

    @Autowired(required = true)
    public void setRestIOSTemplate(RestTemplate restIOSTemplate) {
        List<HttpMessageConverter<?>> converters = restIOSTemplate.getMessageConverters();
        FormHttpMessageConverter fc = new FormHttpMessageConverter();
        fc.setCharset(Charset.forName("ISO-8859-1"));
        converters.add(1,fc);
        /** 设置超时时间 */
        restIOSTemplate.setMessageConverters(converters);
        this.restIOSTemplate = restIOSTemplate;
    }

    public static RestTemplate getBaseRestTemplate() {
        return baseRestTemplate;
    }

    @Autowired(required = true)
    public void setBaseRestTemplate(RestTemplate baseRestTemplate) {
        this.baseRestTemplate = baseRestTemplate;
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

    public static CourseService getCourseService() {
        return courseService;
    }
    @Autowired
    public void setCourseService(CourseService courseService) {
        RestConfig.courseService = courseService;
    }

    public static CourseScheduleService getCourseScheduleService() {
        return courseScheduleService;
    }
    @Autowired
    public void setCourseScheduleService(CourseScheduleService courseScheduleService) {
        RestConfig.courseScheduleService = courseScheduleService;
    }

    public static ClassroomService getClassroomService() {
        return classroomService;
    }
    @Autowired
    public void setClassroomService(ClassroomService classroomService) {
        RestConfig.classroomService = classroomService;
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
    @Value("${wx.xcx.appid}")
    public void setAppId(String appid){
        this.appId = appid;
    }

    @Value("${wx.xcx.appsecret}")
    public void setAppSecret(String appSecret){
        this.appSecret = appSecret;
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

    @Value("${aliyun.oss.qcodesuffix}")
    public void setQcodeSuffix(String qcodesuffix) {
        this.qcodeSuffix = qcodesuffix;
    }

    @Value("${aliyun.oss.qcodefolder}")
    public void setQcodeFolder(String qcodefolder) {
        this.qcodeFolder = qcodefolder;
    }

    public static String getTess4jData() {
        return tess4jData;
    }
    @Value("${tess4j.tess4jData}")
    public void setTess4jData(String tess4jData) {
        RestConfig.tess4jData = tess4jData;
    }

    public static String getQcodeSuffix() {
        return qcodeSuffix;
    }

    public static String getQcodeFolder() {
        return qcodeFolder;
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

    public static String getAppId() {
        return appId;
    }

    public static String getAppSecret() {
        return appSecret;
    }
}
