package com.lixingyong.meneusoft.modules.xcx.service;

import com.baomidou.mybatisplus.service.IService;
import com.lixingyong.meneusoft.modules.xcx.entity.Tag;

import java.util.List;

public interface TagService extends IService<Tag> {
    List<Tag> getTagList();

    Tag getTag(String tag_name);
}
