package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.entity.Notice;
import com.lixingyong.meneusoft.modules.xcx.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @ClassName NoticesController
 * @Description TODO 通知或者公告控制层
 * @Author lixingyong
 * @Date 2018/12/11 11:26
 * @Version 1.0
 */
@Api("通知")
@RestController
public class NoticesController {
    @Autowired
    private NoticeService noticeService;

    @ApiOperation("获取banner信息，无需登录")
    @GetMapping(value = "/notices")
    public R getBanner(){
        List<Notice> notices = noticeService.getNoticeBanner();
        if(notices.size() >  0){
            return R.ok(notices);
        }
        return R.error(501,"轮播信息不存在");
    }

    @ApiOperation("获取id=:id的通知详情")
    @GetMapping(value = "/notice/{id}")
    public R getNoticeInfo(@PathVariable int id){
        Notice notice = noticeService.noticeInfo(id);
        if( null != notice){
            return R.ok(notice);
        }
        return R.error(503,"查询通知详情失败");
    }

    @ApiOperation("获取最新通知公告，这个通知会全局弹窗展示")
    @GetMapping(value = "/notice/new")
    public R getNewNotice(){
        Notice notice = noticeService.getNewNotice();
        if(null != notice){
            return R.ok(notice);
        }
        return R.error(502,"无最新公告");
    }

}
