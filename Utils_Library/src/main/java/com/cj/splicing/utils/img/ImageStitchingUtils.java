package com.cj.splicing.utils.img;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

/**
 * 图片拼接工具类
 * Created By CaiJing On 2022/11/24
 */
public class ImageStitchingUtils {

    /**
     * 横向图片拼接
     * @param first
     * @param second
     * @return
     */
    public static final Bitmap splitHorizontal(Bitmap first,Bitmap second){
        int width= first.getWidth()+second.getWidth();
        int height=Math.max(first.getHeight(),second.getHeight());
        Bitmap result=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(result);
        canvas.drawBitmap(first,0,0,null);
        canvas.drawBitmap(second,first.getWidth(),0,null);
        return result;
    }


    /**
     * 纵向图片拼接
     * @param first
     * @param second
     * @return
     */
    public static final Bitmap splitVertical(Bitmap first,Bitmap second){
        int width= Math.max(first.getWidth(),second.getWidth());
        int height=first.getHeight()+second.getHeight();
        Bitmap result=Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(result);
        canvas.drawBitmap(first,0,0,null);
        canvas.drawBitmap(second,0,first.getHeight(),null);
        return result;
    }


    /**
     * 图片拼接文字
     * @param bitmap
     * @param text
     * @return
     */
    public static final Bitmap splitText(Bitmap bitmap,String text,int deX,int deY){
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//设置画笔
        textPaint.setTextSize(28f);//字体大小
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);//采用默认的宽度
        textPaint.setColor(Color.WHITE);//采用的颜色
        Bitmap newb = Bitmap.createBitmap( bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888 );//创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas( newb );
        cv.drawBitmap( bitmap, 0, 0, null ); //在 0，0坐标载入图片
        //cv.drawText(text,20,300,textPaint);
        cv.drawText(text,deX,deY,textPaint);
        cv.save();//保存
        cv.restore();//存储
        return newb;
    }
}
