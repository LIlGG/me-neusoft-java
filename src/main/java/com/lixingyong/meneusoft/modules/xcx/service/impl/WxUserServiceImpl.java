package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.dao.WxUserDao;
import com.lixingyong.meneusoft.modules.xcx.entity.WxUser;
import com.lixingyong.meneusoft.modules.xcx.service.WxUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName WxUserServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/11/2 17:19
 * @Version 1.0
 */
@Service("wxUserService")
public class WxUserServiceImpl extends ServiceImpl<WxUserDao, WxUser> implements WxUserService {

    @Override
    public void addUser(WxUser wxUser) {
        if(!(baseMapper.insert(wxUser)>0)){
            throw new WSExcetpion("新增失败");
        }
    }

    @Override
    public void updateUser(WxUser wxUser) {
        if(!((baseMapper.update(wxUser,new EntityWrapper<WxUser>().eq(wxUser.getOpenid()!=null,"openid",wxUser.getOpenid())))>0)){
            throw new WSExcetpion("更新失败");
        }else{
            List<WxUser> list =  baseMapper.selectList(new EntityWrapper<WxUser>().eq(wxUser.getOpenid()!=null,"openid",wxUser.getOpenid()));
            wxUser.setUserId(list.get(0).getUserId());
        }
    }

    @Override
    public void delectBatch(String open_id) {

    }

    @Override
    public void delectBatch(Long user_id) {

    }

    @Override
    public boolean isUser(Long user_id) {
        if(this.selectCount(new EntityWrapper<WxUser>().eq(user_id != null,"user_id",user_id))> 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean isUser(String open_id) {
        if(baseMapper.selectCount(new EntityWrapper<WxUser>().eq(open_id != null,"openid",open_id))> 0){
            return true;
        }
        return false;
    }
}
