package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.github.GitHubUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.common.validator.Assert;
import com.lixingyong.meneusoft.common.validator.ValidatorUtils;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.Feedback;
import com.lixingyong.meneusoft.modules.xcx.service.FeedbackService;
import com.lixingyong.meneusoft.modules.xcx.vo.FeedBackParam;
import com.lixingyong.meneusoft.modules.xcx.vo.Issue;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
@Api("用户反馈")
@RestController
@Validated
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @ApiOperation("获取当前用户反馈列表")
    @GetMapping("/feedbacks")
    @Token
    public R getFeedbacks(@LoginUser String userId){
        List<Feedback> feedbackList = feedbackService.getFeedbacks(Integer.valueOf(userId));
        return R.ok("反馈列表获取成功！").put("data", feedbackList);
    }

    @ApiOperation("获取某一条反馈的详细信息以及评论")
    @GetMapping("/feedback/{id}")
    public R feedbacksInfo(@PathVariable int id){
        try {
            Map<String, Object> feedBackDetail = GitHubUtil.getFeedBackDetailInfo(id);
            // 将重新获取的反馈信息更新
            feedbackService.updateIssue(id, (Issue)feedBackDetail.get("issue"));
            return R.ok("获取反馈信息成功").put("data",feedBackDetail);
        }catch (WSExcetpion e){
            if(e.getCode() == 401){
                feedbackService.deleteIssue(id);
            }
            return R.error(e.getCode(),e.getMsg());
        }
    }

    @ApiOperation("新增一条反馈信息")
    @PostMapping("/feedback")
    @Token
    public R setFeedback(@ModelAttribute @Valid FeedBackParam params, @LoginUser String userId){
        // 验证用户的反馈频率，每人每天最多反馈五条
        int count = feedbackService.frequencyValidate(Integer.valueOf(userId));
        if(count >= 5){
            return R.error("您今天的反馈次数已经达到上限");
        }
        // 构造反馈内容
        String body = params.getContent() + "\n\n\n"
                + "MeNeusoft_version: " + params.getMeNeusoftVersion() + "\n"
                + String.format("手机：%s-%s-%s \n", params.getBrand(), params.getModel(), params.getSystem())
                + String.format("微信：%s，SDK：%s \n", params.getVersion(), params.getSdkVersion());
        // 向github新建反馈信息
        int number = GitHubUtil.createdIssues(params.getTitle(), body, new String[]{params.getLabel(), "用户反馈"});
        if(number != -1){
            // 将反馈信息保存在数据库中
            feedbackService.saveFeedBackInfo(Integer.valueOf(userId), number, params.getTitle(), "open", params.getLabel()+ ",用户反馈");
            return R.ok("反馈成功");
        }
        return R.error("反馈失败");
    }

    @ApiOperation("新增一条反馈评论 :id 为反馈的id")
    @PostMapping("/feedback/comment/{id}")
    @Token
    public R setFeedbackComment(@PathVariable("id") String issueId,@NotBlank(message = "评论内容不能为空") @RequestParam("content") String  content){
        if(GitHubUtil.addIssueComment(issueId, content)){
            return R.ok("发布评论成功");
        }
       return R.error("发布评论失败");
    }
}
