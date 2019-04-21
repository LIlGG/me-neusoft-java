package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("trem")
public class Term  extends BaseEntity {
    private int id;

    private String name;

    private Date startTime;

    private Date endTime;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
