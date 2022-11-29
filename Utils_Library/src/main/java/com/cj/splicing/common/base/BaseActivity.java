package com.cj.splicing.common.base;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.collector.ActivityCollector;
import com.cj.splicing.utils.widget.LoadDialogUtils;
import com.gyf.barlibrary.ImmersionBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import io.rong.imkit.GlideKitImageEngine;
import io.rong.imkit.RongIM;
import io.rong.imkit.config.RongConfigCenter;
import io.rong.imkit.userinfo.RongUserInfoManager;
import io.rong.imkit.userinfo.UserDataProvider;
import io.rong.imkit.utils.SystemBarTintManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


/**
 * 描述：Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected ImmersionBar mImmersionBar;
    protected String ACCESS_KEY ;
    protected String SECRET_KEY ;
    //Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //要上传的空间
    String bucketname = "cj-images";
    //上传到七牛后保存的文件名
    String key;
    String Icon_Uri;
    Dialog loadingdialog;


    /**
     * 沉浸式状态栏
     */
    public void checkStatus(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }



    /*
     * 沉浸式布局
     */
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    /**
     * 加载
     *
     * @param msg
     */
    public void LoadingShow(String msg) {
        loadingdialog = LoadDialogUtils.createLoadingDialog(this, msg);
        loadingdialog.show();
    }

    /**
     * 停止加载
     */
    public void LoadingStop() {
        if (loadingdialog != null) {
            LoadDialogUtils.closeDialog(loadingdialog);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        return;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            finish();
            return;
        }
        ActivityCollector.addActivity(this);
        setContentView(addContentView());
        initData();
//        setOnListener(add);

        int color = setColor();//获取状态栏颜色的方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(color);//状态栏所需颜色
        }
    }


    public abstract int setColor();

    /**
     * 初始化
     */
    public abstract void initData();

    /**
     * 设置容器
     */
    public abstract int addContentView();

    /**
     * 使用频率高 一般用于Activity初始化界面
     * 例如 onCreate()里。初始化布局就用到setContentView(R.layout.xxxx) 就是初始化页面布局
     */
    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        initConfig();
       // MyTask myTask=new MyTask();
        //myTask.execute("111","111","222");
    }

    /**
     * 这个一般用于加载自定义控件，或者系统空间的布局
     */
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initConfig();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initConfig();

    }

    /**
     * 初始化数据
     */
    private void initConfig() {
        //Butter Knife初始化
        ButterKnife.bind(this);
        /**
         *   须沉浸式时，若状态栏背景为标题栏相同颜色，
         *   使用标题栏paddingTop 25 达到效果
         */
        if (isImmerseBar()) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.init();
        }
        initPhotoError();

    }


    /**
     * 检验存储权限
     */
    public boolean isCheckSelfPermission_STORAGE() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检验相机存储权限
     */
    public boolean isCheckSelfPermission_CAMERA() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.CAMERA
            }, 1);
            return false;
        } else {
            return true;
        }
    }


    //打开 关闭沉浸式的方法  默认打开
    public boolean isImmerseBar() {
        return true;
    }

    //状态栏字体颜色为暗灰色
    public void darkImmerseFontColor() {
        if (mImmersionBar != null) {
            //mImmersionBar.statusBarDarkFont(true).init();
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
        }
    }

    /**
     * 全屏显示
     * @param t true 隐藏
     */
    public void isCheckTitle(boolean t){
        if(t){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //eventbus注销
        if (mImmersionBar != null) {
            mImmersionBar.destroy();  //销毁
        }
        ActivityCollector.removeActivity(this);
        Log.d("tag", "onDestroy" + this.getClass().toString());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideSoftInput();
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        // 调用隐藏系统默认的输入法  隐藏键盘
        if (getCurrentFocus() != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 展示软键盘
     *
     * @param view
     */
    protected void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (!imm.isActive()){
//            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
//        }
        imm.showSoftInput(view, 0);
    }


    /**
     * 判断软键盘是否弹出
     * @return
     */
    public boolean isSoftShowing() {
        // 获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        // 获取View可见区域的bottom
        Rect rect = new Rect();
        // DecorView即为activity的顶级view
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        // 考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        // 选取screenHeight*2/3进行判断
        closeBoard(this);
        return screenHeight*2/3 > rect.bottom;
    }

    /**
     * 隐藏软键盘
     * @param context
     */
    public void closeBoard(Context context){
        InputMethodManager imm =(InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);

    }



    public boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;

            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * android 7.0系统解决拍照的问题
     */
    private void initPhotoError() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }


    /**
     * 融云初始化
     */
    private int page=0;
    SharedPreferences sharedPreferences;
    public void init_rong(){
        RongIM.getInstance().disconnect();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        RongIM.init(getApplication(), AppContext.AppKey,true);
        RongConfigCenter.featureConfig().setKitImageEngine(new GlideKitImageEngine(){
            @Override
            public void loadConversationPortrait(@NonNull Context context, @NonNull String url, @NonNull ImageView imageView, io.rong.imlib.model.Message message) {
                super.loadConversationPortrait(context, url, imageView, message);
                Glide.with(imageView).load(url)
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(imageView);
            }
        });

        UserInfo userInfo=new UserInfo(Tools.getSharedPreferencesValues(getApplicationContext(),"UId",""),
                Tools.getSharedPreferencesValues(getApplicationContext(),"UserName",""),
                Uri.parse(Tools.getSharedPreferencesValues(getApplicationContext(),"Icon","")));
        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.setUserInfoProvider(new UserDataProvider.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userId) {
                return userInfo;
            }
        }, true);//读取用户信息
        RongIM.getInstance().setMessageAttachedUserInfo(true);//获取信息

        if(Tools.getSharedPreferencesValues(getApplicationContext(),"UId","").contains("admin")){
            Tools.setSharedPreferencesValues(getApplicationContext(),"R_Token","pZN5CK+CtmgSc68+5IHo3eYTaHLicH6H+J4Rp617IdQ=@an1m.cn.rongnav.com;an1m.cn.rongcfg.com");
        }
        UserInfo userInfo1 = new UserInfo(Tools.getSharedPreferencesValues(getApplicationContext(),"UId",""), Tools.getSharedPreferencesValues(getApplicationContext(),"UserName",""),Uri.parse(Tools.getSharedPreferencesValues(getApplicationContext(),"Icon","")));
        RongUserInfoManager.getInstance().refreshUserInfoCache(userInfo1);


        RongIM.connect(sharedPreferences.getString("R_Token",""), new RongIMClient.ConnectCallback() {
            @Override
            public void onSuccess(String t) {
               // Toast.makeText(getApplicationContext(), "融云初始化成功!", Toast.LENGTH_SHORT).show();

                if(page==0) {
                    Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE; //此处以单聊会话为例
                    RongIM.getInstance().clearConversations(null,conversationType);
                    RongIM.getInstance().clearMessages(conversationType,"admin",null);
                    page++;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
                    String targetId = "admin";
                    Long recordTime =Long.valueOf(simpleDateFormat.format(new Date()).toString());
                    RongIMClient.getInstance().cleanRemoteHistoryMessages(conversationType, targetId,  recordTime, new RongIMClient.OperationCallback() {
                        @Override
                        public void onSuccess() {
                            page++;
                        }
                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                        }
                    });
                    return;
                }
                Log.i("登录界面日志","融云初始化成功!");
            }
            @Override
            public void onError(RongIMClient.ConnectionErrorCode e) {
                Log.i("登录界面日志","融云连接异常!");
            }

            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {
            }
        });
    }


}