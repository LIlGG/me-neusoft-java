package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Notice
 * @Description TODO 通知实体类
 * @Author lixingyong
 * @Date 2018/12/11 11:18
 * @Version 1.0
 */

@TableName("notice")
@Data
public class Notice extends BaseEntity {
    @TableId
    private int id;
    private String title;

    private String cover;

    @TableField(value = "abstract")
    private String abstracts;

    private String content;

    private int status;

    private int newest;
    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
