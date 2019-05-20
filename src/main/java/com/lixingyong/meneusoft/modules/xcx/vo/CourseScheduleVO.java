package com.lixingyong.meneusoft.modules.xcx.vo;

import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import lombok.Data;

@Data
public class CourseScheduleVO {
    private Course course;

    private String teachers;

    private String student_no;

    private String className;

    private String allWeek;

    private String day;

    private String session;

    private String classroomName;

    private String termId;
}
