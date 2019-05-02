package com.lixingyong.meneusoft.api.library;

import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.api.VPNInterface;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

public abstract class LibraryAbs extends VPNInterface {
    protected static RestTemplate restIOSTemplate = RestConfig.getRestIOSTemplate();
    /**
     * 根据是否为VPN登录，设置不同的URL
     * @return
     */
    protected static String URL(String url) throws WSExcetpion {
        return isVPN() ? VPNAPI.PROXY + LibraryAPI.LIBRARY_READER_VPN + url : LibraryAPI.LIBRARY_READER + url;
    }
}
