package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.api.news.NewsUtil;
import com.lixingyong.meneusoft.modules.xcx.dao.TagDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Tag;
import com.lixingyong.meneusoft.modules.xcx.service.TagService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TagServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagDao, Tag> implements TagService {
    @Override
    public List<Tag> getTagList() {
        List<Tag> tags = this.baseMapper.selectList(new EntityWrapper<>());
        if(tags.size() < 1){ //为空则直接从官网更新标签
            tags = NewsUtil.getTagList();
            //向数据库中添加标签
            this.insertOrUpdateBatch(tags);
        }
        return tags;
    }

    @Override
    public Tag getTag(String tag_name) {
       return this.baseMapper.selectList(new EntityWrapper<Tag>().eq("name",tag_name)).get(0);
    }
}
