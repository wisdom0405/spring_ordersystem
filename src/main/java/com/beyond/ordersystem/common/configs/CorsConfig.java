package com.beyond.ordersystem.common.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){ // 허용 정책
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:8081") // 허용 url 명시
                .allowedMethods("*") // get요청, post요청 등
                .allowedHeaders("*")
                .allowCredentials(true); // 보안처리 할 것인지
    }
}
