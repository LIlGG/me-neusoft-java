package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.WxUser;

public interface WxUserService extends IService<WxUser> {
    /**
     * @Author lixingyong
     * @Description //TODO 保存用户
     * @Date 2018/11/5
     * @Param [wxUser]
     * @return void
     **/
    void addUser(WxUser wxUser);
    
    /**
     * @Author lixingyong
     * @Description //TODO 更新用户信息
     * @Date 2018/11/5 
     * @Param [wxUser]
     * @return void
     **/
    void updateUser(WxUser wxUser);
    
    /**
     * @Author lixingyong
     * @Description //TODO 根据openid删除用户
     * @Date 2018/11/5 
     * @Param [open_id]
     * @return void
     **/
    void delectBatch(String open_id);
    
    /**
     * @Author lixingyong
     * @Description //TODO 根据userId 删除用户
     * @Date 2018/11/5 
     * @Param [user_id]
     * @return void
     **/
    void delectBatch(Long user_id);
    
    /**
     * @Author lixingyong
     * @Description //TODO 根据userId判断用户是否存在
     * @Date 2018/11/5 
     * @Param [user_id]
     * @return boolean
     **/
    boolean isUser(Long user_id);
    
    /**
     * @Author lixingyong
     * @Description //TODO 根据openId判断用户是否存在
     * @Date 2018/11/5 
     * @Param [open_id]
     * @return boolean
     **/
    boolean isUser(String open_id);
}
