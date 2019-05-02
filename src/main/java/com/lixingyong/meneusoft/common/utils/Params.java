package com.lixingyong.meneusoft.common.utils;

import com.lixingyong.meneusoft.common.exception.WSExcetpion;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Params
 * @Description TODO 参数工具类
 * @Author lixingyong
 * @Date 2018/11/9 15:06
 * @Version 1.0
 */
public final class Params extends HashMap<String, Object> implements Serializable {
    /** 二维码跳转默认页面 */
    private final String PAGE = "pages/index";
    /** 二维码的宽度，默认为 430px，最小 280px，最大 1280px */
    private final int WIDTH = 430;
    /** 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false */
    private final boolean AUTO_COLOR = false;
    /** auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示，默认全 0 */
    private final Map<String,String> LINE_COLOR = new HashMap<String,String>(){{
        this.put("r","0");
        this.put("g","0");
        this.put("b","0");
    }};
    /** 是否需要透明底色，为 true 时，生成透明底色的小程序码，默认 false */
    private final boolean IS_HYALINE = false;

    public Params(String scene){
        put("scene",scene);
        put("page",PAGE);
        put("width",WIDTH);
        put("auto_color",AUTO_COLOR);
        put("line_color",LINE_COLOR);
        put("is_hyaline",IS_HYALINE);
    }

    public Params(String scene,String page){
        this(scene);
        put("page",page);
    }

    public Params(String scene,int width){
        this(scene);
        if(width > 280 && width < 1280){
            put("width",width);
        }else{
            throw new WSExcetpion("请查看二维码长度范围");
        }
    }

    public Params(String scene,String page,int width){
        this(scene,width);
        put("page",page);
    }

    public Params(String scene,String page,int width,boolean autoColor){
        this(scene,page,width);
        put("auto_color",autoColor);
    }

    public Params(String scene,String page,int width,boolean autoColor,Map lineColor){
        this(scene,page,width,autoColor);
        put("line_color",lineColor);
    }

    public Params(String scene,String page,int width,boolean autoColor,Map lineColor,boolean isHyaline){
        this(scene,page,width,autoColor,lineColor);
        put("is_hyaline",isHyaline);
    }
}
