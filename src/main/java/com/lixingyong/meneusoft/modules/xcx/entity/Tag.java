package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.entity
 * @Description TODO
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 16:39
 */
@TableName("tag")
public class Tag extends BaseEntity {
    /** 资讯名称 */
    private String name;
    /** 资讯所包含的资讯 */
    private List<Detail> details;
    @Override
    protected Serializable pkVal() {
        return super.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
