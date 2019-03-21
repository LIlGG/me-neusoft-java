package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.modules.xcx.dao.TagDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Tag;
import com.lixingyong.meneusoft.modules.xcx.service.TagService;
import org.springframework.stereotype.Service;

/**
 * @ClassName TagServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {
}
