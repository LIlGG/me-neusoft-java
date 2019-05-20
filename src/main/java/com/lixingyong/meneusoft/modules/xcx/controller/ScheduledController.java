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
     * 定时更新教师通讯录及老师内容（一周更新一次，更新时间为下午16点）
     */
    @Scheduled(cron = "0 0 16 */7 * ?")
    public void getContactBookData(){
        log.info("执行获取通讯录信息");
        scheduledService.getContactBooksAndTeachers();
        log.info("结束获取通讯录");
    }

    /**
     * 定时更新讲座信息（一天更新一次，更新时间为每天12点）
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void getLectureData(){
        log.info("执行更新讲座信息");
        scheduledService.getLectures();
        log.info("结束更新讲座信息");
    }

    /**
     * 定时更新校历（每月月初更新一次，更新时间为晚上0点整）
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void getTermData(){
        log.info("执行更新校历信息");
        scheduledService.getTerms();
        log.info("结束更新校历信息");
    }

    /**
     * 定时更新成绩及课程统计（一天更新一次，更新时间为每天3点）
     */
    @Scheduled(cron = "0 0 3 * * ?")
    public void getComputeGrade(){
        // 计算每一个课程的成绩平均值及课程评价
        log.info("开始计算课程的成绩平均值");
        scheduledService.computeCourse();
        log.info("结束计算课程的成绩平均值");
        log.info("开始计算课程评价");
        log.info("结束计算课程评价");
    }
}
