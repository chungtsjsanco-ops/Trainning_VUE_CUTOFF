package com.example.backend.database;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MultiDataSourceConfig {

    /**
     * Datasource chính: Spring sẽ bind từ `spring.datasource.*` trong application.yml
     * Đặt tên bean để ConnectionManager inject rõ ràng khi có nhiều DataSource.
     */
    @Bean(name = "defaultDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Datasource phụ: bind từ `app.datasources.second.*` trong application.yml
     */
    @Bean(name = "secondDataSource")
    @ConfigurationProperties(prefix = "app.datasources.second")
    public DataSource secondDataSource() {
        return DataSourceBuilder.create().build();
    }
}

