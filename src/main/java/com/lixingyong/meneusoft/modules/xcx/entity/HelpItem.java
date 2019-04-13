package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("help_item")
public class HelpItem extends BaseEntity{
    @TableId(type= IdType.AUTO)
    private int id;
    /** 帮助标题 */
    private String title;
    /** 帮助具体内容 */
    private String content;
    /** 优先度 */
    private int sort;
    /** 帮助信息发布人 */
    private String userName;
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
