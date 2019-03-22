package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.entity
 * @Description TODO 资讯实体类
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 16:30
 */
@TableName("detail")
@Data
public class Detail extends BaseEntity {
    @TableId(type=IdType.INPUT)
    private Long id;
    /** 资讯作者 */
    private String author;
    /** 资讯标题 */
    private String title;
    /** 资讯内容 */
    private String content;
    /** 资讯网页链接 */
    private String url;
    /** 资讯分类 */
    private String category;
    /** 资讯发布日期 */
    private Date newCreatedAt;
    /** 资讯的标签 */
    @TableField(exist = false)
    private List<Tag> tags;
    @Override
    protected Serializable pkVal() {
        return this.getId();
    }

}
