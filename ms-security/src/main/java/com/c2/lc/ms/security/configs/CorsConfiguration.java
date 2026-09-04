package com.c2.lc.ms.security.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration {

    @Value("${cors.url.list}")
    String CORS_ORIGINS;

    @Bean
    public WebMvcConfigurer configurer(){
        return new WebMvcConfigurer(){
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/*")
                        //.allowedOrigins("http://localhost:3000", "https://lcpwaliveconnectblob.z29.web.core.windows.net/");
                        .allowedOrigins(CORS_ORIGINS.split(","));
            }
        };
    }
}
