package com.lixingyong.meneusoft.modules.xcx.vo;

import com.lixingyong.meneusoft.api.evaluate.VO.HiddenInput;
import com.lixingyong.meneusoft.api.evaluate.VO.QuestionVO;
import lombok.Data;

import java.util.List;

@Data
public class Evaluate {
    private String courseName;
    private String teacherName;
    private List<HiddenInput> hiddlers;
    private List<QuestionVO> items;
}
