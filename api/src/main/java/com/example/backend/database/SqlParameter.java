package com.example.backend.database;

import org.jdbi.v3.core.mapper.RowMapper;

public class SqlParameter {
    private String name = null;
    private String outName = null;
    private Object value = null;
    private Object outValue = null;
    private Object mapper = null;
    private Object outItemType = null;
    private ParameterDirection direction = ParameterDirection.INPUT;
    /**
     * java.sql.Types
     */
    private int sqlType = java.sql.Types.VARCHAR;

    public SqlParameter() {}

    /**
     * SQL parameter with ParameterDirection.INPUT
     * @param name: name of parameter
     * @param value: value of parameter
     * @param sqlType: java.sql.Types
     */
    public SqlParameter(String name, Object value, int sqlType) {
        this(name, value, ParameterDirection.INPUT, sqlType, null, null, null);
    }

    /**
     * SQL parameter with ParameterDirection.OUTPUT
     * @param name: name of parameter
     * @param sqlType: java.sql.Types
     */
    public SqlParameter(String name, int sqlType) {
        this(name, null, ParameterDirection.OUTPUT, sqlType, null, null, null);
    }

    /**
     * SQL parameter with ParameterDirection.OUTPUT
     * @param name: name of parameter
     * @param sqlType: java.sql.Types
     * @param mapper: object mapper
     */
    public <T> SqlParameter(String name, int sqlType, RowMapper<T> mapper) {
        this(name, null, ParameterDirection.OUTPUT, sqlType, null, mapper, null);
    }

    /**
     * SQL parameter with ParameterDirection.OUTPUT
     * @param name: name of parameter
     * @param sqlType: java.sql.Types
     * @param outItemType: type of item output
     */
    public <T> SqlParameter(String name, int sqlType, Class<T> outItemType) {
        this(name, null, ParameterDirection.OUTPUT, sqlType, null, null, outItemType);
    }

    /**
     * SQL parameter with ParameterDirection.OUTPUT
     * @param name: name of parameter
     * @param sqlType: java.sql.Types
     * @param outName: result name in dataset (multiple cursors)
     */
    public SqlParameter(String name, int sqlType, String outName) {
        this(name, null, ParameterDirection.OUTPUT, sqlType, outName);
    }

    /**
     * SQL parameter with ParameterDirection.OUTPUT
     * @param name: name of parameter
     * @param sqlType: java.sql.Types
     * @param outName: result name in dataset (multiple cursors)
     * @param mapper: object mapper
     */
    public <T> SqlParameter(String name, int sqlType, String outName, RowMapper<T> mapper) {
        this(name, null, ParameterDirection.OUTPUT, sqlType, outName, mapper, null);
    }

    /**
     * SQL parameter with ParameterDirection.OUTPUT
     * @param name: name of parameter
     * @param sqlType: java.sql.Types
     * @param outName: result name in dataset (multiple cursors)
     * @param outItemType: type of item output
     */
    public <T> SqlParameter(String name, int sqlType, String outName, Class<T> outItemType) {
        this(name, null, ParameterDirection.OUTPUT, sqlType, outName, null, outItemType);
    }

    /**
     * SQL parameter
     * @param name: name of parameter
     * @param value: value of parameter
     * @param direction: direction of parameter
     * @param sqlType: java.sql.Types
     */
    public SqlParameter(String name, Object value, ParameterDirection direction, int sqlType) {
        this(name, value, direction, sqlType, null, null, null);
    }

    /**
     * SQL parameter
     * @param name: name of parameter
     * @param value: value of parameter
     * @param direction: direction of parameter
     * @param sqlType: java.sql.Types
     * @param mapper: object mapper
     */
    public <T> SqlParameter(String name, Object value, ParameterDirection direction, int sqlType, RowMapper<T> mapper) {
        this(name, value, direction, sqlType, null, mapper, null);
    }

    /**
     * SQL parameter
     * @param name: name of parameter
     * @param value: value of parameter
     * @param direction: direction of parameter
     * @param sqlType: java.sql.Types
     * @param outItemType: type of item output
     */
    public <T> SqlParameter(String name, Object value, ParameterDirection direction, int sqlType, Class<T> outItemType) {
        this(name, value, direction, sqlType, null, null, outItemType);
    }

    /**
     * SQL parameter
     * @param name: name of parameter
     * @param value: value of parameter
     * @param direction: direction of parameter
     * @param sqlType: java.sql.Types
     * @param outName: result name in dataset (multiple cursors)
     */
    public SqlParameter(String name, Object value, ParameterDirection direction, int sqlType, String outName) {
        this(name, value, direction, sqlType, outName, null, null);
    }

    /**
     * SQL parameter
     * @param name: name of parameter
     * @param value: value of parameter
     * @param direction: direction of parameter
     * @param sqlType: java.sql.Types
     * @param outName: result name in dataset (multiple cursors)
     * @param mapper: object mapper
     */
    public <T> SqlParameter(String name, Object value, ParameterDirection direction, int sqlType, String outName, RowMapper<T> mapper) {
        this(name, value, direction, sqlType, outName, mapper, null);
    }

    /**
     * SQL parameter
     * @param name: name of parameter
     * @param value: value of parameter
     * @param direction: direction of parameter
     * @param sqlType: java.sql.Types
     * @param outName: result name in dataset (multiple cursors)
     * @param outItemType: type of item output
     */
    public <T> SqlParameter(String name, Object value, ParameterDirection direction, int sqlType, String outName, Class<T> outItemType) {
        this(name, value, direction, sqlType, outName, null, outItemType);
    }

    private <T> SqlParameter(String name, Object value, ParameterDirection direction, int sqlType, String outName, RowMapper<T> mapper, Class<T> outItemType) {
        this.setName(name);
        this.setValue(value);
        this.setDirection(direction);
        this.setSqlType(sqlType);
        this.setOutName(outName);
        this.setMapper(mapper);
        this.setOutItemType(outItemType);
    }

    public Object getValue() { return value; }
    public void setValue(Object value) { this.value = value; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ParameterDirection getDirection() { return direction; }
    public void setDirection(ParameterDirection direction) { this.direction = direction; }

    /** @return java.sql.Types value */
    public int getSqlType() { return sqlType; }
    /** @param sqlType java.sql.Types */
    public void setSqlType(int sqlType) { this.sqlType = sqlType; }

    public String getOutName() { return outName; }
    public void setOutName(String outName) { this.outName = outName; }
    public Object getOutValue() { return outValue; }
    public void setOutValue(Object outValue) { this.outValue = outValue; }

    @SuppressWarnings("unchecked")
    public <T> RowMapper<T> getMapper() { return (RowMapper<T>) mapper; }
    public <T> void setMapper(RowMapper<T> mapper) { this.mapper = mapper; }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getOutItemType() { return (Class<T>) outItemType; }
    public <T> void setOutItemType(Class<T> outItemType) { this.outItemType = outItemType; }

    public enum ParameterDirection {
        INPUT,
        OUTPUT
    }
}
