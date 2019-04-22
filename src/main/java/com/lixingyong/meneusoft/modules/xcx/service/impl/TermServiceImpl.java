package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.common.utils.DateUtils;
import com.lixingyong.meneusoft.modules.xcx.dao.TermDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Term;
import com.lixingyong.meneusoft.modules.xcx.service.TermService;
import org.springframework.stereotype.Service;


;import java.util.Date;
import java.util.List;

/**
 * @ClassName TermServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("termService")
public class TermServiceImpl extends ServiceImpl<TermDao, Term> implements TermService {
    @Override
    public void addTermList(List<Term> termList) {
        this.insertOrUpdateBatch(termList);
    }

    @Override
    public Term getCurTermInfo() {
        String newDate = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
        Term term = this.selectOne(new EntityWrapper<Term>()
                .le("start_time",newDate)
                .ge("end_time", newDate));
        return term;
    }
}
