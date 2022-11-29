package com.cj.splicing.utils.widget;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;
    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public interface ScrollViewListener {
        void onScrolledToBottom();
        void onScrolledToTop();
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
    private ScrollViewListener mScrollViewListener;

    public void setScanScrollChangedListener(ScrollViewListener ScrollViewListener) {
        mScrollViewListener = ScrollViewListener;
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY <= 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }
        notifyScrollChangedListeners();
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mScrollViewListener != null) {
                mScrollViewListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom) {
            if (mScrollViewListener != null) {
                mScrollViewListener.onScrolledToBottom();
            }
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mScrollViewListener != null) {
            mScrollViewListener.onScrollChanged(l,t,oldl,oldt);
        }
    }


}
