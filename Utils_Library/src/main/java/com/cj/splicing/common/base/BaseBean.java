package com.cj.splicing.common.base;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BaseBean<T> implements Serializable{

    private int code;
    private String msg;
    private String time;
    private String total;
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
    @SerializedName("data")
    private T data;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSuccess() {
        return code == 1;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //    public String getMsg() {
//        if (msg.equals ("登录过期")) {
//            Tools.setSharedPreferencesValues (AppContext.applicationContext, "token", "");
//            Tools.setSharedPreferencesValues (AppContext.applicationContext, "phone", "");
//            AppContext.applicationContext.startActivity (new Intent (AppContext.applicationContext, LoginActivity.class));
//            return msg;
//        } else {
//            return msg;
//        }
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
