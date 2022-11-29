package com.cj.splicing.utils.permission;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cj.splicing.common.base.AppContext;


/**
 * 权限管理类
 * Created By CaiJing On 2022/10/22
 */
public final class PermissionUtils {

    /**
     * 检验存储权限
     */
    public static boolean isCheckSelfPermission_STORAGE(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(AppContext.getActivity() ,new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
            return false;
        } else {
            return true;
        }
    }

    /**
     *
     */
    public static boolean isCheckSelfPermission_PHONE_STATE(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(AppContext.getActivity() ,new String[]{
                    Manifest.permission.READ_PHONE_STATE
            }, 1);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检验相机存储权限
     */
    public static boolean isCheckSelfPermission_CAMERA(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(AppContext.getActivity(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.CAMERA
            }, 1);
            return false;
        } else {
            return true;
        }
    }


    /**
     * 动态权限申请 录音
     * @return true||false
     */
    public static boolean isCheck_RECORD_AUDIO(Context context) {
        if (
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                        || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        ) {
            ActivityCompat.requestPermissions(AppContext.getActivity(), new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.RECORD_AUDIO
                    , Manifest.permission.READ_EXTERNAL_STORAGE

            }, 1);
            return  false;
        }else {
            return  true;
        }
    }



}
