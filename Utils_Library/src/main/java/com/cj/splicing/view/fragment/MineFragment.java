package com.cj.splicing.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cj.splicing.R;
import com.cj.splicing.bmob.UserModel;
import com.cj.splicing.common.base.BaseFragment;
import com.cj.splicing.interfaces.Request;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.toast.ToastUtil;
import com.cj.splicing.utils.widget.RoundImageView;
import com.cj.splicing.view.activity.AboutActivity;
import com.cj.splicing.view.activity.FeedBackActivity;
import com.cj.splicing.view.activity.InformationActivity;
import com.cj.splicing.view.activity.LoginCodeActivity;
import com.cj.splicing.view.activity.SetActivity;
import com.kongzue.dialogx.dialogs.MessageDialog;
import com.kongzue.dialogx.interfaces.OnDialogButtonClickListener;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

/**
 * 我的
 * Created By CaiJing On 2022/11/24
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.iv_icon)
    RoundImageView iv_icon;
    @BindView(R.id.iv_name)
    TextView iv_name;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initViews();
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refresh.finishRefresh(3000);
                getUserInfo();
            }
        });
    }


    MessageDialog messageDialog;
    @OnClick({R.id.tv_set,R.id.tv_feed,R.id.tv_about,R.id.tv_logout,R.id.tv_zx,R.id.iv_icon})
    public void OnClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.tv_set://设置
                intent=new Intent(getContext(), SetActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_feed://意见反馈
                intent=new Intent(getContext(), FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_about://版本信息
                intent=new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_logout://退出登录
                Tools.setSharedPreferencesValues(getContext(),"isLogin",false);
                intent=new Intent(getContext(), LoginCodeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_zx://注销账号
                messageDialog= MessageDialog.show("提示","当前正在进行账号注销，账号注销后将在180天内无法重新注册。\n如您确认请点击确认按钮。","确认","取消");
                messageDialog.setCancelable(false);
                messageDialog.setOkButton(new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog dialog, View v) {
                        editInfo("status","false");
                        dialog.dismiss();
                        return false;
                    }
                });
                messageDialog.setCancelButton(new OnDialogButtonClickListener<MessageDialog>() {
                    @Override
                    public boolean onClick(MessageDialog dialog, View v) {
                        dialog.dismiss();
                        return false;
                    }
                });
                break;
            case R.id.iv_icon://个人资料
                intent=new Intent(getContext(), InformationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    /**
     * 控件初始化
     */
    private void initViews(){
        Glide.with(getContext()).load(Tools.getSharedPreferencesValues(getContext(),"Icon","")).error(R.mipmap.app_icon).into(iv_icon);
        iv_name.setText(Tools.getSharedPreferencesValues(getContext(),"UserName","一个人"));
    }

    /**
     * 获取用户数据
     */
    UserModel model;
    private void getUserInfo(){
        model=new UserModel();
        model.getUserInfo();
        model.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                    initViews();
                }
            }
        });
    }


    /**
     * 修改资料
     */
    UserModel model_edit;
    private void editInfo(String key,String value){
        model_edit=new UserModel();
        model_edit.editUserInfo(key,value);
        LoadingShow("正在注销...");
        model_edit.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                    ToastUtil.Show("注销成功!");
                    RongIM.getInstance().disconnect();
                    Tools.setSharedPreferencesValues(getContext(),"isLogin",false);
                    startActivity(new Intent(getContext(), LoginCodeActivity.class));
                }
                LoadingStop();
            }
        });
    }
}