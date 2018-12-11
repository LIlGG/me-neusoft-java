package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.jwc.JWCUtil;
import com.lixingyong.meneusoft.api.vpn.VPNUtil;
import com.lixingyong.meneusoft.api.wx.WxUtil;
import com.lixingyong.meneusoft.common.utils.*;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.WxUser;
import com.lixingyong.meneusoft.modules.xcx.service.WxUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName WxSafetyController
 * @Description TODO 微信安全控制层
 * @Author lixingyong
 * @Date 2018/11/2 11:38
 * @Version 1.0
 */

@RestController
@RequestMapping("/api/wechat")
@Api("微信安全控制接口")
public class WxSafetyController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private WxUserService wxUserService;
    /**
     * @Author lixingyong
     * @Description //TODO 微信用户授权接口及登录
     * @Date 2018/11/5
     * @Param []
     * @return java.lang.String
     **/
    @ApiOperation("获取用户授权信息及登录")
    @PostMapping(value = "/jscode2session")
    public R jscode2session(@RequestBody Map<String,Object> params) {
        Map<String, Object> map = new HashMap<>();
        Map session,userInfo = (Map)params.get("userInfo");
        session = WxUtil.code2Session(params.get("jsCode").toString());
        session = MapUtils.setHumpKey(session);
        if(null != session){
            //获取到的参数进行保存并执行登录
            //将得到的参数合并
            userInfo.putAll(session);
            WxUser wxUser = ReflectMap.mapToT(userInfo,WxUser.class);
            //当前时间
            Date now = new Date();
            Date expireTime = DateUtils.addDateHours(now, (int)(Math.floor(Double.valueOf(userInfo.get("expiresIn").toString()))/(60*60)));
            wxUser.setKeyExpireTime(expireTime);
            //是否首次授权
            if(!wxUserService.isUser(wxUser.getOpenid())){
                //新增用户信息
                wxUser.setCreateTime(now);
                wxUserService.addUser(wxUser);
            }else{
                //更新用户信息
                wxUserService.updateUser(wxUser);
            }

            String token = jwtUtils.generateToken(wxUser.getUserId(),wxUser.getSessionKey(),wxUser.getOpenid());
            map.put("token",token);
            map.put("expire",jwtUtils.getExpire());
            return R.ok(map);
        }
        return R.error();
    }

    @Token
    @PostMapping(value = "/test")
    public void test() {
        System.out.println("测试");
    }

    @GetMapping(value = "/getVPN")
    public void vpn(){
        String check_url = VPNUtil.loginCheck();
        VPNUtil.getSVpnCookie(check_url);
        VPNUtil.getSessionId();
        JWCUtil.getJWCCookie(1);
        JWCUtil.getJWCInfo(1);
    }


    @GetMapping(value = "/getValidateCode")
    public void getValidateCode(){
        JWCUtil.getValidateCode(1);
    }

    @GetMapping(value = "/logout")
    public void vpnlogout(){
        VPNUtil.logout();
    }
}
