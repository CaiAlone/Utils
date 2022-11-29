package com.cj.splicing.bmob;


import com.cj.splicing.interfaces.Request;
import com.cj.splicing.interfaces.RequestData;

public class BaseModel {
    public Request request;
    public void setRequest(Request request1){
        request=request1;
    }


    public RequestData requestData;
    public void setRequestData(RequestData requestData1){
        requestData=requestData1;
    }
}
