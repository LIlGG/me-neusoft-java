package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.api.ecard.EcardUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.dao.EcardDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Ecard;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.service.EcardService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TagServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("ecardService")
public class EcardServiceImpl extends ServiceImpl<EcardDao, Ecard> implements EcardService {

    @Override
    public void insertOrUpdateEcardInfo(List<Ecard> ecards) {
        this.insertOrUpdateBatch(ecards);
    }

    @Override
    public void updateEcards(User user) throws WSExcetpion {
        if(null == user.getEcardId()){
            throw new WSExcetpion("用户一卡通账户不存在");
        }

        EcardUtil.updateEcardInfo(user.getId(), user.getEcardId());
    }

    @Override
    public List<Ecard> getEcardList(String userId) {
        return this.baseMapper.selectList(new EntityWrapper<Ecard>().eq("user_id", userId).orderBy("id", false));
    }
}
