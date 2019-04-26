package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("grade")
public class Grade extends BaseEntity {
    @TableId(type= IdType.INPUT)
    private int id;

    private int userId;

    private String courseId;

    private String courseName;

    private String credit;

    private String courseType;

    private float grade;

    private float gpa;

    private int term;

    private int year;

    private String termName;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
