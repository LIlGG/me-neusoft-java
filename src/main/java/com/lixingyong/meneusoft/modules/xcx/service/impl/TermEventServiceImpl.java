package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.modules.xcx.dao.TermDao;
import com.lixingyong.meneusoft.modules.xcx.dao.TermEventDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Term;
import com.lixingyong.meneusoft.modules.xcx.entity.TermEvent;
import com.lixingyong.meneusoft.modules.xcx.service.TermEventService;
import com.lixingyong.meneusoft.modules.xcx.service.TermService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

;

/**
 * @ClassName TermEventServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("termEventService")
public class TermEventServiceImpl extends ServiceImpl<TermEventDao, TermEvent> implements TermEventService {
    @Override
    public void addTermEvents(List<TermEvent> termEvents) throws IllegalArgumentException{
        this.insertOrUpdateBatch(termEvents);
    }

    @Override
    public List<TermEvent> getCurTermEvents(int id) {
        List<TermEvent> termEvents = this.baseMapper.selectList(new EntityWrapper<TermEvent>().eq("term_id", id));
        return termEvents;
    }
}
