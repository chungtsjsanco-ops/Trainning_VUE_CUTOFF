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
public class THUEBAOKLLService {

        @Autowired
        DbContext dbContext;

        public ArrayList<Map<String, Object>> LYDO_OB_VIEW()
                        throws AppSqlException {
                // Nếu caller truyền null thì vẫn ép về SECOND cho chắc chắn.
                ConnectionManager.Connections connection = ConnectionManager.Connections.SECOND;
                ArrayList<SqlParameter> parameters = new ArrayList<>();
                parameters.add(new SqlParameter(
                                "RESULTS",
                                null,
                                SqlParameter.ParameterDirection.OUTPUT,
                                java.sql.Types.REF_CURSOR));

                // NOTE: đổi tên SP này theo đúng schema/package của bạn.
                return dbContext.executeSpWithCursorToListMap(
                                connection,
                                "TB_KLL.LYDO_OB_VIEW",
                                parameters);
        }

        public ArrayList<Map<String, Object>> SOLIEU_KLL_VIEW(Number prmDK, String prmNGAY)
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
                                "TB_KLL.SOLIEU_KLL_VIEW",
                                parameters);
        }

        public ArrayList<Map<String, Object>> SOLIEU_KLL_VIEW_OB(String prmNGAY, String prmNGUOI_CAPNHAT)
                        throws AppSqlException {

                ConnectionManager.Connections connection = ConnectionManager.Connections.SECOND;

                ArrayList<SqlParameter> parameters = new ArrayList<>();

                // INPUT
                parameters.add(new SqlParameter(
                                "prmNGAY",
                                prmNGAY,
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.INTEGER));
                parameters.add(new SqlParameter(
                                "prmNGUOI_CAPNHAT",
                                prmNGUOI_CAPNHAT,
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
                                "TB_KLL.SOLIEU_KLL_VIEW_OB",
                                parameters);
        }

        public ArrayList<Map<String, Object>> DANHSACH_TB_KHONG_OB_VIEW()
                        throws AppSqlException {
                ConnectionManager.Connections connection = ConnectionManager.Connections.SECOND;

                ArrayList<SqlParameter> parameters = new ArrayList<>();
                parameters.add(new SqlParameter(
                                "RESULTS",
                                null,
                                SqlParameter.ParameterDirection.OUTPUT,
                                java.sql.Types.REF_CURSOR));

                // NOTE: đổi tên SP này theo đúng schema/package của bạn.
                return dbContext.executeSpWithCursorToListMap(
                                connection,
                                "TB_KLL.DANHSACH_TB_KHONG_OB_VIEW",
                                parameters);
        }

        public ArrayList<Map<String, Object>> TIMKIEM_TB_KHONG_OB_VIEW(String prmTHONGTIN)
                        throws AppSqlException {

                ConnectionManager.Connections connection = ConnectionManager.Connections.SECOND;

                ArrayList<SqlParameter> parameters = new ArrayList<>();

                parameters.add(new SqlParameter(
                                "prmTHONGTIN",
                                prmTHONGTIN,
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
                                "TB_KLL.TIMKIEM_TB_KHONG_OB_VIEW",
                                parameters);
        }

        public Boolean SOLIEU_TB_KLL_2_INS(SolieuKllDTO input) throws AppSqlException {

                String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

                ArrayList<SqlParameter> parameters = new ArrayList<>();

                parameters.add(new SqlParameter(
                                "prmJsonData",
                                input.getPrmJsonData(),
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.CLOB));

                parameters.add(new SqlParameter(
                                "prmNGUOI_CAPNHAT",
                                currentUser,
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.NVARCHAR));

                ArrayList<Map<String, Object>> rs = dbContext.executeSpWithCursorToListMap(
                                ConnectionManager.Connections.SECOND,
                                "TB_KLL.SOLIEU_TB_KLL_2_INS",
                                parameters);
                if (rs != null && !rs.isEmpty()) {
                        if ("1".equals(String.valueOf(rs.get(0).get("item"))))
                                return Boolean.TRUE;
                }
                return Boolean.FALSE;
        }

        public Boolean DANHSACH_TB_KHONG_OB_INS(SolieuKllDTO input) throws AppSqlException {

                String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

                ArrayList<SqlParameter> parameters = new ArrayList<>();

                parameters.add(new SqlParameter(
                                "prmJsonData",
                                input.getPrmJsonData(),
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.CLOB));

                parameters.add(new SqlParameter(
                                "prmNGUOI_CAPNHAT",
                                currentUser,
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.NVARCHAR));

                ArrayList<Map<String, Object>> rs = dbContext.executeSpWithCursorToListMap(
                                ConnectionManager.Connections.SECOND,
                                "TB_KLL.DANHSACH_TB_KHONG_OB_INS",
                                parameters);
                if (rs != null && !rs.isEmpty()) {
                        if ("1".equals(String.valueOf(rs.get(0).get("item"))))
                                return Boolean.TRUE;
                }
                return Boolean.FALSE;
        }

        public Boolean DANHSACH_TB_KHONG_OB_DEL(SolieuKllDTO input) throws AppSqlException {

                String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();

                ArrayList<SqlParameter> parameters = new ArrayList<>();

                parameters.add(new SqlParameter(
                                "prmTHUEBAO_ID",
                                input.getPrmTHUEBAO_ID(),
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.INTEGER));

                parameters.add(new SqlParameter(
                                "prmNGUOI_CAPNHAT",
                                currentUser,
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.NVARCHAR));

                ArrayList<Map<String, Object>> rs = dbContext.executeSpWithCursorToListMap(
                                ConnectionManager.Connections.SECOND,
                                "TB_KLL.DANHSACH_TB_KHONG_OB_DEL",
                                parameters);
                if (rs != null && !rs.isEmpty()) {
                        if ("1".equals(String.valueOf(rs.get(0).get("item"))))
                                return Boolean.TRUE;
                }
                return Boolean.FALSE;
        }
}
