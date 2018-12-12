package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/12 15:25
 * @Version 1.0
 */
@RestController
@Api("用户")
public class UserController {

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R login(){
        return null;
    }

    @ApiOperation("绑定统一认证中心")
    @PostMapping("/bind")
    public R bind(){
        return null;
    }

    @ApiOperation("绑定教务处")
    @PostMapping("/jwc/bind")
    public R jwcBind(){
        return null;
    }

    @ApiOperation("绑定图书馆")
    @PostMapping("/library/bind")
    public R library(){
        return null;
    }

    @ApiOperation("获取当前用户反馈列表")
    @GetMapping("/user/feedbacks")
    public R getFeedbacks(){
        return null;
    }

    @ApiOperation("获取当前用户反馈列表")
    @GetMapping("/user/feedback/{id}")
    public R feedbacksInfo(@PathVariable int id){
        return null;
    }

    @ApiOperation("新增一条反馈信息")
    @PostMapping("/feedback")
    public R setFeedback(){
        return null;
    }

    @ApiOperation("新增一条反馈评论 :id 为反馈的id")
    @PostMapping("/feedback/comment/{id}")
    public R setFeedbackComment(){
        return null;
    }

    /**
     * | 二进制 | 十进制 | 说明           |
     * | 001    | 1      | 开启成绩通知   |
     * | 010    | 2      | 开启图书馆通知 |
     * | 100    | 4      | 开启考试通知   |
     */
    @ApiOperation("用户通知设置 二进制表示")
    @PostMapping("/user/config/notify")
    public R setUserConfigNotify(){
        return null;
    }

    @ApiOperation("获取用户通知设置")
    @GetMapping("/user/config/notify")
    public R getUserConfigNotify(){
        return null;
    }

    @ApiOperation("设置用户类型")
    @PostMapping("/user/config/type")
    public R getUserConfig(){
        return null;
    }

    @ApiOperation("缓存微信模板消息id")
    @PostMapping("/user/msg_id")
    public R setMsgId(){
        return null;
    }
}
