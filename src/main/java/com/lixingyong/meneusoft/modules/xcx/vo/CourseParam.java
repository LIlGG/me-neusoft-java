package com.lixingyong.meneusoft.modules.xcx.vo;

import lombok.Data;

@Data
public class CourseParam {
    private int page;

    private int page_size;

    private String order;

    private String classHour;

    private String credit;

    private String type;

    private String examType;

    private String college;

    private String day;

    private String session;
}
