package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseGrade;

import java.util.List;


public interface CourseGradeService extends IService<CourseGrade> {

    List<CourseGrade> getCourseGradeList(Long courseId);

    void insertOrUpdateCourseGradeAll(List<CourseGrade> courseGradeList);
}
