package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("feedback")
public class Feedback extends BaseEntity{
    /** 用户ID 外键 */
    private int userId;
    /** issues ID */
    @TableId(type= IdType.INPUT)
    private int number;
    /** 反馈标题 */
    private String title;
    /** 反馈状态 */
    private String stat;
    /** 反馈标签 */
    private String tags;
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
