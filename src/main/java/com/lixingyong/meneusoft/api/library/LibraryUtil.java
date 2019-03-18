package com.lixingyong.meneusoft.api.library;

import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.api.vpn.VPNUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.LibraryBook;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @ClassName com.lixingyong.meneusoft.api.library
 * @Description TODO 图书馆网络请求方式
 * @Author mail@lixingyong.com
 * @Date 2019-03-16 15:23
 */
@Component
public class LibraryUtil {
    private static RestTemplate restTemplate;
    private static RedisUtils redisUtils;
    private static Map<String,Object> map = new HashMap<>();
    private static Logger logger = LoggerFactory.getLogger(VPNUtil.class);
    /**
     * @Author lixingyong
     * @Description //TODO 登录图书馆
     * @Date 2019/3/16
     * @Param [uid]
     * @return void
     **/
    public static void libraryLogin(long uid, String barcode, String password){
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "9999");
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "9999");
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey("SVPNCOOKIE")){
            throw new WSExcetpion("redis中数据不完善");
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("barcode", barcode);
        param.add("password", password);
        param.add("login.x",Integer.toString(0));
        param.add("login.y", Integer.toString(0));
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,headers);//将参数和header组成一个请求
        map.put("sid",redisUtils.get("SID"));
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(VPNAPI.PROXY+LibraryAPI.LIBRARYLOGIN,HttpMethod.POST,request,Resource.class,map);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(responseEntity.getHeaders().containsKey("Set-Cookie")){
                if(responseEntity.getHeaders().get("Set-Cookie").size() > 0){
                    if(responseEntity.getHeaders().get("Set-Cookie").get(0).startsWith("JSESSIONID")){
                        String[] cookies = responseEntity.getHeaders().get("Set-Cookie").get(0).split(";");
                        String JSessionId = cookies[0];
                        //将获取到的数据存入redis数据库中并返回
                        redisUtils.set(Long.toString(uid)+"LIBARARYSESSION",JSessionId);
                        logger.info("获取图书馆登录授权Cookie成功");
                        return;
                    }
                }
            }
        }
        throw new WSExcetpion("获取教务处登录信息失败");
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取借阅历史（需要用户已登录）
     * @Date 2019/3/18
     * @Param [restTemplate]
     * @return void
     **/
    public static List<LibraryBook> getHistoryBooks(long uid){
        System.setProperty("http.proxyHost", "localhost");
        System.setProperty("http.proxyPort", "9999");
        System.setProperty("https.proxyHost", "localhost");
        System.setProperty("https.proxyPort", "9999");
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey("SVPNCOOKIE") || !redisUtils.hasKey(Long.toString(uid)+"LIBARARYSESSION")){
            throw new WSExcetpion("redis中数据不完善");
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get(Long.toString(uid)+"LIBARARYSESSION"));
        cookiesList.add(redisUtils.get("SVPNCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        map.put("sid",redisUtils.get("SID"));
        ResponseEntity<String> responseEntity = restTemplate.exchange(VPNAPI.PROXY+LibraryAPI.LIBRARYHISTORY,HttpMethod.GET,request,String.class,map);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            try {
                String html = new String(responseEntity.getBody().getBytes("ISO-8859-1"),"GBK");
                Document document = Jsoup.parse(html);
                return htmlToLibraryBooks(document, uid);
            } catch (UnsupportedEncodingException e) {
                throw new WSExcetpion("字符串编码转换错误");
            }
        } else {
            return null;
        }
    }

    /**
     * @Author lixingyong
     * @Description //TODO 将html页面转换为指定的对象
     * @Date 2019/3/18
     * @Param [restTemplate]
     * @return void
     **/
    private static List<LibraryBook> htmlToLibraryBooks(Document document, Long user_id){
        List<LibraryBook> libraryBooks = new LinkedList<>();
        // 找到所有的table标签
        Element table = document.select("table[width='90%']").first();
        // 查找其下所有tr标签
        Elements trs = table.select("tr");
        // 从序号为2 开始遍历转换为对象
        for(int i = 2; i < trs.size() - 1; i++){
            // 使用tr标签，获取其内部的td
            getTd(trs.get(i), libraryBooks, user_id);
        }
        return libraryBooks;
    }

    /**
     * @Author lixingyong
     * @Description //TODO 根据tr标签，转换其内部的td
     * @Date 2019/3/18
     * @Param [restTemplate]
     * @return void
     **/

    public static void getTd(Element tr,  List<LibraryBook> libraryBooks, Long user_id){
        LibraryBook libraryBook;
        // 获取tr中的所有td标签
        Elements tds = tr.select("td");
        if(libraryBooks.size() > 0){
            if(null == libraryBooks.get(libraryBooks.size() - 1).getNumber()){
                libraryBooks.remove(libraryBooks.size() - 1);
            }
            if(tds.get(2).text().equals(libraryBooks.get(libraryBooks.size() - 1).getNumber())){
                libraryBook = libraryBooks.get(libraryBooks.size() - 1);
            }else {
                libraryBook = new LibraryBook();
                libraryBook.setUserId(Integer.parseInt(user_id.toString()));
                libraryBooks.add(libraryBook);
            }
        } else {
            libraryBook = new LibraryBook();
            libraryBook.setUserId(Integer.parseInt(user_id.toString()));
            libraryBooks.add(libraryBook);
        }
        // 判断tds的第3个属性值（从0开始）
        switch (tds.get(3).text()){
            case "借书日期":
                libraryBook.setIsHistory(0);
                Date date = DateUtils.stringToDate(tds.get(4).text(), DateUtils.DATE2_PATTERN);
                date = DateUtils.addDateMonths(date, 1);
                libraryBook.setDueDate(DateUtils.format(date,DateUtils.DATE2_PATTERN));
                libraryBook.setBookId(tds.get(0).text());
                libraryBook.setTitle(tds.get(1).text());
                libraryBook.setAddress("A5图书馆");
                libraryBook.setNumber(tds.get(2).text());
                break;
            case "续借日期":
                Date date1 = DateUtils.stringToDate(libraryBook.getDueDate(), DateUtils.DATE2_PATTERN);
                date1 = DateUtils.addDateMonths(date1, 1);
                libraryBook.setDueDate(DateUtils.format(date1, DateUtils.DATE2_PATTERN));
                break;
            case "还书日期":
                libraryBook.setIsHistory(1);
                Date resultDate = DateUtils.stringToDate(tds.get(4).text(), DateUtils.DATE2_PATTERN);
                libraryBook.setReturnDate(DateUtils.format(resultDate, DateUtils.DATE2_PATTERN));
                break;
        }
    }
    @Autowired(required = true)
    public void setRestTemplate(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Autowired(required = true)
    private void setRedisUtils(RedisUtils redisUtils){
        this.redisUtils = redisUtils;
    }
}
