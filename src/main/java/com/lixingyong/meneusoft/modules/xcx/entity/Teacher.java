package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("teacher")
public class Teacher extends BaseEntity {
    private int id;
    @TableId(type= IdType.ID_WORKER_STR)
    private String name;

    private String jobTitle;

    private String college;

    private String url;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
