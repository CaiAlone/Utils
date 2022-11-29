package com.cj.splicing.utils.anim;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


import com.cj.splicing.common.base.AppContext;

import java.util.List;

/**
 * 动画基类
 * Created By CaiJing On 2022/8/2
 */
public class AnimationUtils {

    /**
     * 平移动画
     * @param v 控件id
     * @param type 1 X轴, 2 Y轴
     * @param start 开始位置
     * @param end 结束位置
     * @param time 时长
     */
    public static void TransStart(View v,int type,Float start,Float end,long time){
        if(type==1) {
            ObjectAnimator.ofFloat(v,"translationX",start,end).setDuration(time).start();
        }
        else {
            ObjectAnimator.ofFloat(v,"translationY",start,end).setDuration(time).start();
        }
    }

    /**
     * 透明度
     * @param v
     * @param start
     * @param end
     * @param time
     */
    public static void AlphaStart(View v,Float start,Float end,long time){
        ObjectAnimator.ofFloat(v,"alpha",start,end).setDuration(time).start();
    }

    /**
     * 缩放
     * @param v
     * @param type 1 X轴,2 Y轴
     * @param start
     * @param end
     * @param time
     */
    public static void ScaleStart(View v,int type,Float start,Float end,long time){
        if(type==1){
            ObjectAnimator.ofFloat(v,"scaleX",start,end).setDuration(time).start();
        }
        else {
            ObjectAnimator.ofFloat(v,"scaleY",start,end).setDuration(time).start();
        }
    }

    /**
     * 旋转
     * @param v
     * @param start
     * @param end
     * @param time
     */
    public static void RotationStart(View v,Float start,Float end,long time){
        /*v.setPivotX(v.getWidth()/2);
        v.setPivotY(v.getHeight()/2);*/
        ObjectAnimator.ofFloat(v,"rotation",start,end).setDuration(time).start();
    }


