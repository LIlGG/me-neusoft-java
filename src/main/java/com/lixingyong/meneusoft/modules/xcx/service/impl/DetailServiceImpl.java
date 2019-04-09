package com.lixingyong.meneusoft.modules.xcx.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.lixingyong.meneusoft.api.news.NewsAPI;
import com.lixingyong.meneusoft.api.news.NewsUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.modules.xcx.dao.DetailDao;
import com.lixingyong.meneusoft.modules.xcx.entity.Detail;
import com.lixingyong.meneusoft.modules.xcx.entity.Tag;
import com.lixingyong.meneusoft.modules.xcx.service.DetailService;
import com.lixingyong.meneusoft.modules.xcx.service.TagService;
import javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DetailServiceImpl
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 12:00
 * @Version 1.0
 */
@Service("detailService")
public class DetailServiceImpl extends ServiceImpl<DetailDao, Detail> implements DetailService {
    @Autowired
    private TagService tagService;
    @Override
    public List<Detail> getDetailList(Map<String, String> params) throws WSExcetpion {
        // 获取到标签URL之后直接返回最新的新闻内容并更新或保存
        String url;
        if(params.get("tag_name").equals("") || params.get("tag_name").equals("全部")){
            //更新标签
//            tagService.getTagList();
            url = NewsAPI.NEWS_HOME;
        } else {
            // 根据标签查询url
            Tag tag = tagService.getTag(params.get("tag_name"));
            url = tag.getUrl();
            url = url + params.get("page")+".shtml";
        }
        List<Detail> detailList = NewsUtil.getNewsList(url);
        // 更新或保存数据库
        this.insertOrUpdateBatch(detailList);
        return detailList;
    }

    @Override
    public Detail getDetailContent(long id) {
        // 根据当前id查询当前资讯
        Detail detail = this.selectById(id);
        if(null == detail.getContent()){ // 当内容为空时
            // 使用当前文章的URL读取文章并保存
            String url = detail.getUrl();
            String content = NewsUtil.getNewDetail(url);
            detail.setContent(content);
            this.updateById(detail);
        }
        return detail;
    }
}
