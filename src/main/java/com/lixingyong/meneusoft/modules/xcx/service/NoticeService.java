package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Notice;

import java.util.List;

public interface NoticeService  extends IService<Notice> {
    void addNotice(Notice notice);

    List<Notice> getNoticeBanner();

    Notice noticeInfo(int id);

    Notice getNewNotice();

    void updateNotice(Notice notice);
}
