package com.cj.splicing.utils.img;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Bitmap 帮助类之一
 */
public final class BitmapUtils {

    /**
     * Save Bitmap
     *
     * @param name file name
     * @param bm  picture to save
     */
    public static void saveBitmap(String name, Bitmap bm, Context mContext) {
        Log.d("Save Bitmap", "Ready to save picture");
        //指定我们想要存储文件的地址
        String TargetPath = mContext.getFilesDir() + "/images/";
        TargetPath= Environment.getExternalStorageDirectory()+"/images/";
        Log.d("Save Bitmap", "Save Path=" + TargetPath);
        //判断指定文件夹的路径是否存在
        if (!fileIsExist(TargetPath)) {
            Log.d("Save Bitmap", "TargetPath isn't exist");
        } else {
            //如果指定文件夹创建成功，那么我们则需要进行图片存储操作
            File saveFile = new File(TargetPath, name);
            try {
                FileOutputStream saveImgOut = new FileOutputStream(saveFile);
                // compress - 压缩的意思
                bm.compress(Bitmap.CompressFormat.JPEG, 80, saveImgOut);
                //存储完成后需要清除相关的进程
                saveImgOut.flush();
                saveImgOut.close();
                Log.d("Save Bitmap", "The picture is save to your phone!");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }



    /**
     * 保存bitmap到本地
     *
     * @param bitmap
     * @param name
     */
    public static void saveBitmap(Bitmap bitmap, String name) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = "/sdcard/dskqxt/pic/";
        } else {
            Log.d("xxx", "saveBitmap: 1return");
            return;
        }
        try {
            filePic = new File(savePath + name + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("xxx", "saveBitmap: 2return");
            return;
        }
        Log.d("xxx", "saveBitmap: " + filePic.getAbsolutePath());
    }


    /**
     * 图片保存
     * @param context
     * @param bitmap
     */
    public static void saveBitmap(Context context,Bitmap bitmap){
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "images");
        if (!appDir.exists())
        {
            Log.e(TAG, "去创建文件夹");
            appDir.mkdirs();
        }
        String fileName = "djgx_" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try
        {
            Log.e(TAG, "开始保存图片");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            Toast.makeText(context, "图片已保存在:/DCIM/images/"+fileName, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Log.e(TAG, "保存图片失败:" + e.toString());
            Toast.makeText(context, "图片保存失败", Toast.LENGTH_LONG).show();
        } catch (IOException e)
        {
            e.printStackTrace();
            Log.e(TAG, "保存图片失败:" + e.toString());
            Toast.makeText(context, "图片保存失败", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 判断指定目录的文件夹是否存在，如果不存在则需要创建新的文件夹
     * @param fileName 指定目录
     * @return 返回创建结果 TRUE or FALSE
     */
    static boolean fileIsExist(String fileName) {
        //传入指定的路径，然后判断路径是否存在
        File file=new File(fileName);

        if (file.exists())
            return true;
        else{
            //file.mkdirs() 创建文件夹的意思
            return file.mkdirs();
        }
    }
}
