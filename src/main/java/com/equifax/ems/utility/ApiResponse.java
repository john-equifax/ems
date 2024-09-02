package com.equifax.ems.utility;

public class ApiResponse {
    private int status;
    private Object data;
    private String error;

    public ApiResponse(int status, Object data, String error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    // Getters and setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
