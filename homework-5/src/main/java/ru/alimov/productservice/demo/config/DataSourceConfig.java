package ru.alimov.productservice.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:datasource-config.properties")
public class DataSourceConfig {
    @Value("${data-source.db-url}")
    private String db_url;

    @Value("${data-source.db-username}")
    private String db_username;

    @Value("${data-source.db-password}")
    private String db_password;

    public String getDb_url() {
        return db_url;
    }

    public String getDb_username() {
        return db_username;
    }

    public String getDb_password() {
        return db_password;
    }
}
