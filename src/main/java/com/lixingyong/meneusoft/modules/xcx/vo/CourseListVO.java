package com.lixingyong.meneusoft.modules.xcx.vo;

import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import lombok.Data;

@Data
public class CourseListVO {
    /** 课程id */
    private long id;
    /** 课程名 */
    private String name;
    /** 课程序号 */
    private String lessonId;
    /** 课程所属学院 */
    private String college;
    /** 课程学时 */
    private int classHour;
    /** 课程学分 */
    private float credit;
    /** 课程类型 */
    private String type;
    /** 考核类型 */
    private String examType;
    /** 授课教师名（中间使用,号分隔） */
    private String teachers;
    /** 上课周次（中间使用,号分隔） */
    private String allWeek;
    /** 上课星期（中间使用,号分隔） */
    private String days;
    /** 上课课时 */
    private String sessions;
}
