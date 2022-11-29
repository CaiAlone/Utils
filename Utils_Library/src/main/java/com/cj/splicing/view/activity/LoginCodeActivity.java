package com.cj.splicing.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cj.splicing.R;
import com.cj.splicing.bmob.BaseModel;
import com.cj.splicing.bmob.LoginModel;
import com.cj.splicing.common.base.BaseActivity;
import com.cj.splicing.interfaces.Request;
import com.cj.splicing.utils.AccountValidatorUtil;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.anim.AnimationUtils;
import com.cj.splicing.utils.api.BeeAndVibrateManager;
import com.cj.splicing.utils.toast.ToastUtil;
import com.cj.splicing.view.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 密码登录
 * Created By CaiJing On 2022/11/23
 */
public class LoginCodeActivity extends BaseActivity {
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_phone)
    EditText edt_phone;
    @BindView(R.id.xy_box)
    CheckBox xy_box;
    @BindView(R.id.xy_layout)
    LinearLayout xy_layout;
    @BindView(R.id.tv_register)
    TextView tv_register;

    @Override
    public int setColor() {
        return 0;
    }

    @Override
    public void initData() {
        tv_register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_register.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    public int addContentView() {
        return R.layout.activity_login_code;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(Tools.getSharedPreferencesValues(getApplicationContext(),"isLogin",false)==true){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    @OnClick({R.id.btn_login,R.id.tv_user,R.id.tv_root,R.id.tv_register})
    public void OnClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.tv_register://注册
                intent=new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login://登录
                String phone=edt_phone.getText().toString();
                String password=edt_password.getText().toString();
                if(Tools.isEmpty(phone) ||phone.length()==0){
                    ToastUtil.Show("请输入手机号!");
                    return;
                }
                else if(phone.length()!=11&& !AccountValidatorUtil.isMobile(phone)){
                    ToastUtil.Show("请输入正确手机号!");
                    return;
                }
                else if(Tools.isEmpty(password)){
                    ToastUtil.Show("请输入密码!");
                    return;
                }
                else if(password.length()<6){
                    ToastUtil.Show("密码不完整!");
                    return;
                }
                if(!xy_box.isChecked()){
                    BeeAndVibrateManager.vibrate(getApplicationContext(),300);
                    AnimationUtils.AnimationTranslateStart(xy_layout,-30f,30f,0f,0f,150,false);
                    AnimationUtils.AnimationTranslateStart(xy_layout,-30f,30f,0f,0f,150,false);
                    return;
                }
                login(phone,password);
                break;
            case R.id.tv_user://用户协议
                startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("url", "file:///android_asset/用户协议.html"));
                break;
            case R.id.tv_root://隐私政策
                startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("url", "file:///android_asset/隐私政策.html"));
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     * @param phone 17783323127
     * @param password 123456
     */
    LoginModel model;
    private void login(String phone,String password){
        if(model==null) {
            model = new LoginModel();
        }
        LoadingShow("登陆中...");
        model.isLogin(phone,password);
        model.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                    Tools.setSharedPreferencesValues(getApplicationContext(),"isLogin",true);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                LoadingStop();
            }
        });
    }
}