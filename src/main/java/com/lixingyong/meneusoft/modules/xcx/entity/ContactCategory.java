package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("contact_category")
public class ContactCategory extends BaseEntity {
    private int id;

    private String name;

    private String url;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
