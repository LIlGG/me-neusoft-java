package com.lixingyong.meneusoft.common.exception;

import com.lixingyong.meneusoft.common.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.dao.DuplicateKeyException;

import java.io.IOException;

/**
 * @ClassName RRExceptionHandler
 * @Description TODO 全局异常处理器
 * @Author lixingyong
 * @Date 2018/11/2 11:52
 * @Version 1.0
 */
@RestControllerAdvice
public class WSExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = WSExcetpion.class)
    public R handleWSException(WSExcetpion e){
        R r = new R();
        r.put("code", e.getCode());
        r.put("msg", e.getMessage());
        logger.error(e.getMessage(), e);
        return r;
    }

    @ExceptionHandler(value = NullPointerException.class)
    public R handleNullPointerException(Exception e){
        logger.error(e.getMessage(), e);
        return R.error("参数不存在");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R handlerNoFoundException(Exception e) {
//        logger.error(e.getMessage(), e);
        return R.error(404, "无效访问");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e){
        logger.error(e.getMessage(), e);
        return R.error("数据已存在");
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        logger.error(e.getMessage(), e);
        return R.error();
    }

    @ExceptionHandler(IOException.class)
    public R handleIOException(IOException e){
        logger.error(e.getMessage(), e);
        return R.error("错误");
    }
}
