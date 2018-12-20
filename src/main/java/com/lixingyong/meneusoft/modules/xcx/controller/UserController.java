package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.wx.WxUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.JwtUtils;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.entity.Wechat;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import com.lixingyong.meneusoft.modules.xcx.service.WechatService;
import com.lixingyong.meneusoft.modules.xcx.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private WechatService wechatService;
    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R login(@RequestParam String code){
        Map<String,Object> result;
        Map<String,Object> userInfo = null;
        Wechat wechat = new Wechat() ;
        int userId;
        System.out.println("获取到的code"+code);
        try {
            userInfo = WxUtil.code2Session(code);
            wechat = wechatService.getWechat((String)userInfo.get("openid"));

        }catch (WSExcetpion s){
            return R.error(s.getMsg());
        }
        // openid是否已经存在，根据其获取userId
        userId = wechat != null ? wechat.getUserId() : userService.createUser();

        //存在用户或已经新建用户，则准备更新数据
        if(null == wechat){
            wechat = new Wechat();
        }
        wechat.setSessionKey((String)userInfo.get("session_key"));
        wechat.setOpenid((String)userInfo.get("openid"));
        wechat.setUserId(userId);
        wechatService.insertOrUpdate(wechat);
        LoginVO loginVO = userService.login(userId);
        return R.ok().put("data",loginVO);
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
