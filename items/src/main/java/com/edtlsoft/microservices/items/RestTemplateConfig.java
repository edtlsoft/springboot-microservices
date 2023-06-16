package com.edtlsoft.microservices.items;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean("clientRest")
    @LoadBalanced
    public RestTemplate clientRestTemplate() {
        return new RestTemplate();
    }
}
