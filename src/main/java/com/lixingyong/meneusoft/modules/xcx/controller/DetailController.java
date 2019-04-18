package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.entity.Detail;
import com.lixingyong.meneusoft.modules.xcx.entity.Tag;
import com.lixingyong.meneusoft.modules.xcx.service.DetailService;
import com.lixingyong.meneusoft.modules.xcx.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @ClassName DetailController
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/12 15:13
 * @Version 1.0
 */
@Api("资讯")
@RestController
public class DetailController {
    @Autowired
    private DetailService detailService;
    @Autowired
    private TagService tagService;

    @ApiOperation("资讯列表")
    @GetMapping(value = "/details")
    public R getDetailList(@RequestParam Map<String,String> params){
        if(null == params){
            return R.error("请检查参数");
        }
        try {
            List<Detail> details = detailService.getDetailList(params);
            return R.ok().put("data", details);
        }catch (WSExcetpion e){
            throw new WSExcetpion(e.getMsg());
        }
    }

    @ApiOperation("获取所有的资讯标签")
    @GetMapping(value = "/detail/tags")
    public R getDetailTags(){
        // 从数据库中直接读取，而数据库定期从官网更新标签
        try{
            List<Tag> tagList = tagService.getTagList();
            return R.ok().put("data", tagList);
        }catch (WSExcetpion e){
            return R.error(e.getMsg());
        }
    }

    @ApiOperation("获取一篇资讯")
    @GetMapping(value = "/detail/{id}")
    public R getDetail(@PathVariable String id){
        // 根据文章ID获取文章详细内容
        Detail detail = detailService.getDetailContent(Long.parseLong(id));
        return R.ok().put("data",detail);
    }
}
