package com.example.backend.common;

import com.example.backend.database.AppSqlException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResult {

    private static final String CODE_SUCCESS = "00000000";
    private static final String CODE_ERROR = "99999999";

    private String error;

    @JsonProperty("error_code")
    private String errorCode = CODE_SUCCESS;

    private String message;

    @JsonProperty("message_detail")
    private String messageDetail;

    private Object data;

    @JsonProperty("request_id")
    private String requestId;

    public ApiResult() {
        this.message = "Thành công";
        this.requestId = java.util.UUID.randomUUID().toString();
    }

    public ApiResult(Object data) {
        this();
        this.data = data;
    }

    public void setException(AppSqlException e) {
        this.errorCode = CODE_ERROR;
        this.message = "Lỗi xử lý";
        this.messageDetail = e.getAppMessage();
    }

    @JsonIgnore
    public boolean isResultError() {
        return !CODE_SUCCESS.equals(this.errorCode);
    }

    @JsonIgnore
    public ResponseEntity<ApiResult> getResponseEntity() {
        if (!isResultError()) {
            this.error = String.valueOf(HttpStatus.OK.value());
            return ResponseEntity.ok().body(this);
        } else {
            this.error = String.valueOf(HttpStatus.BAD_REQUEST.value());
            return ResponseEntity.badRequest().body(this);
        }
    }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getMessageDetail() { return messageDetail; }
    public void setMessageDetail(String messageDetail) { this.messageDetail = messageDetail; }

    public Object getData() { return data; }
    public void setData(Object data) { this.data = data; }

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
}
