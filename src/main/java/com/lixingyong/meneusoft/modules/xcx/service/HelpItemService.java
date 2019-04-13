package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.HelpItem;

import java.util.List;

public interface HelpItemService extends IService<HelpItem> {
    List<HelpItem> getHelpItemList();
}
