package com.lixingyong.meneusoft.common.api;

/**
 * @ClassName JWCAPI
 * @Description TODO 教务处API接口
 * @Author lixingyong
 * @Date 2018/11/29 10:30
 * @Version 1.0
 */
public final class JWCAPI {
    /** VPN类型教务处首页 */
    public final static String JWCAPI = "http/newjw.neusoft.edu.cn";

    /** 获取Session_Id */
    public final static String JWCSID = JWCAPI + "/jwweb/";

    /** 获取验证码 */
    public final static String CODE = JWCSID + "sys/ValidateCode.aspx";

    /** 登录接口 */
    public final static String LOGIN = JWCSID + "_data/home_login.aspx";
}
