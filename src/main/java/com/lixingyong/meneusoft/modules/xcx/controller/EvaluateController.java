package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.evaluate.EvaluateUtil;
import com.lixingyong.meneusoft.api.evaluate.VO.EvaluateVO;
import com.lixingyong.meneusoft.api.evaluate.VO.TaskListVO;
import com.lixingyong.meneusoft.api.evaluate.VO.ValuationTaskVO;
import com.lixingyong.meneusoft.api.evaluate.util.ValuationUtil;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.Feedback;
import com.lixingyong.meneusoft.modules.xcx.vo.EvaluatesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@Api("快捷评教")
public class EvaluateController {

    @ApiOperation("获取评教列表")
    @GetMapping("/evaluates")
    @Token
    public R getEvaluates(@LoginUser String userId){
        String[] terms = new String[]{"秋","春"};
        List<EvaluatesVO> result = new LinkedList<>();
        // 获取评教信息
        EvaluateVO evaluateVO = EvaluateUtil.getEvaluates(Long.parseLong(userId));
        // 根据获得的评教信息，执行不同的请求，获取其需要评价的信息
        for(ValuationTaskVO valuationTaskVO : evaluateVO.getValuationTasks()){
            EvaluatesVO evaluatesVO = new EvaluatesVO();
            if(valuationTaskVO.getObjCnt() > 0){
                // 获取当前评教列表
                ValuationUtil.taskTargetUrl(valuationTaskVO);
                List<TaskListVO> taskListVOS = EvaluateUtil.getTaskList(Long.parseLong(userId), valuationTaskVO);
                if(!taskListVOS.isEmpty()){
                    evaluatesVO.setEisId(valuationTaskVO.getEisId());
                    evaluatesVO.setPEisId(valuationTaskVO.getPEisId());
                    evaluatesVO.setYear(valuationTaskVO.getAcademicYearNo());
                    evaluatesVO.setTerm(Integer.valueOf(valuationTaskVO.getTermNo()));
                    evaluatesVO.setName(valuationTaskVO.getAcademicYearNo() + "年" + terms[evaluatesVO.getTerm() % 2]+"季学期\t"+valuationTaskVO.getEisName());
                    evaluatesVO.setValue(taskListVOS);
                    result.add(evaluatesVO);
                }
            }
        }
        return R.ok().put("data", result);

    }
}
