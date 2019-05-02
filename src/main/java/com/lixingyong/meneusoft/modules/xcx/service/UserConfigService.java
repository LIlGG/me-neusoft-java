package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.UserConfig;

public interface UserConfigService extends IService<UserConfig> {
    /**
     * @Author lixingyong
     * @Description //TODO 初始化用户设置，默认用户身份为0 本科
     * @Date 2018/12/20
     * @Param [id]
     * @return void
     **/
    void init(int id);

    UserConfig getUserConfig(int userId);

    void setUserConfigNotify(String notify, String userId);

    void setUserType(Integer userType, Integer userId);

    void delUserConfigInfo(Integer valueOf);
}
