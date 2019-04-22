package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Term;
import com.lixingyong.meneusoft.modules.xcx.entity.TermEvent;

import java.util.List;


public interface TermEventService extends IService<TermEvent> {


    void addTermEvents(List<TermEvent> termEvents);

    List<TermEvent> getCurTermEvents(int id);
}
