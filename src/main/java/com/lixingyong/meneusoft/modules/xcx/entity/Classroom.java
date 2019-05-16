package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("classroom")
public class Classroom extends BaseEntity {
    private int campusId;

    private String campusName;

    private int tbId;

    private String tbName;
    @TableId(type= IdType.ID_WORKER_STR)
    private String classroomId;

    private String classroomName;

    @Override
    protected Serializable pkVal() {
        return null;
    }
}
