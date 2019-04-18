package com.lixingyong.meneusoft.modules.xcx.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("lecture")
public class Lecture extends BaseEntity {
    @TableId(type= IdType.INPUT)
    private long id;
    // 讲座标题
    private String title;
    // 讲座开始日期日期
    private Date startTime;
    // 讲座详细开始日期
    private String time;
    // 讲座地点
    private String address;
    // 报告人
    private String reporter;
    // 举办学院
    private String college;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
