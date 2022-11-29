package com.cj.splicing.bean;

import java.io.Serializable;

/**
 * 状态工具类
 */
public class StatusBean implements Serializable {
    private int code;
    private String status;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
