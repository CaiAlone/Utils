package com.cj.splicing.view.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.cj.splicing.R;
import com.cj.splicing.utils.KeybordUtil;
import com.cj.splicing.utils.Tools;

public class EditPopWindows extends PopupWindow {
    private int length=10;
    private Context mContext;
    public EditPopWindows(Context context, View parent) {
        super(context);
        init(context, parent);
    }
    public void set_maxLength(int l){
        this.length=l;
    }
    TextView tv_qr,tv_qx;
    EditText edt_values;
    void init(final Context mContext, View parent) {
        this.mContext=mContext;
        View view = View
                .inflate(mContext, R.layout.pop_edit, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.pop_anim_ins));
        setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.CENTER, 0, 0);

        tv_qr=view.findViewById(R.id.tv_qr);
        tv_qx=view.findViewById(R.id.tv_qx);
        edt_values=view.findViewById(R.id.edt_values);
        edt_values.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(edt_values.getText().toString().trim().length()>length){
                    Toast.makeText(mContext,"输入内容长度请不要超过"+length,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edt_values.getText().toString().trim().length()>length){
                    Toast.makeText(mContext,"输入内容长度请不要超过"+length,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edt_values.getText().toString().trim().length()>length){
                    Toast.makeText(mContext,"输入内容长度请不要超过"+length,Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_qr.setOnClickListener(v->{
            KeybordUtil.close_pop_KeyBoard(mContext,edt_values);

            if(edt_values.getText().toString().trim().length()<=length&&edt_values.getText().toString().trim().length()>0) {
                edtListener.isOK(true,edt_values.getText().toString().trim());
            }
            else {
                if(Tools.isEmpty(edt_values.getText().toString().trim())){
                    Toast.makeText(mContext,"内容不能为空!",Toast.LENGTH_SHORT).show();
                    edtListener.isOK(false, null);
                }
                else {
                    Toast.makeText(mContext, "输入不合法,请重新输入!", Toast.LENGTH_SHORT).show();
                    edtListener.isOK(false, null);
                }
            }
        });

        tv_qx.setOnClickListener(v->{
            KeybordUtil.close_pop_KeyBoard(mContext,edt_values);
            edtListener.isOK(false,null);
        });

    }
    private EdtListener edtListener;

    public void setEdtListener(EdtListener edtListener) {
        this.edtListener = edtListener;
    }

    public interface  EdtListener {
        void isOK(boolean ok,String values);
    }


 /*   *//**
     * 关闭软键盘
     *//*

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()&&getCurrentFocus()!=null){
            if (getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }*/


}
