package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.LibraryBook;
import com.lixingyong.meneusoft.modules.xcx.entity.UserLibrary;

import java.util.List;

public interface LibraryBookService extends IService<LibraryBook> {
    List<LibraryBook> getBooks(UserLibrary userLibrary, int isHistory);
}
