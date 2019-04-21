package com.lixingyong.meneusoft.api.news;

import com.lixingyong.meneusoft.api.utils.RestUtils;
import com.lixingyong.meneusoft.api.vpn.VPNUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.Detail;
import com.lixingyong.meneusoft.modules.xcx.entity.Lecture;
import com.lixingyong.meneusoft.modules.xcx.entity.Tag;
import com.lixingyong.meneusoft.modules.xcx.service.TagService;
import com.lixingyong.meneusoft.modules.xcx.service.impl.TagServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName com.lixingyong.meneusoft.api.news
 * @Description TODO 新闻中心网络请求工具类
 * @Author mail@lixingyong.com
 * @Date 2019-03-21 16:53
 */

public class NewsUtil {
    private static TagService tagService = RestUtils.getTagService();
    private static RestTemplate restTemplate = RestUtils.getRestTemplate();
    private static RedisUtils redisUtils = RestUtils.getRedisUtils();
    private static Map<String,Object> map = new HashMap<>();
    private static Logger logger = LoggerFactory.getLogger(VPNUtil.class);
    /**
     * @Author lixingyong
     * @Description //TODO 根据URL，获取新闻列表
     * @Date 2019/3/21
     * @Param
     * @return
     **/
    public static List<Detail> getNewsList(String url){
        HttpEntity<String> request = new HttpEntity<>(null,null);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            String html = responseEntity.getBody();
            // 对当前页面进行处理（目前仅限文字页面）
            List<Detail> details = new LinkedList<>();
            Document document = Jsoup.parse(html);
            Elements articles = document.select(".archive-list").first().select("article");
            if(articles != null){
                for (int i = 0; i < articles.size(); i++) {
                    Detail detail = new Detail();
                    detail.setUrl(articles.get(i).select("a").first().attr("href"));
                    try {
                        String str = detail.getUrl().substring(detail.getUrl().indexOf("2"), detail.getUrl().lastIndexOf("."));
                        String[] ids = str.split("/");
                        String id = "";
                        for(String s: ids){
                            id = id+s;
                        }
                        detail.setId(Long.parseLong(id));
                    }catch (Exception e){
                        continue;
                    }
                    detail.setTitle(articles.get(i).select(".title").first().select("a").last().text());
                    detail.setContent("");
                    detail.setAuthor("");
                    detail.setCategory(articles.get(i).select(".category").first().text().split(" ")[1]);
                    detail.setNewCreatedAt(DateUtils.stringToDate(articles.get(i).select(".category").first().text().split(" ")[0], DateUtils.DATE_PATTERN));
                    Tag tag = tagService.getTag(detail.getCategory());
                    List<Tag> tags = new LinkedList<>();
                    tags.add(tag);
                    detail.setTags(tags);
                    details.add(detail);
                }
            }else {
                Elements lis = document.select(".list-box").first().select("li");
                String tagName = document.select(".list-bread").first().select("a").last().text();
                if(lis != null){
                    for(int i = 0; i < lis.size(); i++){
                        Detail detail = new Detail();
                        detail.setUrl(lis.get(i).select("a").first().attr("href"));
                        String str = detail.getUrl().substring(detail.getUrl().indexOf("2"), detail.getUrl().lastIndexOf("."));
                        String[] ids = str.split("//");
                        String id = "";
                        for(String s: ids){
                            id = id+s;
                        }
                        detail.setId(Long.parseLong(id));
                        detail.setTitle(lis.get(i).select("a").first().text());
                        detail.setContent("");
                        detail.setAuthor("");
                        detail.setCategory(tagName);
                        String cDate = lis.get(i).select("i").first().text();
                        String nian = cDate.substring(0, cDate.indexOf("年"));
                        String yue = cDate.substring(cDate.indexOf("年") + 1, cDate.indexOf("月"));
                        String ri = cDate.substring(cDate.indexOf("月")+ 1, cDate.indexOf("日"));
                        detail.setNewCreatedAt(DateUtils.stringToDate(nian+"-"+yue+"-"+ri, DateUtils.DATE_PATTERN));
                        Tag tag = tagService.getTag(detail.getCategory());
                        List<Tag> tags = new LinkedList<>();
                        tags.add(tag);
                        detail.setTags(tags);
                        details.add(detail);
                    }
                }
            }
            return details;
        }else if(responseEntity.getStatusCode().is4xxClientError()){
            throw new WSExcetpion("没有更多新闻了");
        }else{
            throw new WSExcetpion("获取新闻信息失败");
        }
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取所有可用的标签
     * @Date 2019/3/21
     * @Param
     * @return
     **/
    public static List<Tag> getTagList() {
        HttpEntity<String> request = new HttpEntity<>(null,null);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(NewsAPI.NEWS_HOME,HttpMethod.GET,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            String html = responseEntity.getBody();
            Document document = Jsoup.parse(html);
            Elements as = document.select(".explore > .row").first().select("a");
            List<Tag> tags = new LinkedList<>();
            for(int i = 0; i < as.size(); i++){
                String tagUrl = as.get(i).attr("href");
                if(tagUrl.contains("/image") || tagUrl.contains("lecture")){
                    continue;
                }
                Tag tag = new Tag();
                tag.setUrl(tagUrl);
                tag.setName(as.get(i).text());
                tags.add(tag);
            }
            return tags;
        } else{
            throw new WSExcetpion("查询标签列表失败，请稍候再试");
        }
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取当前点击新闻的详细内容
     * @Date 2019/3/22
     * @Param [url]
     * @return java.lang.String
     **/
    public static String getNewDetail(String url) {
        HttpEntity<String> request = new HttpEntity<>(null,null);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            String html = responseEntity.getBody();
            Document document = Jsoup.parse(html);
            String content = document.select(".content").first().html();
            return content;
        } else{
            throw new WSExcetpion("查询标签列表失败，请稍候再试");
        }
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取学术讲座页数
     * @Date 2019/3/21
     * @Param
     * @return
     **/
    public static int getLecturePageCount(int count){
        HttpEntity<String> request = new HttpEntity<>(null,null);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = null;
        if(count == 0){
            responseEntity = restTemplate.exchange(NewsAPI.LECTURE,HttpMethod.GET,request,String.class);
            if(responseEntity.getStatusCode().is2xxSuccessful()){
                String body = responseEntity.getBody();
                Document html = Jsoup.parse(body);
                Elements s = html.select(".pagination > a");
                count = html.select(".pagination > a").size() - 1;
                count = getLecturePageCount(count);
            }
        } else {
            try {
                responseEntity = restTemplate.exchange(NewsAPI.LECTURE + count + ".shtml",HttpMethod.GET,request,String.class);
                if(responseEntity.getStatusCode().is2xxSuccessful()){
                    count = getLecturePageCount(count + 1);
                }
            }catch (Exception e) {
                return count - 1;
            }
        }
        return count;
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取学术讲座列表
     * @Date 2019/3/21
     * @Param
     * @return
     **/
    public static  List<Lecture> getLectures(int pageIndex) {
        List<Lecture> lectureList = new LinkedList<>();
        HttpEntity<String> request = new HttpEntity<>(null,null);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(NewsAPI.LECTURE + pageIndex + ".shtml",HttpMethod.GET,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            String body = responseEntity.getBody();
            Document html = Jsoup.parse(body);
            Elements titles = html.select("article > .info").select(".title");
            for(Element title : titles){
                Lecture lecture = new Lecture();
                String url = title.select("a").last().attr("href");
                String[] urls = url.substring(url.indexOf("2"), url.lastIndexOf(".")).split("/");
                StringBuilder id = new StringBuilder();
                for(String s: urls){
                    id.append(s);
                }
                lecture.setId(Long.parseLong(id.toString()));
                lecture.setTitle(title.select("a").text());
                // 获取当前讲座的详细信息
                getLectureDetail(url, lecture);
                if(lecture.getStartTime() != null){
                    lectureList.add(lecture);
                }
            }
        }
        return lectureList;
    }

    private static void getLectureDetail(String url, Lecture lecture) {
        HttpEntity<String> request = new HttpEntity<>(null,null);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            String body = responseEntity.getBody();
            Document html = Jsoup.parse(body);
            String info = html.selectFirst(".info").text();
            System.out.println(lecture.getTitle());
            Elements p = html.select(".content > p");
            if(p.size() < 2){
                return;
            }
            // 获取college
            lecture.setCollege(getLectureCollege(info, p));
            // 获取报告人信息
            lecture.setReporter(getLectureReporter(p));
            // 获取报告地点及时间
            getLectureAddressAndDate(info, p, lecture);
        }
    }

    private static void getLectureAddressAndDate(String info, Elements p, Lecture lecture) {
        boolean isTime = false;
        boolean isAddress = false;
        for(int i  = 0; i < p.size(); i++){
            if(isTime && isAddress){
                return;
            }
            String text = p.get(i).text();
            if(text.contains("时间") && !isTime){
                String date = "";
                String[] dates = text.split("：");

                if(dates.length < 2) {
                    text = p.get(i + 1).text();
                    if(text.split(";")[0].contains("月")){
                        date = text;
                    }else {
                        try {
                            date = text.split("：")[1];
                        }catch (ArrayIndexOutOfBoundsException e){
                            date = "";
                        }

                    }
                } else{
                    for(int j = 1 ; j < dates.length; j++){
                        date += dates[j];
                    }
                }
                if(!date.contains("月")){
                    continue;
                }
                lecture.setTime(date);
                String d = null;
                int dex = date.indexOf("（");
                try {
                    d = date.substring(0, date.indexOf("（"));
                }catch (StringIndexOutOfBoundsException e){
                    try {
                        d = date.substring(0, date.indexOf(" "));
                    }catch (StringIndexOutOfBoundsException e1){
                        try {
                            d = date.substring(0, date.indexOf("——"));
                        }catch (StringIndexOutOfBoundsException e2 ) {
                            try {
                                d = date.substring(0, date.indexOf("("));
                            }catch (StringIndexOutOfBoundsException e3){
                                continue;
                            }
                        }
                    }
                }
                String nian = null;
                String yue = null;
                String ri = null;
                if(d.contains("年")){
                    nian = d.substring(0, d.indexOf("年"));
                    yue = d.substring(d.indexOf("年") + 1, d.indexOf("月"));
                } else{
                    int index = info.indexOf("-");
                    nian = info.substring(index - 4, index);
                    yue = d.substring(0, d.indexOf("月"));
                }
                if(d.contains("日")){
                    ri = d.substring(d.indexOf("月") + 1, d.indexOf("日"));
                } else {
                    ri = d.substring(d.indexOf("月") + 1);
                }
                try {
                    lecture.setStartTime(DateUtils.stringToDate(nian+"-"+yue+"-"+ri, DateUtils.DATE_PATTERN));
                }catch (IllegalArgumentException e){
                    lecture.setStartTime(null);
                }
                isTime = true;
            } else if(text.contains("地点") && !isAddress){
                String[] ar = text.split("：");
                String address = null;
                if(ar.length < 2){
                    address = p.get(i + 1).text();
                } else {
                    address = ar[1];
                }
                lecture.setAddress(address);
                isAddress = true;
            }
        }
    }


    // 获取讲座人信息
    private static String getLectureReporter(Elements p) {
        String reporter = "";
        String text = p.get(1).text();
        if(!text.contains("，") || text.contains("：") || text.contains("；") || text.length() < 20){
            int i  = p.size() - 1;
            for(;i > 0; i--){
                text = p.get(i).text();
                if(text.contains("嘉宾") || text.contains("主讲人")){
                    if(text.contains("：")){
                        String[] rs = text.split("：");
                        if(rs.length < 2){
                            text = p.get(i + 1).text();
                            int index = text.indexOf("，");
                            if(index < 0 || index > 4) {
                                return reporter;
                            }
                        } else {
                            text = rs[1];
                            break;
                        }
                    } else {
                        try {
                            text = p.get(i + 1).text();
                        }catch (IndexOutOfBoundsException e){
                            text = "";
                        }

                        break;
                    }
                }
            }
            if(i == 0){
                text = p.last().text();
                int index = text.indexOf("，");
                if(index < 0 || index > 4){
                    return reporter;
                }
            }
        }
        int index = text.indexOf("，");
        if(index != -1){
            reporter = text.substring(0, text.indexOf("，", index + 1));
        } else {
            reporter = text;
        }
        return reporter;
    }

    private static String getLectureCollege(String info, Elements p) {
        String college = "";
        if(info.contains("投稿人")){
            String s = info.substring(info.indexOf("投稿人："), info.indexOf("责任编辑："));
            college = s.split("：")[1];
        } else {
            // 获取第一段信息
            String s = p.get(0).text();
            try {
                s = s.substring(0, s.indexOf("特邀"));
                String s1 = s.substring(s.lastIndexOf("，") + 1);
                if(s1.contains("。")){
                    s1 = s1.substring(s.lastIndexOf("。") + 1);
                }
                college = s1.replace("携手","、");
            }catch (StringIndexOutOfBoundsException e){
                for(Element element : p){
                    String text = element.text();
                    if(text.contains("单位")){
                        try {
                            college = text.split("：")[1];
                        }catch (ArrayIndexOutOfBoundsException e1){
                            college = "学校";
                        }
                    }
                }
            }

        }
        return college;
    }
}
