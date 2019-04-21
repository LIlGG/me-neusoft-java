package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.LostFindDao;
import com.lixingyong.meneusoft.modules.xcx.entity.LostFind;
import com.lixingyong.meneusoft.modules.xcx.service.LostFindService;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @ClassName LostFindServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("lostFindService")
public class LostFindServiceImpl extends ServiceImpl<LostFindDao, LostFind> implements LostFindService {

    @Override
    public List<LostFind> getLostFindList(int count, int pageSize, String category, int userId) {
        Page<LostFind> page = new Page<>(count, pageSize);
       Wrapper<LostFind> entityWrapper =  new EntityWrapper<>();
       entityWrapper.eq("category", category);
       if(userId != 0){
           entityWrapper.eq("user_id", userId);
       }
        return this.baseMapper.selectPage(page, entityWrapper);
    }

    @Override
    public void addLostFind(LostFind lostFind) {
        this.baseMapper.insert(lostFind);
    }

    @Override
    public LostFind getLostFind(int itemId) {
        return this.baseMapper.selectById(itemId);
    }

    @Override
    public void updateLostFindInfo(LostFind lostFind) {
        this.baseMapper.updateById(lostFind);
    }

}
