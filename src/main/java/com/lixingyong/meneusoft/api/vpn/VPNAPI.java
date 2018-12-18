package com.lixingyong.meneusoft.api.vpn;

/**
 * @ClassName VPNAPI
 * @Description TODO VPN接口
 * @Author lixingyong
 * @Date 2018/11/20 14:33
 * @Version 1.0
 */
public final class VPNAPI {
    /** VPN-外网 */
    public final static String VPN = "https://vpn.neusoft.edu.cn";

    /** VPN登录API */
    public final static String VPN_LOGIN_CHECK = VPN + "/remote/logincheck";

    /** VPN获取COOKIE API */
    public final static String VPN_HOST_CHECK = VPN + "/remote/hostcheck_install";

    /** VPN获取fgt_sslvpn_sid */
    public final static String GETSID = VPN + "/remote/portal";

    /** 登出 */
    public final static String LOGOUT = VPN + "/remote/logout";

    /** 授权页面代理前缀 */
    public final static String PROXY = VPN + "/proxy/{sid}/";

    /** 验证VPN登录是否已过期 */
    public final static String ISLOGIN = GETSID + "?action=6";
}
