package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/20 16:46
 * @Version 1.0
 */
@Data
@TableName("user")
public class User extends BaseEntity {
    /** 用户id */
    @TableId
    private int id;

    /** 学号 */
    private String studentId;

    /** 教学密码 */
    private String password;

    /** 教务处账号 */
    private String jwcStudentId;

    /** 教务处密码 */
    private String jwcPassword;

    /** 教务处验证状态
     *  0：无法登录 1：正常
     */
    private int jwcVerify;

    /** 统一认证状态
     * 0：无法登录 1：正常
     */
    private int  verify;

    @TableField(exist = false)
    private Wechat wechat;

    @TableField(exist = false)
    private UserLibrary userLibrary;
    @Override
    protected Serializable pkVal() {
        return this.getId();
    }
}
