package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.modules.xcx.service.ScheduledService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务控制层
 */
@Slf4j
@Component
public class ScheduledController {
    @Autowired
    private ScheduledService scheduledService;

    /**
     * 定时更新教师通讯录及老师内容（一周更新一次）
     */
    @Scheduled(cron = "0 0 16 */7 * ?")
    public void getContactBookData(){
        log.info("执行获取通讯录信息");
        scheduledService.getContactBooksAndTeachers();
        log.info("结束获取通讯录");
    }
}
