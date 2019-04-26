package com.lixingyong.meneusoft.api.ufs;

import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.Grade;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * UFS系统
 */
public class UFSUtil {
    private static RestTemplate restTemplate = RestConfig.getRestTemplate();
    private static RedisUtils redisUtils = RestConfig.getRedisUtils();
    private static Map<String,Object> map = new HashMap<>();
    private static UserService userService = RestConfig.getUserService();

    /**
     * 获取会话cookie
     */
    private static String getCookie(){
        /** 获取redis保存的VPNcookies */
//        if(!redisUtils.hasKey("SVPNCOOKIE") || !redisUtils.hasKey("SID")){
//            throw new WSExcetpion("redis中不存在SVPNCOOKIE");
//        }
        HttpHeaders headers = new HttpHeaders();
        List<String> cookieList = new ArrayList<>();
        cookieList.add(null);
        headers.put(HttpHeaders.COOKIE,cookieList); //将Cookies放入Header
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.37");
        HttpEntity<String> request = new HttpEntity<>(null,headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(UFSAPI.LOGIN, HttpMethod.GET,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            // 获取cookie
            List<String> cookies = responseEntity.getHeaders().get("Set-Cookie");
            if(!cookies.isEmpty()){
                return cookies.get(0).split(";")[0];
            }
        }
        return null;
    }

    /**
     * 执行登录UFS系统
     */
    public static boolean LoginUFS(int userId, String account, String pw){
                // 若cookie中存在当前cookie,则执行注销程序
        if(redisUtils.hasKey(userId+"UFS")){
            // 执行注销程序
            if(logout(redisUtils.get(userId+"UFS"))){
                // 清除redis保存的数据
                redisUtils.delete(userId+"UFS");
            }
        }
        //登录前首先获取本次登录所需cookie
        String cookie = getCookie();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 将cookie放入header
        List<String> cookieList = new ArrayList<>();
        cookieList.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookieList); //将Cookies放入Header
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("username", account);
        param.add("upwd", pw);
        param.add("typeid", "01");
        param.add("method","登录");
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,headers);//将参数和header组成一个请求
        ResponseEntity<String> response = restTemplate.exchange(UFSAPI.LOGIN_ACTION,HttpMethod.POST,request,String.class);
        if(response.getStatusCode().is2xxSuccessful()){
            String html = response.getBody();
            if(html.contains("学生用户导航")){
                // 将当前cookie保存
                redisUtils.set(userId+"UFS",cookie);
                return true;
            }
        }
        return false;
    }

    /**
     * 查询当前用户身份(查询年份)
     */
    public static String baseInfo(int userId){
        loginVail(userId);
        HttpHeaders headers = new HttpHeaders();
        //登录前首先获取本次登录所需cookie
        String cookie = redisUtils.get(userId+"UFS");
        // 将cookie放入header
        List<String> cookies = new ArrayList<>();
        cookies.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookies); //将Cookies放入Header
        HttpEntity request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        ResponseEntity<String> response = restTemplate.exchange(UFSAPI.BASE_INFO,HttpMethod.GET,request,String.class);
        if(response.getStatusCode().is2xxSuccessful()){
            String html = response.getBody();
            Document document = Jsoup.parse(html);
            Elements tr = document.select(".list").select("tr");
            Elements td = tr.get(4).select("td");
            if(td.get(1).text().contains("级")){
                String grades = td.get(1).text().substring(0, td.get(1).text().indexOf("级"));
                String grade = grades.substring(grades.length() - 2);
                return grade;
            }
        }
        return null;
    }

    /**
     * 获取至今为止所有的学年
     */
    public static List<String> schoolYear(int userId){
        List<String> schoolYears = new ArrayList<>();
        loginVail(userId);
        HttpHeaders headers = new HttpHeaders();
        //登录前首先获取本次登录所需cookie
        String cookie = redisUtils.get(userId+"UFS");
        // 将cookie放入header
        List<String> cookies = new ArrayList<>();
        cookies.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookies); //将Cookies放入Header
        HttpEntity request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        ResponseEntity<String> response = restTemplate.exchange(UFSAPI.QUERY_SCORE_PAGE,HttpMethod.GET,request,String.class);
        if(response.getStatusCode().is2xxSuccessful()){
            String html = response.getBody();
            Document document = Jsoup.parse(html);
            Element select = document.selectFirst("select[name=courseYearList]");
            Elements option = select.select("option");
            for(int i = 1; i < option.size(); i++){
                schoolYears.add(option.get(i).text());
            }
            return schoolYears;
        }
        return null;
    }

    /**
     * 根据学期和学年获取成绩信息
     */
    public static List<Grade> getGradeList(String courseYear, int term, int userId){
        List<Grade> grades = new ArrayList<>();
        loginVail(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        //登录前首先获取本次登录所需cookie
        String cookie = redisUtils.get(userId+"UFS");
        // 将cookie放入header
        List<String> cookies = new ArrayList<>();
        cookies.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookies); //将Cookies放入Header
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        String studentId = userService.getUserInfo(userId).getStudentId();
        params.add("orderBy", "1");
        params.add("studentNo", studentId);
        params.add("courseYearList", courseYear);
        params.add("courseTermList", Integer.toString(term));
        params.add("method", "查询");
        HttpEntity request = new HttpEntity<>(params,headers);//将参数和header组成一个请求
        ResponseEntity<String> response = restTemplate.exchange(UFSAPI.QUERY_SCORE,HttpMethod.POST,request,String.class);
        if(response.getStatusCode().is2xxSuccessful()){
            String html = response.getBody();
            Document document = Jsoup.parse(html);
            if(html.contains("学年必须填写") || html.contains("学期必须填写")){
                throw new WSExcetpion("传入数据不正确");
            }
            if(html.contains("没有满足条件的记录")){
                return grades;
            }
            // 处理成绩信息
            Element gradeList = document.selectFirst(".list");
            grades =  dealGradeList(courseYear, term, userId, gradeList, studentId);
            if(grades != null){
                return grades;
            }
        }
        return grades;
    }

    private static List<Grade> dealGradeList(String courseYear, int term, int userId, Element gradeList, String studentId ) {
        List<Grade> grades = new ArrayList<>();
        Elements trs = gradeList.select("tr");
        for(int i = 1; i < trs.size(); i++){
            Elements tds = trs.get(i).select("td");
            Grade grade = new Grade();
            String courseId = tds.get(0).text();
            int id = 0;
            for(Byte b : courseId.getBytes()){
                id += (b.hashCode() - 65);
            }
            studentId = studentId.substring(0, 2) + studentId.substring(studentId.length() - 3);
            id += Integer.valueOf(studentId);
            grade.setId(id);
            grade.setCourseId(tds.get(0).text());
            grade.setCourseName(tds.get(1).text());
            grade.setCourseType("必修");
            grade.setCredit(tds.get(2).text());
            grade.setGrade(Float.valueOf(tds.get(3).text()));
            grade.setGpa(Float.valueOf(tds.get(4).text()));
            grade.setTerm(term);
            grade.setUserId(userId);
            String[] years = courseYear.split("-");
            StringBuffer termName = new StringBuffer();
            termName.append("20").append(years[0]).append("~").append("20").append(years[1]).append("学年");
            grade.setYear(Integer.valueOf("20"+ years[0]));
            if(term == 1){
                termName.append("秋季");
            } else if (term == 2){
                termName.append("春季");
            } else {
                termName.append("小");
            }
            termName.append("学期");
            grade.setTermName(termName.toString());

            grades.add(grade);
        }
        return grades;
    }

    /**
     * 登出
     * @param session
     * @return
     */
    public static boolean logout(String session) {
        // 将cookie放入header
        HttpHeaders headers = new HttpHeaders();
        List<String> cookieList = new ArrayList<>();
        cookieList.add(session);
        headers.put(HttpHeaders.COOKIE,cookieList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(UFSAPI.LOGOUT, HttpMethod.GET,request,Map.class);
        if(responseEntity.getStatusCode().is3xxRedirection()){
            return true;
        }
        return false;
    }

    /**
     * 获取当前用户的sesssion
     */
    public static String getSession(int userId){
        if(redisUtils.hasKey(userId+"UFS")){
            return redisUtils.get(userId+"UFS");
        }
        return null;
    }

    /**
     * 登录验证
     */
    private static void loginVail(int userId) throws WSExcetpion {
        // 若cookie中不存在cookie,则执行登录程序
        if(!redisUtils.hasKey(userId+"UFS")){
            User user = userService.getUserInfo(userId);
            if(user.getVerify() == 1){
                if(!LoginUFS(userId, user.getStudentId(), user.getPassword())){
                    throw new WSExcetpion("登录UFS系统失败");
                }
            }else {
                throw new WSExcetpion("当前用户账号不存在或不可登录");
            }
        }
    }
}
