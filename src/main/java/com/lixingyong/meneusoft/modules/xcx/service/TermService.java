package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Term;

import java.util.List;


public interface TermService extends IService<Term> {
    void addTermList(List<Term> termList);

    Term getCurTermInfo();

}
