package com.lixingyong.meneusoft.modules.xcx.entity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

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

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public Date getKeyExpireTime() {
        return keyExpireTime;
    }

    public void setKeyExpireTime(Date keyExpireTime) {
        this.keyExpireTime = keyExpireTime;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
