package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Detail;

import java.util.List;
import java.util.Map;

public interface DetailService extends IService<Detail> {
    List<Detail> getDetailList(Map<String, String> params);

    Detail getDetailContent(long id);
}
