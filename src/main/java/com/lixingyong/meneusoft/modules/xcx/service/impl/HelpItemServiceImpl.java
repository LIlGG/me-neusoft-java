package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.HelpItemDao;
import com.lixingyong.meneusoft.modules.xcx.entity.HelpItem;
import com.lixingyong.meneusoft.modules.xcx.service.HelpItemService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @ClassName TagServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("helpItemService")
public class HelpItemServiceImpl extends ServiceImpl<HelpItemDao, HelpItem> implements HelpItemService {
    @Override
    public List<HelpItem> getHelpItemList() {
        return this.baseMapper.selectList(new EntityWrapper<HelpItem>().orderBy("sort"));
    }
}
