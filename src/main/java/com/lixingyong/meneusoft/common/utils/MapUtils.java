package com.lixingyong.meneusoft.common.utils;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MapUtils
 * @Description TODO Map工具类
 * @Author lixingyong
 * @Date 2018/11/5 16:46
 * @Version 1.0
 */
public class MapUtils {
    
    /**
     * @Author lixingyong
     * @Description //TODO 将map中的下划线key转换为驼峰式key
     * @Date 2018/11/5
     * @Param [map]
     * @return java.util.Map
     **/
    public static Map setHumpKey(Map<String,Object> map){
        Map<String,Object> new_map = new HashMap<>();
        for(String key:map.keySet()){
            //判断是否有下划线
            if(key.contains("_")){
                new_map.put(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL,key),map.get(key));
                continue;
            }
            new_map.put(key,map.get(key));
        }
        return new_map;
    }
}
