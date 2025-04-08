package com.calculadora.renda.fixa.di.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DiConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
