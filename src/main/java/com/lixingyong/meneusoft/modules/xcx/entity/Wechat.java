package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Wechat
 * @Description TODO 用户微信相关的信息
 * @Author lixingyong
 * @Date 2018/12/11 16:13
 * @Version 1.0
 */
@Data
@TableName("wechat")
public class Wechat extends BaseEntity {
    /** 微信认证id */
    @TableId
    private int id;
    /** 微信用户唯一标识 */
    private String openid;
    /** 会话密匙 */
    private String sessionKey;
    /** 用户昵称 */
    private String nickName;
    /** 用户头像url */
    private String avatarUrl;
    /** 地址 */
    private String gender;
    /** 对应的用户id，外键 */
    private int userId;
    @Override
    protected Serializable pkVal() {
        return  super.id;
    }
}
