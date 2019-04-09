package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.vo.LoginVO;


public interface UserService extends IService<User> {

    /**
     * @Author lixingyong
     * @Description //TODO 创建一个用户
     * @Date 2018/12/20
     * @Param
     * @return
     **/
    int createUser();

    /**
     * @Author lixingyong
     * @Description //TODO 根据userId，获取用户信息
     * @Date 2018/12/20
     * @Param [userId]
     * @return com.lixingyong.meneusoft.modules.xcx.entity.User
     **/
    User getUserInfo(int userId);

    /**
     * @Author lixingyong
     * @Description //TODO 用户登录，根据userId获取信息
     * @Date 2018/12/20
     * @Param [userId]
     * @return com.lixingyong.meneusoft.modules.xcx.vo.LoginVO
     **/
    LoginVO login(int userId);

    /**
     * @Author lixingyong
     * @Description //TODO 绑定教务处，如果存在，则更新。如果不存在，则新增
     * @Date 2019/2/9
     * @Param [user_id, student_id, password]
     * @return void
     **/
    void insertOrUpdateJwcAccount(String user_id, String student_id, String password, String code);

    void insertOrUpdateEcardInfo(String userId, String id);
}
