package com.cj.splicing.common.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cj.splicing.view.adapter.Actvity_Image_Adapter;
import com.gyf.barlibrary.ImmersionBar;
import com.qiniu.util.Auth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 描述：fragment 基类
 * 作者：李昌骏 on 2018\08\23 0007 14:57
 * 电话：13881771371
 */
public abstract class BaseFragment extends Fragment {
    public abstract int getContentViewId();

    protected Context context;
    protected View mRootView;
    protected ImmersionBar mImmersionBar;
    private Unbinder unbinder;
    Dialog loadingdialog;
    private String birthday;
    private boolean GPS_FIRST_FIX;
    private Double log,lat;
    protected String ACCESS_KEY= AppContext.ACCESS_KEY;
    protected String SECRET_KEY=AppContext.SECRET_KEY;
    Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
    //要上传的空间
    String bucketname = "cj-images";
    //上传到七牛后保存的文件名
    String key = getKey();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate (getContentViewId (), container, false);
        unbinder = ButterKnife.bind (this, mRootView);
        //绑定framgent
        this.context = getActivity ();
        initAllMembersView (savedInstanceState);
        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initConfig();
    }

    protected abstract void initAllMembersView(Bundle savedInstanceState);


    /**
     * 初始化数据
     */
    private void initConfig() {
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

    //打开 关闭沉浸式的方法  默认打开
    public boolean isImmerseBar() {
        return true;
    }

    //状态栏字体颜色为暗灰色
    public void darkImmerseFontColor() {
        if (mImmersionBar != null) {
            mImmersionBar.statusBarDarkFont(true, 0.2f).init();
        }
    }

    /**
     * android 7.0系统解决拍照的问题
     */
    private void initPhotoError(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();//解绑
    }

    @Override
    public void onDestroy() {
        super.onDestroy ();
    }

    @Override
    public void onStop() {
        super.onStop();
        LoadingStop();
    }

    /**
     * 加载
     * @param msg
     */
    public void LoadingShow(String msg){

    }

    /**
     * 停止加载
     */
    public void LoadingStop(){

    }


    /**fragment给activity回传值的接口**/
    public interface OnFragmentListener{
        /**object需要实现Serializable或Parcelable接口**/
        void onFragment(int position, Object img);
    }



    public OnFragmentListener onFragmentListener;

    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    /**
     * 七牛上传文件名
     *
     * @return
     */
    private String getKey() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String key = simpleDateFormat.format(new Date()).toString();
        return key + ".png";
    }


    /**
     * 沉浸式状态栏
     */
    public void checkStatus(){
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }


    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput() {
        // 调用隐藏系统默认的输入法  隐藏键盘
        if (getActivity().getCurrentFocus() != null) {
            ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 展示软键盘
     *
     * @param view
     */
    protected void showSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (!imm.isActive()){
//            imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
//        }
        imm.showSoftInput(view, 0);
    }



}
