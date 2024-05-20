package com.itteam.estatesapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActualAPIDomainConfig {
    @Value("${spring.application.actual-api-domain}")
    private String ACTUAL_API_DOMAIN;

    @Bean
    public String getACTUAL_API_DOMAIN(){
        return ACTUAL_API_DOMAIN;
    }
}
