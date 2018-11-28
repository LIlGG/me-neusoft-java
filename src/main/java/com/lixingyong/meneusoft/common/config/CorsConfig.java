package com.lixingyong.meneusoft.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName CorsConfig
 * @Description TODO Cors配置器
 * @Author lixingyong
 * @Date 2018/11/2 14:55
 * @Version 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    /**
     * @Author lixingyong
     * @Description //TODO 跨域请求配置
     * @Date 2018/11/2
     * @Param [registry]
     * @return void
     **/
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .maxAge(3600);
    }
}
