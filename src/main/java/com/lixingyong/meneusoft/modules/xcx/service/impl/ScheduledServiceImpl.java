package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.lixingyong.meneusoft.api.contact.ContactUtil;
import com.lixingyong.meneusoft.api.neusoft.NeusoftUtil;
import com.lixingyong.meneusoft.api.news.NewsUtil;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.*;
import com.lixingyong.meneusoft.modules.xcx.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    @Override
    public void getContactBooksAndTeachers() {
        // 获取通讯录分类内容
        List<ContactCategory> contactCategories = contactCategoryService.getCategories();
        for(ContactCategory contactCategory : contactCategories){
            List<ContactBook> contactBooks = new ArrayList<>();
            List<Teacher> teachers = ContactUtil.getContactBooksAndTeachers(contactCategory, contactBooks );
            contactBookService.insertOrUpdateContactBooks(contactBooks);
            teacherService.insertOrUpdateTeachers(teachers);
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
