package com.cj.splicing.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cj.splicing.R;
import com.cj.splicing.common.base.BaseActivity;
import com.cj.splicing.common.base.BaseFragment;
import com.cj.splicing.utils.ImgUtil;
import com.cj.splicing.utils.img.BitmapUtils;
import com.cj.splicing.utils.img.ImageStitchingUtils;
import com.cj.splicing.utils.toast.ToastUtil;
import com.cj.splicing.utils.widget.CustomViewPager;
import com.cj.splicing.view.fragment.SplicingFourFragment;
import com.cj.splicing.view.fragment.SplicingOneFragment;
import com.cj.splicing.view.fragment.SplicingThreeFragment;
import com.cj.splicing.view.fragment.SplicingTwoFragment;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图片拼接
 * Created By CaiJing On 2022/11/25
 */
public class SplicingImgActivity extends BaseActivity implements BaseFragment.OnFragmentListener {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.custom)
    CustomViewPager custom;
    @BindView(R.id.iv_pj1)
    ImageView iv_pj1;
    @BindView(R.id.iv_pj2)
    ImageView iv_pj2;
    @BindView(R.id.iv_pj3)
    ImageView iv_pj3;
    @BindView(R.id.iv_pj4)
    ImageView iv_pj4;
    List<String> img;
    int position=0;

    ArrayList<Fragment> mFragments = new ArrayList<>();
    FragmentPagerAdapter fragmentPagerAdapter;

    @Override
    public int setColor() {
        return 0;
    }

    @Override
    public void initData() {
        tv_title.setText("图片拼接");
        initViewPager();
        img=new ArrayList<String>();
    }

    @Override
    public int addContentView() {
        return R.layout.activity_splicing_img;
    }

    @OnClick({R.id.iv_quit,R.id.btn_qr,R.id.iv_pj1,R.id.iv_pj2,R.id.iv_pj3,R.id.iv_pj4})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_quit://返回
                finish();
                break;
            case R.id.btn_qr://确认拼图
                if(img.size()>0){
                    switchImg();
                }
                else {
                    ToastUtil.Show("请选择图片后重试!");
                }
                break;
            case R.id.iv_pj1://拼接1
                custom.setCurrentItem(0);
                break;
            case R.id.iv_pj2://拼接2
                custom.setCurrentItem(1);
                break;
            case R.id.iv_pj3://拼接3
                custom.setCurrentItem(2);
                break;
            case R.id.iv_pj4://拼接4
                custom.setCurrentItem(3);
                break;
            default:
                break;
        }
    }


    int fragmentPosition=0;
    private void initViewPager(){
        mFragments.add(new SplicingOneFragment());
        mFragments.add(new SplicingTwoFragment());
        mFragments.add(new SplicingThreeFragment());
        mFragments.add(new SplicingFourFragment());
        custom.setOffscreenPageLimit(mFragments.size()); //解决报空问题
        fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };
        if(fragmentPagerAdapter==null){
            fragmentPagerAdapter=new FragmentPagerAdapter(getSupportFragmentManager()) {
                @NonNull
                @Override
                public Fragment getItem(int position) {
                    return mFragments.get(position);
                }

                @Override
                public int getCount() {
                    return mFragments.size();
                }
            };
            custom.setAdapter(fragmentPagerAdapter);
        }
        else {
            custom.setAdapter(fragmentPagerAdapter);
        }
        custom.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragmentPosition=position;
                deIcon();
                switch (position){
                    case 0:
                        Glide.with(getApplicationContext()).load(R.mipmap.icon_pj1_true).into(iv_pj1);
                        break;
                    case 1:
                        Glide.with(getApplicationContext()).load(R.mipmap.icon_pj2_true).into(iv_pj2);
                        break;
                    case 2:
                        Glide.with(getApplicationContext()).load(R.mipmap.icon_pj3_true).into(iv_pj3);
                        break;
                    case 3:
                        Glide.with(getApplicationContext()).load(R.mipmap.icon_pj4_true).into(iv_pj4);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 重置控件状态
     */
    private void deIcon(){
        Glide.with(getApplicationContext()).load(R.mipmap.icon_pj1_false).into(iv_pj1);
        Glide.with(getApplicationContext()).load(R.mipmap.icon_pj2_false).into(iv_pj2);
        Glide.with(getApplicationContext()).load(R.mipmap.icon_pj3_false).into(iv_pj3);
        Glide.with(getApplicationContext()).load(R.mipmap.icon_pj4_false).into(iv_pj4);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments == null)
            {
                return;
            }
            // 查找在Fragment中onActivityResult方法并调用
            for (Fragment fragment : fragments)
            {


                if(fragment instanceof SplicingOneFragment&&fragmentPosition==0){
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                else if(fragment instanceof SplicingTwoFragment&&fragmentPosition==1){
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                else if(fragment instanceof SplicingThreeFragment&&fragmentPosition==2){
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                else if(fragment instanceof SplicingFourFragment&&fragmentPosition==3){
                    fragment.onActivityResult(requestCode, resultCode, data);
                }
                /*if (fragment != null)
                {
                    fragment.onActivityResult(requestCode, resultCode, data);
                }*/
            }
        }
    }





    @Override
    public void onFragment(int position, Object img) {
        this.position=position;
        this.img=(ArrayList<String>)img;
    }

    Bitmap bitmap,bitmap1,bitmap2,bitmap3;
    private void switchImg(){
        switch (position){
            case 0:
                bitmap1=BitmapFactory.decodeFile(img.get(0));
                bitmap2=BitmapFactory.decodeFile(img.get(1));
                bitmap3=BitmapFactory.decodeFile(img.get(2));
                bitmap1=Bitmap.createScaledBitmap(bitmap1,800,400,true);
                bitmap2=Bitmap.createScaledBitmap(bitmap2,400,400,true);
                bitmap3=Bitmap.createScaledBitmap(bitmap3,400,400,true);
                bitmap= ImageStitchingUtils.splitHorizontal(bitmap2,bitmap3);
                bitmap=Bitmap.createScaledBitmap(bitmap,800,400,true);
                bitmap=ImageStitchingUtils.splitVertical(bitmap1,bitmap);
                if(bitmap!=null){
                    BitmapUtils.saveBitmap(getApplicationContext(),bitmap);
                }
                bitmap1.recycle();
                bitmap2.recycle();
                bitmap3.recycle();
                bitmap.recycle();
                break;
            case 1:
                bitmap1=BitmapFactory.decodeFile(img.get(0));
                bitmap2=BitmapFactory.decodeFile(img.get(1));
                bitmap1=Bitmap.createScaledBitmap(bitmap1,800,400,true);
                bitmap2=Bitmap.createScaledBitmap(bitmap2,800,400,true);
                bitmap= ImageStitchingUtils.splitVertical(bitmap1,bitmap2);
                bitmap=Bitmap.createScaledBitmap(bitmap,800,800,true);
                //bitmap=ImageStitchingUtils.splitVertical(bitmap1,bitmap2);
                if(bitmap!=null){
                    BitmapUtils.saveBitmap(getApplicationContext(),bitmap);
                }
                bitmap1.recycle();
                bitmap2.recycle();
                bitmap.recycle();
                break;
            case 2:
                bitmap1=BitmapFactory.decodeFile(img.get(0));
                bitmap2=BitmapFactory.decodeFile(img.get(1));
                bitmap1=Bitmap.createScaledBitmap(bitmap1,400,800,true);
                bitmap2=Bitmap.createScaledBitmap(bitmap2,400,800,true);
                bitmap= ImageStitchingUtils.splitHorizontal(bitmap1,bitmap2);
                bitmap=Bitmap.createScaledBitmap(bitmap,800,800,true);
                //bitmap=ImageStitchingUtils.splitVertical(bitmap1,bitmap2);
                if(bitmap!=null){
                    BitmapUtils.saveBitmap(getApplicationContext(),bitmap);
                }
                bitmap1.recycle();
                bitmap2.recycle();
                bitmap.recycle();
                break;
            case 3:
                bitmap1=BitmapFactory.decodeFile(img.get(0));
                bitmap2=BitmapFactory.decodeFile(img.get(1));
                bitmap3=BitmapFactory.decodeFile(img.get(2));
                bitmap1=Bitmap.createScaledBitmap(bitmap1,400,800,true);
                bitmap2=Bitmap.createScaledBitmap(bitmap2,400,400,true);
                bitmap3=Bitmap.createScaledBitmap(bitmap3,400,400,true);
                bitmap= ImageStitchingUtils.splitVertical(bitmap2,bitmap3);
                bitmap=Bitmap.createScaledBitmap(bitmap,400,800,true);
                bitmap=ImageStitchingUtils.splitHorizontal(bitmap1,bitmap);
                if(bitmap!=null){
                    BitmapUtils.saveBitmap(getApplicationContext(),bitmap);
                }
                bitmap1.recycle();
                bitmap2.recycle();
                bitmap3.recycle();
                bitmap.recycle();
                break;
            default:
                break;
        }
    }
}