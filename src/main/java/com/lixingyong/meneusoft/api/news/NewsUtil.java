package com.lixingyong.meneusoft.api.news;

import com.lixingyong.meneusoft.api.utils.RestUtils;
import com.lixingyong.meneusoft.api.vpn.VPNUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.Detail;
import com.lixingyong.meneusoft.modules.xcx.entity.Tag;
import com.lixingyong.meneusoft.modules.xcx.service.TagService;
import com.lixingyong.meneusoft.modules.xcx.service.impl.TagServiceImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
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
                        detail.setContent("");
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
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
     * @Description //TODO 获取学术讲座的新闻
     * @Date 2019/3/21
     * @Param
     * @return
     **/
}
