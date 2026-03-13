package com.example.backend.sample.dto;

import javax.validation.constraints.*;

import com.example.backend.common.ModelValidation;

import java.io.Serializable;

public class SolieuKllDTO implements Serializable {

    private String prmJsonData;

    @NotEmpty(message = "Người cập nhật không được để trống")
    @Size(max = 50, message = "Người cập nhật không được vượt quá 50 ký tự")
    private String prmNGUOI_CAPNHAT;

    @PositiveOrZero(message = ModelValidation.POSITIVE_OR_ZERO)
    private Long prmTHUEBAO_ID;
    
    public SolieuKllDTO() {
    }

    // Case 1: JSON data
    public SolieuKllDTO(String prmJsonData, String prmNGUOI_CAPNHAT) {
        this.prmJsonData = prmJsonData;
        this.prmNGUOI_CAPNHAT = prmNGUOI_CAPNHAT;
    }

    // Case 2: update theo THUEBAO_ID
    public SolieuKllDTO(Long prmTHUEBAO_ID, String prmNGUOI_CAPNHAT) {
        this.prmTHUEBAO_ID = prmTHUEBAO_ID;
        this.prmNGUOI_CAPNHAT = prmNGUOI_CAPNHAT;
    }

    public String getPrmJsonData() {
        return prmJsonData;
    }

    public void setPrmJsonData(String prmJsonData) {
        this.prmJsonData = prmJsonData;
    }

    public String getPrmNGUOI_CAPNHAT() {
        return prmNGUOI_CAPNHAT;
    }

    public void setPrmNGUOI_CAPNHAT(String prmNGUOI_CAPNHAT) {
        this.prmNGUOI_CAPNHAT = prmNGUOI_CAPNHAT;
    }

    public Long getPrmTHUEBAO_ID() {
        return prmTHUEBAO_ID;
    }

    public void setPrmTHUEBAO_ID(Long prmTHUEBAO_ID) {
        this.prmTHUEBAO_ID = prmTHUEBAO_ID;
    }
}
