package com.lixingyong.meneusoft.api.evaluate;

import com.lixingyong.meneusoft.api.evaluate.VO.*;
import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import com.lixingyong.meneusoft.modules.xcx.vo.Evaluate;
import com.lixingyong.meneusoft.modules.xcx.vo.EvaluatesVO;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 获取评教信息（非VPN状态）
 */
public class EvaluateUtil extends EvaluateAbs {
    /**
     * 获取会话cookie
     */
    private static String getCookie() throws WSExcetpion{
        setCookies(new LinkedList<>());
        HttpEntity request = httpEntity();
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL(EvaluateAPI.EVALUATE_LOGIN_PAGE), HttpMethod.GET,request,String.class,getMap());
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
     * 注销系统
     */
    private static boolean logout(String cookie){
        List<String> cookieList = new ArrayList<>();
        cookieList.add(cookie);
        setCookies(cookieList);
        // 将cookie放入header
        HttpEntity request = httpEntity();
        ResponseEntity<Map> responseEntity = restTemplate.exchange(URL(EvaluateAPI.EVALUATE_LOGOUT), HttpMethod.POST,request,Map.class,getMap());
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            // 将当前cookie清除
            return !(boolean) responseEntity.getBody().get("success");
        }
        return false;
    }


     /**
     * 执行登录UFS系统（使用课程评价系统测试）
     * @param
     * @param account
     * @param pw
     */
    public static boolean ufsLogin(long userId, String account, String pw) throws WSExcetpion {
        // 若cookie中存在当前cookie,则执行注销程序
        if(redisUtils.hasKey(userId+"EVALUATE_COOKIE")){
            // 执行注销程序
            if(logout(redisUtils.get(userId+"EVALUATE_COOKIE"))){
                // 清除redis保存的数据
                redisUtils.delete(userId+"EVALUATE_COOKIE");
            }
        }

        //登录前首先获取本次登录所需cookie
        String cookie = getCookie();
        // 将cookie放入header
        List<String> cookieList = new ArrayList<>();
        cookieList.add(cookie);
        setCookies(cookieList);
        HttpHeaders headers = httpHeaders();
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("loginModel.studentNo", account);
        param.add("loginModel.password", pw);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,headers);//将参数和header组成一个请求
        ResponseEntity<Map> response = restTemplate.exchange(URL(EvaluateAPI.EVALUATE_LOGIN),HttpMethod.POST,request,Map.class, getMap());
        if(response.getStatusCode().is2xxSuccessful()){
            String errorMsg = response.getBody().get("errorMsg").toString();
            if(StringUtils.isNotBlank(errorMsg)){
                throw new WSExcetpion(errorMsg,21001);
            }
            if(response.getBody().get("success").toString().equals("true")){
                // 将当前cookie保存
                redisUtils.set(userId+"EVALUATE_COOKIE",cookie);
                return true;
            }
        }
        return false;
    }

    /**
     * 登录验证
     */
    private static void loginVail(int userId) throws WSExcetpion{
        // 若cookie中不存在cookie,则执行登录程序
        if(!redisUtils.hasKey(userId+"EVALUATE_COOKIE")){
            User user = userService.getUserInfo(userId);
            if(user.getVerify() == 1){
                if(!ufsLogin(userId, user.getStudentId(), user.getPassword())){
                    throw new WSExcetpion("登录评教系统失败");
                }
            }else {
                throw new WSExcetpion("当前用户账号不存在或不可登录");
            }
        }
    }

    /**
     * 登录主界面以后，获取当前评教信息
     */
    public static EvaluateVO getEvaluates(int userId) throws WSExcetpion {
        loginVail(userId);
        setCookies(new LinkedList<>());
        HttpEntity request = vailHttpEntity(userId);
        ResponseEntity<EvaluateVO> response = restTemplate.exchange(URL(EvaluateAPI.EVALUATE_LOGIN_ACTION),HttpMethod.POST,request,EvaluateVO.class, getMap());
        if(response.getStatusCode().is2xxSuccessful()){
           EvaluateVO evaluateVO = response.getBody();
           if(evaluateVO.getErrorMsg().length() > 0){
               if(evaluateVO.getErrorMsg().contains("您没有权限访问本页面")){
                   //删除cookie，重新登录
                   redisUtils.delete(userId+"EVALUATE_COOKIE");
                   evaluateVO = getEvaluates(userId);
               }
           }
           return evaluateVO;
        }
        return null;
    }

    public static List<TaskListVO> getTaskList(int userId, ValuationTaskVO valuationTaskVO) {
        if(!(valuationTaskVO.getUrl().length() > 0)){
            return null;
        }
        setCookies(new LinkedList<>());
        // 执行获取当前列表的全部数据
        HttpEntity request = vailHttpEntity(userId);//将参数和header组成一个请求
        map = new HashMap<>();
        map.put("requestType", "query");
        map.put("isDetail", "");
        map.put("eisId", valuationTaskVO.getEisId());
        map.put("yearNo", valuationTaskVO.getAcademicYearNo());
        map.put("termNo", valuationTaskVO.getTermNo());
        setMap(map);
        ResponseEntity<String> response = restTemplate.exchange(URL(valuationTaskVO.getUrl()),HttpMethod.GET,request,String.class, getMap());
        if(response.getStatusCode().is2xxSuccessful()){
            String html = response.getBody();
            if(html.contains("您没有权限访问本页面")){
                //删除cookie，重新登录
                redisUtils.delete(userId+"EVALUATE_COOKIE");
                getTaskList(userId, valuationTaskVO);
            }
            Document document =  Jsoup.parse(html);

            try {
                Element tbody =  document.select(".table").first().select("tbody").first();
                // 处理tbody
                return taskTrs(tbody);
            }catch (NullPointerException n){
                return null;
            }
        }
        return null;
    }

    private static List<TaskListVO> taskTrs(Element tbody) {
        List<TaskListVO> taskListVOS = new LinkedList<>();
        Elements trs = tbody.select("tr");
        for(Element tr : trs){
            TaskListVO taskListVO = new TaskListVO();
            taskListVO.setStatus(0);
            Elements tds = tr.select("td");
            taskListVO.setId(Integer.valueOf(tds.first().text()));
            String name = tds.get(1).text();
            taskListVO.setTeacherName(name.substring(0, name.indexOf("(")));
            taskListVO.setCourseName(name.substring(name.indexOf("(") + 1, name.indexOf(")")));
            String[] urls = tds.get(2).select("a").first().attr("href").split("=");
            taskListVO.setTaskId(urls[urls.length - 1]);
            taskListVOS.add(taskListVO);
        }
        return taskListVOS;
    }

    /**
     * 获取问题列表
     * @param evaluatesVO
     * @param url
     * @param taskId
     */
    public static Evaluate getTaskIssue(EvaluatesVO evaluatesVO, String url, int taskId, int userId) throws WSExcetpion {
        loginVail(userId);
        setCookies(new LinkedList<>());
        HttpEntity request = vailHttpEntity(userId);//将参数和header组成一个请求
        map = new HashMap<>();
        map.put("requestType", "query");
        map.put("isDetail", "Detail");
        map.put("eisId", evaluatesVO.getEisId());
        map.put("yearNo", evaluatesVO.getYear());
        map.put("termNo", evaluatesVO.getTerm());
        map.put("taskId", taskId);
        setMap(map);
        ResponseEntity<String> response = restTemplate.exchange(URL(url),HttpMethod.GET, request, String.class, getMap());
        if(response.getStatusCode().is2xxSuccessful()){
            String html = response.getBody();
            if(html.contains("您没有权限访问本页面")){
                //删除cookie，重新登录
                redisUtils.delete(userId+"EVALUATE_COOKIE");
                getTaskIssue(evaluatesVO, url, taskId, userId);
            }
            Document document =  Jsoup.parse(html);
            Element frmValRecords = document.select("#frmValRecords").first();
            Elements valRecords =  frmValRecords.select(".valRecord");
            Evaluate evaluate = new Evaluate();
            List<QuestionVO> questions =  getQuestions(valRecords);
            List<HiddenInput> hiddenInputs = getHiddens(frmValRecords);
            evaluate.setItems(questions);
            evaluate.setHiddlers(hiddenInputs);
            return evaluate;
        }
        return null;
    }

    private static List<HiddenInput> getHiddens(Element frmValRecords) {
        List<HiddenInput> hiddens = new LinkedList<>();
        Elements hiddenInputs = frmValRecords.select("input[type=hidden]");
        for(Element input : hiddenInputs){
            HiddenInput hiddenInput = new HiddenInput();
            hiddenInput.setName(input.attr("name"));
            hiddenInput.setValue(input.attr("value"));
            hiddens.add(hiddenInput);
        }
        return hiddens;
    }

    private static List<QuestionVO> getQuestions(Elements valRecords) {
        List<QuestionVO> questions = new LinkedList<>();
        // 处理查找出来的数据
        for(Element valRecord : valRecords){
            QuestionVO  question = new QuestionVO();
            Element panel =  valRecord.select(".panel-body").first();
            if(panel.children().hasClass("radio-inline")){
                // 这时当前valRecord就是选择题
                // 选择题先保存所有的选项
                List<RadioVO> radios = new LinkedList<>();
                String name  = questionRadios(panel.children(), radios);
                question.setRadios(radios);
                question.setName(name);
                question.setType("radio");
            } else if(panel.children().hasClass("form-control")){
                // 这时就是简答
                // 简答题直接保存问题即可
                String name = panel.children().first().attr("name");
                question.setName(name);
                question.setType("textarea");
            }
            String[] names = valRecord.select(".panel-heading").text().split("、");
            StringBuilder name = new StringBuilder();
            for(int i = 1; i < names.length; i++){
                name.append(names[i]);
                if(i != names.length - 1){
                    name.append("、");
                }
            }
            question.setTitle(name.toString());
            questions.add(question);
        }
        return questions;
    }

    private static String questionRadios(Elements labels, List<RadioVO> radios) {
        for(Element label : labels){
            RadioVO radioVO = new RadioVO();
            Element radio = label.select("input").first();
            String[] radioInput = label.text().split("、");
            radioVO.setValue(radioInput[0].equals("")?radio.attr("value"): radioInput[0]);
            StringBuilder name = new StringBuilder();
            for(int i = 1; i < radioInput.length; i++){
                name.append(radioInput[i]);
                if(i != radioInput.length - 1){
                    name.append("、");
                }
            }
            radioVO.setName(name.toString());
            radioVO.setChecked(false);
            radios.add(radioVO);
        }
        return labels.first().select("input").first().attr("name");
    }
}
