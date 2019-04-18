package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.jwc.JWCUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.utils.LoginUtil;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.controller
 * @Description TODO 校园网中转控制器
 * @Author mail@lixingyong.com
 * @Date 2019-03-05 20:32
 */

@Api("其余配置")
@RestController
public class NeusoftController {
    /**
     * @Author lixingyong
     * @Description //TODO 获取验证码
     * @Date 2018/12/18
     * @Param []
     * @return void
     **/
    @Token
    @GetMapping(value = "/getValidateCode/{type}")
    public R getValidateCode(@PathVariable String type, @LoginUser String user_id){
        String code = null;
        // 判断当前类型
        try{
            switch (type){
                case "jwc":
                    LoginUtil.exeJWCLogin(Long.parseLong(user_id));
                    code = JWCUtil.getValidateCode(Long.parseLong(user_id));
                    break;
            }
        }catch (WSExcetpion w){
            return R.error(w.getMsg());
        }
        if(null != code){
            return R.ok().put("url",code);
        }
        return R.error("未知错误");
    }
}
