package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
@Data
public class CourseCount extends BaseEntity {
    @TableId(type= IdType.INPUT)
    private long id;
    private long courseId;

    private String lessonId;
    /** 挂科率 */
    private float failRate;
    /** 平均分 */
    private float avgGrade;
    /** 课程名 */
    @TableField(exist = false)
    private String name;
    /** 授课教师 */
    @TableField(exist = false)
    private String teacher;
    /** 评教平均分 */
    private float star;
    /** 好评人数 */
    private int good;
    /** 一般评价人数 */
    private int normal;
    /** 差评人数 */
    private int bad;
    /** 成绩统计人数 */
    private int gradeAll;
    /** 所在校区 */
    @TableField(exist = false)
    private String campus;
    /** 上课周次 */
    @TableField(exist = false)
    private String day;
    /** 学分 */
    @TableField(exist = false)
    private float credit;
    /** 考核方式 */
    @TableField(exist = false)
    private String examType;
    /** 课程类型 */
    @TableField(exist = false)
    private String type;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
