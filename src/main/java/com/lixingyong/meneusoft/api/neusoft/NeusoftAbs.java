package com.lixingyong.meneusoft.api.neusoft;

import com.lixingyong.meneusoft.api.VPNInterface;
import com.lixingyong.meneusoft.api.jwc.JWCAPI;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;

public abstract class NeusoftAbs extends VPNInterface {

    protected static String URL(String url) throws WSExcetpion {
        return isVPN() ? VPNAPI.PROXY + JWCAPI.JWC_VPN_API + url :  JWCAPI.JWC_API + url;
    }
}
