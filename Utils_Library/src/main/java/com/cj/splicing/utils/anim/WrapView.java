package com.cj.splicing.utils.anim;

import android.view.View;

/** * 包装类用于封装View的with、height, * 你可以通过getXxx以及setXxx方法设置View的宽和高 */
public class WrapView {
    private View view;
    private int width;
    private int height;

    public WrapView(View view) {
        this.view = view;
    }

    public int getWidth() {
        return view.getLayoutParams().width;
    }

    public void setWidth(int width) {
        this.width = width;
        view.getLayoutParams().width=width;
        //修改View的高度
        view.requestLayout();
        // 刷新View的布局 }
    }
    public int getHeight() {
     return view.getLayoutParams().height;
 }
 public void setHeight( int height) {
     this .height=height; view.getLayoutParams().height = height; view.requestLayout();
 }
}
