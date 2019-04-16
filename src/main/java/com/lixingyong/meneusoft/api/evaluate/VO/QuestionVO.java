package com.lixingyong.meneusoft.api.evaluate.VO;

import lombok.Data;

import java.util.List;

@Data
public class QuestionVO {
    // 问题名称
    private String title;
    // 问题类型
    private String type;
    // 问题对象名
    private String name;
    // 选择题选项
    private List<RadioVO> radios;
    // 问题回答内容
    private String comment;
}
