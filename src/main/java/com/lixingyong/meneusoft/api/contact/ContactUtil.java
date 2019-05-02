package com.lixingyong.meneusoft.api.contact;

import com.lixingyong.meneusoft.api.RestConfig;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactBook;
import com.lixingyong.meneusoft.modules.xcx.entity.ContactCategory;
import com.lixingyong.meneusoft.modules.xcx.entity.Teacher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 获取校园通讯录（外网状态）
 */
public class ContactUtil {
    private static RestTemplate restTemplate = RestConfig.getRestTemplate();

    public static List<Teacher> getContactBooksAndTeachers(ContactCategory contactCategory, List<ContactBook> contactBooks) throws WSExcetpion {
        HttpEntity<String> request = new HttpEntity<>(null, null);
        List<Teacher> teachers = null;
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(URLDecoder.decode(contactCategory.getUrl(), "UTF-8"), HttpMethod.GET,request,String.class);
            if(responseEntity.getStatusCode().is2xxSuccessful()){

                Document document = Jsoup.parse(Objects.requireNonNull(responseEntity.getBody()));
                Elements items = document.select(".wrapper-lists-our-team").first().select(".item");
                // 处理查找到的信息
                teachers = dealItems(items, contactCategory, contactBooks);
                return teachers;
            }
        } catch (Exception e){
            return teachers;
        }
        return null;
    }

    private static List<Teacher> dealItems(Elements items, ContactCategory contactCategory, List<ContactBook> contactBooks) {
        List<Teacher> teachers = new ArrayList<>();
        for(Element item : items){
            Element info = item.selectFirst(".content-team");
            Element title = info.selectFirst(".title");
            Element regency = info.selectFirst(".regency");
            Elements ourTeams = info.select(".our_team_email");
            //创建教师
            try {
                Teacher teacher = createdTeacher(title, regency, contactCategory.getName());
                teachers.add(teacher);
            }catch (Exception ignored){

            }
            // 创建通讯录
            try {
                ContactBook contactBook = createdContactBook(title,regency, ourTeams, contactCategory.getId());
                contactBooks.add(contactBook);
            }catch (Exception ignored){

            }
        }
        return teachers;
    }

    private static ContactBook createdContactBook(Element title, Element regency, Elements ourTeams, int id) {
        ContactBook contactBook = new ContactBook();
        contactBook.setTitle(title.selectFirst("a").text());
        contactBook.setEmail(ourTeams.first().text().contains("@")? ourTeams.first().text(): ourTeams.last().text());
        contactBook.setPhone(ourTeams.first().text().contains("@")? ourTeams.last().text(): ourTeams.first().text());
        contactBook.setContactCategoryId(id);
        contactBook.setComment(regency.text());
        return contactBook;
    }

    private static Teacher createdTeacher(Element title, Element regency, String name) {
        Teacher teacher = new Teacher();
        teacher.setName(title.selectFirst("a").text());
        teacher.setUrl(title.selectFirst("a").attr("href"));
        teacher.setCollege(name);
        teacher.setJobTitle(regency.text());
        return teacher;
    }
}
