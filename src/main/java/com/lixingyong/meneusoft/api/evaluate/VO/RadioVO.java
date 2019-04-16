package com.lixingyong.meneusoft.api.evaluate.VO;

import lombok.Data;

@Data
public class RadioVO {
    // 问题名称
    private String name;
    // 问题选项
    private String value;
    // 问题是否选中
    private boolean checked;
}
