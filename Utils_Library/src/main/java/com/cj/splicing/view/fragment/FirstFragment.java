package com.cj.splicing.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cj.splicing.R;
import com.cj.splicing.common.base.BaseFragment;
import com.cj.splicing.view.activity.SplicingImgActivity;
import com.cj.splicing.view.activity.SplicingTxtActivity;

import butterknife.OnClick;

/**
 * 首页
 * Created By CaiJing On 2022/11/24
 */
public class FirstFragment extends BaseFragment {


    @Override
    public int getContentViewId() {
        return R.layout.fragment_first;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

    }

    @OnClick({R.id.layout_img,R.id.layout_txt})
    public void OnClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.layout_img://图片拼接
                intent=new Intent(getContext(), SplicingImgActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_txt://文字拼接
                intent=new Intent(getContext(), SplicingTxtActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}