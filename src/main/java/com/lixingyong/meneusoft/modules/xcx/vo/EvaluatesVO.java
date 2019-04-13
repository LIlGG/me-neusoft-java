package com.lixingyong.meneusoft.modules.xcx.vo;

import com.lixingyong.meneusoft.api.evaluate.VO.TaskListVO;
import lombok.Data;

import java.util.List;

@Data
public class EvaluatesVO {
    private String year;
    private int term;
    private String name;
    private String pEisId;
    private String eisId;
    private List<TaskListVO> value;
}
