package com.itteam.estatesapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActualUIDomainConfig {
    @Value("${spring.application.actual-ui-domain}")
    private String ACTUAL_DOMAIN;

    @Bean
    public String getACTUAL_UI_DOMAIN(){
        return ACTUAL_DOMAIN;
    }
}
