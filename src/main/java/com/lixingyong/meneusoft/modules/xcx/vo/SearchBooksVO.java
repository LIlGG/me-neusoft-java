package com.lixingyong.meneusoft.modules.xcx.vo;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.vo
 * @Description TODO
 * @Author mail@lixingyong.com
 * @Date 2019-03-20 17:25
 */
public class SearchBooksVO {
    private String cover;
    /** 书名 */
    private String title;
    /** 出版社 */
    private String press;
    /** 作者 */
    private String author;
    /** 出版年份 */
    private String publish_year;
    /** 图书详细地址ID */
    private String detailId;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublish_year() {
        return publish_year;
    }

    public void setPublish_year(String publish_year) {
        this.publish_year = publish_year;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}
