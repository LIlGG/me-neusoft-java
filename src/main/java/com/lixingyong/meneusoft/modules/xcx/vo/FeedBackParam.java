package com.lixingyong.meneusoft.modules.xcx.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "内容不能为空")
    private String content;
    private String sdkVersion;
}
