package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseEvaluate;
import com.lixingyong.meneusoft.modules.xcx.entity.Wechat;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseEvaluateParam;

import java.util.List;


public interface CourseEvaluateService extends IService<CourseEvaluate> {

    List<CourseEvaluate> getCourseEvaluateList(Long courseId);

    CourseEvaluate getMyEvaluate(Long courseId, Long valueOf);

    int getCurUserCourseEvaluate(long courseId, Long valueOf);

    void addCourseEvaluate(CourseEvaluateParam courseEvaluateParam, Course course, Wechat wechat);

    CourseEvaluate getMyEvaluate(int id);

    void updateCourseEvaluate(CourseEvaluate courseEvaluate);

    void insertOrUpdateCourseEvaluate(List<CourseEvaluate> courseEvaluateList);
}
