package com.cj.splicing.common.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.cj.splicing.utils.collector.ActivityCollector;
import com.kongzue.dialogx.DialogX;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import cn.bmob.v3.Bmob;

/**
 * 全局状态基类
 * Created By CaiJing On 2022/10/19
 */
public class AppContext extends Application {
    private static Context context;
    public static final String AppKey="";//融云 AppKey
    public static final String AppSecret="";//融云 AppSecret
    public static final String BmobAppKey="";//Bmob Appkey
    public static final String ACCESS_KEY="";//七牛云
    public static final String SECRET_KEY="";//七牛云
    public static final String PANGOLIN_APP_ID="";//穿山甲


    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        Bmob.resetDomain("");
        Bmob.initialize(this, BmobAppKey);
        DialogX.init(this);
    }

    /**
     * 获取 容器context
     * @return
     */
    public static Context getContext(){
        return context;
    }


    public static Activity getActivity(){
        return ActivityCollector.getActivity();
    }

    /**
     * 获取当前 Ip
     * @return
     */
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface.getNetworkInterfaces(); enNetI
                    .hasMoreElements();) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address &&!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}
