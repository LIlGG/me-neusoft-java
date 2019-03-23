package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.api.bind.Type;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.dao.UserLibraryDao;
import com.lixingyong.meneusoft.modules.xcx.entity.UserLibrary;
import com.lixingyong.meneusoft.modules.xcx.service.UserLibraryService;
import com.lixingyong.meneusoft.modules.xcx.utils.BindUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName NoticeServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("userLibraryService")
public class UserLibraryServiceImpl extends ServiceImpl<UserLibraryDao, UserLibrary> implements UserLibraryService {

    @Override
    public void createdLibrary(int id) {
        UserLibrary userLibrary = new UserLibrary();
        userLibrary.setPassword("");
        userLibrary.setStudentId("");
        userLibrary.setVerify(0);
        userLibrary.setUserId(id);
        this.baseMapper.insert(userLibrary);
    }

    @Override
    public UserLibrary getUserLibrary(int userId) {
        List<UserLibrary> userLibraries = this.baseMapper.selectList(new EntityWrapper<UserLibrary>().eq("user_id",userId));
        if(userLibraries.size() > 0){
            return userLibraries.get(0);
        }
        return null;
    }

    @Override
    public void insertOrUpdateLibraryAccount(String userId, String student_id, String password) {
        // 判断图书馆账号是否可以使用
        boolean usable =  BindUtil.accountStatus(userId, student_id, password,null,Type.LIBRARY);
        if(usable){
            // 保存当前账号
            UserLibrary userLibrary = new UserLibrary();
            userLibrary.setUserId(Integer.valueOf(userId));
            userLibrary.setStudentId(student_id);
            userLibrary.setPassword(password);
            userLibrary.setVerify(1);
            this.baseMapper.update(userLibrary, new EntityWrapper<UserLibrary>().eq("user_id",userLibrary.getUserId()));
        } else {
            throw new WSExcetpion("图书馆登录失败，请稍候再试");
        }
    }

    /**
     * @Author lixingyong
     * @Description //TODO 判断用户账号是否可用，并返回账号密码
     * @Date 2019/3/18
     * @Param [userId]
     * @return java.util.Map<java.lang.String                                                               ,                                                               java.lang.String>
     **/
    @Override
    public UserLibrary getUserAccount(UserLibrary userLibrary) {
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",userLibrary.getUserId());
        map.put("verify", 1);
        return this.baseMapper.selectList(new EntityWrapper<UserLibrary>().allEq(map)).get(0);
    }
}
