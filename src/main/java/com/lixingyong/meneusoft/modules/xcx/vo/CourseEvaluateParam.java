package com.lixingyong.meneusoft.modules.xcx.vo;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 课程评价参数
 */
@Data
public class CourseEvaluateParam {
    @Min(value = 0, message = "id参数不合法")
    private int id;
    @Min(value = 0, message = "id参数不合法")
    private long courseId;
    @Min(value = 1, message = "评论内容不合法")
    @Max(value = 4, message = "评论内容不合法")
    private int callName;
    @Min(value = 1, message = "评论内容不合法")
    @Max(value = 5, message = "评论内容不合法")
    private int examType;
    @Min(value = 1, message = "评论内容不合法")
    @Max(value = 2, message = "评论内容不合法")
    private int task;
    @Min(value = 1, message = "评论内容不合法")
    @Max(value = 2, message = "评论内容不合法")
    private int assess;
    @NotEmpty(message = "评论内容不能为空")
    private String comment;
}
