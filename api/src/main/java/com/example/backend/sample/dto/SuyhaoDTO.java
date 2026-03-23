package com.example.backend.sample.dto;

import javax.validation.constraints.*;

import com.example.backend.common.ModelValidation;

import java.io.Serializable;

public class SuyhaoDTO implements Serializable {

    private String prmJsonData;

    @Size(max = 200, message = "Tên OLT không được để trống")
    private String prmSYSTEMNAME;
    
    @Size(max = 200, message = "Đơn vị không được để trống")
    private String prmDONVI;

    @Size(max = 200, message = "Tổ kỹ thuật không được để trống")
    private String prmTOKYTHUAT;


    public SuyhaoDTO(String prmSYSTEMNAME,String prmDONVI,String prmTOKYTHUAT) {
        this.prmSYSTEMNAME = prmSYSTEMNAME;
        this.prmDONVI= prmDONVI;
        this.prmTOKYTHUAT= prmTOKYTHUAT;
    }

    public String getSYSTEMNAME() {
        return prmSYSTEMNAME;
    }

    public void setPrmSYSTEMNAME(String prmSYSTEMNAME) {
        this.prmSYSTEMNAME = prmSYSTEMNAME;
    }

    public String getPrmDONVI() {
        return prmDONVI;
    }

    public void setPrmDONVI(String prmDONVI) {
        this.prmDONVI = prmDONVI;
    }

    public String getPrmTOKYTHUAT() {
        return prmTOKYTHUAT;
    }

    public void setPrmTOKYTHUAT(String prmTOKYTHUAT) {
        this.prmTOKYTHUAT = prmTOKYTHUAT;
    }
}
