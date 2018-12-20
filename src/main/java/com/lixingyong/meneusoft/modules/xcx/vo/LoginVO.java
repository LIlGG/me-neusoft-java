package com.lixingyong.meneusoft.modules.xcx.vo;

import lombok.Data;

/**
 * @ClassName LoginVO
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/20 18:36
 * @Version 1.0
 */
@Data
public class LoginVO {
    private int jwc_verify;

    private int library_verify;

    private String token;

    private int user_type;

    private int verify;
}
