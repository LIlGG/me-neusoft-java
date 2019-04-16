package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactCategory;

import java.util.List;


public interface ContactCategoryService extends IService<ContactCategory> {

    List<ContactCategory> getCategories();
}
