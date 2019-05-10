package com.lixingyong.meneusoft.common.interceptor;

import com.lixingyong.meneusoft.common.exception.WSExcetpion;
import com.lixingyong.meneusoft.common.utils.JwtUtils;
import com.lixingyong.meneusoft.modules.xcx.annotation.Token;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName AuthorizationInterceptor
 * @Description TODO 验证用户登录信息的拦截器
 * @Author lixingyong
 * @Date 2018/11/2 16:20
 * @Version 1.0
 */
@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtUtils jwtUtils;

    public static final String USER_KEY = "user_id";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Token annotation;
        if(handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(Token.class);
        }else{
            return true;
        }

        if(annotation == null){
            return true;
        }

        //获取用户凭证
        String token = request.getHeader(jwtUtils.getHeader());
        if(StringUtils.isBlank(token)){
            token = request.getParameter(jwtUtils.getHeader());
        }

        //凭证为空
        if(StringUtils.isBlank(token)){
            throw new WSExcetpion("验证信息不存在", HttpStatus.UNAUTHORIZED.value());
        }

        //凭证不是Bearer开头的
        if(!StringUtils.startsWith(token,"Bearer")){
            throw new WSExcetpion("验证信息不正确，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }

        token = StringUtils.substring(token,token.indexOf(" ")+1);
        Claims claims = jwtUtils.getClaimByToken(token);
        if(claims == null || jwtUtils.isTokenExpired(claims.getExpiration())){
            throw new WSExcetpion("验证信息失效，请重新登录", HttpStatus.UNAUTHORIZED.value());
        }
        //设置userId到request里，后续根据userId，获取用户信息
        request.setAttribute(USER_KEY, Long.parseLong(claims.get("user_id").toString()));
        return true;
    }
}
