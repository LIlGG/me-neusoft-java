package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.lixingyong.meneusoft.api.contact.ContactUtil;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactCategory;
import com.lixingyong.meneusoft.modules.xcx.entity.Teacher;
import com.lixingyong.meneusoft.modules.xcx.service.ContactBookService;
import com.lixingyong.meneusoft.modules.xcx.service.ContactCategoryService;
import com.lixingyong.meneusoft.modules.xcx.service.ScheduledService;
import com.lixingyong.meneusoft.modules.xcx.service.TeacherService;
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
}
