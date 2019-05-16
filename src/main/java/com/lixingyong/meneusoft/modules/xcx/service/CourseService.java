package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseParam;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public interface CourseService extends IService<Course> {

    List<Course> getCourseList(CourseParam courseParam) throws Exception;

    List<Course> getCourseList(HashMap<String, String> parms);

    Course getCourseList(long courseId);
}
