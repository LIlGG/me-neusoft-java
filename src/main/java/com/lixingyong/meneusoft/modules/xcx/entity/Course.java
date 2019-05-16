package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("course")
public class Course extends BaseEntity {
    @TableId(type= IdType.INPUT)
    private long id;
    /** 课程号 */

    private String courseId;
    /** 课程名 */
    private String name;
    /** 课程所属学院 */
    private String college;
    /**课序号*/
    private String lessonId;
    /** 课学时 */
    private int classHour;
    /**课程学分*/
    private float credit;
    /** 课程类别 */
    private String type;
    /** 考试类型 */
    private String examType;
    /** 所属学期 */
    private int termId;
    @TableField(exist = false)
    private List<CourseSchedule> courseSchedules;
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
