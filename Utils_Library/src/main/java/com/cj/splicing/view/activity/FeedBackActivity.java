package com.cj.splicing.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cj.splicing.R;
import com.cj.splicing.bean.FeedBack;
import com.cj.splicing.bmob.FeedBackModel;
import com.cj.splicing.common.base.BaseActivity;
import com.cj.splicing.interfaces.Request;
import com.cj.splicing.interfaces.RequestData;
import com.cj.splicing.utils.ImgUtil;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.UriToFile;
import com.cj.splicing.utils.upload.ImageUploadUtils;
import com.cj.splicing.view.adapter.Actvity_Image_Adapter;
import com.kongzue.dialogx.dialogs.BottomMenu;
import com.kongzue.dialogx.dialogs.PopTip;
import com.kongzue.dialogx.interfaces.OnIconChangeCallBack;
import com.kongzue.dialogx.interfaces.OnMenuItemClickListener;
import com.yalantis.ucrop.UCrop;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 反馈
 * Created By CaiJing On 2022/11/7
 */
public class FeedBackActivity extends BaseActivity {


    @BindView(R.id.iv_quit)
    ImageView iv_quit;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_class)
    TextView tv_class;
    @BindView(R.id.swipe_img)
    SwipeRecyclerView swipe_img;
    @BindView(R.id.rootView)
    LinearLayout rootView;
    @BindView(R.id.edit_values)
    EditText edit_values;


    Actvity_Image_Adapter adapter;
    private int type=0;
    private int position=0;

    @Override
    public int setColor() {
        return 0;
    }

    @Override
    public void initData() {
        tv_title.setText("反馈");
        darkImmerseFontColor();
        adapter=new Actvity_Image_Adapter(getApplicationContext());
        swipe_img.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));
        swipe_img.setLongPressDragEnabled(false);
        swipe_img.setAdapter(adapter);
        swipe_img.setOnItemMoveListener(mItemMoveListener);// 监听拖拽，更新UI。

        adapter.setImageListener(new Actvity_Image_Adapter.ImageListener() {
            @Override
            public void voice_iv(int positions) {
                type=1;
                position=positions;
                ImgUtil.choicePhoto(FeedBackActivity.this,rootView);
            }

            @Override
            public void itemOnclick(int positions, int type) {
                position=positions;
                ImgUtil.choicePhoto(FeedBackActivity.this,rootView);
            }

            @Override
            public void delete(int position) {//删除
                listImage.remove(position);
                adapter.setData(listImage);
                adapter.notifyDataSetChanged();
            }
        });
        listImage=new ArrayList<String>();

    }

    @Override
    public int addContentView() {
        return R.layout.activity_feed_back;
    }

    @OnClick({R.id.iv_quit,R.id.tv_class,R.id.tv_sub})
    public void OnClick(View v){
        Intent intent;
        switch (v.getId()){
            case R.id.iv_quit://返回
                finish();
                break;
            case R.id.tv_class://类型
                showClass();
                break;
            case R.id.tv_sub://提交
                if(Tools.isEmpty(tv_class.getText().toString())){
                    PopTip.show("请选择反馈类型!");
                    return;
                }
                if(Tools.isEmpty(edit_values.getText().toString().trim())){
                    PopTip.show("请输入反馈内容!");
                    return;
                }
                if (edit_values.getText().toString().trim().length()<10){
                    PopTip.show("反馈内容不能小于10个字符!");
                    return;
                }
                if(listImage.size()==0){
                    PopTip.show("请至少上传一张截图凭证!");
                    return;
                }
                postImage();
                break;
            default:
                break;
        }
    }


    /**
     * 反馈上传
     */
    FeedBack feedBack;
    FeedBackModel feedBackModel;
    private void submit(){
        feedBack=new FeedBack();
        feedBackModel=new FeedBackModel();
        feedBack.setFeedBackClass(tv_class.getText().toString());
        feedBack.setMessage(edit_values.getText().toString().trim());
        feedBack.setUId(Tools.getSharedPreferencesValues(getApplicationContext(),"UId",""));
        switch (listImage.size()){
            case 1:
                feedBack.setPhotoA(listImage.get(0));
                break;
            case 2:
                feedBack.setPhotoA(listImage.get(0));
                feedBack.setPhotoB(listImage.get(1));
                break;
            case 3:
                feedBack.setPhotoA(listImage.get(0));
                feedBack.setPhotoB(listImage.get(1));
                feedBack.setPhotoC(listImage.get(2));
                break;
            default:
                break;
        }
        feedBackModel.submit(feedBack);
        feedBackModel.setRequest(new Request() {
            @Override
            public void isSucceed(boolean type) {
                if(type){
                    PopTip.show("提交成功!");
                    finish();
                }
                else {
                    PopTip.show("提交失败!");
                }
            }
        });
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
                    submit();
                }
                else {
                    PopTip.show("图片上传失败!");
                }
            }
        });
    }


    /**
     * 反馈类型展开
     */
    private void showClass(){
        BottomMenu.show(new String[]{"应用故障", "账号异常","功能建议","用户举报","其它","取消"})
                .setTitle("请选择反馈类型").setCancelable(false)
                .setOnIconChangeCallBack(new OnIconChangeCallBack<BottomMenu>() {
                    @Override
                    public int getIcon(BottomMenu dialog, int index, String menuText) {
                        switch (menuText){
                            case "应用故障":
                                return R.drawable.ic_bug_black;
                            case "账号异常":
                                return R.drawable.ic_user_error_black;
                            case "功能建议":
                                return R.drawable.ic_feed_black;
                            case "用户举报":
                                return R.drawable.ic_report_black;
                            case "其它":
                                return R.drawable.ic_other;
                            case "取消":
                                return R.drawable.ic_diss_black;
                            default:
                                break;
                        }
                        return 0;
                    }
                })
                .setOnMenuItemClickListener(new OnMenuItemClickListener<BottomMenu>() {
                    @Override
                    public boolean onClick(BottomMenu dialog, CharSequence text, int index) {
                        switch (index){
                            case 5://取消
                                dialog.dismiss();
                                break;
                            default:
                                tv_class.setText(text);
                                break;
                        }
                        return false;
                    }
                });
    }



    OnItemMoveListener mItemMoveListener = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
            // 此方法在Item拖拽交换位置时被调用。
            // 第一个参数是要交换为之的Item，第二个是目标位置的Item。

            // 交换数据，并更新adapter。
            int fromPosition = srcHolder.getAdapterPosition();
            int toPosition = targetHolder.getAdapterPosition();
            if (fromPosition==listImage.size()||toPosition==listImage.size()){
                return false;
            }
            Collections.swap(listImage, fromPosition, toPosition);
            adapter.notifyItemMoved(fromPosition, toPosition);
            // 返回true，表示数据交换成功，ItemView可以交换位置。
            return true;
        }

        @Override
        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {

        }
    };



    List<String> listImage=new ArrayList<String>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
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
                    //LoadingShow("uploading...");
                    Uri resultUri = UCrop.getOutput(data);
                    //剪切返回，显示剪切的图片到布局
                    if(position<listImage.size()&&position<3) {
                        listImage.set(position,Uri.parse(UriToFile.UriToFile(resultUri, getApplicationContext()).getAbsolutePath()).toString());
                    }
                    else {
                        listImage.add(Uri.parse(UriToFile.UriToFile(resultUri, getApplicationContext()).getAbsolutePath()).toString());
                    }
                    adapter.setData(listImage);
                    //imageUploadUtils.LuBan(Uri.parse(UriToFile.UriToFile(resultUri,getApplicationContext()).getAbsolutePath()).toString());
                    break;
            }
        }
    }
}