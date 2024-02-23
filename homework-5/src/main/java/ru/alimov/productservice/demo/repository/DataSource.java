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
        dsConfig.setJdbcUrl(dataSourceConfig.getDb_url());
        dsConfig.setUsername(dataSourceConfig.getDb_username());
        dsConfig.setPassword(dataSourceConfig.getDb_password());
        ds = new HikariDataSource(dsConfig);
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }


}