    /**
     * 背景渐变
     * @param v
     * @param start
     * @param end
     * @param time
     */
    public static void BackGroundAlphaStart(View v,int start,int end,long time){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            ObjectAnimator.ofArgb(v,"backgroundColor",start,end,start).setDuration(time).start();
        }
    }

    /**
     * 控件长宽变化
     * @param v com.tm.dart.util.RoundImageView{285ee87 VFED..C.. ...P.... 260,144-460,344 #7f09017a app:id/iv_animation}
     * @param type
     * @param length
     * @param time
     */
    public static void SetViewLengthStart(View v,int type,int length,long time){
        if(type==1) {
            ObjectAnimator.ofInt(new com.cj.splicing.utils.anim.WrapView(v), "width",length).setDuration(time).start();
        }
        else {
            ObjectAnimator.ofInt(new com.cj.splicing.utils.anim.WrapView(v), "height",length).setDuration(time).start();
        }
    }

    /**
     * 透明度
     * @param v com.tm.dart.util.RoundImageView{285ee87 VFED..C.. ...P.... 260,144-460,344 #7f09017a app:id/iv_animation}
     * @param start 1.0f
     * @param end 0.3f
     * @param time 100
     * @param isfillafter true
     */
    public static void AnimationAlphaStart(View v,float start,float end,long time,boolean isfillafter){
        Animation animation=new AlphaAnimation(start,end);
        animation.setDuration(time);
        if(isfillafter){
            animation.setFillAfter(true);
        }
        else {
            animation.setFillAfter(false);
        }
        v.startAnimation(animation);
    }

    /**
     * anim 资源目录动画
     * @param v com.tm.dart.util.RoundImageView{285ee87 VFED..C.. ...P.... 260,144-460,344 #7f09017a app:id/iv_animation}
     * @param anim 2130771980
     */
    public static void AnimationStart(View v,int anim){
        Animation animation= android.view.animation.AnimationUtils.loadAnimation(AppContext.getContext(),anim);
        v.startAnimation(animation);
    }

    /**
     * 旋转
     * @param v com.tm.dart.util.RoundImageView{9e72fae VFED..C.. ...P..ID 260,144-460,344 #7f09017a app:id/iv_animation}
     * @param start 0
     * @param end 1080
     * @param time 1000
     * @param isfillafter true
     */
    public static void AnimationRotateStart(View v,int start,int end,long time,boolean isfillafter){
        Animation animation=new RotateAnimation(start,end,v.getWidth()/2,v.getHeight()/2);
        animation.setDuration(time);
        animation.setFillAfter(isfillafter);
        v.startAnimation(animation);
    }

    /**
     * 无限旋转
     * @param v com.tm.dart.util.RoundImageView{9e72fae VFED..C.. ...P..ID 260,144-460,344 #7f09017a app:id/iv_animation}
     */
    public static void AnimationRotateStart(View v,boolean f){
        int multiple=1000;//增加1000倍
        float endDegrees=360f * multiple;
        long duration=3000 * multiple;
        RotateAnimation animation =new RotateAnimation(0f, endDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(duration);
        animation.setRepeatCount(Animation.INFINITE); //无限循环
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);
        if (f) {
                v.startAnimation(animation);
        } else {
                v.clearAnimation();
        }
    }

    /**
     * 无限旋转
     * @param v com.tm.dart.util.RoundImageView{9e72fae VFED..C.. ...P..ID 260,144-460,344 #7f09017a app:id/iv_animation}
     */
    public static void AnimationRotateStart(View v,boolean f,float degress){
        int multiple=1000;//增加1000倍
        float endDegrees=360f * multiple;
        long duration=3000 * multiple;
        RotateAnimation animation =new RotateAnimation(degress, endDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(duration);
        animation.setRepeatCount(Animation.INFINITE); //无限循环
        animation.setInterpolator(new LinearInterpolator());
        animation.setFillAfter(true);

        if (f) {
            v.startAnimation(animation);
        } else {
            v.clearAnimation();
        }
    }


    /**
     * 缩放
     * @param v com.tm.dart.util.RoundImageView{12b2040 VFED..C.. ...P.... 260,144-460,344 #7f09017a app:id/iv_animation}
     * @param startX 0.5
     * @param endX 1.5
     * @param startY 1.0
     * @param endY 1.5
     * @param time 1000
     * @param isfillafter true
     */
    public static void AnimationScaleStart(View v,float startX,float endX,float startY,float endY,long time,boolean isfillafter){
        Animation animation=new ScaleAnimation(startX,endX,startY,endY,v.getWidth()/2,v.getHeight()/2);
        animation.setDuration(time);
        animation.setFillAfter(isfillafter);
        v.startAnimation(animation);
    }


    /**
     * 平移
     * @param v com.tm.dart.util.RoundImageView{bd4b1c VFED..C.. ...P.... 260,660-460,860 #7f09017a app:id/iv_animation}
     * @param startX 0.0
     * @param endX 500.0
     * @param startY 0.0
     * @param endY 500.0
     * @param time 1000
     * @param isfillafter true
     */
    public static void AnimationTranslateStart(View v,float startX,float endX,float startY,float endY,long time,boolean isfillafter){
        Animation animation=new TranslateAnimation(startX,endX,startY,endY);
        v.setPivotX(v.getWidth()/2);
        v.setPivotY(v.getHeight()/2);
        animation.setDuration(time);
        animation.setFillAfter(isfillafter);
        v.startAnimation(animation);
    }

    /**
     * 组合动画
     * @param v com.tm.dart.util.RoundImageView{51de1b4 VFED..C.. ...P.... 260,660-460,860 #7f09017a app:id/iv_animation}
     * @param res res
     * @param time 1000
     * @param isfillafter true
     */
    public static void AnimationSetStart(View v, List<Integer> res,long time,boolean isfillafter){
        AnimationSet animation=new AnimationSet(true);
        for(int id:res){
            animation.addAnimation(android.view.animation.AnimationUtils.loadAnimation(AppContext.getContext(),id));
        }
        animation.setDuration(time);
        animation.setFillAfter(isfillafter);
        animation.setInterpolator(AppContext.getContext(), android.R.anim.anticipate_interpolator);
        v.startAnimation(animation);
    }


}
