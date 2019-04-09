package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Ecard;
import com.lixingyong.meneusoft.modules.xcx.entity.User;

import java.util.List;

public interface EcardService extends IService<Ecard> {
    void insertOrUpdateEcardInfo(List<Ecard> ecards);

    void updateEcards(User user);

    List<Ecard> getEcardList(String userId);
}
