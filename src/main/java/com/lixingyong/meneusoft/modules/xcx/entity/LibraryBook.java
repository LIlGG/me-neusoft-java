package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.entity
 * @Description TODO
 * @Author mail@lixingyong.com
 * @Date 2019-03-18 11:45
 */
@TableName("library_book")
@Data
public class LibraryBook extends BaseEntity {
    @TableField(exist = false)
    private int id;

    private int userId;
    /** 是否为历史借阅 0： 否 1： 是 */
    private int isHistory;
    /** 到期时间 */
    private Date dueTime;
    /** 书籍ID，用于借阅 */
    private String bookId;
    /** 图书作者信息 */
    private String author;
    /**  书名 */
    private String title;
    /** 出版年份 */
    private int publishYear;
    /** 到期日期 */
    private String dueDate;
    /** 归还日期 */
    private String returnDate;
    /** 归还时间 */
    private String returnTime;
    /** 欠费金额 */
    private float arrearage;
    /** 图书馆地址 */
    private String address;
    /**索书号 */
    @TableId(value = "number", type = IdType.INPUT)
    private String number;
    @Override
    protected Serializable pkVal() {
        return super.id;
    }
}
