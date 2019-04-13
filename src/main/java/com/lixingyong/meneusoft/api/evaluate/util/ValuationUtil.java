package com.lixingyong.meneusoft.api.evaluate.util;

import com.lixingyong.meneusoft.api.evaluate.EISID;
import com.lixingyong.meneusoft.api.evaluate.EvaluateAPI;
import com.lixingyong.meneusoft.api.evaluate.VO.ValuationTaskVO;

import java.util.Objects;

public class ValuationUtil {
    public static String taskTargetUrl(ValuationTaskVO taskVO){
        String targetUrl = "";
        if(EISID.fromTypeName(taskVO.getPEisId()) == EISID.EIS_SU){
            if(taskVO.getVersionNo() == 2){
                targetUrl = "/ValItemsAction.action?method=querySuValRecordV2&";
            }else{
                targetUrl = "/view/jsp/valuation/valitems.jsp?";
            }
        } else {
            switch (Objects.requireNonNull(EISID.fromTypeName(taskVO.getEisId()))){
                case EIS_KC:
                    targetUrl = "/view/jsp/valuation/valitemsKc.jsp?";
                    break;
                case EIS_JC:
                    if(taskVO.getVersionNo() == 2){
                        targetUrl = "/ValItemsAction.action?method=queryJcValRecordV3&";
                    } else {
                        targetUrl = "/view/jsp/valuation/valitemsJc.jsp?";
                    }
                    break;
                case EIS_TKC:
                    targetUrl = "/view/jsp/valuation/valitemsTkc.jsp?";
                    break;
                case EIS_TJC:
                    targetUrl = "/view/jsp/valuation/valitemsTjc.jsp?";
                    break;
                case EIS_TXM:
                    targetUrl = "/view/jsp/valuation/valitemsTxm.jsp?";
                    break;
                case EIS_MD:
                    targetUrl = "/ValItemsAction.action?method=queryMdValRecordV2&";
                    break;
            }
        }
        taskVO.setUrl(EvaluateAPI.EVALUATE_HOME + targetUrl + EvaluateAPI.TASK_SUFFIX);
        return taskVO.getUrl();
    }
}
