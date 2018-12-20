package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserLibrary
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/20 17:48
 * @Version 1.0
 */
@Data
@TableName("user_library")
public class UserLibrary extends BaseEntity {
    /** id */
    @TableId
    private int id;

    /** 用户id */
    private int userId;

    /** 图书馆登录学号 */
    private String studentId;

    /** 图书馆登录密码 */
    private String password;

    /** 图书馆登录状态 */
    private int verify;
    @Override
    protected Serializable pkVal() {
        return super.id;
    }
}
