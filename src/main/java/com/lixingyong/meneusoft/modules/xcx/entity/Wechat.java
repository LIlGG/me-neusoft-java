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
    @Override
    protected Serializable pkVal() {
        return  super.id;
    }
}
