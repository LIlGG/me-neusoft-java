package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.api.library.LibraryUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.dao.LibraryBookDao;
import com.lixingyong.meneusoft.modules.xcx.entity.LibraryBook;
import com.lixingyong.meneusoft.modules.xcx.entity.UserLibrary;
import com.lixingyong.meneusoft.modules.xcx.service.LibraryBookService;
import com.lixingyong.meneusoft.modules.xcx.utils.LoginUtil;
import com.lixingyong.meneusoft.modules.xcx.vo.BookSearchVO;
import com.lixingyong.meneusoft.modules.xcx.vo.DetailBookVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName LibraryBookServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("libraryBookService")
public class LibraryBookServiceImpl extends ServiceImpl<LibraryBookDao, LibraryBook> implements LibraryBookService {
    @Override
    public List<LibraryBook> getBooks(UserLibrary userLibrary, int isHistory) {
        // 执行登录图书馆
        LibraryUtil.libraryLogin((long) userLibrary.getUserId(), userLibrary.getStudentId(), userLibrary.getPassword());
        // 首先获取图书馆系统中的最新信息
        List<LibraryBook> libraryBookList = LibraryUtil.getHistoryBooks((long) userLibrary.getUserId());
        if(null != libraryBookList && libraryBookList.size() > 0){
            // 更新数据库中当前用户的所有数据
            this.insertOrUpdateBatch(libraryBookList);
        }
        libraryBookList = this.selectList(new EntityWrapper<LibraryBook>().eq("is_history",isHistory));
        // 获取成功则更新数据库的信息
        // 获取失败则直接取出数据库中的数据
        return libraryBookList;
    }

    @Override
    public BookSearchVO searchBook(Map<String, Object> params) throws WSExcetpion {
        String keyword = (String) params.get("keyword");
        if(StringUtils.isNotBlank(keyword)){
            if(keyword.length() < 3){
                throw new WSExcetpion("书名检索长度需大于2");
            }
            // 执行搜索功能
            BookSearchVO books =  LibraryUtil.bookSearch((String)params.get("keyword"),Integer.valueOf(params.get("cur_page").toString()));
            return books;
        } else {
            throw new WSExcetpion("请输入书名");
        }
    }

    @Override
    public DetailBookVO getBookDetail(String detailId) throws WSExcetpion {
        DetailBookVO detailBookVO = LibraryUtil.detailBook(detailId);
        return detailBookVO;
    }

    @Override
    public void delLibraryAll(Integer userId) {
        this.baseMapper.delete(new EntityWrapper<LibraryBook>().eq("user_id", userId));
    }
}
