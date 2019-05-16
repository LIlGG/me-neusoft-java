package com.lixingyong.meneusoft.modules.xcx.utils;

import com.lixingyong.meneusoft.api.jwc.JWCUtil;
import com.lixingyong.meneusoft.api.library.LibraryUtil;
import com.lixingyong.meneusoft.api.vpn.VPNUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import io.lettuce.core.dynamic.annotation.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.utils
 * @Description TODO 登录校园网工具包
 * @Author mail@lixingyong.com
 * @Date 2019-03-05 20:02
 */
@Component
public class LoginUtil{

    private static Logger logger = LoggerFactory.getLogger(LoginUtil.class);
    //校园网用户
    private static String[] userName;

    // 校园网密码
    private static String[] password;

    // 登录失败重连次数
    private static int frequency;

    // 重新连接间隔（单位：毫秒）
    private static long interval;

    // 提供Fiddler拦截代理
    public static void fiddler(){
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "8888");
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "8888");
    }
    // 仅连接VPN
    public static boolean exeVpnLogin() throws WSExcetpion{
//        fiddler();
        if(VPNUtil.isLogin()){
            return true;
        }
        logger.debug("VPN未登录，执行登录流程");
        if(userName.length < 1 || null == userName){
            throw new WSExcetpion("服务器数据错误，请联系管理员!");
        }
        if(password.length < 1 || null == password){
            throw new WSExcetpion("服务器数据错误，请联系管理员!");
        }
        if(password.length != userName.length){
            throw new WSExcetpion("服务器数据错误，请联系管理员!");
        }
        for(int i = 0; i < userName.length; i++){
            synchronized (VPNUtil.class){
                logger.debug("使用账号："+userName[i] + "进行登录");
                try {
                    String redir = VPNUtil.loginCheck(userName[i], password[i]);
                    VPNUtil.getSVpnCookie(redir);
                    VPNUtil.getSessionId();
                    logger.info("登录VPN成功");
                    return true;
                }catch (WSExcetpion s) {
                    logger.error("账号"+userName[i] + "登录失败，准备尝试其它账号,错误信息："+s.getMsg());
                }
            }
        }
        logger.error("登录VPN失败，请稍候再试");
        return false;
    }

    // 连接至教务处
    public static boolean exeJWCLogin(long uid) throws WSExcetpion{
        logger.debug("用户"+uid+"执行登录教务处流程");
        try {
            JWCUtil.getJWCCookie(uid);
            //JWCUtil.getJWCInfo(uid);
            logger.debug("登录教务处流程执行完成");
            return true;
        }catch (WSExcetpion s){
            logger.error("用户"+uid+"登录教务网失败，错误信息："+s.getMsg());
        }
        return false;
    }

    @Value("${neusoft.username}")
    private void setUserName(String username){
        this.userName = username.split("&");
    }

    @Value("${neusoft.password}")
    private void setPassword(String password){
        this.password = password.split("&");
    }

    @Value("${neusoft.frequency}")
    public static void setFrequency(int frequency) {
        LoginUtil.frequency = frequency;
    }

    @Value("${neusoft.interval}")
    public static void setInterval(long interval) {
        LoginUtil.interval = interval;
    }
}
