package com.example.backend.database;

import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.Call;
import org.springframework.stereotype.Component;

@Component
public class DbContext {

    private final String MSG_SQL_INJECTION = "Tham số đầu vào không đúng định dạng.";
    private final String ITEM_PREFIX_VALUE = "items_";
    private final ConnectionManager connectionManager;
    private ConnectionManager.Connections connection = ConnectionManager.Connections.DEFAULT;

    public ConnectionManager.Connections getConnection() {
        return this.connection;
    }

    public void setConnection(ConnectionManager.Connections connection) {
        this.connection = connection;
    }

    public DbContext(ConnectionManager connectionManager) {
        super();
        this.connectionManager = connectionManager;
    }

    public javax.sql.DataSource getDataSource() {
        return getDataSource(this.connection);
    }

    private javax.sql.DataSource getDataSource(ConnectionManager.Connections connection) {
        return connectionManager.getDataSource(connection);
    }

    private boolean validateSqlInjection(String param) {
        SqlInjectionValidator sqlInjectionValidator = new SqlInjectionValidator();
        return sqlInjectionValidator.validate(param);
    }

    private boolean validateSqlInjection(SqlParameter[] params) {
        boolean result = true;
        if (params == null) return true;
        for (SqlParameter sqlParameter : params) {
            if (sqlParameter != null) {
                if (sqlParameter.getDirection() == SqlParameter.ParameterDirection.INPUT) {
                    int sqlType = sqlParameter.getSqlType();
                    if (sqlType == Types.VARCHAR || sqlType == Types.NVARCHAR || sqlType == Types.LONGNVARCHAR) {
                        Object value = sqlParameter.getValue();
                        if (value != null) {
                            result = this.validateSqlInjection(value.toString().toLowerCase().trim());
                        }
                    }
                }
                if (!result)
                    break;
            }
        }
        return result;
    }

    public int executeNonQuery(String sql) {
        return executeNonQuery(this.connection, sql);
    }

    public int executeNonQuery(ConnectionManager.Connections connection, String sql) {
        return executeNonQuery(connection, sql, (Object[]) null);
    }

    public int executeNonQuery(String sql, List<SqlParameter> args) {
        return executeNonQuery(this.connection, sql, args.toArray());
    }

    public int executeNonQuery(ConnectionManager.Connections connection, String sql, List<SqlParameter> args) {
        return executeNonQuery(connection, sql, args.toArray());
    }

    public int executeNonQuery(String sql, ArrayList<SqlParameter> args) {
        return executeNonQuery(this.connection, sql, args.toArray());
    }

    public int executeNonQuery(ConnectionManager.Connections connection, String sql, ArrayList<SqlParameter> args) {
        return executeNonQuery(connection, sql, args.toArray());
    }

    public int executeNonQuery(String sql, Object[] args) {
        return executeNonQuery(this.connection, sql, args);
    }

