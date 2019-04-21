package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@TableName("lost_find")
public class LostFind extends BaseEntity{
    @TableId(type= IdType.AUTO)
    private int id;

    private int userId;
    @NotBlank(message = "标题不能为空")
    private String title;
    /** 用户昵称 */
    private String nickName;
    /** 截图链接 */
    private String pictures;
    /** 信息 */
    @NotBlank(message = "描述不能为空")
    private String info;
    @NotBlank(message = "地点不能为空")
    private String address;
    @NotBlank(message = "联系方式不能为空")
    private String contact;

    private String cardInfo;
    @NotBlank(message = "类型不能为空")
    private String category;

    private int status;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
