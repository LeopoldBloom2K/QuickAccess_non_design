package com.example.myapp.model.data;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    // JSON의 "success" 키와 매핑
    @SerializedName("success")
    private boolean success;

    // JSON의 "message" 키와 매핑
    @SerializedName("message")
    private String message;

    // Getter & Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}