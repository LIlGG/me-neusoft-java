package com.lixingyong.meneusoft.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName WSInterceptor
 * @Description TODO 一个自定义的拦截器
 * @Author lixingyong
 * @Date 2018/11/2 13:41
 * @Version 1.0
 */
@Component
public class WSInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("访问的URL：\t\t"+request.getRequestURL());
        return true;
    }
}
