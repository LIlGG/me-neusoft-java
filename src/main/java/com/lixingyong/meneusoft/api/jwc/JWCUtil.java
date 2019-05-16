package com.lixingyong.meneusoft.api.jwc;

import com.aliyun.oss.OSSClient;
import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.api.jwc.utils.Tess4JUtil;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.JSONUtils;
import com.lixingyong.meneusoft.common.utils.MD5Utils;
import com.lixingyong.meneusoft.common.utils.OSSClientUtil;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.Classroom;
import com.lixingyong.meneusoft.modules.xcx.entity.Course;
import com.lixingyong.meneusoft.modules.xcx.entity.CourseSchedule;
import org.apache.http.client.methods.HttpHead;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @ClassName JWCUtil
 * @Description TODO    教务处安全接口工具
 * @Author lixingyong
 * @Date 2018/11/29 14:57
 * @Version 1.0
 */

public class JWCUtil extends JWCAbs {
    private static Logger logger = LoggerFactory.getLogger(JWCUtil.class);
    /**
     * @Author lixingyong
     * @Description //TODO 获取教务处的Cookie，当前Cookie针对与每个账号而言
     * @Date 2018/11/29
     * @Param [uid] 当前登录账号的Uid，根据Uid获取对应用户的教务处Cookie
     * @return boolean
     **/
    public static void getJWCCookie(long uid) throws WSExcetpion {
        setCookies(new LinkedList<>());
        HttpEntity request = httpEntity();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.JWCSID), HttpMethod.GET, request, String.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(responseEntity.getHeaders().containsKey("Set-Cookie")){
                if(responseEntity.getHeaders().get("Set-Cookie").size() > 0){
                    if(responseEntity.getHeaders().get("Set-Cookie").get(0).startsWith("ASP")){
                        String[] cookies = responseEntity.getHeaders().get("Set-Cookie").get(0).split(";");
                        String ASPCookie = cookies[0];
                        //将获取到的数据存入redis数据库中并返回
                        redisUtils.set(uid+"ASPCOOKIE",ASPCookie);
                        logger.info("获取教务处Cookie成功");
                        return;
                    }
                }
            }
        }
        throw new WSExcetpion("获取教务处COOKIE失败");
    }


    /***
     * @Author lixingyong
     * @Description //TODO 获取经过处理后的浏览器状态
     * @Date 2018/11/29
     * @Param [uid, studentId, password, center]
     * @return java.util.Map<java.lang.String                                                               ,                                                               java.lang.Object>
     **/
    public static void getJWCInfo(long uid) throws WSExcetpion {
        setCookies(new LinkedList<>());
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey(uid +"ASPCOOKIE")){
            throw new WSExcetpion("redis中缺少ASPCOOKIE");
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get(uid+"ASPCOOKIE"));
        setCookies(cookiesList);
        HttpEntity request = httpEntity();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.LOGIN),HttpMethod.GET,request,String.class, getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(responseEntity.getBody().indexOf("__VIEWSTATE") > 0){
                String body = responseEntity.getBody();
                String viewState = body.substring(body.indexOf("__VIEWSTATE"),body.lastIndexOf("id=\"pcInfo\""));
                viewState = viewState.substring(viewState.indexOf("value")+7,viewState.lastIndexOf("\""));
                // 保存当前viewState至redis数据库中
                redisUtils.set(uid +"VIEWSTATE",viewState);
                logger.info("获取教务处信息成功");
                return;
            }
        }
        throw new WSExcetpion("获取教务处登录信息失败");
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取验证码(保存在OSS中)
     * @Date 2018/12/7
     * @Param [uid]
     * @return void
     **/
    public static String getValidateCode(long uid) throws WSExcetpion{
        setCookies(new LinkedList<>());
        // 判断redis中是否存着对应的Cookie
        setCookies(uid +"ASPCOOKIE");
        HttpEntity request = httpEntity();
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(URL(JWCAPI.CODE),HttpMethod.GET,request,Resource.class,getMap());
        InputStream in = null;
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            try {
                in = responseEntity.getBody().getInputStream();
                OSSClient oss = OSSClientUtil.getOSSClient();
                OSSClientUtil.uploadObject2OSS(oss,in,codeFolder+uid+suffix,bucketName,folder);
                logger.info("获取验证码并上传至OSS成功");
                String url = OSSClientUtil.getUrl(folder + "/" + codeFolder+uid+suffix);
                return url;
            } catch (IOException e) {
                e.printStackTrace();
            }catch (WSExcetpion s){
                s.getMsg();
            }finally {
                if(null != in){
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * @Author lixingyong
     * @Description //TODO 获取验证码（直接返回流）
     * @Date 2018/12/7
     * @Param [uid]
     * @return void
     **/
    public static InputStream getCodeInput(long uid) throws WSExcetpion{
        setCookies(new LinkedList<>());
        // 判断redis中是否存着对应的Cookie
        setCookies(uid +"ASPCOOKIE");
        HttpHeaders httpHeaders = httpHeaders();
        httpHeaders.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36");
        String url = URL(JWCAPI.COURSE);
        if(isVPN()){
            url = url.replaceAll("\\{sid}", redisUtils.get("SID"));
        }
        httpHeaders.set("Referer",url);
        HttpEntity httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(URL(JWCAPI.CODE),HttpMethod.GET,httpEntity,byte[].class,getMap());
        InputStream in = null;
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            in = new ByteArrayInputStream(responseEntity.getBody());
        }
        return in;
    }


    /**
     * @Author lixingyong
     * @Description //TODO 执行教务处登录流程
     * @Date 2019/3/7
     * @Param [uid, account, pw, vCode]
     * @return void
     **/
    public static void jwcStudentLogin(long uid, String account, String pw, String vCode) throws WSExcetpion{
        setCookies(new LinkedList<>());
        // 判断redis中是否存着对应的Cookie
        if( !redisUtils.hasKey(uid+"VIEWSTATE")){
            throw new WSExcetpion("redis中缺少VIEWSTATE");
        }
        setCookies(uid +"ASPCOOKIE");
        HttpHeaders httpHeaders = httpHeaders();
        httpHeaders.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36");
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // 获取参数
        String viewState = redisUtils.get(uid +"VIEWSTATE");
        String pcInfo = "";
        MultiValueMap<String,String> param = studentLoginEncryption(viewState, pcInfo, account, pw, vCode);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,httpHeaders);//将参数和header组成一个请求
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(URL(JWCAPI.LOGIN), HttpMethod.POST,request,Resource.class, getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return;
        }
        throw new WSExcetpion("获取教务处登录信息失败");
    }

    /**
     * @Author lixingyong
     * @Description //TODO 教务处登录流程执行前进行账号加密措施
     * @Date 2019/3/7
     * @Param [viewState, pcInfo, typeName, account, pw, vCode]
     * @return java.lang.String
     **/
    private static MultiValueMap<String, String> studentLoginEncryption(String viewState, String pcInfo, String account, String pw, String vCode){
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();//参数放入一个map中，restTemplate不能用hashMap
        param.add("__VIEWSTATE",viewState);
        param.add("pcInfo",pcInfo);
        param.add("typeName", "学生");
        param.add("dsdsdsdsdxcxdfgfg",chkPwd(account,pw));
        param.add("fgfggfdgtyuuyyuuckjg",chkVCode(vCode));
        param.add("Sel_Type","STU");
        param.add("txt_asmcdefsddsd",account);
        param.add("txt_pewerwedsdfsdff","");
        param.add("txt_sdertfgsadscxcadsads","");
        return param;
    }


    /** 教务处学生基本学分情况查询 */
    public static void stuXyjzqk(long uid){
        setCookies(new LinkedList<>());
        // 判断redis中是否存着对应的Cookie
        if(!redisUtils.hasKey(uid+"VIEWSTATE")){
            throw new WSExcetpion("redis中缺少VIEWSTATE");
        }
        setCookies(uid +"ASPCOOKIE");
        HttpEntity request = httpEntity();
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(URL(JWCAPI.XYJZQK),HttpMethod.GET,request,Resource.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            return;
        }
        throw new WSExcetpion("查询学分失败！");
    }
    private static String chkPwd(String account, String pw) {
        return MD5Utils.getMD5String(account + MD5Utils.getMD5String(pw).substring(0,30).toUpperCase()+"13631").substring(0,30).toUpperCase();
    }

    private static String chkVCode(String code){
        return MD5Utils.getMD5String(MD5Utils.getMD5String(code.toUpperCase()).substring(0,30).toUpperCase()+"13631").substring(0,30).toUpperCase();
    }

    /**
     * 教务处获取校区信息
      */
    public static List<Classroom> getClassroom(long uid) throws WSExcetpion{
        List<Classroom> classrooms = new ArrayList<>();
        setCookies(new LinkedList<>());
        setCookies(uid +"ASPCOOKIE");
        HttpEntity request = httpEntity();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.XQ),HttpMethod.GET,request,String.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            String html = responseEntity.getBody();
            Document document = Jsoup.parse(html);
            Element element = document.selectFirst("select[name=Sel_XQ]");
            Elements options = element.select("option");
            if(options.isEmpty()){
               throw new WSExcetpion("校区信息获取失败");
            }
            for(int i = 1; i < options.size(); i++){
                int value = Integer.valueOf(options.get(i).attr("value"));
                String xqName = options.get(i).text();
                classrooms = getJXL(uid, value, xqName);
            }
        }
        return classrooms;
    }

    private static List<Classroom> getJXL(long uid, int xqValue, String xqName) throws WSExcetpion {
        List<Classroom> classrooms = new ArrayList<>();
        setCookies(new LinkedList<>());
        setCookies(uid +"ASPCOOKIE");
        HttpEntity request = httpEntity();
        map.put("id", xqValue);
        setMap(map);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.JXL),HttpMethod.GET,request,String.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            Elements options = getOptions(responseEntity);
            for(int i = 1; i < options.size(); i++){
                int jxlValue = Integer.valueOf(options.get(i).attr("value"));
                String jxlName = options.get(i).text();
                getRoom(uid,xqValue, xqName,jxlValue, jxlName, classrooms);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return classrooms;
    }

    private static void getRoom(long uid, int xqValue, String xqName, int jxlValue, String jxlName, List<Classroom> classrooms) {
        setCookies(new LinkedList<>());
        setCookies(uid +"ASPCOOKIE");
        HttpEntity request = httpEntity();
        map.put("id", jxlValue);
        setMap(map);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.ROOM),HttpMethod.GET,request,String.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            Elements options = getOptions(responseEntity);
            for(int i = 1; i < options.size(); i++){
                String roomValue = options.get(i).attr("value");
                String roomName = options.get(i).text();
                Classroom classroom = new Classroom();
                classroom.setCampusId(xqValue);
                classroom.setCampusName(xqName);
                classroom.setTbId(jxlValue);
                classroom.setTbName(jxlName);
                classroom.setClassroomId(roomValue);
                classroom.setClassroomName(roomName);
                classrooms.add(classroom);
            }
        }
    }

    private static Elements getOptions(ResponseEntity<String> responseEntity){
        String html = responseEntity.getBody();
        Document document = Jsoup.parse(html);
        Elements jsNodes = document.select("script");
        Element jsNode = null;
        for(Element element : jsNodes){
            if(element.data().contains("option")){
                jsNode = element;
                break;
            }
        }
        if(jsNode == null){
            throw new WSExcetpion("获取失败");
        }
        String js = jsNode.data();
        String select = js.substring(js.indexOf("'"), js.lastIndexOf("'"));
        if(select.isEmpty()){
            throw new WSExcetpion("未找到信息");
        }
        Element selectNode = Jsoup.parse(select);
        Elements options = selectNode.select("option");
        return options;
    }

    /** 获取教务处课程信息 */
    public static void getCourse(long uid) {
        setCookies(new LinkedList<>());
        setCookies(uid +"ASPCOOKIE");
        HttpEntity request = httpEntity();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.COURSE),HttpMethod.GET,request,String.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            String html = responseEntity.getBody();
            Document node = Jsoup.parse(html);
            Element termNode = node.selectFirst("select[name=Sel_XNXQ]");
            Element term = termNode.selectFirst("option");
            int termId = Integer.valueOf(term.attr("value"));
            // 根据获取到的数据请求课程列表
            getCourseList(termId, uid);
        }
    }

    private static void getCourseList(int termId, long uid) {
        List<Course> courses = new ArrayList<>();
        setCookies(new LinkedList<>());
        setCookies(uid +"ASPCOOKIE");
        HttpEntity request = httpEntity();
        map = new HashMap<>();
        map.put("termId", termId);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.COURSE_LIST),HttpMethod.GET,request,String.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            Elements optionNodes = getOptions(responseEntity);
            int i = 1;
            for(;i < optionNodes.size(); i++){
                String lessonId = optionNodes.get(i).attr("value");
                String[] texts = optionNodes.get(i).text().split("\\|");
                String courseId = texts[0];
                String courseName = texts[1];
                logger.info("开始获取"+courseName+"的课程信息");
                Course course = new Course();
                course.setId(Integer.valueOf(lessonId) + termId);
                course.setLessonId(lessonId);
                course.setCourseId(courseId);
                course.setName(courseName);
                course.setTermId(termId);
                getCourseDetail(uid, course);
                courses.add(course);
                if(i % 100 == 0){
                    try {
                        updateCourseData(courses, i / 100);
                    }catch (NullPointerException e){
                        courses = new ArrayList<>();
                        i = i - 99;
                    }
                }
            }
            updateCourseData(courses, i / 100);
        }
    }

    /** 分批次保存数据 */
    public static void updateCourseData(List<Course> courses, int flag) throws NullPointerException{
        logger.info("第"+ flag + "次保存数据开始");
        if(courses!= null && !courses.isEmpty()){
            courseService.insertOrUpdateBatch(courses);
            logger.info("结束获取课程信息");
            logger.info("开始保存课程课表信息");
            List<CourseSchedule> courseScheduleList = new ArrayList<>();
            for(Course course : courses){
                courseScheduleList.addAll(course.getCourseSchedules());
            }
            if(!courseScheduleList.isEmpty()){
                courseScheduleService.insertOrUpdateBatch(courseScheduleList);
                logger.info("结束保存课程课表信息");
            }
        }
        logger.info("第"+ flag + "次保存数据结束");
    }

    private static void getCourseDetail(long uid, Course course) {
        if(!redisUtils.hasKey("COURSE_CODE")){
            //获取验证码(无论对错)
            while(true){
                InputStream code  = getCodeInput(uid);
                try {
                    if(code != null && Tess4JUtil.executeTess4J(code)){
                        break;
                    }
                }catch (NullPointerException e){
                    e.getMessage();
                }
            }
        }
        String code = redisUtils.get("COURSE_CODE");
        setCookies(new LinkedList<>());
        setCookies(uid +"ASPCOOKIE");
        HttpHeaders headers = httpHeaders();
        headers.set("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.131 Safari/537.36");
        headers.set("Content-Type","application/x-www-form-urlencoded;charset=gb2312");
        String url = URL(JWCAPI.COURSE);
        if(isVPN()){
            url = url.replaceAll("\\{sid}", redisUtils.get("SID"));
        }
        headers.set("Referer",url);
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("Sel_XNXQ", Integer.toString(course.getTermId()));
        params.add("Sel_KC", course.getLessonId());
        params.add("gs", "2");
        params.add("txt_yzm",code);
        HttpEntity request = new HttpEntity<>(params,headers);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(JWCAPI.COURSE_DETAIL),HttpMethod.POST,request,String.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            String responseEntityBody = responseEntity.getBody();
            Document html = Jsoup.parse(responseEntityBody);
            Elements jsNodes = html.select("script");
            Element js = null;
            for(Element element : jsNodes){
                if(element.toString().contains("language")){
                    js = element;
                    break;
                }
            }
            if(null!=js && js.data().contains("alert")){
                String jsData = js.data();
                String message = jsData.substring(jsData.indexOf("'"), jsData.lastIndexOf("'"));
                if(message.contains("验证码错误")|| message.contains("需录入验证码")){
                    redisUtils.delete("COURSE_CODE");
                    getCourseDetail(uid, course);
                } else {
                    throw new WSExcetpion(message);
                }
            } else{
                Element table = html.selectFirst("#pageRpt");
                if(table == null){
                    return;
                }
                Elements exportDatas = table.select("table[name=theExportData]");
                String courseInfo = exportDatas.first().text();
                String[] infos=  courseInfo.split("：");
                course.setCollege(infos[1].substring(0,infos[1].lastIndexOf("院")+1));
                course.setClassHour(Integer.valueOf(infos[3].substring(0,infos[3].lastIndexOf("学")-2)));
                course.setCredit(Float.valueOf(infos[4]));
                Elements trs = exportDatas.get(1).select("tr");
                //处理课程tr信息
                getCourseInfo(course, trs);
            }

        }
    }

    private static void getCourseInfo(Course course, Elements trs) {
        List<CourseSchedule> courseSchedules = new ArrayList<>();
        int flag = 1;
        for(int i = 1; i < trs.size(); i++){
            CourseSchedule courseSchedule = new CourseSchedule();
            // 获取所有td
            Elements tds = trs.get(i).select("td");
            if(!tds.get(0).ownText().isEmpty()){
                flag = i;
                courseSchedule.setTeacherName(tds.get(0).ownText());
                courseSchedule.setStudentNo(Integer.valueOf(tds.get(2).ownText()));
                courseSchedule.setClassName(tds.get(5).ownText());
            } else{
                Elements tds2 = trs.get(flag).select("td");
                courseSchedule.setTeacherName(tds2.get(0).ownText());
                courseSchedule.setStudentNo(Integer.valueOf(tds2.get(2).ownText()));
                courseSchedule.setClassName(tds2.get(5).ownText());
            }
            courseSchedule.setAllWeek(getWeek(tds.get(6).ownText()));
            String s = tds.get(7).ownText();
            courseSchedule.setDay(s.substring(0, s.indexOf("[")));
            String z = s.substring(s.indexOf("[")+1, s.lastIndexOf("节"));
            String[] sin = z.split("-");
            StringBuffer session = new StringBuffer();
            if(sin.length == 2){
                for(int x = Integer.valueOf(sin[0]); x <= Integer.valueOf(sin[1]); x++){
                    session.append(x).append(",");
                }
                session.deleteCharAt(session.length()-1);
            } else if(sin.length == 1){
                session.append(sin[0]);
            }
            courseSchedule.setSession(session.toString());
            courseSchedule.setClassroomName(tds.get(8).ownText());
            courseSchedule.setId(course.getId() * 1000 + i);
            courseSchedule.setCourseId(course.getId());
            courseSchedule.setTermId(course.getTermId());
            courseSchedules.add(courseSchedule);
            if(i == 1){
                course.setExamType(tds.get(4).ownText());
                course.setType(tds.get(3).ownText());
            }
        }
        course.setCourseSchedules(courseSchedules);
    }

    private static String getWeek(String ownText) {
        String[] weeks = null;
        StringBuffer sb = new StringBuffer();
        // 判断是否有，号
        if(ownText.contains(",")){
            // 以，号进行分割
            weeks = ownText.split(",");
        }
        // 没有逗号
        if(weeks == null){
            weeks = new String[]{ownText};
        }
        // 遍历数组，根据-取得数值
        for(String w : weeks){
            if(w.contains("-")){
                String[] d = w.split("-");
                for(int i = Integer.valueOf(d[0]); i <= Integer.valueOf(d[1]); i++){
                    sb.append(i).append(",");
                }
            } else {
                sb.append(w).append(",");
            }
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
}
