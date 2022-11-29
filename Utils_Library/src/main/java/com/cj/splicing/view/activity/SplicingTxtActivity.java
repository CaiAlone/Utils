package com.cj.splicing.view.activity;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cj.splicing.R;
import com.cj.splicing.common.base.BaseActivity;
import com.cj.splicing.utils.AndroidBug5497Workaround;
import com.cj.splicing.utils.ImgUtil;
import com.cj.splicing.utils.Tools;
import com.cj.splicing.utils.UriToFile;
import com.cj.splicing.utils.img.BitmapUtils;
import com.cj.splicing.utils.img.ImageStitchingUtils;
import com.cj.splicing.utils.keyboardWatcher.GlobalLayoutListener;
import com.cj.splicing.utils.keyboardWatcher.OnKeyboardChangedListener;
import com.cj.splicing.utils.toast.ToastUtil;
import com.cj.splicing.utils.widget.ResizeLayout;
import com.cj.splicing.utils.widget.keyboardWatcher;
import com.yalantis.ucrop.UCrop;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 文字拼接
 * Created By CaiJing On 20220/11/28
 */
public class SplicingTxtActivity extends BaseActivity {

    @BindView(R.id.layout_yd)
    RelativeLayout layout_yd;
    @BindView(R.id.iv_pj)
    ImageView iv_pj;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.layout)
    RelativeLayout layout;
    @BindView(R.id.layout2)
    RelativeLayout layout2;
    @BindView(R.id.edt_top)
    EditText edt_top;
    @BindView(R.id.edt_bot)
    EditText edt_bot;

    @Override
    public int setColor() {
        return R.color.top;
    }

    @Override
    public void initData() {
        //AndroidBug5497Workaround.assistActivity(this);
        tv_title.setText("图片拼接");
        initMove();
        edt_bot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(!Tools.isEmpty(edt_bot.getText().toString())){
                    edt_top.setText(edt_bot.getText().toString());
                }
                else {
                    edt_top.setText(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!Tools.isEmpty(edt_bot.getText().toString())){
                    edt_top.setText(edt_bot.getText().toString());
                }else {
                    edt_top.setText(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!Tools.isEmpty(edt_bot.getText().toString())){
                    edt_top.setText(edt_bot.getText().toString());
                }else {
                    edt_top.setText(null);
                }
            }
        });
        layout.getViewTreeObserver().addOnGlobalLayoutListener(
                new GlobalLayoutListener(layout, new OnKeyboardChangedListener() {
                    /**
                     * 键盘事件
                     *
                     * @param isShow         键盘是否展示
                     * @param keyboardHeight 键盘高度(当isShow为false时,keyboardHeight=0)
                     * @param screenWidth    屏幕宽度
                     * @param screenHeight   屏幕可用高度(不包含底部虚拟键盘NavigationBar), 即屏幕高度-键盘高度(keyboardHeight)
                     */
                    @Override
                    public void onChange(boolean isShow, int keyboardHeight, int screenWidth, int screenHeight) {
                        // do sth.
                        if(layout_yd.getVisibility()==View.INVISIBLE) {
                           return;
                        }
                        layout_yd.layout(deL, deT, deR, deB);
                        layout_yd.postInvalidate();
                    }
                }));
    }

    @Override
    public int addContentView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_splicing_txt;
    }

    @OnClick({R.id.iv_pj,R.id.iv_quit,R.id.btn_qr})
    public void OnClick(View v){
        switch (v.getId()){
            case R.id.iv_quit://返回
                finish();
                break;
            case R.id.iv_pj://添加图片
                ImgUtil.choicePhoto(this,layout);
                break;
            case R.id.btn_qr://确认拼图
                if(Tools.isEmpty(imgPath)){
                    ToastUtil.Show("请先选择图片后重试!");
                    return;
                }
                if(Tools.isEmpty(edt_top.getText().toString())){
                    ToastUtil.Show("请输入文字后重试!");
                    return;
                }
                splicingText();
            default:
                break;
        }
    }


    private String imgPath;
    private Bitmap bitmap;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                    imgPath=Uri.parse(UriToFile.UriToFile(resultUri, getApplicationContext()).getAbsolutePath()).toString();
                    bitmap= BitmapFactory.decodeFile(imgPath);
                    Glide.with(getApplicationContext()).load(imgPath).into(iv_pj);
                    layout_yd.setVisibility(View.VISIBLE);
                    break;
            }
        }

    }


    /**
     * 初始化控件拖动
     */
    int lastX;
    int lastY;
    int textLeft,textTop;
    int deL=0,deR=0,deT=0,deB=0;
    private void initMove(){
        // 获取屏幕的宽高
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWith = dm.widthPixels;
        final int screenHeight = dm.heightPixels;
        layout_yd.setOnTouchListener(new View.OnTouchListener() {

            boolean isDraging = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isDraging = true;
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (isDraging){
                            // x轴方向的位移差
                            int dx = (int) event.getRawX() - lastX;
                            // y轴方向的位移差
                            int dy = (int) event.getRawY() - lastY;

                            int l = v.getLeft() + dx;
                            int t = v.getTop() + dy;
                            int r = v.getRight() + dx;
                            int b = v.getBottom() + dy;

                            // 判断超出屏幕
                            if (l < 0){
                                l = 0;
                                r = l + v.getWidth();
                            }
                            if (t < 0){
                                t = 0;
                                b = t + v.getHeight();
                            }
                            if (r > screenWith){
                                r = screenWith;
                                l = r - v.getWidth();
                            }
                            if (b > screenHeight){
                                b = screenHeight;
                                t = b - v.getHeight();
                            }
                            // 重绘view
                            if(b<1000) {
                                textLeft=l;
                                textTop=t;
                                deL=l;
                                deB=b;
                                deR=r;
                                deT=t;

                                v.layout(l, t, r, b);
                                v.postInvalidate();
                                lastX = (int) event.getRawX();
                                lastY = (int) event.getRawY();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isDraging = false;
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void splicingText(){
        bitmap=Bitmap.createScaledBitmap(bitmap,800,800,true);
        bitmap=ImageStitchingUtils.splitText(bitmap,edt_top.getText().toString(),textLeft+10,textTop);
        if(bitmap!=null){
            BitmapUtils.saveBitmap(getApplicationContext(),bitmap);
        }
    }

}