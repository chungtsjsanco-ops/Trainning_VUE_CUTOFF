package com.example.backend.sample.controller;

import com.example.backend.common.ApiResult;
import com.example.backend.database.AppSqlException;
import com.example.backend.sample.dto.SolieuKllDTO;
import com.example.backend.sample.service.THUEBAOKLLService;
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
@RequestMapping("/api/thuebao_kll")
public class THUEBAOKLLController {
    @Autowired
    THUEBAOKLLService thuebaokllSpService;

//  public ResponseEntity<ApiResult> LYDO_OB_VIEW(
//             @RequestParam(value = "connection", required = false, defaultValue = "SECOND") ConnectionManager.Connections connection) {

    @Operation(summary = "trả về lý do ko ob")
    @GetMapping("/LYDO_OB_VIEW")
    public ResponseEntity<ApiResult> LYDO_OB_VIEW() {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(thuebaokllSpService.LYDO_OB_VIEW());
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "trả về danh sách tb ko cần ob")
    @GetMapping("/DANHSACH_TB_KHONG_OB_VIEW")
    public ResponseEntity<ApiResult> DANHSACH_TB_KHONG_OB_VIEW() {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(thuebaokllSpService.DANHSACH_TB_KHONG_OB_VIEW());
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "tìm kiếm danh sách thuê bao")
    @GetMapping("/TIMKIEM_TB_KHONG_OB_VIEW")
    public ResponseEntity<ApiResult> layDanhSach_thuebao(@RequestParam(value = "prmTHONGTIN", required = false) String prmTHONGTIN) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(thuebaokllSpService.TIMKIEM_TB_KHONG_OB_VIEW(prmTHONGTIN));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "trả về ds thuê bao")
    @GetMapping("/SOLIEU_KLL_VIEW")
    public ResponseEntity<ApiResult> SOLIEU_KLL_VIEW(
            @RequestParam(value = "prmDK", required = false) Number prmDK,
            @RequestParam(value = "prmNGAY", required = false) String prmNGAY) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(thuebaokllSpService.SOLIEU_KLL_VIEW(prmDK, prmNGAY));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "trả về ds ob")
    @GetMapping("/SOLIEU_KLL_VIEW_OB")
    public ResponseEntity<ApiResult> SOLIEU_KLL_VIEW_OB(
            @RequestParam(value = "prmNGAY", required = false) String prmNGAY,
            @RequestParam(value = "prmNGUOI_CAPNHAT", required = false) String prmNGUOI_CAPNHAT) {
        ApiResult rs = new ApiResult();
        try {
            rs.setData(thuebaokllSpService.SOLIEU_KLL_VIEW_OB(prmNGAY, prmNGUOI_CAPNHAT));
        } catch (AppSqlException e) {
            rs.setException(e);
        }
        return rs.getResponseEntity();
    }

    @Operation(summary = "Ins tb kll")
    @PostMapping("/SOLIEU_TB_KLL_2_INS")
    public ResponseEntity<ApiResult> SOLIEU_TB_KLL_2_INS(@RequestBody SolieuKllDTO dto) {

        ApiResult rs = new ApiResult();

        try {
            rs.setData(thuebaokllSpService.SOLIEU_TB_KLL_2_INS(dto));
        } catch (AppSqlException e) {
            rs.setException(e);
        }

        return rs.getResponseEntity();
    }

    @Operation(summary = "Ins tb kll ko ob")
    @PostMapping("/DANHSACH_TB_KHONG_OB_INS")
    public ResponseEntity<ApiResult> DANHSACH_TB_KHONG_OB_INS(@RequestBody SolieuKllDTO dto) {

        ApiResult rs = new ApiResult();

        try {
            rs.setData(thuebaokllSpService.DANHSACH_TB_KHONG_OB_INS(dto));
        } catch (AppSqlException e) {
            rs.setException(e);
        }

        return rs.getResponseEntity();
    }

    @Operation(summary = "del tb kll ob")
    @PostMapping("/DANHSACH_TB_KHONG_OB_DEL")
    public ResponseEntity<ApiResult> DANHSACH_TB_KHONG_OB_DEL(@RequestBody SolieuKllDTO dto) {

        ApiResult rs = new ApiResult();

        try {
            rs.setData(thuebaokllSpService.DANHSACH_TB_KHONG_OB_DEL(dto));
        } catch (AppSqlException e) {
            rs.setException(e);
        }

        return rs.getResponseEntity();
    }

}
