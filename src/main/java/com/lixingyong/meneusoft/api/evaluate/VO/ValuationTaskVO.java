package com.lixingyong.meneusoft.api.evaluate.VO;

import lombok.Data;

import java.util.List;

@Data
public class ValuationTaskVO {
    private String academicYearNo;

    private String eisId;

    private String eisName;

    private int objCnt;

    private String pEisId;

    private String pEisName;

    private String studentName;

    private String studentNo;

    private String termNo;

    private int versionNo;

    private String url;

    private List<TaskListVO> tasks;
}
