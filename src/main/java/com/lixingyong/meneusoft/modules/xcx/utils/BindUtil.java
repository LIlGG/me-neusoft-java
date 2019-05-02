package com.lixingyong.meneusoft.modules.xcx.utils;

import com.lixingyong.meneusoft.api.bind.Type;
import com.lixingyong.meneusoft.api.evaluate.EvaluateUtil;
import com.lixingyong.meneusoft.api.jwc.JWCUtil;
import com.lixingyong.meneusoft.api.library.LibraryUtil;

/**
 * @ClassName com.lixingyong.meneusoft.modules.xcx.utils
 * @Description TODO 绑定工具类
 * @Author mail@lixingyong.com
 * @Date 2019-02-27 08:51
 */
public class BindUtil {

    /**
     * @Author lixingyong
     * @Description //TODO 判断账号状态以确定是否可以登录
     * @Date 2019/2/27
     * @Param [account, pw, vCode]
     * @return boolean
     **/
    public static boolean accountStatus(String user_id, String account, String pw, String vCode, Type type){
        // 执行登录程序
        // 判断VPN是否已登录
        if(!LoginUtil.exeVpnLogin()){
            return false;
        }
        switch (type){
            case JWC:
                // 执行教务处登录程序
                JWCUtil.jwcStudentLogin(Long.parseLong(user_id), account, pw, vCode);
                return true;
            case Bind:
                // 执行登录统一身份认证程序(使用课程评价系统测试)
                if(EvaluateUtil.ufsLogin(Long.parseLong(user_id), account, pw)){
                    return true;
                }
            case LIBRARY:
                LibraryUtil.libraryLogin(Long.parseLong(user_id),account,pw);
                return true;
        }
        return false;
    }
}
