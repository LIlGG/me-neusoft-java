package com.lixingyong.meneusoft.api.wx;

import com.aliyun.oss.OSSClient;
import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.api.wx.WxAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.JSONUtils;
import com.lixingyong.meneusoft.common.utils.OSSClientUtil;
import com.lixingyong.meneusoft.common.utils.Params;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WxUtil
 * @Description TODO 微信工具类
 * @Author lixingyong
 * @Date 2018/11/5 11:51
 * @Version 1.0
 */
public class WxUtil {
    private static RedisUtils redisUtils = RestConfig.getRedisUtils();
    private static String APP_ID = RestConfig.getAppId();
    private static String APP_SECRET = RestConfig.getAppSecret();
    private static RestTemplate restTemplate = RestConfig.getBaseRestTemplate();

    /** 阿里云API的文件前缀 */
    protected static String qcodeFolder = RestConfig.getQcodeFolder();
    /** 阿里云API的文件后缀 */
    protected static String suffix = RestConfig.getQcodeSuffix();
    /** 阿里云API的bucket名称 */
    protected static String bucketName = RestConfig.getBucketName();
    /**
     * @Author lixingyong
     * @Description //TODO 用户登录
     * @Date 2018/11/5
     * @Param []
     * @return void
     **/
    public static Map code2Session(String jsCode) throws WSExcetpion{
        Map<String,Object> params = new HashMap<>();
        params.put("app_id",APP_ID);
        params.put("secret",APP_SECRET);
        params.put("js_code",jsCode);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(WxAPI.CODE_2_SESSION,String.class,params);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            Map result =  JSONUtils.fromJson(responseEntity.getBody(),Map.class);
            if(!result.containsKey("errcode")){
                //获取到的数据
                return result;
            }
        }
        throw new WSExcetpion("获取用户登录态信息失败");
    }
    
    /**
     * @Author lixingyong
     * @Description //TODO 获取微信接口调用凭证。凭证存储在redis中，如未能成功启动则会抛出异常
     * @Date 2018/11/9
     * @Param []
     * @return java.lang.String
     **/
    public static String getAccessToken() throws WSExcetpion {
        //先查询redis数据库中是否存在accessToken

        if(redisUtils.hasKey("ACCESS_TOKEN")){
            return redisUtils.get("ACCESS_TOKEN",-1);
        }
        //不存在accessToken或者已过期
        Map<String,Object> params = new HashMap<>();
        params.put("app_id",APP_ID);
        params.put("secret",APP_SECRET);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(WxAPI.ACCESS_TOKEN,String.class,params);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            Map result =  JSONUtils.fromJson(responseEntity.getBody(),Map.class);
            if(!result.containsKey("errcode") && result.containsKey("access_token")){
                //获取到的数据
                //将获取到的数据存入redis数据库中并返回
                redisUtils.set("ACCESS_TOKEN",result.get("access_token").toString(),(long)(Math.floor(Double.valueOf(result.get("expires_in").toString()))));
                return result.get("access_token").toString();
            }
        }
        return null;
    }

    public static byte[] getWXACodeUnlimit(Params params, String accessToken) throws WSExcetpion {
        if (params.size() > 0) {
            String param = JSONUtils.toJson(params);
            ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(WxAPI.WX_A_CODE, param, byte[].class, accessToken);
            InputStream in = null;
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                byte[] result = responseEntity.getBody();
                return result;
            }
        }
        return null;
    }

}
