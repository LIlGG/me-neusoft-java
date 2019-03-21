package com.lixingyong.meneusoft.modules.xcx.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.vo
 * @Description TODO
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 11:00
 */
public class DetailBookVO {
    /** 索书号 */
    private String number;

    private List<CollectBook> book_addresses;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<CollectBook> getBook_addresses() {
        if(null == book_addresses){
            return new ArrayList<>();
        }
        return book_addresses;
    }

    public void setBook_addresses(List<CollectBook> book_addresses) {
        this.book_addresses = book_addresses;
    }
}
