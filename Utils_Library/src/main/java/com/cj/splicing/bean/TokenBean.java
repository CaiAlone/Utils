package com.cj.splicing.bean;

import java.io.Serializable;

public class TokenBean implements Serializable {
    private int code;
    private String userId;
    private String token;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public  int getCode() {
        return code;
    }

    public  String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }
}
