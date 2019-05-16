package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseSchedule;
import com.lixingyong.meneusoft.modules.xcx.vo.CourseParam;

import java.util.HashMap;
import java.util.List;


public interface CourseScheduleService extends IService<CourseSchedule> {

    List<CourseSchedule> getCourseList(String classroomName, int id, int weeks, String day);

    List<CourseSchedule> getCourseList(long id, CourseParam courseParam);

    List<CourseSchedule> getCourseScheduleList(HashMap<String, String> params);

    List<CourseSchedule> getSearchCourseList(long id, HashMap<String, String> params);
}
