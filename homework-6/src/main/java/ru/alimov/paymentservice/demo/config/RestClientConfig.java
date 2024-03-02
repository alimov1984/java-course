package ru.alimov.paymentservice.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    RestClient getInstanceRestClient()
    {
        RestClient restClient = RestClient.create();
        return restClient;
    }
}
