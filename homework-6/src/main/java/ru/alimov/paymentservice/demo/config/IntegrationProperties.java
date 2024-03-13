package ru.alimov.paymentservice.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "integration")
public class IntegrationProperties {
    private final ProductServiceProperties productServiceProperties;

    @ConstructorBinding
    public IntegrationProperties(ProductServiceProperties productServiceProperties) {
        this.productServiceProperties = productServiceProperties;
    }

    public record ProductServiceProperties(String url) {
    }

    public ProductServiceProperties getProductServiceProperties() {
        return productServiceProperties;
    }

}
