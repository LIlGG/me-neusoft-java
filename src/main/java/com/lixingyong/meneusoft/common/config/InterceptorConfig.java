package com.lixingyong.meneusoft.common.config;

import com.lixingyong.meneusoft.common.interceptor.WSInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName InterceptorConfig
 * @Description TODO 拦截器配置器
 * @Description SpringBoot 2.0版本之后，需要实现WebMvcConfigurer来注册拦截器
 * @Author lixingyong
 * @Date 2018/11/2 13:47
 * @Version 1.0
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    WSInterceptor wsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(wsInterceptor).addPathPatterns("/*");
    }
}
