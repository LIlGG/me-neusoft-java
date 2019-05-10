package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.entity.Lecture;
import com.lixingyong.meneusoft.modules.xcx.service.LectureService;
import com.lixingyong.meneusoft.modules.xcx.service.ScheduledService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Api("学术讲座")
@RestController
@Validated
public class LectureController {
    @Autowired
    private LectureService lectureService;
    // 获取学术讲座列表
    @RequestMapping("/lectures")
    public R getLectures(@RequestParam("page") int page, @RequestParam("page_size") int pageSize, @RequestParam("start_time") String startTime){
        List<Lecture> lectureList =  lectureService.getLectures(null,page, pageSize, startTime);
        return R.ok(lectureList);
    }

    @PostMapping("/lectures/search")
    public R searchLecture(@NotBlank(message = "标题不能为空") @RequestParam("title") String title, @RequestParam("page") int page, @RequestParam("page_size") int pageSize, @RequestParam("start_time") String startTime){
        List<Lecture> lectureList = lectureService.getLectures(title, page, pageSize, startTime);
        return R.ok(lectureList);
    }
}
