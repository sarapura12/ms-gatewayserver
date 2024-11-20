package com.ms.gatewayserver.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Configuration
public class HttpMessageConvertersConfig {

    @Bean
    public HttpMessageConverters customConverters() {
        HttpMessageConverter<?> additionalConverter = new MappingJackson2HttpMessageConverter();
        return new HttpMessageConverters(additionalConverter);
    }
}