package com.lixingyong.meneusoft.modules.xcx.utils;

import com.lixingyong.meneusoft.api.bind.Type;

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
    public static boolean accountStatus(String account, String pw, String vCode, Type type){
        // 执行登录程序
        switch (type){
            case JWC:
                // 执行教务处登录程序
                System.out.println(account+ pw+ vCode);
                break;
            case Bind:
                break;
            case LIBRARY:
                break;
        }
        return false;
    }
}
