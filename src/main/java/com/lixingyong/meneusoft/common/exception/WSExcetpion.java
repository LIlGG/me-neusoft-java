package com.lixingyong.meneusoft.common.exception;

/**
 * @ClassName WSException
 * @Description TODO 自定义的异常类
 * @Author lixingyong
 * @Date 2018/11/2 11:56
 * @Version 1.0
 * @Super RuntimeException
 */

public class WSExcetpion extends RuntimeException  {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public WSExcetpion(String msg) {
        super(msg);
        this.msg = msg;
    }

    public WSExcetpion(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public WSExcetpion(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public WSExcetpion(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
