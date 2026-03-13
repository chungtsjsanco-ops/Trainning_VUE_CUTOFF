package com.example.backend.sample.controller;

import com.example.backend.common.ApiResult;
import com.example.backend.database.AppSqlException;
import com.example.backend.database.ConnectionManager;
import com.example.backend.sample.service.SampleSpService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sample")
public class SampleSpController {

    @Autowired
    SampleSpService sampleSpService;

    /**
     * NOTE (DEMO):
     * - API demo mới, không đè controller cũ.
     * - Luôn đọc dữ liệu từ DB SECOND (không cho chọn DB ở mức API).
     */

    @Operation(summary = "demo trả về dịch vụ viễn thông")
    @GetMapping("/dichvu_vt")
    public ResponseEntity<ApiResult> layDanhSach(
            // NOTE: truyền connection=DEFAULT hoặc SECOND để đọc dữ liệu từ DB tương ứng
            @RequestParam(value = "connection", required = false, defaultValue = "SECOND") ConnectionManager.Connections connection) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(sampleSpService.spDanhSachDichvu(connection));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "demo trả về loại hình viễn thông")
    @GetMapping("/loaihinh_tb")
    public ResponseEntity<ApiResult> layDanhSach(
            // tham số đầu vào
            @RequestParam(value = "loai", required = false) String loai,
            // connection DB
            @RequestParam(value = "connection", required = false, defaultValue = "SECOND") ConnectionManager.Connections connection) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(sampleSpService.spDanhSachloaihinh(loai, connection));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "demo trả về danh sách thuê bao")
    @GetMapping("/ds_thuebao")
    public ResponseEntity<ApiResult> layDanhSach_thuebao(
            // tham số đầu vào
            @RequestParam(value = "DICHVUVT_ID", required = false) String DICHVUVT_ID,
            @RequestParam(value = "LOAIHINH_TB", required = false) String LOAIHINH_TB,
            // connection DB
            @RequestParam(value = "connection", required = false, defaultValue = "SECOND") ConnectionManager.Connections connection) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(sampleSpService.spDanhSachthuebao(DICHVUVT_ID,LOAIHINH_TB, connection));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }
}
