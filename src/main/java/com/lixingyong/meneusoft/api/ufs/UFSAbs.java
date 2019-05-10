package com.lixingyong.meneusoft.api.ufs;

import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.api.VPNInterface;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import org.springframework.http.HttpEntity;

import java.util.ArrayList;
import java.util.List;

public abstract class UFSAbs extends VPNInterface {
    protected static UserService userService = RestConfig.getUserService();
    protected static String URL(String url) throws WSExcetpion {
        return isVPN() ? VPNAPI.PROXY + UFSAPI.UFS_VPN + url :  UFSAPI.UFS + url;
    }

    protected static String HOST() throws WSExcetpion{
        if(isVPN()){
            return "vpn.neusoft.edu.cn";
        }
        return "ufs.neusoft.edu.cn";
    }

    /**
     * 执行有登录验证的方法
     * @return
     */
    protected static HttpEntity vailHttpEntity(int uid) throws  WSExcetpion{
        //登录前首先获取本次登录所需cookie
        if(!redisUtils.hasKey(uid+"UFS")){
            throw new WSExcetpion("redis中不存在UFS");
        }
        String cookie = redisUtils.get(uid+"UFS");
        // 将cookie放入header
        List<String> cookies = new ArrayList<>();
        cookies.add(cookie);
        setCookies(cookies);
        return httpEntity();
    }
}
