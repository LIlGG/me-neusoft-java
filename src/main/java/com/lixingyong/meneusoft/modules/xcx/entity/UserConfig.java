package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName UserConfig
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/20 18:00
 * @Version 1.0
 */
@Data
@TableName("user_config")
public class UserConfig extends BaseEntity {
    /** 用户设置主键id */
    @TableId
    private int id;

    /** 用户id */
    private int userId;

    /** 通知设置 */
    private String notify;

    /** 用户身份类型
     *  0：本科 1：专科
     * */
    private int userType;

    @Override
    protected Serializable pkVal() {
        return super.id;
    }
}
