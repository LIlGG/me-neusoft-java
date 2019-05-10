package com.lixingyong.meneusoft.common.exception;

import com.lixingyong.meneusoft.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.bootstrap.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.dao.DuplicateKeyException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * @ClassName RRExceptionHandler
 * @Description TODO 全局异常处理器
 * @Author lixingyong
 * @Date 2018/11/2 11:52
 * @Version 1.0
 */
@Slf4j
@RestControllerAdvice
public class WSExceptionHandler {

    @ExceptionHandler(value = WSExcetpion.class)
    public R handleWSException(WSExcetpion e){
        log.error(e.getMessage(), e);
        return R.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(value = NullPointerException.class)
    public R handleNullPointerException(Exception e){
        log.error(e.getMessage(), e);
        return R.error("参数不存在");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public R handlerNoFoundException(Exception e) {
//        logger.error(e.getMessage(), e);
        return R.error(404, "无效访问");
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public R handleDuplicateKeyException(DuplicateKeyException e){
        log.error(e.getMessage(), e);
        return R.error("数据已存在");
    }

    @ExceptionHandler(Exception.class)
    public R handleException(Exception e){
        log.error(e.getMessage(), e);
        return R.error();
    }

    @ExceptionHandler(IOException.class)
    public R handleIOException(IOException e){
        log.error(e.getMessage(), e);
        return R.error("流错误");
    }

    @ExceptionHandler(BindException.class)
    public R handleBException(BindException e){
        List<ObjectError> errors = e.getAllErrors();
        log.error(errors.get(0).getDefaultMessage(), e);
        return R.error(errors.get(0).getDefaultMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public R handleCVException(ConstraintViolationException e){
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        log.error(constraintViolations.iterator().next().getMessage(), e);
        return R.error(constraintViolations.iterator().next().getMessage());
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public R handlerHSEException(HttpServerErrorException e){
        log.error(e.getMessage(), e);
        return R.error("教学系统出现异常，请联系管理员！");
    }
}
