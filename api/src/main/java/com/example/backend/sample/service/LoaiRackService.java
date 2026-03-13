package com.example.backend.sample.service;

import com.example.backend.common.ApiResult;
import com.example.backend.database.AppSqlException;
import com.example.backend.database.ConnectionManager;
import com.example.backend.database.DbContext;
import com.example.backend.database.SqlParameter;
import com.example.backend.sample.dto.LoaiRackDTO;
import com.example.backend.sample.dto.SolieuKllDTO;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;

@Service
public class LoaiRackService {

    @Autowired
    DbContext dbContext;

    public ArrayList<Map<String, Object>> layDsLoaiRack() throws AppSqlException {
        // NOTE: giữ lại hàm cũ để các chỗ đang dùng không cần sửa.
        // Mặc định sẽ chạy trên connection DEFAULT.
        return layDsLoaiRack(ConnectionManager.Connections.DEFAULT);
    }

    public ArrayList<Map<String, Object>> layDsLoaiRack(ConnectionManager.Connections connection) throws AppSqlException {
        // NOTE: ví dụ gọi SP trả về REF_CURSOR
        ArrayList<SqlParameter> parameters = new ArrayList<>();
        parameters.add(new SqlParameter("p_cursor", null, SqlParameter.ParameterDirection.OUTPUT, java.sql.Types.REF_CURSOR));
        return dbContext.executeSpWithCursorToListMap(connection, "ECMS.PKG_IDC_TAINGUYEN.SP_DANHSACH_LOAI_RACK", parameters);
    }

    public Boolean updateInsertLoaiRack(LoaiRackDTO input) throws AppSqlException {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        ArrayList<SqlParameter> parameters = new ArrayList<>();
        parameters.add(new SqlParameter("p_id", input.getpId(), SqlParameter.ParameterDirection.INPUT, Types.NUMERIC));
        parameters.add(new SqlParameter("p_ten", input.getpTen(), SqlParameter.ParameterDirection.INPUT, Types.NVARCHAR));
        parameters.add(new SqlParameter("p_so_u", input.getpSoU(), SqlParameter.ParameterDirection.INPUT, Types.NUMERIC));
        parameters.add(new SqlParameter("p_dai", input.getpDai(), SqlParameter.ParameterDirection.INPUT, Types.NUMERIC));
        parameters.add(new SqlParameter("p_rong", input.getpRong(), SqlParameter.ParameterDirection.INPUT, Types.NUMERIC));
        parameters.add(new SqlParameter("p_cao", input.getpCao(), SqlParameter.ParameterDirection.INPUT, Types.NUMERIC));
        parameters.add(new SqlParameter("p_ghi_chu", input.getpGhiChu(), SqlParameter.ParameterDirection.INPUT, Types.NVARCHAR));
        parameters.add(new SqlParameter("p_ten_tat", input.getpTenTat(), SqlParameter.ParameterDirection.INPUT, Types.NVARCHAR));
        parameters.add(new SqlParameter("p_mo_ta", input.getpMoTa(), SqlParameter.ParameterDirection.INPUT, Types.NVARCHAR));
        parameters.add(new SqlParameter("p_su_dung", input.getpSuDung(), SqlParameter.ParameterDirection.INPUT, Types.NUMERIC));
        parameters.add(new SqlParameter("p_nguoi_cn", currentUser, SqlParameter.ParameterDirection.INPUT, Types.NVARCHAR));
        parameters.add(new SqlParameter("p_mamau", input.getpMaMau(), SqlParameter.ParameterDirection.INPUT, Types.NVARCHAR));
        parameters.add(new SqlParameter("p_result", null, SqlParameter.ParameterDirection.OUTPUT, Types.NUMERIC));
        ArrayList<Map<String, Object>> rs = dbContext.executeSpWithCursorToListMap("ECMS.PKG_IDC_TAINGUYEN.SP_LOAI_RACK_UPDATE_INSERT", parameters);
        if (rs != null && !rs.isEmpty()) {
            if ("1".equals(String.valueOf(rs.get(0).get("item"))))
                return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }    
}
