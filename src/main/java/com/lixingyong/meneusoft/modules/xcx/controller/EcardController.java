package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.Ecard;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.service.EcardService;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
@Api("一卡通")
@RestController("/ecard")
public class EcardController {
    @Autowired
    private EcardService ecardService;
    @Autowired
    private UserService userService;
    @Token
    @ApiOperation("更新一卡通信息")
    @PostMapping("/update")
    public R updateEcard(@LoginUser String userId){
         // 获取当前用户的一卡通信息
        User user =  userService.getUserInfo(Integer.valueOf(userId));
        if(!user.getEcardId().equals("")){
            ecardService.updateEcards(user);
            return R.ok("更新成功");
        }
        return R.error("更新一卡通信息失败");
    }

    @Token
    @ApiOperation("获取一卡通信息")
    @GetMapping("/getEcards")
    public R getEcards(@LoginUser String userId){
        List<Ecard> ecards = new LinkedList<>();
        ecards = ecardService.getEcardList(userId);
        return R.ok(ecards);
    }
}
