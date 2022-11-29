package com.cj.splicing.view.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cj.splicing.R;


public class PhotoPopWindows extends PopupWindow {
    public PhotoPopWindows(Context context, View parent) {
        super(context);
        init(context, parent);
    }
    TextView tv_album,tv_photo,tv_cancel;
    void init(final Context mContext, View parent) {
        View view = View
                .inflate(mContext, R.layout.pop_photo, null);
        view.startAnimation(AnimationUtils.loadAnimation(mContext,
                R.anim.pop_anim_photo_ins));
        setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        setHeight(ViewGroup.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        tv_album=view.findViewById(R.id.tv_album);
        tv_album.setOnClickListener(v->{
            photoListener.isType(1);
        });

        tv_photo=view.findViewById(R.id.tv_photo);
        tv_photo.setOnClickListener(v->{
            photoListener.isType(2);
        });

        tv_cancel=view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(v->{
            photoListener.isType(0);
        });


    }
    private PhotoListener photoListener;

    public void setPhotoListener(PhotoListener photoListener) {
        this.photoListener = photoListener;
    }

    public interface  PhotoListener {
        void isType(int type);
    }
}
