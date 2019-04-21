package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactCategory;
import com.lixingyong.meneusoft.modules.xcx.service.ContactBookService;
import com.lixingyong.meneusoft.modules.xcx.service.ContactCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName ContactController
 * @Description TODO 校园通讯录
 * @Author lixingyong
 * @Date 2018/12/12 15:13
 * @Version 1.0
 */

@Api("通讯录")
@RestController
@RequestMapping("/contact")
public class ContactController {
    @Autowired
    private ContactCategoryService contactCategoryService;
    @Autowired
    private ContactBookService contactBookService;
    @ApiOperation("获取通讯录类别")
    @RequestMapping("/categories")
    public R getCategories(){
        List<ContactCategory> categoryList = contactCategoryService.getCategories();
        return R.ok(categoryList);
    }

    @ApiOperation("获取详细通讯录信息")
    @RequestMapping("/item/{categoryId}")
    public R getContactBooks(@PathVariable("categoryId") int id){
        List<ContactBook> contactBooks = contactBookService.getContactBooks(id);
        return R.ok(contactBooks);
    }
}
