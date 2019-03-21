package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.UserLibrary;

public interface UserLibraryService extends IService<UserLibrary> {
    /**
     * @Author lixingyong
     * @Description //TODO 创建一个图书馆信息
     * @Date 2018/12/20
     * @Param [id]
     * @return void
     **/
    void createdLibrary(int id);

    UserLibrary getUserLibrary(int userId);

    void insertOrUpdateLibraryAccount(String userId, String student_id, String password);

    UserLibrary getUserAccount(UserLibrary userLibrary);
}
