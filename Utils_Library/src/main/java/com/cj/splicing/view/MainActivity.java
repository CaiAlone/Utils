package com.cj.splicing.view;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.cj.splicing.R;
import com.cj.splicing.bmob.LoginModel;
import com.cj.splicing.bmob.UserModel;
import com.cj.splicing.common.base.BaseActivity;
import com.cj.splicing.interfaces.Request;
import com.cj.splicing.utils.AccountValidatorUtil;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.anim.AnimationUtils;
import com.cj.splicing.utils.api.BeeAndVibrateManager;
import com.cj.splicing.utils.collector.ActivityCollector;
import com.cj.splicing.utils.toast.ToastUtil;
import com.cj.splicing.utils.widget.RoundImageView;
import com.cj.splicing.view.activity.LoginCodeActivity;
import com.cj.splicing.view.activity.RegisterActivity;
import com.cj.splicing.view.activity.WebViewActivity;
import com.cj.splicing.view.fragment.FirstFragment;
import com.cj.splicing.view.fragment.MineFragment;
import com.kongzue.dialogx.dialogs.FullScreenDialog;
import com.kongzue.dialogx.interfaces.OnBindView;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * 主页
 * Created By CaiJing On 2022/11/24
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.layout_main)
    LinearLayout layout_main;
    @BindView(R.id.rb_1)
    ImageView rb_1;
    @BindView(R.id.rb_2)
    ImageView rb_2;
    UserModel model;


    private int lastfragment;
    private Fragment[] fragments;
    private FirstFragment firstFragment;
    private MineFragment mineFragment;

    @Override
    public int setColor() {
        return R.color.top;
    }

    @Override
    public void initData() {
        init_fragment();
        model=new UserModel();
        model.getUserInfo();
        model.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                    init_rong();
                    setIMStatusListener();
                }
            }
        });
    }

    @Override
    public int addContentView() {
        return R.layout.activity_main;
    }


    /**
     * 初始化 Fragment
     */
    private void init_fragment() {
        firstFragment = new FirstFragment();
        mineFragment=new MineFragment();
        fragments = new Fragment[]{
                firstFragment,mineFragment
        };
        lastfragment = 0;
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_main, firstFragment).show(firstFragment).commit();

        rb_1.setOnClickListener(view -> {
            deIcon();
            rb_1.setImageResource(R.mipmap.icon_shouye_true);
            switchFragment(lastfragment,0);
            lastfragment=0;
        });
        rb_2.setOnClickListener(view -> {
            deIcon();
            rb_2.setImageResource(R.mipmap.icon_wode_true);
            switchFragment(lastfragment,1);
            lastfragment=1;
        });

    }

    private void deIcon(){
        rb_1.setImageResource(R.mipmap.icon_shouye_false);
        rb_2.setImageResource(R.mipmap.icon_wode_false);
    }

    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.hide(fragments[lastfragment]);
        if (fragments[index].isAdded() == false) {
            fragmentTransaction.add(R.id.layout_main, fragments[index]);
        }
        fragmentTransaction.show(fragments[index]).commitAllowingStateLoss();
    }

    private long lastTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-lastTime)>2000){
                //Toast.makeText(MainActivity.this, "在按一次退出程序", Toast.LENGTH_SHORT).show();
                ActivityCollector.finishAll();
                lastTime=System.currentTimeMillis();
            }else {
                System.exit(0);
            }
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setIMStatusListener() {
        RongIM.setConnectionStatusListener(connectionStatusListener);
    }
    private RongIMClient.ConnectionStatusListener connectionStatusListener = new RongIMClient.ConnectionStatusListener() {
        @Override
        public void onChanged(ConnectionStatus status) {
            //开发者需要根据连接状态码，进行不同业务处理
            if(status.getValue()==3){
                //该账号在其他地方登录
                ToastUtil.Show("当前设备已在其他地方登录,请重新登录!");
                RongIM.getInstance().disconnect();
                Tools.setSharedPreferencesValues(getApplicationContext(),"isLogin",false);
                startActivity(new Intent(MainActivity.this, LoginCodeActivity.class));
                finish();
            }
        }
    };

    FullScreenDialog fullScreenDialog;
    @Override
    protected void onResume() {
        super.onResume();
        if(!Tools.getSharedPreferencesValues(getApplicationContext(),"isLogin",false)){
            //未登录
            FullScreenDialog.show(new OnBindView<FullScreenDialog>(R.layout.view_login) {
                @Override
                public void onBind(FullScreenDialog dialog, View v) {
                    fullScreenDialog=dialog;
                    CheckBox xy_box=v.findViewById(R.id.xy_box);
                    LinearLayout xy_layout=v.findViewById(R.id.xy_layout);
                    EditText edt_phone=v.findViewById(R.id.edt_phone);
                    EditText edt_password=v.findViewById(R.id.edt_password);
                    RoundImageView iv_icon=v.findViewById(R.id.iv_icon);
                    if(!Tools.isEmpty(Tools.getSharedPreferencesValues(getApplicationContext(),"Phone",""))) {
                        edt_phone.setText(Tools.getSharedPreferencesValues(getApplicationContext(),"Phone",""));
                        if(!Tools.isEmpty(Tools.getSharedPreferencesValues(getApplicationContext(),"Icon",""))){
                            Glide.with(getApplicationContext()).load(Tools.getSharedPreferencesValues(getApplicationContext(),"Icon","")).into(iv_icon);
                        }
                    }

                    TextView tv_cancel=v.findViewById(R.id.tv_cancel);
                    tv_cancel.setOnClickListener(v1->{
                        //取消
                        dialog.dismiss();
                        Tools.setSharedPreferencesValues(getApplicationContext(),"isLogin",false);
                        startActivity(new Intent(getApplicationContext(),LoginCodeActivity.class));
                    });

                    TextView tv_quit=v.findViewById(R.id.tv_quit);
                    tv_quit.setOnClickListener(v1->{
                        //退出
                        Tools.setSharedPreferencesValues(getApplicationContext(),"isLogin",false);
                        dialog.dismiss();
                        System.exit(0);
                    });

                    Button btn_login=v.findViewById(R.id.btn_login);
                    btn_login.setOnClickListener(v1->{
                        //登录
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
                    });

                    TextView tv_register=v.findViewById(R.id.tv_register);
                    tv_register.setOnClickListener(v1->{
                        //去注册
                        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                    });

                    TextView tv_user=v.findViewById(R.id.tv_user);
                    tv_user.setOnClickListener(v1->{
                        //用户协议
                        startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("url", "file:///android_asset/用户协议.html"));
                    });

                    TextView tv_root=v.findViewById(R.id.tv_root);
                    tv_root.setOnClickListener(v1->{
                        //隐私政策
                        startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("url", "file:///android_asset/隐私政策.html"));
                    });
                }
            }).setCancelable(false);
        }
        else if(fullScreenDialog!=null) {
            fullScreenDialog.dismiss();
        }
    }


    /**
     * 登录
     * @param phone 17783323127
     * @param password 123456
     */
    LoginModel loginModel;
    private void login(String phone,String password){
        if(loginModel==null) {
            loginModel = new LoginModel();
        }
        LoadingShow("登陆中...");
        loginModel.isLogin(phone,password);
        loginModel.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                    Tools.setSharedPreferencesValues(getApplicationContext(),"isLogin",true);
                    if(fullScreenDialog!=null){
                        fullScreenDialog.dismiss();
                    }
                    initData();
                }
                LoadingStop();
            }
        });
    }

}