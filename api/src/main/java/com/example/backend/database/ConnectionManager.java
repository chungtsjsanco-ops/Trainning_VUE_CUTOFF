package com.example.backend.database;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ConnectionManager {

    public enum Connections {
        DEFAULT,
        SECOND
    }

    /**
     * DEFAULT = datasource chính (bind từ spring.datasource.*)
     * SECOND  = datasource phụ (bind từ app.datasources.second.*)
     */
    private final DataSource defaultDataSource;
    private final DataSource secondDataSource;

    public ConnectionManager(
            @Qualifier("defaultDataSource") DataSource defaultDataSource,
            @Qualifier("secondDataSource") DataSource secondDataSource) {
        this.defaultDataSource = defaultDataSource;
        this.secondDataSource = secondDataSource;
    }

    public DataSource getDataSource() {
        return defaultDataSource;
    }

    public DataSource getDataSource(Connections connection) {
        if (connection == null) return defaultDataSource;
        switch (connection) {
            case SECOND:
                return secondDataSource;
            case DEFAULT:
            default:
                return defaultDataSource;
        }
    }
}
