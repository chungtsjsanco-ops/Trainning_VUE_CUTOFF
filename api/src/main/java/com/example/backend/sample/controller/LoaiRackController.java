package com.example.backend.sample.controller;

import com.example.backend.common.ApiResult;
import com.example.backend.common.ResultOutput;
import com.example.backend.database.AppSqlException;
import com.example.backend.database.ConnectionManager;
import com.example.backend.sample.dto.LoaiRackDTO;
import com.example.backend.sample.service.LoaiRackService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/loai-rack")
public class LoaiRackController {

    @Autowired
    LoaiRackService loaiRackService;

    @Operation(summary = "Lấy danh sách loại rack")
    @PostMapping("/lay-danhsach")
    public ResponseEntity<ApiResult> layDanhSach(
            // NOTE: truyền connection=DEFAULT hoặc SECOND để đọc dữ liệu từ DB tương ứng
            @RequestParam(value = "connection", required = false, defaultValue = "DEFAULT")
            ConnectionManager.Connections connection
    ) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(loaiRackService.layDsLoaiRack(connection));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "Thêm mới hoặc cập nhật loại rack")
    @PostMapping("/capnhat")
    public ResponseEntity<ApiResult> capNhat(@Valid @RequestBody LoaiRackDTO input) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(new ResultOutput(loaiRackService.updateInsertLoaiRack(input)));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }
}
