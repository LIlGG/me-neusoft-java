package com.lixingyong.meneusoft.api.evaluate;

import com.google.gson.Gson;
import com.lixingyong.meneusoft.api.evaluate.VO.EvaluateVO;
import com.lixingyong.meneusoft.api.evaluate.VO.TaskListVO;
import com.lixingyong.meneusoft.api.evaluate.VO.ValuationTaskVO;
import com.lixingyong.meneusoft.api.evaluate.util.ValuationUtil;
import com.lixingyong.meneusoft.api.jwc.JWCAPI;
import com.lixingyong.meneusoft.api.utils.RestUtils;
import com.lixingyong.meneusoft.api.vpn.VPNAPI;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.service.UserService;
import io.swagger.models.auth.In;
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

public class EvaluateUtil {
    private static RestTemplate restTemplate = RestUtils.getRestTemplate();
    private static RedisUtils redisUtils = RestUtils.getRedisUtils();
    private static Map<String,Object> map = new HashMap<>();
    private static UserService userService = RestUtils.getUserService();
    /**
     * 获取会话cookie
     */
    private static String getCookie(){
        /** 获取redis保存的VPNcookies */
//        if(!redisUtils.hasKey("SVPNCOOKIE") || !redisUtils.hasKey("SID")){
//            throw new WSExcetpion("redis中不存在SVPNCOOKIE");
//        }
        HttpEntity<String> request = new HttpEntity<>(null,null);
        ResponseEntity<String> responseEntity = restTemplate.exchange(EvaluateAPI.EVALUATE_LOGIN_PAGE, HttpMethod.GET,request,String.class);
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
    private static boolean getCookie(String cookie){
        // 将cookie放入header
        HttpHeaders headers = new HttpHeaders();
        List<String> cookieList = new ArrayList<>();
        cookieList.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookieList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);
        ResponseEntity<Map> responseEntity = restTemplate.exchange(EvaluateAPI.EVALUATE_LOGOUT, HttpMethod.POST,request,Map.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            if(!(boolean)responseEntity.getBody().get("success")){
                // 将当前cookie清除
                return true;
            }
        }
        return false;
    }


     /**
     * 执行登录UFS系统（使用课程评价系统测试）
     * @param
     * @param account
     * @param pw
     */
    public static boolean ufsLogin(long userId, String account, String pw) {
        // 若cookie中存在当前cookie,则执行注销程序
        if(redisUtils.hasKey(userId+"UFS_COOKIE")){
            // 执行注销程序
            if(getCookie(redisUtils.get(userId+"UFS_COOKIE"))){
                // 清除redis保存的数据
                redisUtils.delete(userId+"UFS_COOKIE");
            }
        }
        //登录前首先获取本次登录所需cookie
        String cookie = getCookie();
        HttpHeaders headers = new HttpHeaders();
        // 将cookie放入header
        List<String> cookieList = new ArrayList<>();
        cookieList.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookieList); //将Cookies放入Header
        MultiValueMap<String,String> param = new LinkedMultiValueMap<>();
        param.add("loginModel.studentNo", account);
        param.add("loginModel.password", pw);
        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(param,headers);//将参数和header组成一个请求
        ResponseEntity<Map> response = restTemplate.exchange(EvaluateAPI.EVALUATE_LOGIN,HttpMethod.POST,request,Map.class);
        if(response.getStatusCode().is2xxSuccessful()){
            if((boolean)response.getBody().get("success")){
                // 将当前cookie保存
                redisUtils.set(userId+"UFS_COOKIE",cookie);
                return true;
            }
        }
        return false;
    }

    /**
     * 登录验证
     */
    private static void loginVail(Long userId) throws WSExcetpion{
        // 若cookie中不存在cookie,则执行登录程序
        if(!redisUtils.hasKey(userId+"UFS_COOKIE")){
            User user = userService.getUserInfo(userId.intValue());
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
    public static EvaluateVO getEvaluates(Long userId) throws WSExcetpion {
        loginVail(userId);
        HttpHeaders headers = new HttpHeaders();
        //登录前首先获取本次登录所需cookie
        String cookie = redisUtils.get(userId+"UFS_COOKIE");
        // 将cookie放入header
        List<String> cookies = new ArrayList<>();
        cookies.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookies); //将Cookies放入Header
        HttpEntity request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        ResponseEntity<EvaluateVO> response = restTemplate.exchange(EvaluateAPI.EVALUATE_LOGIN_ACTION,HttpMethod.POST,request,EvaluateVO.class);
        if(response.getStatusCode().is2xxSuccessful()){
           EvaluateVO evaluateVO = response.getBody();
           if(evaluateVO.getErrorMsg().length() > 0){
               if(evaluateVO.getErrorMsg().contains("您没有权限访问本页面")){
                   //删除cookie，重新登录
                   redisUtils.delete(userId+"UFS_COOKIE");
                   evaluateVO = getEvaluates(userId);
               }
           }
           return evaluateVO;
        }
        return null;
    }

    public static List<TaskListVO> getTaskList(Long userId, ValuationTaskVO valuationTaskVO) {
        if(!(valuationTaskVO.getUrl().length() > 0)){
            return null;
        }
        // 执行获取当前列表的全部数据
        loginVail(userId);
        HttpHeaders headers = new HttpHeaders();
        //登录前首先获取本次登录所需cookie
        String cookie = redisUtils.get(userId+"UFS_COOKIE");
        // 将cookie放入header
        List<String> cookies = new ArrayList<>();
        cookies.add(cookie);
        headers.put(HttpHeaders.COOKIE,cookies); //将Cookies放入Header
        HttpEntity request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        map.put("eisId", valuationTaskVO.getEisId());
        map.put("yearNo", valuationTaskVO.getAcademicYearNo());
        map.put("termNo", valuationTaskVO.getTermNo());
        ResponseEntity<String> response = restTemplate.exchange(valuationTaskVO.getUrl(),HttpMethod.GET,request,String.class, map);
        if(response.getStatusCode().is2xxSuccessful()){
            String html = response.getBody();
            if(html.contains("您没有权限访问本页面")){
                //删除cookie，重新登录
                redisUtils.delete(userId+"UFS_COOKIE");
                getTaskList(userId, valuationTaskVO);
            }
            Document document =  Jsoup.parse(html);
            Element tbody =  document.select(".table").first().select("tbody").first();
            // 处理tbody
            return taskTrs(tbody);
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
}
