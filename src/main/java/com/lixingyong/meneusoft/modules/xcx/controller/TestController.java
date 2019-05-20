package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.jwc.JWCUtil;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseSchedule;
import com.lixingyong.meneusoft.modules.xcx.service.ClassroomService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseScheduleService;
import com.lixingyong.meneusoft.modules.xcx.service.CourseService;
import com.lixingyong.meneusoft.modules.xcx.service.ScheduledService;
import com.lixingyong.meneusoft.modules.xcx.utils.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private ClassroomService classroomService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseScheduleService courseScheduleService;
    @Autowired
    private ScheduledService scheduledService;
    @RequestMapping("/classroom")
    public void classroom(){
        LoginUtil.exeJWCLogin(1);
        List<Classroom> classroomList = JWCUtil.getClassroom(1);
        classroomService.insertOrUpdateBatch(classroomList);
    }

    @RequestMapping("/course")
    public void getCourse(){
        LoginUtil.exeJWCLogin(1);
        log.info("开始获取课程及课程课表信息");
        JWCUtil.getCourse(1);
    }

    @RequestMapping("/test")
    public void test(){
        try {
            Document document = Jsoup.parse(new File("F:\\mzm.gsp.activity.confbean.SActivityCfg.xml"), "UTF-8");
            Elements elements =document.getElementsByTag("mzm.gsp.activity.confbean.SActivityCfg");
            for(int i = 0; i < elements.size(); i++){
                elements.get(i).attr("couldBeSingleTeam", "true");
            }
            document.outputSettings(new Document.OutputSettings().syntax(Document.OutputSettings.Syntax.xml));
            String html=document.outerHtml();
            byte[] contentInBytes = html.getBytes();
            FileOutputStream fop = new FileOutputStream("F:\\1.xml");
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/contact")
    public void getContactBookData(){
        log.info("执行获取通讯录信息");
        scheduledService.getContactBooksAndTeachers();
        log.info("结束获取通讯录");
    }

    @RequestMapping("/computeGrade")
    public void getComputeGrade(){
        // 计算每一个课程的成绩平均值及课程评价
        log.info("开始计算课程的成绩平均值");
        scheduledService.computeCourse();
        log.info("结束计算课程的成绩平均值");
        log.info("开始计算课程评价");
        log.info("结束计算课程评价");
    }
}
