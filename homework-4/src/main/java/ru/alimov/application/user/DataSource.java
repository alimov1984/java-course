package ru.alimov.application.user;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class DataSource {
    private static HikariConfig dsConfig;
    private static HikariDataSource ds;

    static {
        dsConfig = new HikariConfig();
        dsConfig.setJdbcUrl("jdbc:postgresql://localhost:5432/db_user_store");
        dsConfig.setUsername("test_user");
        dsConfig.setPassword("test_pas");
        ds = new HikariDataSource(dsConfig);
    }

    private DataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

}
