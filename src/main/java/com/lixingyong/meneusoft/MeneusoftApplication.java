package com.lixingyong.meneusoft;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

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

}
