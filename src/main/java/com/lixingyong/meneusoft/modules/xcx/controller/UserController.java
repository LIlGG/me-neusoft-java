package com.lixingyong.meneusoft.modules.xcx.controller;

import com.lixingyong.meneusoft.api.jwc.JWCUtil;
import com.lixingyong.meneusoft.api.wx.WxUtil;
import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.interceptor.AuthorizationInterceptor;
import com.lixingyong.meneusoft.common.utils.JwtUtils;
import com.lixingyong.meneusoft.common.utils.R;
import com.lixingyong.meneusoft.common.validator.Assert;
import com.lixingyong.meneusoft.modules.xcx.annotation.LoginUser;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import com.lixingyong.meneusoft.modules.xcx.entity.LibraryBook;
import com.lixingyong.meneusoft.modules.xcx.entity.User;
import com.lixingyong.meneusoft.modules.xcx.entity.UserConfig;
import com.lixingyong.meneusoft.modules.xcx.entity.Wechat;
import com.lixingyong.meneusoft.modules.xcx.service.*;
import com.lixingyong.meneusoft.modules.xcx.vo.LoginVO;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import lombok.extern.java.Log;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserController
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/12 15:25
 * @Version 1.0
 */
@Api("用户")
@RestController
@Validated
public class UserController {
    @Autowired
    private WechatService wechatService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserLibraryService userLibraryService;
    @Autowired
    private UserConfigService userConfigService;
    @Autowired
    private LibraryBookService libraryBookService;
    @Autowired
    private GradeService gradeService;
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public R login(@RequestParam String code){
        Map<String,Object> result;
        Map<String,Object> userInfo = null;
        Wechat wechat = new Wechat() ;
        int userId;
        try {
            userInfo = WxUtil.code2Session(code);
            wechat = wechatService.getWechat((String)userInfo.get("openid"));

        }catch (WSExcetpion s){
            return R.error(s.getMsg());
        }
        // openid是否已经存在，根据其获取userId
        userId = wechat != null ? wechat.getUserId() : userService.createUser();

        //存在用户或已经新建用户，则准备更新数据
        if(null == wechat){
            wechat = new Wechat();
        }
        wechat.setSessionKey((String)userInfo.get("session_key"));
        wechat.setOpenid((String)userInfo.get("openid"));
        wechat.setUserId(userId);
        wechatService.insertOrUpdate(wechat);
        LoginVO loginVO = userService.login(userId);
        return R.ok().put("data",loginVO);
    }

    @ApiOperation("绑定统一认证中心")
    @PostMapping("/bind")
    @Token
    public R bind(@NotBlank(message = "账号不能为空") @Length(min = 6, message = "账号不合法") @RequestParam("student_id") String account, @NotBlank(message = "密码不能为空") @Length(min = 3, message = "密码不合法") @RequestParam("password") String password, @LoginUser String userId){
        try {

            userService.insertOrUpdateUfsAccount(account,password, userId);
        }catch (NullPointerException e){
            return R.error("当前用户不存在或账号密码为空");
        } catch (WSExcetpion e){
            return R.error(e.getMsg());
        }
        return R.ok("绑定UFS成功");
    }

    @Token
    @ApiOperation("绑定教务处")
    @PostMapping("/jwc/bind")
    public R jwcBind(@NotBlank(message = "账号不能为空") @Length(min = 6, message = "账号不合法") @RequestParam String student_id,@NotBlank(message = "密码不能为空") @Length(min = 3, message = "密码不合法") @RequestParam String password,@RequestParam String vcode, @LoginUser String userId){
        try {
            // 更新或新增当前用户的教务处账号和密码
            userService.insertOrUpdateJwcAccount(userId,student_id, password, vcode);
            JWCUtil.stuXyjzqk(Long.parseLong(userId));
        } catch (NullPointerException e){
            return R.error("当前用户不存在或账号密码为空");
        } catch (WSExcetpion e){
            return R.error(e.getMsg());
        }
        return R.ok("绑定教务处成功");
    }

    @Token
    @ApiOperation("绑定图书馆")
    @PostMapping("/library/bind")
    public R library(@NotBlank(message = "账号不能为空") @Length(min = 6, message = "账号不合法") @RequestParam String student_id,@NotBlank(message = "密码不能为空") @Length(min = 2, message = "密码不合法") @RequestParam String password, @LoginUser String userId){
        // 更新或新增当前用户的图书馆账号和密码
        try {
            userLibraryService.insertOrUpdateLibraryAccount(userId,student_id,password);
        }catch (NullPointerException e){
            return R.error("当前用户不存在或账号密码为空");
        }catch (WSExcetpion e){
            return R.error(e.getMsg());
        }
        return R.ok("绑定图书馆成功");
    }

    @Token
    @ApiOperation("绑定一卡通")
    @PostMapping("/ecard/bind")
    public R eCardBind(@NotBlank(message = "绑定失败") @RequestParam String  result,@LoginUser String userId){
        try {
            //截取最后的id
            if(result.startsWith("http")){
                String[] ids = result.split("/");
                String id = ids[ids.length - 1];
                // 将当前id保存起来
                userService.insertOrUpdateEcardInfo(userId, id);
                return R.ok("绑定一卡通成功");
            } else {
                return R.error("二维码数据不正确");
            }
        }catch (WSExcetpion e){
            return R.error(e.getMsg());
        }
    }

    /**
     * | 二进制 | 十进制 | 说明           |
     * | 001    | 1      | 开启成绩通知   |
     * | 010    | 2      | 开启图书馆通知 |
     * | 100    | 4      | 开启考试通知   |
     */
    @ApiOperation("用户通知设置 二进制表示")
    @PostMapping("/user/config/notify")
    @Token
    public R setUserConfigNotify(@RequestParam("notify") String notify, @LoginUser String userId){
        userConfigService.setUserConfigNotify(notify, userId);
        return R.ok("设置成功");
    }

    @ApiOperation("获取用户通知设置")
    @GetMapping("/user/config/notify")
    @Token
    public R getUserConfigNotify(@LoginUser String userId){
        UserConfig userConfig =  userConfigService.getUserConfig(Integer.valueOf(userId));
        return R.ok(userConfig.getNotify());
    }

    @ApiOperation("设置用户类型")
    @PostMapping("/user/config/type")
    @Token
    public R getUserConfig(@RequestParam("user_type") String user_type,@LoginUser String userId){
        try {
            userConfigService.setUserType(Integer.valueOf(user_type), Integer.valueOf(userId));
            return R.ok("更新成功");
        }catch (Exception e){
            return R.error("更新失败");
        }
    }

    @ApiOperation("清空用户信息")
    @PostMapping("/goodbye")
    @Token
    public R delUserAllInfo(@LoginUser String userId){
        userLibraryService.delUserLibraryInfo(Integer.valueOf(userId));
        userConfigService.delUserConfigInfo(Integer.valueOf(userId));
        libraryBookService.delLibraryAll(Integer.valueOf(userId));
        gradeService.delGradeAll(Integer.valueOf(userId));
        wechatService.delWechat(Integer.valueOf(userId));
        userService.delUserInfo(Integer.valueOf(userId));
        return R.ok("已删除用户信息");
    }


    @ApiOperation("缓存微信模板消息id")
    @PostMapping("/user/msg_id")
    public R setMsgId(){
        return null;
    }
}
