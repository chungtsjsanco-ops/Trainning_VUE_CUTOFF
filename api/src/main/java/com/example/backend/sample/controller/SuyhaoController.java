package com.example.backend.sample.controller;

import com.example.backend.common.ApiResult;
import com.example.backend.database.AppSqlException;
import com.example.backend.sample.dto.SolieuKllDTO;
import com.example.backend.sample.service.SuyhaoService;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/suyhao")
public class SuyhaoController {
    @Autowired
    SuyhaoService suyhaoService;

    @Operation(summary = "trả về danh sách suy hao chi tiết")
    @GetMapping("/SOLIEU_SUYHAO_CTS_VIEWNGAY")
    public ResponseEntity<ApiResult> suyhao_chitiet(@RequestParam(value = "prmNGAY", required = false) String prmNGAY) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(suyhaoService.SOLIEU_SUYHAO_CTS_VIEWNGAY(prmNGAY));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "trả về danh sách suy hao tổng hợp theo olt")
    @GetMapping("/SOLIEU_SUYHAO_OLT_VIEWNGAY")
    public ResponseEntity<ApiResult> suyhao_olt(@RequestParam(value = "prmNGAY", required = false) String prmNGAY) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(suyhaoService.SOLIEU_SUYHAO_OLT_VIEWNGAY(prmNGAY));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "trả về Điểm tin tổng hợp")
    @GetMapping("/SOLIEU_SUYHAO_OLT_TONGHOP_NGAY")
    public ResponseEntity<ApiResult> tonghop(@RequestParam(value = "prmNGAY", required = false) String prmNGAY) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(suyhaoService.SOLIEU_SUYHAO_OLT_TONGHOP_NGAY(prmNGAY));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "trả về Điểm tin theo khoảng thời gian")
    @GetMapping("/SOLIEU_SUYHAO_OLT_TONGHOP_TUNGAY_DENNGAY")
    public ResponseEntity<ApiResult> tonghop_tungay_denngay(
            @RequestParam(value = "prmTUNGAY", required = false) String prmTUNGAY,
            @RequestParam(value = "prmDENNGAY", required = false) String prmDENNGAY) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(suyhaoService.SOLIEU_SUYHAO_OLT_TONGHOP_TUNGAY_DENNGAY(prmTUNGAY, prmDENNGAY));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

}
