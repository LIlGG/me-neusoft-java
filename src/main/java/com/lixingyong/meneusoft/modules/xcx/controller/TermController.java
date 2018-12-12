package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName TermControlller
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/12 15:58
 * @Version 1.0
 */
@RestController
@Api("校历")
public class TermController {

    @ApiOperation("获取当前学期")
    @GetMapping("/term")
    public R term(){
        return null;
    }

    @ApiOperation("获取当前学期事件")
    @GetMapping("/term/events")
    public R termEvents(){
        return null;
    }
}
