package com.lixingyong.meneusoft.modules.xcx.vo;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.vo
 * @Description TODO 图书搜索映射类
 * @Author mail@lixingyong.com
 * @Date 2019-03-20 15:45
 */
public class BookSearchVO{
    /** 当前页面 */
    private String curPage;
    /** 页面总数 */
    private String pageSize;
    /** 所包含的书 */
    List<SearchBooksVO> books;

    public String getCurPage() {
        return curPage;
    }

    public void setCurPage(String curPage) {
        this.curPage = curPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public List<SearchBooksVO> getBooks() {
        if(null == books){
            books = new LinkedList<>();
        }
        return books;
    }

    public void setBooks(List<SearchBooksVO> books) {
        this.books = books;
    }
}
