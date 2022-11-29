package com.cj.splicing.bean;

import cn.bmob.v3.BmobObject;

/**
 * 反馈
 */
public class FeedBack extends BmobObject {
    private String UId;
    private String FeedBackClass;
    private String Message;
    private String PhotoA;
    private String PhotoB;
    private String PhotoC;

    public void setUId(String UId) {
        this.UId = UId;
    }

    public void setFeedBackClass(String feedBackClass) {
        FeedBackClass = feedBackClass;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setPhotoA(String photoA) {
        PhotoA = photoA;
    }

    public void setPhotoB(String photoB) {
        PhotoB = photoB;
    }

    public void setPhotoC(String photoC) {
        PhotoC = photoC;
    }

    public String getUId() {
        return UId;
    }

    public String getFeedBackClass() {
        return FeedBackClass;
    }

    public String getMessage() {
        return Message;
    }

    public String getPhotoA() {
        return PhotoA;
    }

    public String getPhotoB() {
        return PhotoB;
    }

    public String getPhotoC() {
        return PhotoC;
    }
}
