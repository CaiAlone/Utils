package com.cj.splicing.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.cj.splicing.R;
import com.cj.splicing.common.base.BaseFragment;
import com.cj.splicing.utils.ImgUtil;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.UriToFile;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图片拼接D
 * Created By CaiJing On 2022/11/25
 */
public class SplicingFourFragment extends BaseFragment {

    @BindView(R.id.layout)
    LinearLayout layout;
    @BindView(R.id.iv_pj_left)
    ImageView iv_pj_left;
    @BindView(R.id.iv_right_top)
    ImageView iv_right_top;
    @BindView(R.id.iv_right_bottom)
    ImageView iv_right_bottom;
    int[] ids=new int[]{R.id.iv_pj_left,R.id.iv_right_top,R.id.iv_right_bottom};


    @Override
    public int getContentViewId() {
        return R.layout.fragment_splicing_four;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        listImage.add(null);
        listImage.add(null);
        listImage.add(null);
    }

    @OnClick({R.id.iv_pj_left,R.id.iv_right_top,R.id.iv_right_bottom})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.iv_pj_left:
                position=0;
                ImgUtil.choicePhoto(getActivity(),layout);
                break;
            case R.id.iv_right_top:
                position=1;
                ImgUtil.choicePhoto(getActivity(),layout);
                break;
            case R.id.iv_right_bottom:
                position=2;
                ImgUtil.choicePhoto(getActivity(),layout);
                break;
            default:
                break;
        }
    }



    int position=0;
    List<String> listImage=new ArrayList<String>();
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImgUtil.TAKE_PHOTO://相机返回
                    //相机返回图片，调用裁剪的方法
                    ImgUtil.startUCrop(getActivity(), ImgUtil.imageUri, 1, 1);
                    break;
                case ImgUtil.CHOOSE_PHOTO://相册返回
                    try {
                        if (data != null) {
                            Uri uri = data.getData();
                            //相册返回图片，调用裁剪的方法
                            ImgUtil.startUCrop(getActivity(), uri, 1, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        //ToastUtil.Show(  "图片选择失败");
                    }
                    break;
                case UCrop.REQUEST_CROP://剪切返回
                    //LoadingShow("uploading...");
                    Uri resultUri = UCrop.getOutput(data);
                    //剪切返回，显示剪切的图片到布局
                    if(position<3) {
                        listImage.set(position,Uri.parse(UriToFile.UriToFile(resultUri, getContext()).getAbsolutePath()).toString());
                    }
                    ImageView imageView=mRootView.findViewById(ids[position]);
                    Glide.with(getContext()).load(listImage.get(position)).into(imageView);
                    for(int i=0;i<listImage.size();i++){
                        if(Tools.isEmpty(listImage.get(i))){
                            return;
                        }
                    }
                    onFragmentListener.onFragment(3,listImage);
                    break;
            }
        }
    }

    //activity和fragment联系时候调用，fragment必须依赖activty
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //获取实现接口的activity
        onFragmentListener = (OnFragmentListener) getActivity();//或者myListener=(MainActivity) context;

    }
}