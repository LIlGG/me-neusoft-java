package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName DetailController
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/12 15:13
 * @Version 1.0
 */
@RestController
@Api("资讯")
public class DetailController {

    @ApiOperation("资讯列表")
    @GetMapping(value = "/details")
    public R getBanner(){
        return null;
    }

    @ApiOperation("获取所有的资讯标签")
    @GetMapping(value = "/detail/tags")
    public R getDetailTags(){
        return null;
    }

    @ApiOperation("获取一篇资讯")
    @GetMapping(value = "/detail/{id}")
    public R getDetail(@PathVariable int id){
        return null;
    }
}
