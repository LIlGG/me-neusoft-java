package com.lixingyong.meneusoft.api.library;

import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.LibraryBook;
import com.lixingyong.meneusoft.modules.xcx.vo.BookSearchVO;
import com.lixingyong.meneusoft.modules.xcx.vo.CollectBook;
import com.lixingyong.meneusoft.modules.xcx.vo.DetailBookVO;
import com.lixingyong.meneusoft.modules.xcx.vo.SearchBooksVO;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
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
public class LibraryUtil extends LibraryAbs {
    /**
     * @Author lixingyong
     * @Description //TODO 登录图书馆
     * @Date 2019/3/16
     * @Param [uid]
     * @return void
     **/
    public static void libraryLogin(long uid, String barcode, String password) throws WSExcetpion{
        setCookies(new LinkedList<>());
        HttpHeaders headers = httpHeaders();
        headers.set("Content-Type","application/x-www-form-urlencoded;charset=ISO-8859-1");
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
        // 判断redis中是否存着对应的Cookie
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("barcode", barcode);
        param.add("password", password);
        param.add("login.x",Integer.toString(0));
        param.add("login.y", Integer.toString(0));
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,headers);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(LibraryAPI.LIBRARY_LOGIN),HttpMethod.POST,request,String.class, getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            try {
                String html = new String(responseEntity.getBody().getBytes("ISO-8859-1"),"GBK");
                if(html.contains("alert")){
                    String[] m = html.split("alert");
                    String msgErrpr = m[1].substring(m[1].indexOf("\"") + 1, m[1].indexOf(")") - 1);
                    if(StringUtils.isNotBlank(msgErrpr))
                        throw new WSExcetpion(msgErrpr,30010);
                }
            } catch (UnsupportedEncodingException e) {
               throw new WSExcetpion("服务器错误，请联系管理员", 30099);
            }
            if(responseEntity.getHeaders().containsKey("Set-Cookie")){
                if(responseEntity.getHeaders().get("Set-Cookie").size() > 0){
                    if(responseEntity.getHeaders().get("Set-Cookie").get(0).startsWith("JSESSIONID")){
                        String[] cookies = responseEntity.getHeaders().get("Set-Cookie").get(0).split(";");
                        String JSessionId = cookies[0];
                        //将获取到的数据存入redis数据库中并返回
                        redisUtils.set(uid+"LIBARARYSESSION",JSessionId);
                        logger.info("获取图书馆登录授权Cookie成功");
                        return;
                    }
                }
            }
        }
        throw new WSExcetpion("获取图书馆登录信息失败");
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取借阅历史（需要用户已登录）
     * @Date 2019/3/18
     * @Param [restTemplate]
     * @return void
     **/
    public static List<LibraryBook> getHistoryBooks(long uid) throws WSExcetpion{
        setCookies(new LinkedList<>());
        setCookies(uid+"LIBARARYSESSION");
        HttpEntity request = httpEntity();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(LibraryAPI.LIBRARY_HISTORY),HttpMethod.GET,request,String.class, getMap());
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


    /**
     * @Author lixingyong
     * @Description //TODO 图书检索
     * @Date 2019/3/18
     * @Param [restTemplate]
     * @return void
     **/
    public static BookSearchVO bookSearch(String title, int curPage){
        setCookies(new LinkedList<>());
        HttpHeaders headers = httpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("type","title");
        try {
            param.add("word", new String(title.getBytes("GBK"), "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        param.add("match","mh");
        param.add("recordtype","01");
        param.add("library_id","all");
        if(curPage == 1){
            param.add("kind","simple");
            param.add("x","14");
            param.add("y","4");
        } else{
            param.add("searchtimes","1");
            param.add("size", "10");
            param.add("curpage", Integer.toString(curPage));
            param.add("apabi_page","1");
            param.add("orderby","title");
            param.add("ordersc","asc");
            param.add("page", Integer.toString(curPage - 1));
            param.add("pagesize", "10");
            param.add("page", Integer.toString(curPage - 1));
            param.add("pagesize", "10");
        }

        HttpEntity request = new HttpEntity<>(param,headers);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restIOSTemplate.postForEntity(URL(LibraryAPI.SEARCH), request, String.class, getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            try{
                String html = new String(responseEntity.getBody().getBytes("ISO-8859-1"),"gb2312");
                if(html.contains("没有检索到")){
                    throw new WSExcetpion("未检索到对应的图书");
                }
                // 获取所有的table
                Document document = Jsoup.parse(html);

                return bookSearchHtml(document);
            }catch (UnsupportedEncodingException e){
                throw new WSExcetpion("字符串编码转换错误");
            }
        }
        return null;
    }

    private static BookSearchVO bookSearchHtml(Document document) {
        BookSearchVO bookSearchVOList = new BookSearchVO();
        Elements elements = document.select("table[width='97%']");
        Element books = elements.get(1).select("table[width='97%']").first();
        // 获取当前节点的所有tr节点
        Elements tr = books.select("tr");
        List<SearchBooksVO> booksVOList = new LinkedList<>();
        // 从第2个tr节点起开始遍历，直到最后一个
        for(int i = 1; i < tr.size(); i++){
            // 组装bookSearch
            SearchBooksVO searchBooksVO = booksSearchTd(tr.get(i));
            if(searchBooksVO != null){
                booksVOList.add(searchBooksVO);
            }
        }
        bookSearchVOList.setBooks(booksVOList);
        // 设置序号
        Element parentBook = books.parent().select("table").get(1).select("tr").get(1).select("td").first();
        bookSearchVOList.setCurPage(parentBook.select("b").first().text());
        bookSearchVOList.setPageSize(parentBook.select("b").last().text());
        return bookSearchVOList;
    }

    private static SearchBooksVO booksSearchTd(Element element) {
        Elements tds = element.select("td");
        String title = tds.get(1).select("a").first().text();
        if(title.equals("")){
            return null;
        }
        SearchBooksVO searchBooksVO = new SearchBooksVO();
        searchBooksVO.setTitle(title);
        searchBooksVO.setAuthor(tds.get(2).text());
        searchBooksVO.setCover("");
        searchBooksVO.setPress(tds.get(3).text());
        searchBooksVO.setPublish_year(tds.get(5).text());
        String href = tds.get(1).select("a").first().attr("href");
        String id = href.substring(href.lastIndexOf(href.lastIndexOf("'") - 1) + 2, href.lastIndexOf("'"));
        searchBooksVO.setDetailId(id);
        return searchBooksVO;
    }

    /**
     * @Author lixingyong
     * @Description //TODO 查询某个书籍详细的内容
     * @Date 2019/3/21
     * @Param [detailId]， 当前所选书籍的URL id
     * @return com.lixingyong.meneusoft.modules.xcx.vo.DetailBookVO
     **/
    public static DetailBookVO detailBook(String detailId) {
        setCookies(new LinkedList<>());
        HttpEntity request = httpEntity();
        map = new HashMap<>();
        map.put("rid", detailId);
        setMap(map);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(LibraryAPI.DETAILS),HttpMethod.GET,request,String.class, getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            try {
                String html = new String(responseEntity.getBody().getBytes("ISO-8859-1"),"GBK");
                Document document = Jsoup.parse(html);
                return bookDetailHtml(document);
            } catch (UnsupportedEncodingException e) {
                throw new WSExcetpion("字符串编码转换错误");
            }
        } else {
            return null;
        }
    }

    /**
     * @Author lixingyong
     * @Description //TODO 查询Tr并遍历
     * @Date 2019/3/21
     * @Param [document]
     * @return com.lixingyong.meneusoft.modules.xcx.vo.DetailBookVO
     **/
    private static DetailBookVO bookDetailHtml(Document document) {
        Elements tables = document.select("table[width='97%']");
        Elements tr = tables.get(3).select("tr");
        List<CollectBook> collectBooks = new ArrayList<>();
        String number = null;
        for(int i = 2; i < tr.size(); i++){
            //使用tr进行拼接
            if(tr.get(i).select("td").size() > 1){
                number = collectBooksTd(tr.get(i),collectBooks);
            }

        }
        if(null == number){
            return null;
        }
        DetailBookVO detailBookVO = new DetailBookVO();
        detailBookVO.setNumber(number);
        detailBookVO.setBook_addresses(collectBooks);
        return detailBookVO;
    }

    private static String collectBooksTd(Element element, List<CollectBook> collectBooks) {
        Elements tds = element.select("td");
        CollectBook collectBook = new CollectBook();
        collectBook.setAddress(tds.get(4).text());
        collectBook.setBookStatus(tds.get(5).text());
        collectBook.setDueData(tds.get(6).text());
        collectBooks.add(collectBook);
        return tds.get(2).text();
    }

}
