package com.lixingyong.meneusoft.api.evaluate.VO;

import lombok.Data;

@Data
public class TaskListVO {
    private int id;
    private int status;
    private String url;
    private String taskId;
    private String courseName;
    private String teacherName;
}
