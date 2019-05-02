package com.lixingyong.meneusoft.api.evaluate;

import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.api.VPNInterface;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import org.springframework.http.HttpEntity;

import java.util.*;

public abstract class EvaluateAbs extends VPNInterface {
    protected static UserService userService = RestConfig.getUserService();

    /**
     * 根据是否为VPN登录，设置不同的URL
     * @return
     */
    protected static String URL(String url) throws WSExcetpion{
        return isVPN() ? VPNAPI.PROXY + EvaluateAPI.EVALUATE_VPN_HOME + url : EvaluateAPI.EVALUATE_HOME + url;
    }

    /**
     * 执行有登录验证的方法
     * @return
     */
    protected static HttpEntity vailHttpEntity(int uid) throws  WSExcetpion{
        //登录前首先获取本次登录所需cookie
        String cookie = redisUtils.get(uid+"EVALUATE_COOKIE");
        // 将cookie放入header
        List<String> cookies = new ArrayList<>();
        cookies.add(cookie);
        setCookies(cookies);
        return httpEntity();
    }
}
