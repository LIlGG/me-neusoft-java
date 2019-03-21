package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName BaseEntity
 * @Description TODO
 * @Author lixingyong
 * @Date 2018/12/11 10:28
 * @Version 1.0
 */
@Getter
@Setter
public abstract class BaseEntity<T extends Model> extends Model<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type=IdType.AUTO)
    protected int id;
    /**
     * @Author lixingyong
     * @Description //TODO 新增日期
     * @Date 2018/12/11
     **/
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    protected Date createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    protected Date updatedAt;

    @TableField(value = "deleted_at")
    protected Date deletedAt;
}
