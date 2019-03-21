package com.lixingyong.meneusoft.modules.xcx.vo;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.vo
 * @Description TODO
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 11:51
 */
public class CollectBook {
    /** 图书馆地址 */
    private String address;

    /** 当前图书状态 */
    private String bookStatus;

    /** 应还日期 */
    private String dueData;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getDueData() {
        return dueData;
    }

    public void setDueData(String dueData) {
        this.dueData = dueData;
    }
}
