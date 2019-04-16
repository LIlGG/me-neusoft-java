package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.ContactCategoryDao;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactCategory;
import com.lixingyong.meneusoft.modules.xcx.service.ContactCategoryService;
import org.springframework.stereotype.Service;

;import java.util.List;

/**
 * @ClassName ContactCategoryServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("contactCategoryService")
public class ContactCategoryServiceImpl extends ServiceImpl<ContactCategoryDao, ContactCategory> implements ContactCategoryService {
    @Override
    public List<ContactCategory> getCategories() {
        return this.baseMapper.selectList(new EntityWrapper<>());
    }
}
