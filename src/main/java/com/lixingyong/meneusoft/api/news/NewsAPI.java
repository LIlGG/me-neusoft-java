package com.lixingyong.meneusoft.api.news;

/**
 * @ClassName com.lixingyong.meneusoft.api.news
 * @Description TODO 新闻中心API(无需内网登录)
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 16:52
 */
public final class NewsAPI {
    /** 新闻中心首页 */
    public static final String NEWS_HOME = "http://news.neusoft.edu.cn/";

    /** 新闻中心新闻页面 */
    public static final String NEWS = NEWS_HOME + "news/";

    /** 学术讲座通知页面 */
    public static final String LECTURE = NEWS + "event/lecture/";
}
