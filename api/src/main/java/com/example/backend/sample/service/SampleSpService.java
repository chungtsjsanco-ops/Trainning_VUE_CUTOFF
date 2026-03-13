package com.example.backend.sample.service;

import com.example.backend.database.AppSqlException;
import com.example.backend.database.ConnectionManager;
import com.example.backend.database.DbContext;
import com.example.backend.database.SqlParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class SampleSpService {

        @Autowired
        DbContext dbContext;

        public ArrayList<Map<String, Object>> spDanhSachDichvu(ConnectionManager.Connections connection)
                        throws AppSqlException {
                // Nếu caller truyền null thì vẫn ép về SECOND cho chắc chắn.
                if (connection == null) {
                        connection = ConnectionManager.Connections.SECOND;
                }
                ArrayList<SqlParameter> parameters = new ArrayList<>();
                parameters.add(new SqlParameter(
                                "p_cursor",
                                null,
                                SqlParameter.ParameterDirection.OUTPUT,
                                java.sql.Types.REF_CURSOR));

                // NOTE: đổi tên SP này theo đúng schema/package của bạn.
                return dbContext.executeSpWithCursorToListMap(
                                connection,
                                "DEMO_DICHVU_VT",
                                parameters);
        }

        public ArrayList<Map<String, Object>> spDanhSachloaihinh(String loai, ConnectionManager.Connections connection)
                        throws AppSqlException {

                if (connection == null) {
                        connection = ConnectionManager.Connections.SECOND;
                }

                ArrayList<SqlParameter> parameters = new ArrayList<>();

                // INPUT
                parameters.add(new SqlParameter(
                                "p_DICHVUVT_ID",
                                loai,
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.VARCHAR));

                // OUTPUT cursor
                parameters.add(new SqlParameter(
                                "p_cursor",
                                null,
                                SqlParameter.ParameterDirection.OUTPUT,
                                java.sql.Types.REF_CURSOR));

                return dbContext.executeSpWithCursorToListMap(
                                connection,
                                "DEMO_LOAIHINH_TB",
                                parameters);
        }

        public ArrayList<Map<String, Object>> spDanhSachthuebao(String DICHVUVT_ID, String LOAIHINH_TB,
                        ConnectionManager.Connections connection)
                        throws AppSqlException {

                if (connection == null) {
                        connection = ConnectionManager.Connections.SECOND;
                }

                ArrayList<SqlParameter> parameters = new ArrayList<>();

                // INPUT
                parameters.add(new SqlParameter(
                                "p_DICHVUVT_ID",
                                DICHVUVT_ID,
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.VARCHAR));
                parameters.add(new SqlParameter(
                                "p_LOAIHINH_TB",
                                LOAIHINH_TB,
                                SqlParameter.ParameterDirection.INPUT,
                                java.sql.Types.VARCHAR));
                // OUTPUT cursor
                parameters.add(new SqlParameter(
                                "p_cursor",
                                null,
                                SqlParameter.ParameterDirection.OUTPUT,
                                java.sql.Types.REF_CURSOR));

                return dbContext.executeSpWithCursorToListMap(
                                connection,
                                "DEMO_DANHSACH_TB",
                                parameters);
        }

}
