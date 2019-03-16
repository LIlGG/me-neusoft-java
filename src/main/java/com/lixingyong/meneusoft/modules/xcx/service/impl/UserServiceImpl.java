package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.api.bind.Type;
import com.lixingyong.meneusoft.common.utils.JwtUtils;
import com.lixingyong.meneusoft.modules.xcx.dao.UserDao;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.entity.UserConfig;
import com.lixingyong.meneusoft.modules.xcx.service.UserConfigService;
import com.lixingyong.meneusoft.modules.xcx.service.UserLibraryService;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import com.lixingyong.meneusoft.modules.xcx.service.WechatService;
import com.lixingyong.meneusoft.modules.xcx.utils.BindUtil;
import com.lixingyong.meneusoft.modules.xcx.vo.LoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName WechatServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/20 16:24
 * @Version 1.0
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private WechatService wechatService;
    @Autowired
    private UserLibraryService userLibraryService;
    @Autowired
    private JwtUtils jwtUtils;
    @Override
    public int createUser() {
        User user = new User();
        user.setJwcVerify(0);
        user.setVerify(0);
        if(this.baseMapper.insert(user) > 0){
            //成功添加之后，初始化用户设置
            userConfigService.init(user.getId());
            userLibraryService.createdLibrary(user.getId());
            return user.getId();
        }
        return -1;
    }

    @Override
    public User getUserInfo(int userId) {
        User user  = this.baseMapper.selectById(userId);
        if(null != user){
            user.setWechat(wechatService.getWechat(userId,"userid"));
            user.setUserLibrary(userLibraryService.getUserLibrary(userId));
        }
        return user;
    }

    @Override
    public LoginVO login(int userId) {
        LoginVO loginVO = new LoginVO();
        //根据userId，创建Token
        String token = jwtUtils.generateToken(userId);
        User user = this.getUserInfo(userId);
        UserConfig userConfig = userConfigService.getUserConfig(userId);
        loginVO.setJwc_verify(user.getJwcVerify());
        loginVO.setLibrary_verify(user.getUserLibrary().getVerify());
        loginVO.setToken(token);
        loginVO.setUser_type(userConfig.getUserType());
        loginVO.setVerify(user.getVerify());
        return loginVO;
    }


    @Override
    public void insertOrUpdateJwcAccount(String user_id, String student_id, String password, String code) {
        // 判断当前教务处账号是否可用
        BindUtil.accountStatus(student_id, password, code, Type.JWC);
    }

    @Override
    public void insertOrUpdateLibraryAccount(String userId, String student_id, String password) {
        // 判断图书馆账号是否可以使用
        BindUtil.accountStatus(student_id, password,null,Type.LIBRARY);
    }

}
