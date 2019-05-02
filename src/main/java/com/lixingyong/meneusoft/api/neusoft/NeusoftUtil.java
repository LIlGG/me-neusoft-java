package com.lixingyong.meneusoft.api.neusoft;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.common.utils.RedisUtils;
import com.lixingyong.meneusoft.modules.xcx.entity.Term;
import com.lixingyong.meneusoft.modules.xcx.entity.TermEvent;
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
 * 其他所有类型的教学系统请求
 */
public class NeusoftUtil extends NeusoftAbs {
    /**
     * 获取校历列表请求（需登录VPN）
     */
    public static Map<Integer, String> termList(){
        setCookies(new LinkedList<>());
        Map<Integer, String> terms = new LinkedHashMap<>();
        HttpEntity request = httpEntity();
        ResponseEntity<String> response = restTemplate.exchange(URL(NeusoftAPI.TERM), HttpMethod.GET,request,String.class, getMap());
        if(response.getStatusCode().is2xxSuccessful()){
            Document html = Jsoup.parse(Objects.requireNonNull(response.getBody()));
            Elements options = html.select("select[name=sel_xnxq] > option");
            for(Element option : options){
                Integer value = Integer.valueOf(option.attr("value"));
                terms.put(value, option.text());
            }
        }
        return terms;
    }

    public static Term term(Integer termId, String termName) {
        setCookies(new LinkedList<>());
        Term term = new Term();
        HttpHeaders httpHeaders = httpHeaders();
        MultiValueMap<String,Object> param = new LinkedMultiValueMap<>();
        param.add("sel_xnxq", termId);
        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(param,httpHeaders);//将参数和header组成一个请求
        ResponseEntity<String> response = restTemplate.exchange(URL(NeusoftAPI.TERM), HttpMethod.POST, request,String.class, getMap());
        if(response.getStatusCode().is2xxSuccessful()){
            Document html = Jsoup.parse(Objects.requireNonNull(response.getBody()));
            Element table = html.selectFirst("b:containsOwn("+termName+")");
            Element td = table.parents().select("tr").first().nextElementSibling().selectFirst("td");
            String[] dates = td.text().split("至");
            for(int i = 0; i < dates.length; i++){
                dates[i] = dates[i].replaceAll("[()]", "");
            }
            term.setId(termId);
            term.setName(termName);
            term.setStartTime(DateUtils.stringToDate(dates[0], DateUtils.DATE_PATTERN));
            term.setEndTime(DateUtils.stringToDate(dates[1], DateUtils.DATE_PATTERN));
        }
        return term;
    }

    public static List<TermEvent> getTermEvents(int id, String date) {
        setCookies(new LinkedList<>());
        ArrayList<TermEvent> termEvents = new ArrayList<>();
        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(null,null);//将参数和header组成一个请求
        String[] ym = date.split("-");
        map = new HashMap<>();
        map.put("year",ym[0]);
        map.put("month", ym[1]);
        date = ym[0] + "-"+ ym[1].replace("0", "");
        ResponseEntity<String> response = restTemplate.exchange(NeusoftAPI.YEAR_API, HttpMethod.GET,request,String.class, map);
        if(response.getStatusCode().is2xxSuccessful()){
            String body = response.getBody();
            Map map = new Gson().fromJson(body, Map.class);
            List datas = (List) map.get("data");
            if(datas.size() ==  0){
                return getTermEvents(id, date);
            }
            LinkedTreeMap data = (LinkedTreeMap) datas.get(0);
            try {
                ArrayList<LinkedTreeMap> holidays  = (ArrayList<LinkedTreeMap>) data.get("holiday");
                for(LinkedTreeMap holiday : holidays) {
                    setTermEvent(id, date, termEvents, ym, holiday);
                }
            }catch (ClassCastException e){
                LinkedTreeMap holidays = (LinkedTreeMap) data.get("holiday");
                setTermEvent(id, date, termEvents, ym, holidays);
            } catch (NullPointerException ignored){

            }


        }
        return termEvents;
    }

    private static void setTermEvent(int id, String date, ArrayList<TermEvent> termEvents, String[] ym, LinkedTreeMap holiday) {
        if (holiday.get("festival").toString().contains(date)) {
            TermEvent event = new TermEvent();
            event.setId(id + Integer.valueOf(ym[0]) + Integer.valueOf(ym[1]));
            event.setTermId(id);
            event.setName(holiday.get("name").toString());
            List<Map> list = (List<Map>) holiday.get("list");
            String startTime = "", endTime = "";
            for (int i = 0; i < list.size(); i++) {
                if (Integer.valueOf(list.get(i).get("status").toString()) == 1) {
                    startTime = list.get(i).get("date").toString();
                    break;
                }
            }
            for (int i = list.size() - 1; i >= 0; i--) {
                if (Integer.valueOf(list.get(i).get("status").toString()) == 1) {
                    endTime = list.get(i).get("date").toString();
                    break;
                }
            }
            event.setStartTime(DateUtils.stringToDate(startTime, DateUtils.DATE_PATTERN));
            event.setEndTime(DateUtils.stringToDate(endTime, DateUtils.DATE_PATTERN));
            termEvents.add(event);
        }
    }
}
