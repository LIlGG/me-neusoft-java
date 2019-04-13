package com.lixingyong.meneusoft.modules.xcx.vo;

import lombok.Data;

/**
 * 反馈信息的参数
 */
@Data
public class FeedBackParam {
    private String brand;
    private String model;
    private String version;
    private String system;
    private String meNeusoftVersion;
    private String label;
    private String title;
    private String content;
    private String sdkVersion;
}
