package com.lixingyong.meneusoft.modules.xcx.vo;

import lombok.Data;

import java.util.List;

@Data
public class RoomVO {
    private String roomName;

    private List<ClassUse> classUse;
}
