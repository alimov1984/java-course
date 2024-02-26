package ru.alimov.productservice.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:datasource-config.properties")
public class DataSourceConfig {
    @Value("${data-source.db-url}")
    private String dbUrl;

    @Value("${data-source.db-username}")
    private String dbUsername;

    @Value("${data-source.db-password}")
    private String dbPassword;

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
