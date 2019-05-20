package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourseEvaluate extends BaseEntity {
    @TableId(type= IdType.AUTO)
    private int id;

    private int userId;

    private long courseId;
    /** 课序号 */
    private String lessonId;
    /** 课程类型 */
    private String type;
    /** 课程考核方式 */
    private int examType;
    /** 课程点名类型*/
    private int callName;
    /** 课程是否有作业 */
    private int task;
    /** 课程评价 */
    private int assess;
    /** 当前评论是否显示 */
    private int status;
    /** 授课教师名 */
    private String teacherName;
    /** 课程名 */
    private String courseName;
    /** 用户头像 */
    private String avatar;
    /** 用户昵称 */
    private String nickName;
    /** 评价内容 */
    private String comment;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
