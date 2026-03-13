package com.example.backend.common;

public class ResultOutput {
    private Object result = null;

    public ResultOutput() {}

    public ResultOutput(Object result) {
        this.setResult(result);
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
