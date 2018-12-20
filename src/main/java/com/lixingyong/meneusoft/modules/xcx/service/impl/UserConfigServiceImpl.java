package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.UserConfigDao;
import com.lixingyong.meneusoft.modules.xcx.entity.UserConfig;
import com.lixingyong.meneusoft.modules.xcx.service.UserConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName NoticeServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("userConfigService")
public class UserConfigServiceImpl extends ServiceImpl<UserConfigDao, UserConfig> implements UserConfigService {
    @Override
    public void init(int id) {
        UserConfig userConfig = new UserConfig();
        userConfig.setNotify("");
        userConfig.setUserId(id);
        userConfig.setUserType(0);
        this.baseMapper.insert(userConfig);
    }

    @Override
    public UserConfig getUserConfig(int userId) {
        List<UserConfig> userConfigs = this.baseMapper.selectList(new EntityWrapper<UserConfig>().eq("user_id",userId));
        if(userConfigs.size() > 0){
            return userConfigs.get(0);
        }
        return null;
    }

}
