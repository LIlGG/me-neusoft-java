package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.entity
 * @Description TODO
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 16:39
 */
@TableName("tag")
@Data
public class Tag extends BaseEntity {
    @TableId
    private int id;
    /** 标签名称 */
    private String name;
    /** 标签所属URL */
    private String url;
    /** 标签类型 */
    @TableField(exist = false)
    private String type = "org";
    /** 标签所包含的资讯 */
    @TableField(exist = false)
    private List<Detail> details;

    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
