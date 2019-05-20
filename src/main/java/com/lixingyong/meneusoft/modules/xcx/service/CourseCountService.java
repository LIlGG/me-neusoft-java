package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseCount;

import java.util.List;


public interface CourseCountService extends IService<CourseCount> {

    CourseCount getCourseCount(long courseId);

    void insertOrUpdateCourseCountAll(List<CourseCount> courseCountList);
}
