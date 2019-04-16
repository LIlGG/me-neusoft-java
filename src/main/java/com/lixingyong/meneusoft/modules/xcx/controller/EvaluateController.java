package com.lixingyong.meneusoft.modules.xcx.controller;

import com.google.gson.Gson;
import com.lixingyong.meneusoft.api.evaluate.EvaluateUtil;
import com.lixingyong.meneusoft.api.evaluate.VO.EvaluateVO;
import com.lixingyong.meneusoft.api.evaluate.VO.HiddenInput;
import com.lixingyong.meneusoft.api.evaluate.VO.TaskListVO;
import com.lixingyong.meneusoft.api.evaluate.VO.ValuationTaskVO;
import com.lixingyong.meneusoft.api.evaluate.util.ValuationUtil;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.vo.Evaluate;
import com.lixingyong.meneusoft.modules.xcx.vo.EvaluatesVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
                    evaluatesVO.setVersion(valuationTaskVO.getVersionNo());
                    result.add(evaluatesVO);
                }
            }
        }
        return R.ok().put("data", result);
    }

    /**
     * 获取当前评教任务的详细内容
     */
    @ApiOperation("获取评教列表")
    @PostMapping("/evaluate/{taskId}")
    @Token
    public R getEvaluates(@PathVariable("taskId") int taskId, @RequestParam Map<String, Object> bodys, @LoginUser String userId){
        EvaluatesVO evaluatesVO = new Gson().fromJson((String) bodys.get("data"), EvaluatesVO.class);
        // 根据获取到的数据，请求网址并封装问题详情
        String url = ValuationUtil.taskTargetUrl(evaluatesVO.getEisId(), evaluatesVO.getPEisId(), evaluatesVO.getVersion());
        // 通过当前url和参数，请求问题列表
        Evaluate evaluate = EvaluateUtil.getTaskIssue(evaluatesVO, url + "&taskId={taskId}", taskId, Long.parseLong(userId));
        if(null != evaluate){
            for(TaskListVO taskListVO : evaluatesVO.getValue()){
                if(Integer.valueOf(taskListVO.getTaskId()) == taskId){
                    evaluate.setCourseName(taskListVO.getCourseName());
                    evaluate.setTeacherName(taskListVO.getTeacherName());
                }
            }
            return R.ok("获取评教信息成功").put("data", evaluate);
        }
        return R.error("获取评教问题失败");
    }


    /**
     * 用户提交的评教信息
     */
    @ApiOperation("用户提交评教")
    @PostMapping("/evaluate")
    @Token
    public R evaluate(@RequestParam Map<String, Object> params, @LoginUser String userId){
        List hiddenInputs = new Gson().fromJson(params.get("hiddlers").toString() , List.class);
        // 保存当前的评价
        return null;
    }
}
