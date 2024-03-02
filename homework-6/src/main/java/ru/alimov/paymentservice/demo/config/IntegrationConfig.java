package ru.alimov.paymentservice.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "integration")
public class IntegrationConfig {
    private final RestClientProperties productServiceProperties;

    @ConstructorBinding
    public IntegrationConfig(RestClientProperties productService) {
        this.productServiceProperties = productService;
    }

    public RestClientProperties getProductServiceProperties() {
        return productServiceProperties;
    }

}
