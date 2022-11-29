package com.cj.splicing.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;


import com.cj.splicing.utils.NotificationUtil;
import com.cj.splicing.utils.Tools;

import java.lang.reflect.Method;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.listener.OnReceiveMessageWrapperListener;
import io.rong.imlib.model.ReceivedProfile;
import io.rong.message.RecallNotificationMessage;

public class MessgeService extends Service {
    private static final Class[] mStartForegroundSignature = new Class[]{
            int.class, Notification.class};
    private static final Class[] mStopForegroundSignature = new Class[]{boolean.class};
    private NotificationManager mNM;
    private Method mStartForeground;
    private Method mStopForeground;
    private Object[] mStartForegroundArgs = new Object[2];
    private Object[] mStopForegroundArgs = new Object[1];
    private Uri sound;
    private Uri soundDefault;

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.e("服务", "服务StartCommand...");
        if(Tools.isEmpty(sharedPreferences.getString("Message_Count",""))){
            NotificationUtil.stopForegound(this);
        }
        else {
            NotificationUtil.startForeground(this, getPackageName(), "111", "消息", "您当前有未读消息,请点击查看...");
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("服务","服务Created...");
        new Thread(r).start();
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            while (true) {
                handler.sendEmptyMessage(0);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationUtil.stopForegound(this);
    }



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getUnreadMessage();
        }
    };

    /**
     * 读取未读消息的数量.
     */
    private void getUnreadMessage() {
        RongIMClient.addOnReceiveMessageListener(new OnReceiveMessageWrapperListener() {
            @Override
            public void onReceivedMessage(io.rong.imlib.model.Message message, ReceivedProfile profile) {
                if(message!=null){
                    Log.e("通知",message.getExtra()+"...");
                }
                Log.e("通知",message.getExtra()+"...");
            }
        });
        RongIMClient.setOnRecallMessageListener(listener);
    }


    /**
     * 融云消息监听
     */




    RongIMClient.OnRecallMessageListener listener=new RongIMClient.OnRecallMessageListener() {
        @Override
        public boolean onMessageRecalled(io.rong.imlib.model.Message message, RecallNotificationMessage recallNotificationMessage) {
            if(message!=null){
                Log.e("通知",message.getExtra()+"...");
                return true;
            }
            Log.e("通知",message.getExtra()+"...");
            return false;
        }
    };


    // IBinder是远程对象的基本接口，是为高性能而设计的轻量级远程调用机制的核心部分。但它不仅用于远程
    // 调用，也用于进程内调用。这个接口定义了与远程对象交互的协议。
    // 不要直接实现这个接口，而应该从Binder派生。
    // Binder类已实现了IBinder接口
    class MyBinder extends Binder {
        /** * 获取Service的方法 * @return 返回PlayerService */
        public MessgeService getService(){
            return MessgeService.this;
        }
    }

}