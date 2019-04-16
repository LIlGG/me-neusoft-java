package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;;
import com.lixingyong.meneusoft.modules.xcx.dao.ContactBookDao;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;
import com.lixingyong.meneusoft.modules.xcx.service.ContactBookService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ContactBookServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("contactBookService")
public class ContactBookServiceImpl extends ServiceImpl<ContactBookDao, ContactBook> implements ContactBookService {

    @Override
    public void insertOrUpdateContactBooks(List<ContactBook> contactBooks) {
        this.insertOrUpdateBatch(contactBooks);
    }

    @Override
    public List<ContactBook> getContactBooks(int id) {
        return this.selectList(new EntityWrapper<ContactBook>().eq("contact_category_id", id));
    }
}
