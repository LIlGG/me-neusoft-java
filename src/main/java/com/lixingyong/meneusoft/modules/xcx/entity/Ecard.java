package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

@TableName("ecard")
@Data
public class Ecard extends BaseEntity{
    @TableId(type= IdType.INPUT)
    private int id;

    /** 用户id */
    private int userId;

    /** 交易时间 */
    private String transTime;

    /** 交易地址 */
    private String addr;

    /** 交易金额 */
    private float money;

    /** 余额 */
    private float balance;
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
