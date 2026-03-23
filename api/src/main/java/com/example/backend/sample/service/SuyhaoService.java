package com.example.backend.sample.service;

import com.example.backend.common.ApiResult;
import com.example.backend.database.AppSqlException;
import com.example.backend.database.ConnectionManager;
import com.example.backend.database.DbContext;
import com.example.backend.database.SqlParameter;
import com.example.backend.sample.dto.SuyhaoDTO;

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

        public ArrayList<Map<String, Object>> SOLIEU_SUYHAO_OLT_TONGHOP_TUNGAY_DENNGAY(String prmTUNGAY,
                        String prmDENNGAY)
                        throws AppSqlException {

                ConnectionManager.Connections connection = ConnectionManager.Connections.SECOND;

                ArrayList<SqlParameter> parameters = new ArrayList<>();
                // INPUT
                parameters.add(new SqlParameter(
                                "prmTUNGAY",
                                prmTUNGAY,
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.VARCHAR));
                parameters.add(new SqlParameter(
                                "prmDENNGAY",
                                prmDENNGAY,
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
                                "SUYHAO_CTS.SOLIEU_SUYHAO_OLT_TONGHOP_TUNGAY_DENNGAY",
                                parameters);
        }

        public ArrayList<Map<String, Object>> VIEW_DS_DONVI(Number prmDK,
                        String prmDONVI)
                        throws AppSqlException {

                ConnectionManager.Connections connection = ConnectionManager.Connections.SECOND;

                ArrayList<SqlParameter> parameters = new ArrayList<>();
                // INPUT
                parameters.add(new SqlParameter(
                                "prmDK",
                                prmDK,
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.INTEGER));
                parameters.add(new SqlParameter(
                                "prmDONVI",
                                prmDONVI,
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
                                "SUYHAO_CTS.VIEW_DS_DONVI",
                                parameters);
        }

        public Boolean CNTT_OLT_CONFIG_INS(SuyhaoDTO input) throws AppSqlException {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        ArrayList<SqlParameter> parameters = new ArrayList<>();
        parameters.add(new SqlParameter("prmSYSTEMNAME", input.getSYSTEMNAME(), SqlParameter.ParameterDirection.INPUT, java.sql.Types.NVARCHAR));
        parameters.add(new SqlParameter("prmDONVI", input.getPrmDONVI(), SqlParameter.ParameterDirection.INPUT, java.sql.Types.NVARCHAR));
        parameters.add(new SqlParameter("prmTOKYTHUAT", input.getPrmTOKYTHUAT(), SqlParameter.ParameterDirection.INPUT, java.sql.Types.NVARCHAR));
        parameters.add(new SqlParameter("prmNSD", currentUser, SqlParameter.ParameterDirection.INPUT, java.sql.Types.NVARCHAR));
        ArrayList<Map<String, Object>> rs = dbContext.executeSpWithCursorToListMap("CNTT_OLT_CONFIG_INS", parameters);
        if (rs != null && !rs.isEmpty()) {
            if ("1".equals(String.valueOf(rs.get(0).get("item"))))
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }    
}
