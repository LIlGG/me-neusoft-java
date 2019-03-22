package com.lixingyong.meneusoft.modules.xcx.entity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName WxUser
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/11/2 11:17
 * @Version 1.0
 */
@TableName("wx_user")
@Data
public class WxUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    @TableId
    private Long userId;

    /** 用户昵称 */
    private String nickName;

    /** 创建时间 */
    private Date createTime;

    /** 微信号注册所在城市 */
    private String city;

    /** 微信号注册所在省份 */
    private String province;

    /** 微信号注册所在国家 */
    private String country;

    /** 用户头像URL链接 */
    private String avatarUrl;

    /** 用户openid */
    private String openid;

    /** 会话密匙 */
    private String sessionKey;

    /** 用户unionid */
    private String unionId;

    /** 会话密匙过期时间 */
    private Date keyExpireTime;
}
