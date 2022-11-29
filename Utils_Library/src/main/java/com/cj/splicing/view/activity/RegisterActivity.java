package com.cj.splicing.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cj.splicing.R;
import com.cj.splicing.bmob.LoginModel;
import com.cj.splicing.bmob.PsdModel;
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
 * 用户注册
 * Created By CaiJing On 2022/11/25
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tv_code)
    TextView tv_code;
    @BindView(R.id.edt_phone)
    EditText edt_phone;
    @BindView(R.id.edt_code)
    EditText edt_code;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.btn_code)
    Button btn_code;
    @BindView(R.id.xy_box)
    CheckBox xy_box;
    @BindView(R.id.xy_layout)
    LinearLayout xy_layout;
    private Toast toast;

    @Override
    public int setColor() {
        return 0;
    }

    @Override
    public void initData() {
        darkImmerseFontColor();
        tv_code.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_code.getPaint().setAntiAlias(true);//抗锯齿
    }

    @Override
    public int addContentView() {
        return R.layout.activity_register;
    }

    @OnClick({R.id.btn_code,R.id.tv_code,R.id.btn_login,R.id.tv_user,R.id.tv_root})
    public void OnClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.btn_code://获取验证码
                String phone=edt_phone.getText().toString();
                if(phone.length()==0){
                    ToastUtil.Show("请输入手机号!");
                }
                else if(phone.length()!=11&&!AccountValidatorUtil.isMobile(phone)){
                    ToastUtil.Show("请输入正确手机号!");
                }
                else if(btn_code.getText().equals("获取验证码")||btn_code.getText().equals("重新获取")){
                    getCode(phone);
                    btn_code.setBackgroundColor(getColor(R.color.btn_code_false));
                    new CountDownTimer(60000,1000){
                        @Override
                        public void onTick(long l) {
                            btn_code.setText(l/1000+"s 后重试");
                        }
                        @Override
                        public void onFinish() {
                            btn_code.setText("重新获取");
                            btn_code.setBackgroundColor(getColor(R.color.btn_code_true));
                        }
                    }.start();
                }
                else {
                    if(toast!=null){
                        toast.cancel();
                        toast = Toast.makeText(this,"请勿重复获取",Toast.LENGTH_SHORT);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setText("请勿重复获取");
                        toast.show();
                    }
                    else {
                        toast = Toast.makeText(this,"请勿重复获取",Toast.LENGTH_SHORT);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.setText("请勿重复获取");
                        toast.show();
                    }
                }
                break;
            case R.id.tv_code://密码登录
                intent=new Intent(getApplicationContext(),LoginCodeActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login://登录
                if(Tools.isEmpty(edt_phone.getText().toString())||edt_phone.getText().toString().length()!=11){
                    ToastUtil.Show("请输入正确手机号!");
                    return;
                }
                if(Tools.isEmpty(edt_code.getText().toString())){
                    ToastUtil.Show("请输入验证码!");
                    return;
                }
                if(edt_code.getText().toString().length()!=6){
                    ToastUtil.Show("验证码位数错误!");
                    return;
                }
                if(Tools.isEmpty(edt_password.getText().toString())){
                    ToastUtil.Show("请输入密码!");
                    return;
                }
                if(edt_password.getText().toString().length()<6||edt_password.getText().toString().length()>12){
                    ToastUtil.Show("密码长度为6-12位!");
                    return;
                }
                if(!xy_box.isChecked()){
                    BeeAndVibrateManager.vibrate(getApplicationContext(),300);
                    AnimationUtils.AnimationTranslateStart(xy_layout,-30f,30f,0f,0f,150,false);
                    AnimationUtils.AnimationTranslateStart(xy_layout,-30f,30f,0f,0f,150,false);
                    return;
                }
                register(edt_phone.getText().toString(),edt_password.getText().toString());
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
     * 发送验证码
     */
    private void getCode(String phone){
        PsdModel.getLoginCode(phone);
    }

    /**
     * 登录
     */
    LoginModel model;
    private void register(String phone,String password){
        if(model==null){
            model=new LoginModel();
        }
        LoadingShow("Loading...");
        model.isExist(phone,password);
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