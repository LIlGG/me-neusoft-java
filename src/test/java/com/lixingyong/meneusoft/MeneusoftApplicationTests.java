package com.lixingyong.meneusoft;

import com.lixingyong.meneusoft.modules.xcx.entity.Notice;
import com.lixingyong.meneusoft.modules.xcx.service.NoticeService;
import com.lixingyong.meneusoft.modules.xcx.service.impl.NoticeServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeneusoftApplicationTests {
    @Autowired
    private NoticeService noticeService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void mybatisTest(){
        Notice notice = new Notice();
        notice.setTitle("测试");
        notice.setContent("内容");
        notice.setCreatedAt(new Date());
        noticeService.addNotice(notice);
    }

}
