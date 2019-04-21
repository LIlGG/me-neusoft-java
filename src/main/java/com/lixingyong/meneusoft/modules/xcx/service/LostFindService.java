package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.LostFind;

import java.util.List;


public interface LostFindService extends IService<LostFind> {
    List<LostFind> getLostFindList(int page, int pageSize, String category, int my);

    void addLostFind(LostFind lostFind);

    LostFind getLostFind(int itemId);

    void updateLostFindInfo(LostFind lostFind);
}
