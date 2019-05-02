package com.lixingyong.meneusoft.api.wx;

/**
 * @ClassName WxAPI
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/11/5 14:26
 * @Version 1.0
 */
public final class WxAPI {
    /** 微信API host */
    public final static String HOST = "https://api.weixin.qq.com";
    /** 获取用户openid及session_key */
    public final static String CODE_2_SESSION = HOST+"/sns/jscode2session?appid={app_id}&secret={secret}&js_code={js_code}&grant_type=authorization_code";

    /** 获取小程序AccessToken授权凭证*/
    public final static String ACCESS_TOKEN = HOST+"/cgi-bin/token?grant_type=client_credential&appid={app_id}&secret={secret}";

    /** 获取A类小程序码 */
    public final static String WX_A_CODE = HOST+"/wxa/getwxacodeunlimit?access_token={token}";
}
