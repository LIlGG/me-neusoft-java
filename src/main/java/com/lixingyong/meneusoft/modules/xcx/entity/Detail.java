package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.entity
 * @Description TODO 资讯实体类
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 16:30
 */
@TableName("detail")
public class Detail extends BaseEntity {
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
    /** 资讯的标签 */
    @TableField(exist = false)
    private List<Tag> tags;
    @Override
    protected Serializable pkVal() {
        return super.id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
