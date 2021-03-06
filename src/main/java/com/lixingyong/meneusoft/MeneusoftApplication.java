package com.lixingyong.meneusoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;
@EnableScheduling
@SpringBootApplication
public class MeneusoftApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeneusoftApplication.class, args);
    }

    /**
     * @Author lixingyong
     * @Description //TODO 配置RestTemplate以进行网络请求
     * @Date 2018/11/2
     * @Param []
     * @return org.springframework.web.client.RestTemplate
     **/
    @Bean
    public RestTemplate getRestTemplate(){
        RestTemplate restTemplate = null;
        try {
            restTemplate = new RestTemplate();
        }catch (Throwable e){

        }
        return restTemplate;
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }

}
