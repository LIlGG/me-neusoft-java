package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;

import java.util.List;


public interface ContactBookService extends IService<ContactBook> {

    void insertOrUpdateContactBooks(List<ContactBook> contactBooks);

    List<ContactBook> getContactBooks(int id);
}
