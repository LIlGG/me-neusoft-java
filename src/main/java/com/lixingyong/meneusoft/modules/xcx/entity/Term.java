package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("term")
public class Term  extends BaseEntity {
    @TableId(type= IdType.INPUT)
    private int id;

    private String name;

    private Date startTime;

    private Date endTime;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
