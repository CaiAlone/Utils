package com.cj.splicing.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.cj.splicing.R;
import com.cj.splicing.bmob.UserModel;
import com.cj.splicing.common.base.BaseActivity;
import com.cj.splicing.interfaces.MyCallBack;
import com.cj.splicing.interfaces.Request;
import com.cj.splicing.interfaces.RequestData;
import com.cj.splicing.utils.ImgUtil;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.UriToFile;
import com.cj.splicing.utils.data.AgeChooseUtil;
import com.cj.splicing.utils.data.CityChooseUtil;
import com.cj.splicing.utils.data.GenderChooseUtil;
import com.cj.splicing.utils.upload.ImageUploadUtils;
import com.cj.splicing.utils.widget.RoundImageView;
import com.cj.splicing.view.popupwindow.EditPopWindows;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人资料
 * Created By CaiJing On 2022/11/10
 */
public class InformationActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_icon)
    RoundImageView iv_icon;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_sign)
    TextView tv_sign;
    @BindView(R.id.tv_gender)
    TextView tv_gender;
    @BindView(R.id.tv_age)
    TextView tv_age;
    @BindView(R.id.tv_city)
    TextView tv_city;
    @BindView(R.id.Relative_Info)
    LinearLayout rootView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;


    @Override
    public int setColor() {
        return 0;
    }

    @Override
    public void initData() {
        darkImmerseFontColor();
        tv_title.setText("个人资料");
        initViews();

        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
            }
        });
        refresh.autoRefresh();

    }

    @Override
    public int addContentView() {
        return R.layout.activity_information;
    }

    @OnClick({R.id.iv_quit,R.id.btn_cancel,R.id.Relative_gender,R.id.Relative_Age,R.id.Relative_City,R.id.iv_icon,R.id.tv_name,R.id.tv_sign})
    public void OnCliCk(View v){
        switch (v.getId()){
            case R.id.btn_cancel:
            case R.id.iv_quit://返回
                finish();
                break;
            case R.id.Relative_gender://性别
                GenderChooseUtil.initJsonData(this);
                GenderChooseUtil.showPickerView(this, new MyCallBack() {
                    @Override
                    public void callBack(Object object) {
                        tv_gender.setText(object.toString()+"");
                        editInfo("sex",object.toString()+"");
                    }
                });
                break;
            case R.id.Relative_Age://年龄
                AgeChooseUtil.initJsonData(this);
                AgeChooseUtil.showPickerView(this, new MyCallBack() {
                    @Override
                    public void callBack(Object object) {
                        tv_age.setText(object.toString()+"");
                        editInfo("age",object.toString()+"");
                    }
                });

                break;
            case R.id.Relative_City://城市
                CityChooseUtil.initJsonData(this);
                CityChooseUtil.showPickerView(this, new MyCallBack() {
                    @Override
                    public void callBack(Object object) {
                        tv_city.setText(object.toString()+"");
                        editInfo("city",object.toString()+"");
                    }
                });
                break;
            case R.id.iv_icon://修改头像
                ImgUtil.choicePhoto(InformationActivity.this,rootView);
                break;
            case R.id.tv_name://修改用户名
                EditPopWindows editPopWindows2=new EditPopWindows(getApplicationContext(),rootView);
                editPopWindows2.set_maxLength(10);
                editPopWindows2.setEdtListener(new EditPopWindows.EdtListener() {
                    @Override
                    public void isOK(boolean ok, String values) {
                        if(ok){
                            tv_name.setText(values);
                            editInfo("name",values);
                            editPopWindows2.dismiss();
                        }
                        else {
                            editPopWindows2.dismiss();
                        }
                    }
                });
                break;
            case R.id.tv_sign://修改签名
                EditPopWindows editPopWindows3=new EditPopWindows(getApplicationContext(),rootView);
                editPopWindows3.set_maxLength(12);
                editPopWindows3.setEdtListener(new EditPopWindows.EdtListener() {
                    @Override
                    public void isOK(boolean ok, String values) {
                        if(ok){
                            tv_sign.setText(values);
                            editInfo("sign",values);
                            editPopWindows3.dismiss();
                        }
                        else {
                            editPopWindows3.dismiss();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    private void initViews(){
        Glide.with(getApplicationContext()).load(Tools.getSharedPreferencesValues(getApplicationContext(),"Icon","")).into(iv_icon);
        tv_name.setText(Tools.getSharedPreferencesValues(getApplicationContext(),"UserName",""));
        tv_sign.setText(Tools.getSharedPreferencesValues(getApplicationContext(),"Sign",""));
        tv_gender.setText(Tools.getSharedPreferencesValues(getApplicationContext(),"Sex",1)==1?"男":"女");
        tv_age.setText(Tools.getSharedPreferencesValues(getApplicationContext(),"Age",""));
        tv_city.setText(Tools.getSharedPreferencesValues(getApplicationContext(),"City",""));
    }


    List<String> listImage=new ArrayList<String>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK||resultCode==96) {
            switch (requestCode) {
                case ImgUtil.TAKE_PHOTO://相机返回
                    //相机返回图片，调用裁剪的方法
                    ImgUtil.startUCrop(this, ImgUtil.imageUri, 1, 1);
                    break;
                case ImgUtil.CHOOSE_PHOTO://相册返回
                    try {
                        if (data != null) {
                            Uri uri = data.getData();
                            //相册返回图片，调用裁剪的方法
                            ImgUtil.startUCrop(this, uri, 1, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //ToastUtil.Show(  "图片选择失败");
                    }
                    break;
                case UCrop.REQUEST_CROP://剪切返回
                    Uri resultUri = UCrop.getOutput(data);
                    if(listImage.size()>0){
                        listImage.clear();
                    }
                    if(resultUri!=null) {
                        listImage.add(Uri.parse(UriToFile.UriToFile(resultUri, getApplicationContext()).getAbsolutePath()).toString());
                        postImage();
                    }
                    break;
            }
        }
    }

    /**
     * 图片上传
     */
    ImageUploadUtils imageUploadUtils;
    private void postImage(){
        imageUploadUtils=new ImageUploadUtils();
        imageUploadUtils.LuBan(listImage);
        imageUploadUtils.setRequestData(new RequestData() {
            @Override
            public void isSucceed(boolean type, String key, Object values) {
                listImage=(List<String>) values;
                if(type&&listImage!=null){
                    editInfo("icon",listImage.get(0));
                }
            }
        });
    }

    /**
     * 修改资料
     */
    UserModel model;
    private void editInfo(String key,String value){
        model=new UserModel();
        model.editUserInfo(key,value);
        model.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                    refresh.autoRefresh();
                }
            }
        });
    }

    /**
     * 获取个人信息
     */
    /**
     * 获取用户信息
     */
    private void getData(){
        model=new UserModel();
        model.getUserInfo();
        model.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                   initViews();
                }
                refresh.finishRefresh();
            }
        });
    }

}