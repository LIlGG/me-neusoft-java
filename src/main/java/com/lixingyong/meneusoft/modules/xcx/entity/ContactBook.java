package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Data
@TableName("contact_book")
public class ContactBook extends BaseEntity {
    @TableId(type= IdType.ID_WORKER_STR)
    private String title;
    @NotBlank(message = "email不能为空")
    private String email;

    private String phone;

    private String comment;

    private int contactCategoryId;
    @Override
    protected Serializable pkVal() {
        return null;
    }
}
