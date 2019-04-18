package com.lixingyong.meneusoft.modules.xcx.controller;


import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.entity.Feedback;
import com.lixingyong.meneusoft.modules.xcx.entity.HelpItem;
import com.lixingyong.meneusoft.modules.xcx.service.HelpItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("用户帮助")
@RestController
public class HelpItemController {
    @Autowired
    private HelpItemService helpItemService;

    @ApiOperation("获取当前用户反馈列表")
    @GetMapping("/helps")
    public R getHelps(){
       List<HelpItem> helpList =  helpItemService.getHelpItemList();
       return R.ok("获取帮助列表成功！").put("data", helpList);
    }
}
