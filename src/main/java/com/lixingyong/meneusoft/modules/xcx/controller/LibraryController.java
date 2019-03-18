package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.LibraryBook;
import com.lixingyong.meneusoft.modules.xcx.entity.UserLibrary;
import com.lixingyong.meneusoft.modules.xcx.service.LibraryBookService;
import com.lixingyong.meneusoft.modules.xcx.service.UserLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.controller
 * @Description TODO 图书馆控制层
 * @Author mail@lixingyong.com
 * @Date 2019-03-18 11:59
 */
@RestController
@RequestMapping("/library")
public class LibraryController {
    @Autowired
    private LibraryBookService libraryBookService;
    @Autowired
    private UserLibraryService userLibraryService;
    /**
     * @Author lixingyong
     * @Description //TODO 获取用户已经借阅的图书
     * @Date 2019/3/18
     * @Param
     * @return
     **/
    @Token
    @RequestMapping("/books")
    public R books(@RequestParam("is_history") int isHistory, @LoginUser String userId) {
        // 首先判断用户的图书馆账号是否能够成功登录
        UserLibrary userLibrary = new UserLibrary();
        userLibrary.setUserId(Integer.valueOf(userId));
        try {
            userLibrary = userLibraryService.getUserAccount(userLibrary);
            if(null != userLibrary.getStudentId() && !userLibrary.getStudentId().equals("")){
                List<LibraryBook> list = libraryBookService.getBooks(userLibrary, isHistory);
                return R.ok(list);
            }
        }catch (WSExcetpion e){
            return R.error(e.getMsg());
        }
        return R.error();
    }
}
