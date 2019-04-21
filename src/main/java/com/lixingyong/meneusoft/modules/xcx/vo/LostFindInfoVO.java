package com.lixingyong.meneusoft.modules.xcx.vo;

import com.lixingyong.meneusoft.modules.xcx.entity.LostFind;
import lombok.Data;

@Data
public class LostFindInfoVO{
    private LostFind data;

    private int isMe = 0;

    private int isOwner = 0;
}
