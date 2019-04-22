package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.neusoft.NeusoftUtil;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.entity.Term;
import com.lixingyong.meneusoft.modules.xcx.entity.TermEvent;
import com.lixingyong.meneusoft.modules.xcx.service.ScheduledService;
import com.lixingyong.meneusoft.modules.xcx.service.TermEventService;
import com.lixingyong.meneusoft.modules.xcx.service.TermService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @ClassName TermControlller
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/12 15:58
 * @Version 1.0
 */
@Api("校历")
@RestController
public class TermController {
    @Autowired
    private TermService termService;
    @Autowired
    private ScheduledService scheduledService;
    @Autowired
    private TermEventService termEventService;
    @ApiOperation("获取当前学期")
    @GetMapping("/term")
    public R term(){
        //根据当前日期获取信息
        Term term = termService.getCurTermInfo();
        return R.ok(term);
    }

    @ApiOperation("获取当前学期事件")
    @GetMapping("/term/events")
    public R termEvents(){
        // 获取当前学期
        Term term = termService.getCurTermInfo();
        // 查询当前学期的事件
        List<TermEvent> termEvents = termEventService.getCurTermEvents(term.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("events", termEvents);
        map.put("term", term);
        return R.ok().put("data", map);
    }
}
