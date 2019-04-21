package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.WechatDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Wechat;
import com.lixingyong.meneusoft.modules.xcx.entity.WxUser;
import com.lixingyong.meneusoft.modules.xcx.service.WechatService;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName WechatServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/20 16:24
 * @Version 1.0
 */
@Service("wechatService")
public class WechatServiceImpl extends ServiceImpl<WechatDao, Wechat> implements WechatService {
    @Override
    public Wechat getWechat(String openid) {
        List<Wechat>  wechats= this.baseMapper.selectList(new EntityWrapper<Wechat>().eq("openid",openid));
        if(wechats.size() > 0){
            return wechats.get(0);
        }
        return null;
    }

    @Override
    public Wechat getWechat(int id, String type) {
        List<Wechat>  wechats = new ArrayList<>();
        if(type.equals("userid")){
            wechats = this.baseMapper.selectList(new EntityWrapper<Wechat>().eq("user_id",id));
        }
        if(wechats.size() > 0){
            return wechats.get(0);
        }
        return null;
    }
    @Override
    public Wechat selectByUserId(String userId) {
        return this.baseMapper.selectList(new EntityWrapper<Wechat>().eq("user_id", userId)).get(0);
    }

    @Override
    public void addOtherData(Wechat wechat, String result) throws JSONException {
        JSONObject jsonObject = new JSONObject(result);
        wechat.setNickName(jsonObject.get("nickName").toString());
        wechat.setAvatarUrl(jsonObject.get("avatarUrl").toString());
        if(Integer.valueOf(jsonObject.get("gender").toString()) == 0){
            wechat.setGender("女");
        }else{
            wechat.setGender("男");
        }

        this.insert(wechat);
    }
}
