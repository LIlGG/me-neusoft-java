package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourseGrade extends BaseEntity {
    @TableId(type= IdType.INPUT)
    private long id;
    private long courseId;
    private String lessonId;
    private int g0;
    private int g60;
    private int g70;
    private int g80;
    private int g90;
    private int year;
    @Override
    protected Serializable pkVal() {
        return id;
    }
}
