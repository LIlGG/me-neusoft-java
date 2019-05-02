package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.wx.WxUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.*;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.Wechat;
import com.lixingyong.meneusoft.modules.xcx.service.WechatService;
import com.lixingyong.meneusoft.modules.xcx.utils.wx.crypto.WXCore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @ClassName WxSafetyController
 * @Description TODO 微信安全控制层
 * @Author lixingyong
 * @Date 2018/11/2 11:38
 * @Version 1.0
 */

@RestController
@Api("微信安全控制接口")
public class WxSafetyController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private WechatService wechatService;
    @Token
    @PostMapping(value = "/unionid")
    public R unionid(@RequestParam Map<String,String> params, @LoginUser String userId){
        // 获取用户信息
        try {
            Wechat wechat = wechatService.selectByUserId(userId);
            if(null != wechat){
                // 执行加密解密
                String result = WXCore.decrypt(params.get("encrypted_data"), wechat.getSessionKey(), params.get("iv"));
                if(!result.equals("")){
                    // 将数据保存到数据库中
                    wechatService.addOtherData(wechat ,result);
                    return R.ok();
                }
            }
            return R.error("用户数据为空，请重新登录！");
        }catch (Exception e){
            return R.error("获取用户信息失败,请重新登录再试！");
        }
    }

    /**
     * 生成小程序码
     */
    @ApiOperation("小程序码生成")
    @Token
    @GetMapping("/wechat/qcode")
    public byte[] qCode(@RequestParam("page") String page, @RequestParam("scene") String scene){
        // 根据传入的参数获取小程序码
        String accessToken = WxUtil.getAccessToken();
        // 设置小程序码的状态
        Params params = new Params(scene, page);
        // 获取小程序码
       return WxUtil.getWXACodeUnlimit(params, accessToken);
    }
}
