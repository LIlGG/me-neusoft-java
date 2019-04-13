package com.lixingyong.meneusoft.api.evaluate.VO;

import lombok.Data;

import java.util.List;

@Data
public class EvaluateVO {
    private String desStudentNo;

    private String errorMsg;

    private String isLoginFromUfs;

    private String success;

    private List<ValuationTaskVO> valuationTasks;
}
