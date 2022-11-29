package com.cj.splicing.bean;
import cn.bmob.v3.BmobObject;

/**
 * 用户表实体
 * Created By CaiJing On 2022/11/7
 */
public class User extends BmobObject {
    private String UId;
    private String UserName;
    private String Phone;
    private String Icon;
    private String Sign;
    private String Password;
    private String R_Token;
    private boolean Status;
    private int Sex;
    private String Age;
    private String City;

    public void setCity(String city) {
        City = city;
    }

    public String getCity() {
        return City;
    }


    public void setSex(int sex) {
        Sex = sex;
    }

    public void setAge(String age) {
        Age = age;
    }

    public int getSex() {
        return Sex;
    }

    public String getAge() {
        return Age;
    }

    public boolean getStatus() {
        return Status;
    }
    public void setStatus(boolean status) {
        Status = status;
    }


    public String getIcon() {
        return Icon;
    }

    public String getUserName() {
        return UserName;
    }

    public String getPassword() {
        return Password;
    }

    public String getPhone() {
        return Phone;
    }

    public String getSign() {
        return Sign;
    }

    public String getUId() {
        return UId;
    }

    public void setIcon(String icon) {
        Icon = icon;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public void setUId(String UId) {
        this.UId = UId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setR_Token(String r_Token) {
        R_Token = r_Token;
    }

    public String getR_Token() {
        return R_Token;
    }
}

