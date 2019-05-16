package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;

import java.util.List;


public interface ClassroomService extends IService<Classroom> {

    String getClassId(String ownText);

    List<Classroom> getClassroom(int classroom);
}
