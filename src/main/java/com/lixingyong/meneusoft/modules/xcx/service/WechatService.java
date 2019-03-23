package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Wechat;
import org.springframework.boot.configurationprocessor.json.JSONException;

public interface WechatService extends IService<Wechat> {

    /**
     * @Author lixingyong
     * @Description //TODO 根据openid，获取wechat信息
     * @Date 2018/12/20
     * @Param [openid]
     * @return com.lixingyong.meneusoft.modules.xcx.entity.Wechat
     **/
    Wechat getWechat(String openid);

    Wechat getWechat(int userId, String userid);

    Wechat selectByUserId(String userId);

    void addOtherData(Wechat wechat, String result) throws JSONException;
}
