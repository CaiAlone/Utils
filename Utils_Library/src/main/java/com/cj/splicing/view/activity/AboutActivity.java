package com.cj.splicing.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;


import com.cj.splicing.R;
import com.cj.splicing.common.base.BaseActivity;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.widget.RoundImageView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于我们
 * Created By CaiJing On 2022/11/7
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_app)
    RoundImageView iv_app;
    @BindView(R.id.tv_version)
    TextView tv_version;
    @BindView(R.id.tv_fk)
    TextView tv_fk;

    @Override
    public int setColor() {
        return 0;
    }

    @Override
    public void initData() {
        darkImmerseFontColor();
        tv_title.setText("关于我们");
        iv_app.setImageResource(R.mipmap.app_icon);
        tv_version.setText("Version："+ Tools.getAppVersion(this));

    }

    @Override
    public int addContentView() {
        return R.layout.activity_about;
    }

    @OnClick({R.id.iv_quit,R.id.tv_fk,R.id.tv_xy,R.id.tv_ys})
    public void OnClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.iv_quit://返回
                finish();
                break;
            case R.id.tv_fk://反馈
                intent=new Intent(getApplicationContext(),FeedBackActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_xy://用户协议
                startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("url", "file:///android_asset/用户协议.html"));
                break;
            case R.id.tv_ys://隐私政策
                startActivity(new Intent(getApplicationContext(), WebViewActivity.class).putExtra("url", "file:///android_asset/隐私政策.html"));
                break;
            default:
                break;
        }
    }

}