package com.lixingyong.meneusoft.api.github;

import com.google.gson.Gson;
import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.vo.Issue;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;


public class GitHubUtil {
    private static RestTemplate restTemplate = RestConfig.getRestTemplate();
    /** githubToken */
    private static String token = RestConfig.getToken();

    public static int createdIssues(String title, String body, String[] lables){
        // 创建Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization","token "+token);
        Map<String, Object> param = new HashMap<>();
        param.put("title", title);
        param.put("body", body);
        param.put("labels", lables);
        String bodys = new Gson().toJson(param);
        HttpEntity<String> request = new HttpEntity<>(bodys,headers);//将参数和header组成一个请求
        ResponseEntity<String> responseEntity = restTemplate.exchange(GitHubAPI.ISSUES, HttpMethod.POST,request,String.class);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
            String response = responseEntity.getBody();
            Map result = new Gson().fromJson(response, HashMap.class);
            if(!result.get("number").equals("")){
                Double d = (Double)result.get("number");
                return d.intValue();
            }
        }
        return  -1;
    }

    /**
     * 获取详细的反馈内容，包括评论
     * @param id
     * @return
     */
    public static Map<String, Object> getFeedBackDetailInfo(int id) throws WSExcetpion{
        Map<String, Object> result = new HashMap<>();
        ResponseEntity<String> responseEntity = null;
        List comments = new ArrayList();
        HttpHeaders headers = new HttpHeaders();
        Issue issue = new Issue();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization","token "+token);
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("issueId", id);
        try {
            responseEntity = restTemplate.exchange(GitHubAPI.ISSUE_DETAIL, HttpMethod.GET,request,String.class,map);
            if(responseEntity.getStatusCode().is2xxSuccessful()){
                // 获取issue信息
                Map body = new Gson().fromJson(responseEntity.getBody(), Map.class);
                issue.setTitle(body.get("title").toString());
                issue.setBody(body.get("body").toString());
                issue.setState(body.get("state").toString());
                // 获取当前issue的详细评论
                ResponseEntity<String> response = restTemplate.exchange(GitHubAPI.ISSUE_COMMENTS, HttpMethod.GET,request,String.class,map);
                if(response.getStatusCode().is2xxSuccessful()){
                    comments = new Gson().fromJson(response.getBody(), List.class);
                }
            }
            result.put("issue",issue);
            result.put("comments", comments);
            return result;
        }catch (HttpClientErrorException e){
            if(e.getRawStatusCode() == 410){
                throw new WSExcetpion("该Issue已被删除", 401);
            }
            throw new WSExcetpion("请求错误", e.getRawStatusCode());
        }
    }

    public static boolean addIssueComment(String issueId, String content) {
        // 创建Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization","token "+token);
        Map<String, Object> param = new HashMap<>();
        param.put("body", content);
        String bodys = new Gson().toJson(param);
        HttpEntity<String> request = new HttpEntity<>(bodys,headers);//将参数和header组成一个请求
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("issueId", issueId);
        ResponseEntity<String> responseEntity = restTemplate.exchange(GitHubAPI.ISSUE_COMMENTS, HttpMethod.POST,request,String.class,map);
        if(responseEntity.getStatusCode().is2xxSuccessful()){
           return true;
        }
        return false;
    }
}