    public int executeNonQuery(ConnectionManager.Connections connection, String sql, Object[] args) {
        try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
            return handle.execute(sql, args);
        }
    }

    public List<Map<String, Object>> executeQueryToListMap(String sql) {
        return executeQueryToListMap(this.connection, sql);
    }

    public List<Map<String, Object>> executeQueryToListMap(ConnectionManager.Connections connection, String sql) {
        try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
            return handle.createQuery(sql).mapToMap().list();
        }
    }

    public Map<String, Object> executeQueryToMap(String sql) {
        return executeQueryToMap(this.connection, sql);
    }

    public Map<String, Object> executeQueryToMap(ConnectionManager.Connections connection, String sql) {
        try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
            return handle.createQuery(sql).mapToMap().first();
        }
    }

    public <T> List<T> executeQueryToEntities(String sql, Class<T> type) {
        return executeQueryToEntities(this.connection, sql, type);
    }

    public <T> List<T> executeQueryToEntities(ConnectionManager.Connections connection, String sql, Class<T> type) {
        try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
            return handle.createQuery(sql).mapTo(type).list();
        }
    }

    public <T> List<T> executeQueryToEntities(String sql, RowMapper<T> mapper) {
        return executeQueryToEntities(this.connection, sql, mapper);
    }

    public <T> List<T> executeQueryToEntities(ConnectionManager.Connections connection, String sql, RowMapper<T> mapper) {
        try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
            return handle.createQuery(sql).map(mapper).list();
        }
    }

    public <T> T executeQueryToEntity(String sql, Class<T> type) {
        return executeQueryToEntity(this.connection, sql, type);
    }

    public <T> T executeQueryToEntity(ConnectionManager.Connections connection, String sql, Class<T> type) {
        try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
            return handle.createQuery(sql).mapTo(type).first();
        }
    }

    public <T> T executeQueryToEntity(String sql, RowMapper<T> mapper) {
        return executeQueryToEntity(this.connection, sql, mapper);
    }

    public <T> T executeQueryToEntity(ConnectionManager.Connections connection, String sql, RowMapper<T> mapper) {
        try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
            return handle.createQuery(sql).map(mapper).first();
        }
    }

    // #region Thực thi stored procedure nhiều cursor trên Oracle với các tham số theo thứ tự

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(String storedProcedureName)
            throws AppSqlException {
        return executeSpIdxWithCursorToListMap(this.connection, storedProcedureName, (SqlParameter) null);
    }

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(String storedProcedureName,
            SqlParameter param) throws AppSqlException {
        return executeSpIdxWithCursorToListMap(this.connection, storedProcedureName, param);
    }

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName) throws AppSqlException {
        return executeSpIdxWithCursorToListMap(connection, storedProcedureName, (SqlParameter[]) null);
    }

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter param) throws AppSqlException {
        return executeSpIdxWithCursorToListMap(connection, storedProcedureName,
                param != null ? new SqlParameter[] { param } : null);
    }

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(String storedProcedureName,
            List<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorToListMap(this.connection, storedProcedureName, params);
    }

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(String storedProcedureName,
            ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorToListMap(this.connection, storedProcedureName, params);
    }

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(String storedProcedureName,
            SqlParameter[] params) throws AppSqlException {
        return executeSpIdxWithCursorToListMap(this.connection, storedProcedureName, params);
    }

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName, List<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorToListMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName, ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorToListMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public ArrayList<Map<String, Object>> executeSpIdxWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter[] params) throws AppSqlException {
        Map<String, ArrayList<Map<String, Object>>> cursors = executeSpIdxWithCursorsToListMap(connection,
                storedProcedureName, params);
        for (String key : cursors.keySet()) {
            return cursors.get(key);
        }
        return null;
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpIdxWithCursorsToListMap(String storedProcedureName,
            List<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorsToListMap(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpIdxWithCursorsToListMap(String storedProcedureName,
            ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorsToListMap(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpIdxWithCursorsToListMap(String storedProcedureName,
            SqlParameter[] params) throws AppSqlException {
        return executeSpIdxWithCursorsToListMap(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpIdxWithCursorsToListMap(
            ConnectionManager.Connections connection, String storedProcedureName, List<SqlParameter> params)
            throws AppSqlException {
        return executeSpIdxWithCursorsToListMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpIdxWithCursorsToListMap(
            ConnectionManager.Connections connection, String storedProcedureName, ArrayList<SqlParameter> params)
            throws AppSqlException {
        return executeSpIdxWithCursorsToListMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    @SuppressWarnings("unchecked")
    public Map<String, ArrayList<Map<String, Object>>> executeSpIdxWithCursorsToListMap(
            ConnectionManager.Connections connection, String storedProcedureName, SqlParameter[] params)
            throws AppSqlException {
        Map<String, ArrayList<Map<String, Object>>> result = new HashMap<>();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].getDirection() == SqlParameter.ParameterDirection.OUTPUT)
                    params[i].setMapper(null);
            }
        }
        this.executeStoredProcedureIdx(connection, storedProcedureName, params);
        if (params != null) {
            int count = 1;
            for (int i = 0; i < params.length; i++) {
                if (params[i].getDirection() == SqlParameter.ParameterDirection.OUTPUT) {
                    String outName = params[i].getOutName();
                    if (outName == null || outName.trim() == "")
                        outName = ITEM_PREFIX_VALUE + count;
                    result.put(outName, (ArrayList<Map<String, Object>>) params[i].getOutValue());
                    count++;
                }
            }
        }
        return result;
    }
    // #endregion

    // #region Lấy về dòng đầu tiên

    public Map<String, Object> executeSpWithCursorToMap(String storedProcedureName) throws AppSqlException {
        return executeSpWithCursorToMap(this.connection, storedProcedureName, (SqlParameter) null);
    }

    public Map<String, Object> executeSpWithCursorToMap(String storedProcedureName, SqlParameter param)
            throws AppSqlException {
        return executeSpWithCursorToMap(this.connection, storedProcedureName, param);
    }

    public Map<String, Object> executeSpWithCursorToMap(ConnectionManager.Connections connection,
            String storedProcedureName) throws AppSqlException {
        return executeSpWithCursorToMap(connection, storedProcedureName, (SqlParameter[]) null);
    }

    public Map<String, Object> executeSpWithCursorToMap(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter param) throws AppSqlException {
        return executeSpWithCursorToMap(connection, storedProcedureName,
                param != null ? new SqlParameter[] { param } : null);
    }

    public Map<String, Object> executeSpWithCursorToMap(String storedProcedureName, List<SqlParameter> params)
            throws AppSqlException {
        return executeSpWithCursorToMap(this.connection, storedProcedureName, params);
    }

    public Map<String, Object> executeSpWithCursorToMap(String storedProcedureName, ArrayList<SqlParameter> params)
            throws AppSqlException {
        return executeSpWithCursorToMap(this.connection, storedProcedureName, params);
    }

    public Map<String, Object> executeSpWithCursorToMap(String storedProcedureName, SqlParameter[] params)
            throws AppSqlException {
        return executeSpWithCursorToMap(this.connection, storedProcedureName, params);
    }

    public Map<String, Object> executeSpWithCursorToMap(ConnectionManager.Connections connection,
            String storedProcedureName, List<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Map<String, Object> executeSpWithCursorToMap(ConnectionManager.Connections connection,
            String storedProcedureName, ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Map<String, Object> executeSpWithCursorToMap(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter[] params) throws AppSqlException {
        ArrayList<Map<String, Object>> cursor = executeSpWithCursorToListMap(connection, storedProcedureName, params);
        if (!(cursor == null || cursor.size() == 0))
            return cursor.get(0);
        return null;
    }

    // #endregion

    // #region Thực thi stored procedure nhiều cursor trên Oracle với các tham số theo tên

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(String storedProcedureName)
            throws AppSqlException {
        return executeSpWithCursorToListMap(this.connection, storedProcedureName, (SqlParameter) null);
    }

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(String storedProcedureName, SqlParameter param)
            throws AppSqlException {
        return executeSpWithCursorToListMap(this.connection, storedProcedureName, param);
    }

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName) throws AppSqlException {
        return executeSpWithCursorToListMap(connection, storedProcedureName, (SqlParameter[]) null);
    }

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter param) throws AppSqlException {
        return executeSpWithCursorToListMap(connection, storedProcedureName,
                param != null ? new SqlParameter[] { param } : null);
    }

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(String storedProcedureName,
            List<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToListMap(this.connection, storedProcedureName, params);
    }

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(String storedProcedureName,
            ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToListMap(this.connection, storedProcedureName, params);
    }

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(String storedProcedureName,
            SqlParameter[] params) throws AppSqlException {
        return executeSpWithCursorToListMap(this.connection, storedProcedureName, params);
    }

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName, List<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToListMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName, ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToListMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public ArrayList<Map<String, Object>> executeSpWithCursorToListMap(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter[] params) throws AppSqlException {
        Map<String, ArrayList<Map<String, Object>>> cursors = executeSpWithCursorsToListMap(connection,
                storedProcedureName, params);
        for (String key : cursors.keySet()) {
            return cursors.get(key);
        }
        return null;
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpWithCursorsToListMap(String storedProcedureName,
            List<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorsToListMap(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpWithCursorsToListMap(String storedProcedureName,
            ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorsToListMap(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpWithCursorsToListMap(String storedProcedureName,
            SqlParameter[] params) throws AppSqlException {
        return executeSpWithCursorsToListMap(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpWithCursorsToListMap(
            ConnectionManager.Connections connection, String storedProcedureName, List<SqlParameter> params)
            throws AppSqlException {
        return executeSpWithCursorsToListMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Map<String, ArrayList<Map<String, Object>>> executeSpWithCursorsToListMap(
            ConnectionManager.Connections connection, String storedProcedureName, ArrayList<SqlParameter> params)
            throws AppSqlException {
        return executeSpWithCursorsToListMap(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    @SuppressWarnings("unchecked")
    public Map<String, ArrayList<Map<String, Object>>> executeSpWithCursorsToListMap(
            ConnectionManager.Connections connection, String storedProcedureName, SqlParameter[] params)
            throws AppSqlException {
        Map<String, ArrayList<Map<String, Object>>> result = new HashMap<>();
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i].getDirection() == SqlParameter.ParameterDirection.OUTPUT)
                    params[i].setMapper(null);
            }
        }
        this.executeStoredProcedure(connection, storedProcedureName, params);
        if (params != null) {
            int count = 1;
            for (int i = 0; i < params.length; i++) {
                if (params[i].getDirection() == SqlParameter.ParameterDirection.OUTPUT) {
                    String outName = params[i].getOutName();
                    if (outName == null || outName.trim() == "")
                        outName = ITEM_PREFIX_VALUE + count;
                    if (params[i].getSqlType() == java.sql.Types.REF_CURSOR) {
                        result.put(outName, (ArrayList<Map<String, Object>>) params[i].getOutValue());
                    } else {
                        ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("item", params[i].getOutValue());
                        arrayList.add(item);
                        result.put(outName, arrayList);
                    }
                    count++;
                }
            }
        }
        return result;
    }
    // #endregion

    // #region Thực thi stored procedure nhiều cursor trên Oracle với các tham số theo thứ tự — mapper JDBI

    public ArrayList<?> executeSpIdxWithCursorToEntities(String storedProcedureName) throws AppSqlException {
        return executeSpIdxWithCursorToEntities(this.connection, storedProcedureName, (SqlParameter) null);
    }

    public ArrayList<?> executeSpIdxWithCursorToEntities(String storedProcedureName, SqlParameter param)
            throws AppSqlException {
        return executeSpIdxWithCursorToEntities(this.connection, storedProcedureName, param);
    }

    public ArrayList<?> executeSpIdxWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName) throws AppSqlException {
        return executeSpIdxWithCursorToEntities(connection, storedProcedureName, (SqlParameter[]) null);
    }

    public ArrayList<?> executeSpIdxWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter param) throws AppSqlException {
        return executeSpIdxWithCursorToEntities(connection, storedProcedureName,
                param != null ? new SqlParameter[] { param } : null);
    }

    public ArrayList<?> executeSpIdxWithCursorToEntities(String storedProcedureName, List<SqlParameter> params)
            throws AppSqlException {
        return executeSpIdxWithCursorToEntities(this.connection, storedProcedureName, params);
    }

    public ArrayList<?> executeSpIdxWithCursorToEntities(String storedProcedureName, ArrayList<SqlParameter> params)
            throws AppSqlException {
        return executeSpIdxWithCursorToEntities(this.connection, storedProcedureName, params);
    }

    public ArrayList<?> executeSpIdxWithCursorToEntities(String storedProcedureName, SqlParameter[] params)
            throws AppSqlException {
        return executeSpIdxWithCursorToEntities(this.connection, storedProcedureName, params);
    }

    public ArrayList<?> executeSpIdxWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, List<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorToEntities(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public ArrayList<?> executeSpIdxWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorToEntities(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public ArrayList<?> executeSpIdxWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter[] params) throws AppSqlException {
        Map<String, ArrayList<?>> cursors = executeSpIdxWithCursorsToEntities(connection, storedProcedureName, params);
        for (String key : cursors.keySet()) {
            return cursors.get(key);
        }
        return null;
    }

    public Map<String, ArrayList<?>> executeSpIdxWithCursorsToEntities(String storedProcedureName,
            List<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorsToEntities(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<?>> executeSpIdxWithCursorsToEntities(String storedProcedureName,
            ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorsToEntities(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<?>> executeSpIdxWithCursorsToEntities(String storedProcedureName,
            SqlParameter[] params) throws AppSqlException {
        return executeSpIdxWithCursorsToEntities(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<?>> executeSpIdxWithCursorsToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, List<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorsToEntities(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Map<String, ArrayList<?>> executeSpIdxWithCursorsToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpIdxWithCursorsToEntities(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Map<String, ArrayList<?>> executeSpIdxWithCursorsToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter[] params) throws AppSqlException {
        Map<String, ArrayList<?>> result = new HashMap<>();
        this.executeStoredProcedureIdx(connection, storedProcedureName, params);
        if (params != null) {
            int count = 1;
            for (int i = 0; i < params.length; i++) {
                if (params[i].getDirection() == SqlParameter.ParameterDirection.OUTPUT) {
                    String outName = params[i].getOutName();
                    if (outName == null || outName.trim() == "")
                        outName = ITEM_PREFIX_VALUE + count;
                    if (params[i].getSqlType() == java.sql.Types.REF_CURSOR) {
                        result.put(outName, (ArrayList<?>) params[i].getOutValue());
                    } else {
                        ArrayList<Object> arrayList = new ArrayList<Object>();
                        arrayList.add(params[i].getOutValue());
                        result.put(outName, arrayList);
                    }
                    count++;
                }
            }
        }
        return result;
    }
    // #endregion

    // #region Thực thi stored procedure nhiều cursor trên Oracle với các tham số theo tên — 1 entity

    public Object executeSpWithCursorToEntity(String storedProcedureName, SqlParameter param) throws AppSqlException {
        return executeSpWithCursorToEntity(this.connection, storedProcedureName, param);
    }

    public Object executeSpWithCursorToEntity(ConnectionManager.Connections connection, String storedProcedureName)
            throws AppSqlException {
        return executeSpWithCursorToEntity(connection, storedProcedureName, (SqlParameter[]) null);
    }

    public Object executeSpWithCursorToEntity(ConnectionManager.Connections connection, String storedProcedureName,
            SqlParameter param) throws AppSqlException {
        return executeSpWithCursorToEntity(connection, storedProcedureName,
                param != null ? new SqlParameter[] { param } : null);
    }

    public Object executeSpWithCursorToEntity(String storedProcedureName, List<SqlParameter> params)
            throws AppSqlException {
        return executeSpWithCursorToEntity(this.connection, storedProcedureName, params);
    }

    public Object executeSpWithCursorToEntity(String storedProcedureName, ArrayList<SqlParameter> params)
            throws AppSqlException {
        return executeSpWithCursorToEntity(this.connection, storedProcedureName, params);
    }

    public Object executeSpWithCursorToEntity(String storedProcedureName, SqlParameter[] params)
            throws AppSqlException {
        return executeSpWithCursorToEntity(this.connection, storedProcedureName, params);
    }

    public Object executeSpWithCursorToEntity(ConnectionManager.Connections connection, String storedProcedureName,
            List<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToEntity(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Object executeSpWithCursorToEntity(ConnectionManager.Connections connection, String storedProcedureName,
            ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToEntity(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Object executeSpWithCursorToEntity(ConnectionManager.Connections connection, String storedProcedureName,
            SqlParameter[] params) throws AppSqlException {
        ArrayList<?> result = executeSpWithCursorToEntities(connection, storedProcedureName, params);
        if (!(result == null || result.size() == 0))
            return result.get(0);
        return null;
    }

    // #endregion

    // #region Thực thi stored procedure nhiều cursor trên Oracle với các tham số theo tên — mapper JDBI

    public ArrayList<?> executeSpWithCursorToEntities(String storedProcedureName) throws AppSqlException {
        return executeSpWithCursorToEntities(this.connection, storedProcedureName, (SqlParameter) null);
    }

    public ArrayList<?> executeSpWithCursorToEntities(String storedProcedureName, SqlParameter param)
            throws AppSqlException {
        return executeSpWithCursorToEntities(this.connection, storedProcedureName, param);
    }

    public ArrayList<?> executeSpWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName) throws AppSqlException {
        return executeSpWithCursorToEntities(connection, storedProcedureName, (SqlParameter[]) null);
    }

    public ArrayList<?> executeSpWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter param) throws AppSqlException {
        return executeSpWithCursorToEntities(connection, storedProcedureName,
                param != null ? new SqlParameter[] { param } : null);
    }

    public ArrayList<?> executeSpWithCursorToEntities(String storedProcedureName, List<SqlParameter> params)
            throws AppSqlException {
        return executeSpWithCursorToEntities(this.connection, storedProcedureName, params);
    }

    public ArrayList<?> executeSpWithCursorToEntities(String storedProcedureName, ArrayList<SqlParameter> params)
            throws AppSqlException {
        return executeSpWithCursorToEntities(this.connection, storedProcedureName, params);
    }

    public ArrayList<?> executeSpWithCursorToEntities(String storedProcedureName, SqlParameter[] params)
            throws AppSqlException {
        return executeSpWithCursorToEntities(this.connection, storedProcedureName, params);
    }

    public ArrayList<?> executeSpWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, List<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToEntities(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public ArrayList<?> executeSpWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorToEntities(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public ArrayList<?> executeSpWithCursorToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter[] params) throws AppSqlException {
        Map<String, ArrayList<?>> cursors = executeSpWithCursorsToEntities(connection, storedProcedureName, params);
        for (String key : cursors.keySet()) {
            return cursors.get(key);
        }
        return null;
    }

    public Map<String, ArrayList<?>> executeSpWithCursorsToEntities(String storedProcedureName,
            List<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorsToEntities(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<?>> executeSpWithCursorsToEntities(String storedProcedureName,
            ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorsToEntities(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<?>> executeSpWithCursorsToEntities(String storedProcedureName, SqlParameter[] params)
            throws AppSqlException {
        return executeSpWithCursorsToEntities(this.connection, storedProcedureName, params);
    }

    public Map<String, ArrayList<?>> executeSpWithCursorsToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, List<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorsToEntities(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Map<String, ArrayList<?>> executeSpWithCursorsToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, ArrayList<SqlParameter> params) throws AppSqlException {
        return executeSpWithCursorsToEntities(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Map<String, ArrayList<?>> executeSpWithCursorsToEntities(ConnectionManager.Connections connection,
            String storedProcedureName, SqlParameter[] params) throws AppSqlException {
        Map<String, ArrayList<?>> result = new HashMap<>();
        this.executeStoredProcedure(connection, storedProcedureName, params);
        if (params != null) {
            int count = 1;
            for (int i = 0; i < params.length; i++) {
                if (params[i].getDirection() == SqlParameter.ParameterDirection.OUTPUT) {
                    String outName = params[i].getOutName();
                    if (outName == null || outName.trim() == "")
                        outName = ITEM_PREFIX_VALUE + count;
                    result.put(outName, (ArrayList<?>) params[i].getOutValue());
                    count++;
                }
            }
        }
        return result;
    }
    // #endregion

    // #region Stored Procedure (named params)

    public void executeStoredProcedure(String storedProcedureName) throws AppSqlException {
        this.executeStoredProcedure(this.connection, storedProcedureName, (SqlParameter) null);
    }

    public void executeStoredProcedure(String storedProcedureName, SqlParameter param) throws AppSqlException {
        this.executeStoredProcedure(this.connection, storedProcedureName, param);
    }

    public void executeStoredProcedure(ConnectionManager.Connections connection, String storedProcedureName)
            throws AppSqlException {
        this.executeStoredProcedure(connection, storedProcedureName, (SqlParameter[]) null);
    }

    public void executeStoredProcedure(ConnectionManager.Connections connection, String storedProcedureName,
            SqlParameter param) throws AppSqlException {
        this.executeStoredProcedure(connection, storedProcedureName,
                param != null ? new SqlParameter[] { param } : null);
    }

    public void executeStoredProcedure(String storedProcedureName, List<SqlParameter> params) throws AppSqlException {
        this.executeStoredProcedure(this.connection, storedProcedureName, params);
    }

    public void executeStoredProcedure(String storedProcedureName, ArrayList<SqlParameter> params)
            throws AppSqlException {
        this.executeStoredProcedure(this.connection, storedProcedureName, params);
    }

    public void executeStoredProcedure(String storedProcedureName, SqlParameter[] params) throws AppSqlException {
        this.executeStoredProcedure(this.connection, storedProcedureName, params);
    }

    public void executeStoredProcedure(ConnectionManager.Connections connection, String storedProcedureName,
            List<SqlParameter> params) throws AppSqlException {
        this.executeStoredProcedure(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public void executeStoredProcedure(ConnectionManager.Connections connection, String storedProcedureName,
            ArrayList<SqlParameter> params) throws AppSqlException {
        this.executeStoredProcedure(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public void executeStoredProcedure(ConnectionManager.Connections connection, String storedProcedureName,
            SqlParameter[] params) throws AppSqlException {
        if (this.validateSqlInjection(params)) {
            String sql = this.getSqlSpString(storedProcedureName, params, false);
            try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
                Call call = handle.createCall(sql);
                if (!(params == null || params.length == 0)) {
                    for (SqlParameter sqlParameter : params) {
                        if (sqlParameter.getDirection() == SqlParameter.ParameterDirection.INPUT)
                            if (sqlParameter.getValue() != null)
                                call.bind(sqlParameter.getName(), sqlParameter.getValue());
                            else
                                call.bindNull(sqlParameter.getName(), sqlParameter.getSqlType());
                        else
                            call.registerOutParameter(sqlParameter.getName(), sqlParameter.getSqlType());
                    }
                }
                try {
                    call.invoke(outParameters -> {
                        if (params != null) {
                            for (int i = 0; i < params.length; i++) {
                                if (params[i].getDirection() == SqlParameter.ParameterDirection.OUTPUT) {
                                    if (params[i].getSqlType() == java.sql.Types.REF_CURSOR) {
                                        if (params[i].getOutItemType() != null) {
                                            params[i].setOutValue(outParameters.getRowSet(params[i].getName())
                                                    .mapTo(params[i].getOutItemType()).list());
                                        } else if (params[i].getMapper() != null) {
                                            params[i].setOutValue(outParameters.getRowSet(params[i].getName())
                                                    .map(params[i].getMapper()).list());
                                        } else {
                                            ResultSet resultSet = (ResultSet) outParameters
                                                    .getObject(params[i].getName());
                                            ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
                                            if (resultSet != null) {
                                                try {
                                                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                                                    final int columnCount = resultSet.getMetaData().getColumnCount();
                                                    while (resultSet.next()) {
                                                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                                                        for (int j = 0; j < columnCount; j++) {
                                                            if (resultSetMetaData.getColumnTypeName(j + 1) == "DATE") {
                                                                if (resultSet.getObject(j + 1) != null) {
                                                                    map.put(resultSetMetaData.getColumnName(j + 1),
                                                                            resultSet.getObject(j + 1).toString());
                                                                } else {
                                                                    map.put(resultSetMetaData.getColumnName(j + 1), null);
                                                                }
                                                            } else if (resultSetMetaData.getColumnTypeName(j + 1) == "CLOB") {
                                                                Clob clob = resultSet.getClob(j + 1);
                                                                if (clob != null) {
                                                                    Reader reader = clob.getCharacterStream();
                                                                    StringBuffer buffer = new StringBuffer();
                                                                    int ch;
                                                                    while ((ch = reader.read()) != -1) {
                                                                        buffer.append("" + (char) ch);
                                                                    }
                                                                    map.put(resultSetMetaData.getColumnName(j + 1),
                                                                            buffer.toString());
                                                                } else {
                                                                    map.put(resultSetMetaData.getColumnName(j + 1), null);
                                                                }
                                                            } else if (resultSetMetaData.getColumnTypeName(j + 1) == "BLOB") {
                                                                Blob blob = resultSet.getBlob(j + 1);
                                                                if (blob != null) {
                                                                    byte[] imageData = blob.getBytes(1, (int) blob.length());
                                                                    map.put(resultSetMetaData.getColumnName(j + 1),
                                                                            DatatypeConverter.printBase64Binary(imageData));
                                                                } else {
                                                                    map.put(resultSetMetaData.getColumnName(j + 1), null);
                                                                }
                                                            } else {
                                                                map.put(resultSetMetaData.getColumnName(j + 1),
                                                                        resultSet.getObject(j + 1));
                                                            }
                                                        }
                                                        arrayList.add(map);
                                                    }
                                                } catch (Exception ie) {
                                                    ie.printStackTrace();
                                                }
                                                params[i].setOutValue(arrayList);
                                            }
                                        }
                                    } else {
                                        params[i].setOutValue(outParameters.getObject(params[i].getName()));
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new AppSqlException(e);
                }
            }
        } else {
            throw new AppSqlException(this.MSG_SQL_INJECTION);
        }
    }
    // #endregion Stored Procedure

    // #region Stored Procedure Idx (positional params)

    public void executeStoredProcedureIdx(String storedProcedureName) throws AppSqlException {
        this.executeStoredProcedureIdx(this.connection, storedProcedureName, (SqlParameter) null);
    }

    public void executeStoredProcedureIdx(String storedProcedureName, SqlParameter param) throws AppSqlException {
        this.executeStoredProcedureIdx(this.connection, storedProcedureName, param);
    }

    public void executeStoredProcedureIdx(ConnectionManager.Connections connection, String storedProcedureName)
            throws AppSqlException {
        this.executeStoredProcedureIdx(connection, storedProcedureName, (SqlParameter[]) null);
    }

    public void executeStoredProcedureIdx(ConnectionManager.Connections connection, String storedProcedureName,
            SqlParameter param) throws AppSqlException {
        this.executeStoredProcedureIdx(connection, storedProcedureName,
                param != null ? new SqlParameter[] { param } : null);
    }

    public void executeStoredProcedureIdx(String storedProcedureName, List<SqlParameter> params)
            throws AppSqlException {
        this.executeStoredProcedureIdx(this.connection, storedProcedureName, params);
    }

    public void executeStoredProcedureIdx(String storedProcedureName, ArrayList<SqlParameter> params)
            throws AppSqlException {
        this.executeStoredProcedureIdx(this.connection, storedProcedureName, params);
    }

    public void executeStoredProcedureIdx(String storedProcedureName, SqlParameter[] params) throws AppSqlException {
        this.executeStoredProcedureIdx(this.connection, storedProcedureName, params);
    }

    public void executeStoredProcedureIdx(ConnectionManager.Connections connection, String storedProcedureName,
            List<SqlParameter> params) throws AppSqlException {
        this.executeStoredProcedureIdx(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public void executeStoredProcedureIdx(ConnectionManager.Connections connection, String storedProcedureName,
            ArrayList<SqlParameter> params) throws AppSqlException {
        this.executeStoredProcedureIdx(connection, storedProcedureName,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public void executeStoredProcedureIdx(ConnectionManager.Connections connection, String storedProcedureName,
            SqlParameter[] params) throws AppSqlException {
        if (this.validateSqlInjection(params)) {
            String sql = this.getSqlSpString(storedProcedureName, params, true);
            try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
                Call call = handle.createCall(sql);
                if (!(params == null || params.length == 0)) {
                    for (int i = 0; i < params.length; i++) {
                        if (params[i].getDirection() == SqlParameter.ParameterDirection.INPUT)
                            if (params[i].getValue() != null)
                                call.bind(i, params[i].getValue());
                            else
                                call.bindNull(i, params[i].getSqlType());
                        else
                            call.registerOutParameter(i, params[i].getSqlType());
                    }
                }
                try {
                    call.invoke(outParameters -> {
                        if (params != null) {
                            for (int i = 0; i < params.length; i++) {
                                if (params[i].getDirection() == SqlParameter.ParameterDirection.OUTPUT) {
                                    if (params[i].getSqlType() == java.sql.Types.REF_CURSOR) {
                                        if (params[i].getMapper() != null) {
                                            params[i].setOutValue(
                                                    outParameters.getRowSet(i).map(params[i].getMapper()).list());
                                        } else {
                                            ResultSet resultSet = (ResultSet) outParameters.getObject(i);
                                            ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
                                            try {
                                                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                                                final int columnCount = resultSet.getMetaData().getColumnCount();
                                                while (resultSet.next()) {
                                                    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                                                    for (int j = 0; j < columnCount; j++) {
                                                        if (resultSetMetaData.getColumnTypeName(j + 1) == "DATE") {
                                                            if (resultSet.getObject(j + 1) != null) {
                                                                map.put(resultSetMetaData.getColumnName(j + 1),
                                                                        resultSet.getObject(j + 1).toString());
                                                            }
                                                        } else if (resultSetMetaData.getColumnTypeName(j + 1) == "CLOB") {
                                                            Clob clob = resultSet.getClob(j + 1);
                                                            if (clob != null) {
                                                                Reader reader = clob.getCharacterStream();
                                                                StringBuffer buffer = new StringBuffer();
                                                                int ch;
                                                                while ((ch = reader.read()) != -1) {
                                                                    buffer.append("" + (char) ch);
                                                                }
                                                                map.put(resultSetMetaData.getColumnName(j + 1),
                                                                        buffer.toString());
                                                            } else {
                                                                map.put(resultSetMetaData.getColumnName(j + 1), null);
                                                            }
                                                        } else if (resultSetMetaData.getColumnTypeName(j + 1) == "BLOB") {
                                                            Blob blob = resultSet.getBlob(j + 1);
                                                            if (blob != null) {
                                                                byte[] imageData = blob.getBytes(1, (int) blob.length());
                                                                map.put(resultSetMetaData.getColumnName(j + 1),
                                                                        DatatypeConverter.printBase64Binary(imageData));
                                                            } else {
                                                                map.put(resultSetMetaData.getColumnName(j + 1), null);
                                                            }
                                                        } else {
                                                            map.put(resultSetMetaData.getColumnName(j + 1),
                                                                    resultSet.getObject(j + 1));
                                                        }
                                                    }
                                                    arrayList.add(map);
                                                }
                                            } catch (Exception ie) {
                                                ie.printStackTrace();
                                            }
                                            params[i].setOutValue(arrayList);
                                        }
                                    } else {
                                        params[i].setOutValue(outParameters.getObject(i));
                                    }
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new AppSqlException(e);
                }
            }
        } else {
            throw new AppSqlException(this.MSG_SQL_INJECTION);
        }
    }
    // #endregion Stored Procedure Idx

    // #region Function

    public Object executeFunction(String functionName, int sqlTypeReturn) throws AppSqlException {
        return this.executeFunction(this.connection, functionName, sqlTypeReturn, (SqlParameter) null);
    }

    public Object executeFunction(String functionName, int sqlTypeReturn, SqlParameter param) throws AppSqlException {
        return this.executeFunction(this.connection, functionName, sqlTypeReturn, param);
    }

    public Object executeFunction(ConnectionManager.Connections connection, String functionName, int sqlTypeReturn)
            throws AppSqlException {
        return this.executeFunction(connection, functionName, sqlTypeReturn, (SqlParameter[]) null);
    }

    public Object executeFunction(ConnectionManager.Connections connection, String functionName, int sqlTypeReturn,
            SqlParameter param) throws AppSqlException {
        return this.executeFunction(connection, functionName, sqlTypeReturn,
                param != null ? new SqlParameter[] { param } : null);
    }

    public Object executeFunction(String functionName, int sqlTypeReturn, List<SqlParameter> params)
            throws AppSqlException {
        return this.executeFunction(this.connection, functionName, sqlTypeReturn, params);
    }

    public Object executeFunction(String functionName, int sqlTypeReturn, ArrayList<SqlParameter> params)
            throws AppSqlException {
        return this.executeFunction(this.connection, functionName, sqlTypeReturn, params);
    }

    public Object executeFunction(String functionName, int sqlTypeReturn, SqlParameter[] params)
            throws AppSqlException {
        return this.executeFunction(this.connection, functionName, sqlTypeReturn, params);
    }

    public Object executeFunction(ConnectionManager.Connections connection, String functionName, int sqlTypeReturn,
            List<SqlParameter> params) throws AppSqlException {
        return this.executeFunction(connection, functionName, sqlTypeReturn,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Object executeFunction(ConnectionManager.Connections connection, String functionName, int sqlTypeReturn,
            ArrayList<SqlParameter> params) throws AppSqlException {
        return this.executeFunction(connection, functionName, sqlTypeReturn,
                params != null ? params.toArray(new SqlParameter[params.size()]) : null);
    }

    public Object executeFunction(ConnectionManager.Connections connection, String functionName, int sqlTypeReturn,
            SqlParameter[] params) throws AppSqlException {
        if (this.validateSqlInjection(params)) {
            ArrayList<Object> result = new ArrayList<Object>();
            String resultParamNameBuilder = "result";
            if (params != null) {
                for (SqlParameter sqlParameter : params) {
                    if (sqlParameter.getDirection() == SqlParameter.ParameterDirection.OUTPUT)
                        resultParamNameBuilder = resultParamNameBuilder + "_" + sqlParameter.getName();
                }
            }
            final String resultParamName = resultParamNameBuilder;
            String sql = "{ :" + resultParamName + " = " + this.getSpFnSqlString(functionName, params, false) + " }";
            try (Handle handle = Jdbi.create(getDataSource(connection)).open()) {
                Call call = handle.createCall(sql);
                if (!(params == null || params.length == 0)) {
                    for (SqlParameter sqlParameter : params) {
                        if (sqlParameter.getDirection() == SqlParameter.ParameterDirection.INPUT)
                            if (sqlParameter.getValue() != null)
                                call.bind(sqlParameter.getName(), sqlParameter.getValue());
                            else
                                call.bindNull(sqlParameter.getName(), sqlParameter.getSqlType());
                        else
                            call.registerOutParameter(sqlParameter.getName(), sqlParameter.getSqlType());
                    }
                }
                call.registerOutParameter(resultParamName, sqlTypeReturn);
                try {
                    call.invoke(outParameters -> {
                        if (params != null) {
                            for (int i = 0; i < params.length; i++) {
                                if (params[i].getDirection() == SqlParameter.ParameterDirection.OUTPUT) {
                                    if (params[i].getSqlType() == java.sql.Types.REF_CURSOR) {
                                        if (params[i].getMapper() != null) {
                                            params[i].setOutValue(outParameters.getRowSet(params[i].getName())
                                                    .map(params[i].getMapper()).list());
                                        } else {
                                            ResultSet resultSet = (ResultSet) outParameters
                                                    .getObject(params[i].getName());
                                            ArrayList<Map<String, Object>> arrayList = new ArrayList<Map<String, Object>>();
                                            try {
                                                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                                                final int columnCount = resultSet.getMetaData().getColumnCount();
                                                while (resultSet.next()) {
                                                    LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                                                    for (int j = 0; j < columnCount; j++) {
                                                        if (resultSetMetaData.getColumnTypeName(j + 1) == "DATE") {
                                                            if (resultSet.getObject(j + 1) != null) {
                                                                map.put(resultSetMetaData.getColumnName(j + 1),
                                                                        resultSet.getObject(j + 1).toString());
                                                            }
                                                        } else if (resultSetMetaData.getColumnTypeName(j + 1) == "CLOB") {
                                                            Clob clob = resultSet.getClob(j + 1);
                                                            if (clob != null) {
                                                                Reader reader = clob.getCharacterStream();
                                                                StringBuffer buffer = new StringBuffer();
                                                                int ch;
                                                                while ((ch = reader.read()) != -1) {
                                                                    buffer.append("" + (char) ch);
                                                                }
                                                                map.put(resultSetMetaData.getColumnName(j + 1),
                                                                        buffer.toString());
                                                            } else {
                                                                map.put(resultSetMetaData.getColumnName(j + 1), null);
                                                            }
                                                        } else if (resultSetMetaData.getColumnTypeName(j + 1) == "BLOB") {
                                                            Blob blob = resultSet.getBlob(j + 1);
                                                            if (blob != null) {
                                                                byte[] imageData = blob.getBytes(1, (int) blob.length());
                                                                map.put(resultSetMetaData.getColumnName(j + 1),
                                                                        DatatypeConverter.printBase64Binary(imageData));
                                                            } else {
                                                                map.put(resultSetMetaData.getColumnName(j + 1), null);
                                                            }
                                                        } else {
                                                            map.put(resultSetMetaData.getColumnName(j + 1),
                                                                    resultSet.getObject(j + 1));
                                                        }
                                                    }
                                                    arrayList.add(map);
                                                }
                                            } catch (Exception ie) {
                                                ie.printStackTrace();
                                            }
                                            params[i].setOutValue(arrayList);
                                        }
                                    } else {
                                        params[i].setOutValue(outParameters.getObject(params[i].getName()));
                                    }
                                }
                            }
                        }
                        result.add(outParameters.getObject(resultParamName));
                    });
                } catch (Exception e) {
                    throw new AppSqlException(e);
                }
            }
            if (result.size() == 1)
                return result.get(0);
            return null;
        } else {
            throw new AppSqlException(this.MSG_SQL_INJECTION);
        }
    }
    // #endregion Function

    // #region SQL String helpers

    private String getSqlSpString(String storedProcedureName, SqlParameter[] params, boolean useIndex) {
        return "{ " + this.getSpFnSqlString(storedProcedureName, params, useIndex) + " }";
    }

    private String getSpFnSqlString(String spFnName, SqlParameter[] params, boolean useIndex) {
        String prs = "";
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (prs != "")
                    prs += ", ";
                if (useIndex)
                    prs += "?";
                else
                    prs += ":" + params[i].getName();
            }
        }
        return "call " + spFnName + "(" + prs + ")";
    }
    // #endregion SQL String
}
