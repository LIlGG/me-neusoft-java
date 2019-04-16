package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Teacher;

import java.util.List;


public interface TeacherService extends IService<Teacher> {

    void insertOrUpdateTeachers(List<Teacher> teachers);
}
