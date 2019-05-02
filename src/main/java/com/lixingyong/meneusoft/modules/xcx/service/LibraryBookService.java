package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.LibraryBook;
import com.lixingyong.meneusoft.modules.xcx.entity.UserLibrary;
import com.lixingyong.meneusoft.modules.xcx.vo.BookSearchVO;
import com.lixingyong.meneusoft.modules.xcx.vo.DetailBookVO;

import java.util.List;
import java.util.Map;

public interface LibraryBookService extends IService<LibraryBook> {
    List<LibraryBook> getBooks(UserLibrary userLibrary, int isHistory);

    BookSearchVO searchBook(Map<String, Object> params);

    DetailBookVO getBookDetail(String detailId);

    void delLibraryAll(Integer valueOf);
}
