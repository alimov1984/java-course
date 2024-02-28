package ru.alimov.productservice.demo.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import ru.alimov.productservice.demo.config.DataSourceConfig;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;

@Component
@DependsOn("dataSourceConfig")
public class DataSource {
    private DataSourceConfig dataSourceConfig;
    private HikariConfig dsConfig;
    private HikariDataSource ds;

    public DataSource(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
        this.dsConfig = new HikariConfig();
    }

    @PostConstruct
    public void initPoolConfiguration() {
        dsConfig.setJdbcUrl(dataSourceConfig.getDbUrl());
        dsConfig.setUsername(dataSourceConfig.getDbUsername());
        dsConfig.setPassword(dataSourceConfig.getDbPassword());
        ds = new HikariDataSource(dsConfig);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }


}
