package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.NoticeDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Notice;
import com.lixingyong.meneusoft.modules.xcx.service.NoticeService;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName NoticeServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("noticeService")
public class NoticeServiceImpl extends ServiceImpl<NoticeDao, Notice> implements NoticeService {
    @Override
    public void addNotice(Notice notice) {
        this.baseMapper.insert(notice);
    }

    @Override
    public List<Notice> getNoticeBanner() {
        return this.baseMapper.selectList(new EntityWrapper<Notice>().eq("newest",0).orderBy("updated_at"));
    }

    @Override
    public Notice noticeInfo(int id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public Notice getNewNotice() {
        List<Notice> notices = this.baseMapper.selectList(new EntityWrapper<Notice>().eq("newest",1).orderBy("updated_at",false));
        if(notices.size() > 0){
            return notices.get(0);
        }
        return null;
    }

    @Override
    public void updateNotice(Notice notice) {
        this.baseMapper.updateById(notice);
    }
}
