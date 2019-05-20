package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.lixingyong.meneusoft.api.contact.ContactUtil;
import com.lixingyong.meneusoft.api.neusoft.NeusoftUtil;
import com.lixingyong.meneusoft.api.news.NewsUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.common.utils.Params;
import com.lixingyong.meneusoft.modules.xcx.entity.*;
import com.lixingyong.meneusoft.modules.xcx.service.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service("scheduledService")
public class ScheduledServiceImpl implements ScheduledService {
    @Autowired
    private ContactCategoryService contactCategoryService;
    @Autowired
    private ContactBookService contactBookService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private LectureService lectureService;
    @Autowired
    private TermService termService;
    @Autowired
    private TermEventService termEventService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private CourseEvaluateService courseEvaluateService;
    @Autowired
    private CourseCountService courseCountService;
    @Autowired
    private CourseGradeService courseGradeService;
    @Override
    public void getContactBooksAndTeachers() {
        // 获取通讯录分类内容
        List<ContactCategory> contactCategories = contactCategoryService.getCategories();
        for(ContactCategory contactCategory : contactCategories){
            List<ContactBook> contactBooks = new ArrayList<>();
            List<Teacher> teachers = ContactUtil.getContactBooksAndTeachers(contactCategory, contactBooks );
            try {
                log.info("开始更新" + contactCategory.getName() + "的通讯录");
                contactBookService.insertOrUpdateContactBooks(contactBooks);
                teacherService.insertOrUpdateTeachers(teachers);
            }catch (Exception e){
                log.error("更新" + contactCategory.getName()+"通讯录信息失败");
            }
        }
    }

    @Override
    public void getLectures() {
        // 获取学术讲座的页面数量
        int lecturePageCount = NewsUtil.getLecturePageCount(0);
        if(lecturePageCount > 0){
            // 获取学术讲座内容
            for(int i = 1 ; i <= lecturePageCount; i++){
                // 获取一整页的学术讲座
                List<Lecture> lectures = NewsUtil.getLectures(i);
                lectureService.insertOrUpdateLectures(lectures);
            }
        }
    }

    @Override
    public void getTerms() {
        // 初次更新教务系统的校历信息
        Map<Integer, String> termOptionList = NeusoftUtil.termList();
        List<Term> termList = new LinkedList<>();
        for(Map.Entry<Integer, String> termOption :  termOptionList.entrySet()){
            // 遍历获取每个学期的起始日期和结束日期
            Term term = NeusoftUtil.term(termOption.getKey(), termOption.getValue());
            termList.add(term);
            // 读取每个学期的事件
            getTermEvents(term);
        }
        termService.addTermList(termList);
    }

    @Override
    public void computeCourse() {
        // 查询所有成绩数据
        List<Course> courseList = courseService.getCourseAll();
        List<CourseGrade> courseGradeList = new ArrayList<>();
        List<CourseCount> courseCountList = new ArrayList<>();
        int i = 1;
        for(;i < courseList.size(); i++){
            Course course = courseList.get(i);
            // 获取本年度当前课程的所有成绩信息(根据课程号)
            List<Grade> grades = gradeService.getGradeList(course.getCourseId());
            CourseCount courseCount = new CourseCount();
            if(!grades.isEmpty()){ // 如果当前课程有成绩信息
                // 创建课程信息
                CourseGrade courseGrade = new CourseGrade();
                int g0 = 0, g60 = 0, g70 = 0, g80 = 0, g90 = 0;
                int gradeAll = 0;
                int fail = 0;
                float allGrade = 0;
                for(Grade grade : grades) {
                    gradeAll++;
                    allGrade += grade.getGrade();
                    int g = Integer.valueOf(String.valueOf(grade.getGrade() / 10f).split("\\.")[0]);
                    switch (g) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            fail++;
                            g0++;
                            break;
                        case 6:
                            g60++;
                            break;
                        case 7:
                            g70++;
                            break;
                        case 8:
                            g80++;
                            break;
                        case 9:
                        case 10:
                            g90++;
                            break;
                    }
                }
                courseGrade.setId(course.getId());
                courseGrade.setCourseId(course.getId());
                courseGrade.setG0(g0);
                courseGrade.setG60(g60);
                courseGrade.setG70(g70);
                courseGrade.setG80(g80);
                courseGrade.setG90(g90);
                courseGrade.setLessonId(course.getLessonId());


                if(gradeAll != 0){
                    courseCount.setFailRate(fail / (float)gradeAll);
                    if(allGrade != 0){
                        courseCount.setAvgGrade(allGrade / gradeAll);
                    }else {
                        courseCount.setAvgGrade(0);
                    }
                } else {
                    courseCount.setFailRate(0);
                }
                courseCount.setId(course.getId());
                courseCount.setCourseId(course.getId());
                courseCount.setGradeAll(gradeAll);
                courseCount.setLessonId(course.getLessonId());
                courseGradeList.add(courseGrade);
            }

            // 获取当前课程的所有评价信息
            List<CourseEvaluate> courseEvaluateList = courseEvaluateService.getCourseEvaluateList(course.getId());
            if(!courseEvaluateList.isEmpty()){
                int good = 0, normal = 0, bad = 0, flag = 0;
                for(CourseEvaluate  courseEvaluate : courseEvaluateList){
                    flag ++;
                    switch (courseEvaluate.getAssess()){
                        case 1:
                            good ++;
                            break;
                        case 2:
                            normal ++;
                            break;
                        case 3:
                            bad ++;
                            break;
                    }
                }
                courseCount.setStar((good + normal + bad) / (float)flag);
                courseCount.setGood(good);
                courseCount.setNormal(normal);
                courseCount.setBad(bad);
            }

            if(!grades.isEmpty() || !courseEvaluateList.isEmpty()){
                courseCountList.add(courseCount);
            }

            if(i % 100 == 0){
                // 每100条数据保存一次
                if(!courseGradeList.isEmpty()){
                    courseGradeService.insertOrUpdateCourseGradeAll(courseGradeList);
                }
                if(!courseCountList.isEmpty()){
                    courseCountService.insertOrUpdateCourseCountAll(courseCountList);
                }
                courseCountList = new ArrayList<>();
                courseGradeList = new ArrayList<>();
            }
        }
        if(!courseGradeList.isEmpty()){
            courseGradeService.insertOrUpdateCourseGradeAll(courseGradeList);
        }
        if(!courseCountList.isEmpty()){
            courseCountService.insertOrUpdateCourseCountAll(courseCountList);
        }
    }

    private void getTermEvents(Term term) {
        // 获取当前学期之间的月份
        try {
            List<TermEvent> termEvents = new ArrayList<>();
            List<String> dates = DateUtils.getMonthBetween(term.getStartTime(), term.getEndTime());
            // 根据当前学期，获取年份和月份
            for(String date : dates){
                List<TermEvent> list;
                while(true){
                    list  = NeusoftUtil.getTermEvents(term.getId(), date);
                    if(list != null){
                        break;
                    }
                }
                termEvents.addAll(list);
            }
            try {
                termEventService.addTermEvents(termEvents);
            }catch (IllegalArgumentException e){
                log.info(term.getName() + "没有查询到具体事件");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
