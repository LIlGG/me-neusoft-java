package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.lixingyong.meneusoft.api.contact.ContactUtil;
import com.lixingyong.meneusoft.api.news.NewsUtil;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactCategory;
import com.lixingyong.meneusoft.modules.xcx.entity.Lecture;
import com.lixingyong.meneusoft.modules.xcx.entity.Teacher;
import com.lixingyong.meneusoft.modules.xcx.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
