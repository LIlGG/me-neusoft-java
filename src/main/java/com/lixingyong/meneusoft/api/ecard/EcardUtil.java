package com.lixingyong.meneusoft.api.ecard;
import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.Ecard;
import com.lixingyong.meneusoft.modules.xcx.service.EcardService;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.core.io.Resource;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取一卡通（外网状态）
 */
public class EcardUtil {
    private static RestTemplate restTemplate = RestConfig.getRestTemplate();
    private static RedisUtils redisUtils = RestConfig.getRedisUtils();
    private static Map<String,Object> map = new HashMap<>();
    private static EcardService ecardService = RestConfig.getEcardService();
    private static Logger logger = LoggerFactory.getLogger(EcardUtil.class);

    public static void loginEcard(long uid, String ecardId) throws WSExcetpion {
        HttpEntity<String> request = new HttpEntity<>(null, null);
        map.put("ecardId",ecardId);
        ResponseEntity<Resource> responseEntity = restTemplate.exchange(EcardAPI.ECARDCOOKIE, HttpMethod.GET,request,Resource.class,map);
        if(responseEntity.getStatusCode().is3xxRedirection()){
            if(responseEntity.getHeaders().containsKey("Set-Cookie")){
                if(responseEntity.getHeaders().get("Set-Cookie").size() > 0){
                    if(responseEntity.getHeaders().get("Set-Cookie").get(0).startsWith("sessionid")){
                        String[] cookies = responseEntity.getHeaders().get("Set-Cookie").get(0).split(";");
                        String session = cookies[0];
                        //将获取到的数据存入redis数据库中并返回
                        redisUtils.set(uid+"ECARDCOOKIE",session);
                        logger.info("获取一卡通Cookie成功");
                        return;
                    }
                }
            }
        }
        throw new WSExcetpion("获取一卡通Cookie失败");
    }


    public static void updateEcardInfo(long uid, String ecardId) throws WSExcetpion {
        HttpHeaders headers = new HttpHeaders();
        /** 获取redis保存的cookies */
        if(!redisUtils.hasKey(uid+"ECARDCOOKIE")){
            // 执行登录
            loginEcard(uid, ecardId);
        }
        List<String> cookiesList = new ArrayList<>();
        cookiesList.add(redisUtils.get(uid+"ECARDCOOKIE"));
        headers.put(HttpHeaders.COOKIE,cookiesList); //将Cookies放入Header
        HttpEntity<String> request = new HttpEntity<>(null,headers);//将参数和header组成一个请求
        for(int i = 1 ; i <= 12 ;i++){
            String month = StringUtils.leftPad(String.valueOf(i), 2,"0");
            map.put("month", month);
            ResponseEntity<String> responseEntity = restTemplate.exchange( EcardAPI.ECATDDETAIL, HttpMethod.GET,request,String.class,map);
            if(responseEntity.getStatusCode().is2xxSuccessful()){
                Document document = Jsoup.parse(responseEntity.getBody());
                analysisEcardHtml(document,uid);
            }
        }
    }

    private static void analysisEcardHtml(Document document, long uid) {
        Elements elements = document.select(".table-striped > tbody");
        if(elements.isEmpty()){
            return;
        }
        Elements trs = elements.first().select("tr");
        List<Ecard> ecards = new ArrayList<>(trs.size());
        for(Element tr : trs){
            Elements tds = tr.select("td");
            Ecard ecard = new Ecard();
            ecard.setId(Integer.valueOf(tds.first().text()));
            ecard.setUserId((int)uid);
            ecard.setAddr((tds.get(3).text()));
            String regEx="[-]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(tds.get(5).text());
            ecard.setMoney(Float.valueOf(m.replaceAll("").trim()));
            ecard.setBalance(Float.valueOf(tds.get(6).text()));
            ecard.setTransTime(tds.get(1).text());
            ecards.add(ecard);
        }
        /** 保存数据 */
        ecardService.insertOrUpdateEcardInfo(ecards);
    }


}
