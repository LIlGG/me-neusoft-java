package com.lixingyong.meneusoft.modules.sys.controller;

import com.lixingyong.meneusoft.common.utils.Params;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.common.utils.WxUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName WxController
 * @Description TODO 微信操作相关控制类
 * @Author lixingyong
 * @Date 2018/11/8 16:59
 * @Version 1.0
 */
@RestController
@RequestMapping("/wx")
public class WxController {

    /**
     * @Author lixingyong
     * @Description //TODO 获取A类微信小程序码
     * @Date 2018/11/8
     * @Param []
     * @return com.lixingyong.meneusoft.common.utils.R
     **/
    @RequestMapping(value = "/getWXACode",method = RequestMethod.GET)
    public R getWXACode(){

        System.out.println(WxUtil.getWXACodeUnlimit(new Params("1")));

        return null;
    }
}
