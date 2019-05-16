package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 课程课表
 */
@Data
public class CourseSchedule extends BaseEntity {
    @TableId(type= IdType.INPUT)
    private long id;

    private long courseId;

    private String teacherName;

    private int studentNo;

    private String className;

    private String allWeek;

    private String day;

    private String session;

    private String classroomName;

    private int termId;
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
