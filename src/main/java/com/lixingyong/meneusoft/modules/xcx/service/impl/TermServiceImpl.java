package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.TermDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Term;
import com.lixingyong.meneusoft.modules.xcx.service.TermService;
import org.springframework.stereotype.Service;


;

/**
 * @ClassName TermServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("termService")
public class TermServiceImpl extends ServiceImpl<TermDao, Term> implements TermService {
}
