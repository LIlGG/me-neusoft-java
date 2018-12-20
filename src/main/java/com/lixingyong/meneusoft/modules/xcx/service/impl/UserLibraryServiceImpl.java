package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.UserLibraryDao;
import com.lixingyong.meneusoft.modules.xcx.entity.UserLibrary;
import com.lixingyong.meneusoft.modules.xcx.service.UserLibraryService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
