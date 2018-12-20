package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Wechat;

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
}
