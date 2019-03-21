package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.LibraryBook;
import com.lixingyong.meneusoft.modules.xcx.entity.UserLibrary;
import com.lixingyong.meneusoft.modules.xcx.service.LibraryBookService;
import com.lixingyong.meneusoft.modules.xcx.service.UserLibraryService;
import com.lixingyong.meneusoft.modules.xcx.vo.BookSearchVO;
import com.lixingyong.meneusoft.modules.xcx.vo.DetailBookVO;
import io.micrometer.core.instrument.search.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    /**
     * @Author lixingyong
     * @Description //TODO 图书搜索
     * @Date 2019/3/20
     * @Param
     * @return
     **/
    @PostMapping("/search")
    public R booksSearch(@RequestParam Map<String,Object> params){
        try {
            BookSearchVO bookSearchVOS = libraryBookService.searchBook(params);
            if(null == bookSearchVOS){
                throw new WSExcetpion("获取数据有误，请稍候再试");
            }
            return R.ok().put("data", bookSearchVOS);
        }catch (WSExcetpion e){
            return R.error(e.getMsg());
        }
    }

    /**
     * @Author lixingyong
     * @Description //TODO 图书详细资料查询
     * @Date 2019/3/21
     * @Param
     * @return
     **/
    @PostMapping("/detailBook")
    public R detailBook(@RequestParam String detailId){
        try{
            DetailBookVO detailBookVO = libraryBookService.getBookDetail(detailId);
            if(null == detailId){
                throw new WSExcetpion("获取数据有误，请稍候再试");
            }
            if(null == detailBookVO){
                throw new WSExcetpion("无详细信息，请稍候重试");
            }
            return R.ok().put("details", detailBookVO);
        }catch (WSExcetpion e){
            return R.error(e.getMsg());
        }
    }
}
