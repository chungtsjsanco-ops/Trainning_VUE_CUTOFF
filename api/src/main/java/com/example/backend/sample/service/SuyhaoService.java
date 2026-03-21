package com.example.backend.sample.service;

import com.example.backend.common.ApiResult;
import com.example.backend.database.AppSqlException;
import com.example.backend.database.ConnectionManager;
import com.example.backend.database.DbContext;
import com.example.backend.database.SqlParameter;
import com.example.backend.sample.dto.SolieuKllDTO;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.Map;

@Service
public class SuyhaoService {
    @Autowired
    DbContext dbContext;

    public ArrayList<Map<String, Object>> SOLIEU_SUYHAO_CTS_VIEWNGAY(String prmNGAY)
            throws AppSqlException {

        ConnectionManager.Connections connection = ConnectionManager.Connections.SECOND;

        ArrayList<SqlParameter> parameters = new ArrayList<>();
        // INPUT
        parameters.add(new SqlParameter(
                "prmNGAY",
                prmNGAY,
                SqlParameter.ParameterDirection.INPUT,
                java.sql.Types.VARCHAR));
        // OUTPUT cursor
        parameters.add(new SqlParameter(
                "RESULTS",
                null,
                SqlParameter.ParameterDirection.OUTPUT,
                java.sql.Types.REF_CURSOR));

        return dbContext.executeSpWithCursorToListMap(
                connection,
                "SUYHAO_CTS.SOLIEU_SUYHAO_CTS_VIEWNGAY",
                parameters);
    }

    public ArrayList<Map<String, Object>> SOLIEU_SUYHAO_OLT_VIEWNGAY(String prmNGAY)
            throws AppSqlException {

        ConnectionManager.Connections connection = ConnectionManager.Connections.SECOND;

        ArrayList<SqlParameter> parameters = new ArrayList<>();
        // INPUT
        parameters.add(new SqlParameter(
                "prmNGAY",
                prmNGAY,
                SqlParameter.ParameterDirection.INPUT,
                java.sql.Types.VARCHAR));
        // OUTPUT cursor
        parameters.add(new SqlParameter(
                "RESULTS",
                null,
                SqlParameter.ParameterDirection.OUTPUT,
                java.sql.Types.REF_CURSOR));

        return dbContext.executeSpWithCursorToListMap(
                connection,
                "SUYHAO_CTS.SOLIEU_SUYHAO_OLT_VIEWNGAY",
                parameters);
    }

    public ArrayList<Map<String, Object>> SOLIEU_SUYHAO_OLT_TONGHOP_NGAY(String prmNGAY)
            throws AppSqlException {

        ConnectionManager.Connections connection = ConnectionManager.Connections.SECOND;

        ArrayList<SqlParameter> parameters = new ArrayList<>();
        // INPUT
        parameters.add(new SqlParameter(
                "prmNGAY",
                prmNGAY,
                SqlParameter.ParameterDirection.INPUT,
                java.sql.Types.VARCHAR));
        // OUTPUT cursor
        parameters.add(new SqlParameter(
                "RESULTS",
                null,
                SqlParameter.ParameterDirection.OUTPUT,
                java.sql.Types.REF_CURSOR));

        return dbContext.executeSpWithCursorToListMap(
                connection,
                "SUYHAO_CTS.SOLIEU_SUYHAO_OLT_TONGHOP_NGAY",
                parameters);
    }
}
