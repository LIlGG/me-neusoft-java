package com.lixingyong.meneusoft.common.utils;


import org.apache.http.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName R
 * @Description TODO 用于封装返回的数据
 * @Author lixingyong
 * @Date 2018/11/2 11:41
 * @Version 1.0
 */
public class R extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 1L;

    public R(){
        put("code", 0);
        put("msg" , "获取成功");
    }

    public static R error(){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "系统错误");
    }

    public static R error(String msg){
        return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public static R error(int code, String msg){
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(String msg){
        R r= new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok(Object value){
        R r = new R();
        r.put("data",value);
        return r;
    }

    public static R ok() {
        return new R();
    }

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
