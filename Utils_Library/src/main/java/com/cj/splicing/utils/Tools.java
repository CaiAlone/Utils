package com.cj.splicing.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cj.splicing.common.base.AppContext;
import com.lzy.okgo.OkGo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ??????????????????
 *
 * @author
 * @date 2014???12???15???
 * @?????? 1.0
 */
public class Tools {
    private static Toast toast;// ??????
    private static Handler handler = new Handler();// ????????????
    private static boolean isShowDeBug = true;// ??????
    public static int[] wh;// ??????????????????
    public static SharedPreferences mPreferences;// ??????????????????
    public static SharedPreferences mXmlCanch;// ??????????????????
    public static SharedPreferences mXmlPreferences;//??????
    public static String other = "2xdD3#c3E8p()dOddXo3L_wO";// web??????
    public static String fastlogin = "nQQ*osdfwer*392kI_ew-=)eww3dfP";// ??????????????????
    public static Bitmap mBitmap = null;
    public static Editor editor;
    public static Editor editorCanch;

    public static final String XML = "user";
    public static final String CANCH = "canch";
    public static final String login = "login";
    private static final int SHOWTOAST = 3000;
    private static Runnable runnable = new Runnable() {
        public void run() {
            toast.cancel();
        }
    };

    public static String getDurationInString(Long duration) {
        if (duration < 60) {
            return duration + "???";
        } else if (duration < 3600) {
            return (duration / 60) + "??????";
        } else if (duration < 86400) {
            Long hours = duration / 3600;
            Long minutesLeft = duration % 3600;
            Long minutes = minutesLeft / 60;
            return hours + "??????" + minutes + "??????";
        } else {
            Long days = duration / 86400;
            Long hoursLeft = duration % 86400;
            Long hours = hoursLeft / 3600;
            Long minutesLeft = duration % 3600;
            Long minutes = minutesLeft / 60;
            return days + "???" + hours + "??????" + minutes + "??????";
        }
    }

    //???????????????
    public static String date_format(Long micro_second) {
// ?????????
        Double second = Math.floor(micro_second / 1000);
// ??????
//        Double day = Math.floor(second / 3600 / 24);
// ??????
        Double hr = Math.floor(second / 3600);
// ??????
        Double min = Math.floor(second / 60 % 60);
// ???
        Double sec = Math.floor(second % 60);
        return String.format("%.0f", hr) + ":" + String.format("%.0f", min) + ":" + String.format("%.0f", sec);
    }

    public static String getDurationInString3(Long duration) {
        if (duration < 60) {
            return duration + ":";
        } else if (duration < 3600) {
            return (duration / 60) + ":";
        } else {
            Long hours = duration / 3600;
            Long minutesLeft = duration % 3600;
            Long minutes = minutesLeft / 60;

            Long minutesLeft1 = duration % (3600 * 60);

            return hours + ":" + minutes + ":" + minutesLeft1;
        }
    }

    public static String getDurationInString1(Long duration) {
        if (duration < 60) {
            return duration + "???";
        } else if (duration < 3600) {
            return (duration / 60) + "??????";
        } else {
            Long hours = duration / 3600;
            Long minutesLeft = duration % 3600;
            Long minutes = minutesLeft / 60;
            return hours + "??????" + minutes + "??????";
        }
    }

    public static boolean isServiceWork(String serviceName, Context context) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }

    /**
     * ??????properties
     */
    public static Properties getPropertiesValue(Context context) {
        Properties properties = new Properties();
        if (context != null && !context.isRestricted()) {
            try {
                properties.load(context.getAssets().open("peizi.properties"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    public static int dp2Px(float dpValue) {
        Context context = AppContext.getContext();
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static boolean isGif(String str) {
        if (str.contains("gif") || str.contains("GIF")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ??????????????????
     *
     * @param context
     * @param file
     * @param properties
     * @return
     */
    public static boolean saveProperty(Context context, String file,
                                       Properties properties) {
        try {
            File fil = new File(file);
            if (!fil.exists())
                fil.createNewFile();
            FileOutputStream s = new FileOutputStream(fil);
            properties.store(s, "");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ????????????
     */
    public static String getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();
        while (iter.hasNext()) {
            PackageInfo packageinfo = iter.next();
            String packageName = packageinfo.packageName;
            if (packageinfo.packageName.equals(packageName)) {

            }
            return packageinfo.signatures[0].toCharsString();
        }
        return null;
    }

    /**
     * Java???????????? ?????????????????????
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Java???????????? ?????????????????????????????????
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * <p>???base64????????????????????????</p>
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */
    public static void toFile(String base64Code, String targetPath) throws Exception {
        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    /**
     * ??????* ???base64?????????bitmap??????
     * ??????*
     * ??????* @param string base64?????????
     * ??????* @return bitmap
     */
    public static Bitmap base64ToBitmap(String base64String) {
        Bitmap bitmap = null;
        byte[] bitmapArray;
        try {
            bitmapArray = Base64.decode(base64String, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                    bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /** ??????????????? */
    public static final int NO_NET_WORK = 0;
    /** ???wifi?????? */
    public static final int WIFI = 1;
    /** ??????wifi?????? */
    public static final int NO_WIFI = 2;


    /**
     * ????????????????????????
     * @param context
     * @return
     */
    public static boolean isNetWorkAvailable(Context context) {
        boolean isAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    /**
     * ??????????????????
     * @param context
     * @return
     */
    public static int getNetWorkType(Context context) {
        if (!isNetWorkAvailable(context)) {
            return NO_NET_WORK;
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting())
            return WIFI;
        else
            return NO_WIFI;
    }

    /**
     * ???????????????????????????wifi
     * @param context
     * @return ?????????wifi??????true???????????????false
     */
    @SuppressWarnings("static-access")
    public static boolean isWiFiConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        return networkInfo.getType() == manager.TYPE_WIFI ? true : false;
    }

    /**
     * ??????MOBILE??????????????????
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isMobileDataEnable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;
        isMobileDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        return isMobileDataEnable;
    }

    /**
     * ??????wifi ????????????
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isWifiDataEnable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }

    /**
     * ???????????????????????????
     * @param activity
     */
    public static void GoSetting(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * ????????????????????????
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cn);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * ??????????????????????????????
     *
     * @param context
     * @return true????????????
     */
    public static boolean getNetwStates(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cManager.getActiveNetworkInfo();
        return info.isAvailable();
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();

    }

    /**
     * ??????????????????
     *
     * @param context
     * @param color
     * @return
     */
    public static int getColor(Context context, int color) {
        return context.getResources().getColor(color);
    }

    /**
     * ??????draw
     *
     * @param context
     * @return
     */
    public static Drawable getDrawable(Context context, int drawable) {
        return context.getResources().getDrawable(drawable);
    }

    /**
     * ??????toast
     *
     * @param context activity
     * @param text    ???????????????
     */
    public static void showTip(final Context context, final String text) {
        handler.removeCallbacks(runnable);// ??????
        if (toast != null) {
            toast.setText(text);
        } else {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        handler.postDelayed(runnable, SHOWTOAST + 2000);
        toast.show();
    }

    /**
     * ??????toast
     *
     * @param context activity
     * @param text    ???????????????
     */
    public static void showTipOne(final Context context, final String text, final int gravity) {
        handler.removeCallbacks(runnable);// ??????
        if (toast != null) {
            toast.setText(text);
        } else {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, 0, 0);
        }
        handler.postDelayed(runnable, SHOWTOAST + 2000);
        toast.show();
    }


    public static String baseFile(File file) {
        FileInputStream inputFile = null;
        String encodedString = null;
        try {
            inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            encodedString = Base64.encodeToString(buffer, Base64.DEFAULT);
            Log.e("Base64", "Base64---->" + encodedString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedString;
    }

    public static void decoderBase64File(String base64Code, String targetPath) throws Exception {
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    /**
     * ??????toast
     *
     * @param context activity
     * @param text    ???????????????
     */
    public static void showToast(final Context context, final String text) {
        Toast mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.show();
    }

    /**
     * ??????toast
     *
     * @param context activity
     * @param text    ???????????????
     */
    public static void showTip(final Context context, final String text,
                               final int gravity) {
        Toast mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.setGravity(gravity, 0, 0);
        mToast.show();
    }

    public static void showTip(final Context context, final String text,
                               final int gravity, int x, int y) {
        Toast mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.setGravity(gravity, x, y);
        mToast.show();
    }

    /**
     * @param source
     * @return true ?????????
     */
    public static boolean isEmpty(String source) {

        if (source == null || "".equals(source) || "null".equals(source))
            return true;

        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * ??????fragmnet??????
     */
    public static void cleanFragment(FragmentManager manager, Fragment fragment) {
        try {
            // ????????????
            for (int i = 0; i < manager.getBackStackEntryCount(); i++) {
                manager.popBackStack();
            }
            if (fragment != null) {
                manager.beginTransaction().remove(fragment).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ??????SharedPreferences
     *
     * @param context
     * @return
     */
    public static synchronized SharedPreferences getSharedPreferences(Context context) {
        try {
            if (mPreferences == null) {
                mPreferences = context.getSharedPreferences(XML,
                        Context.MODE_PRIVATE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mPreferences;

    }

    /**
     * ??????SharedPreferences
     *
     * @param context
     * @return
     */
    public static SharedPreferences getChachPreferences(Context context) {
        try {
            if (mXmlCanch == null) {
                mXmlCanch = context.getSharedPreferences(CANCH,
                        Context.MODE_PRIVATE);
                editorCanch = mXmlCanch.edit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mXmlCanch;

    }

    /**
     * ??????SharedPreferences
     *
     * @param context
     * @return
     */
    public static SharedPreferences getSharedPreferences(Context context, String xml) {
        return context.getSharedPreferences(xml,
                Context.MODE_PRIVATE);

    }


    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static boolean setSharedPreferencesValues(Context context,
                                                     String key, int values) {
        try {
            if (mPreferences == null) {
                mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            }
            editor = mPreferences.edit();
            editor.putInt(key, values);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static boolean setSharedPreferencesValues(Context context,
                                                     String key, float values) {
        try {
            if (mPreferences == null) {
                mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            }
            editor = mPreferences.edit();
            editor.putFloat(key, values);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static boolean setSharedPreferencesValues(Context context,
                                                     String key, String values) {
        try {
            if (mPreferences == null) {
                mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            }
            editor = mPreferences.edit();
            editor.putString(key, values);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static boolean setXmlCanchsValues(Context context,
                                             String key, String values) {
        try {
            editorCanch = context.getSharedPreferences(CANCH, Context.MODE_PRIVATE).edit();
            editorCanch.putString(key, values);
            editorCanch.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static boolean setXmlCanchsValues(Context context,
                                             String key, int values) {
        try {
            if (mXmlCanch == null) {
                mXmlCanch = getChachPreferences(context);
            }
            editorCanch.putInt(key, values);
            editorCanch.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static boolean setSpValues(Context context, String xml,
                                      String key, String values) {
        try {
            Editor editor = getSharedPreferences(context, xml).edit();
            editor.putString(key, values);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static String getSpValues(Context context, String xml, String key) {
        return getSharedPreferences(context, xml).getString(key, "");
    }

    /**
     * ?????????????????????
     *
     * @param context
     * @return
     */
    public static boolean setSharedPreferencesValues(Context context,
                                                     String key, Boolean values) {
        try {
            if (mPreferences == null) {
                mPreferences = getSharedPreferences(context);
            }
            editor = mPreferences.edit();
            editor.putBoolean(key, values);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static String getSharedPreferencesValues(Context context,
                                                    String key) {
        try {
            if (mPreferences == null) {
                mPreferences = getSharedPreferences(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String result = mPreferences.getString(key, null);
//        String decrypt = ConcealHelper.decryptString(result);
//        if (!Tools.isEmpty(decrypt)) {
//            result = decrypt;
//        }
        return result;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static int getSharedPreferencesValues(Context context,
                                                 String key, int values) {
        try {
            if (mPreferences == null) {
                mPreferences = getSharedPreferences(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return values;
        }
        int result = mPreferences.getInt(key, values);
//        String decrypt = ConcealHelper.decryptString(result);
//        if (!Tools.isEmpty(decrypt)) {
//            result = decrypt;
//        }
        return result;
    }


    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static String getSharedPreferencesValues(Context context,
                                                    String key,String values) {
        try {
            if (mPreferences == null) {
                mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result = mPreferences.getString(key,values);
//        String decrypt = ConcealHelper.decryptString(result);
//        if (!Tools.isEmpty(decrypt)) {
//            result = decrypt;
//        }
        return result;
    }


    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static boolean getSharedPreferencesValues(Context context,
                                                    String key,boolean values) {
        try {
            if (mPreferences == null) {
                mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean result = mPreferences.getBoolean(key,values);
//        String decrypt = ConcealHelper.decryptString(result);
//        if (!Tools.isEmpty(decrypt)) {
//            result = decrypt;
//        }
        return result;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static boolean getSpBooleanValues(Context context, String xml,
                                             String key) {
        return getSharedPreferences(context, xml).getBoolean(key, false);
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static boolean setSpBooleanValues(Context context, String xml,
                                             String key, boolean values) {
        try {
            Editor editor = getSharedPreferences(context, xml).edit();
            editor.putBoolean(key, values);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static String getXmlCanchValues(Context context,
                                           String key) {
        try {
            mXmlCanch = context.getSharedPreferences(CANCH, Context.MODE_PRIVATE);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        String result = mXmlCanch.getString(key, null);
//		String decrypt=ConcealHelper.decryptString(result);
//		if(!AbStrUtil.isEmpty(decrypt)){
//			result=decrypt;
//		}
        return result;
    }

    /**
     * ?????????
     *
     * @param context
     * @return
     */
    public static int getXmlIntCanchValues(Context context,
                                           String key) {
        try {
            if (mXmlCanch == null) {
                mXmlCanch = getChachPreferences(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return mXmlCanch.getInt(key, -1);
    }

    /**
     * ??????xml??????
     *
     * @param context
     * @param xml
     * @return
     */
    public static boolean cleanXML(Context context, String xml) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(xml,
                    Context.MODE_PRIVATE);
            ;
            Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * ??????????????????
     *
     * @param phonenum
     * @return
     */
    public static String hintPhoneNum(String phonenum) {
        if (phonenum == null) {
            return phonenum;
        } else if (phonenum.length() != 11) {
            return phonenum;
        }
        return phonenum.substring(0, 3) + "*****" + phonenum.substring(8);
    }

    /**
     * ?????????????????????
     *
     * @param str
     * @return
     */
    public static boolean isJiaMi(String str) {
        Pattern pattern = Pattern.compile("([0-9]*?|([abc]*?)([xyz]*?))([+-/*=])");
        Matcher m = pattern.matcher(str);
        return m.matches();
    }


    /**
     * ???????????????
     */
    public static String setMD5(String str) {
        if (str == null || str.equals("")) {
            return "";
        }
        try {
            MessageDigest bmd5 = MessageDigest.getInstance("MD5");
            bmd5.update(str.getBytes());
            int i;
            StringBuffer buf = new StringBuffer();
            byte[] b = bmd5.digest();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * MD5??????
     *
     * @param byteStr ?????????????????????
     * @return ?????? byteStr???md5???
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    /**
     * ???????????????
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        String versionName = "";
        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return 0;
            }
        } catch (Exception e) {
        }
        return versioncode;
    }

    public static String getAppName(Context context) {
        if (context == null) {
            return null;
        }
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String appName = appInfo.loadLabel(context.getPackageManager()) + "";
            return appName;
        } catch (Exception e) {
            e.printStackTrace();

            Log.i("chwn", "getAppName >> e:" + e.toString());
        }
        return null;
    }

    /**
     * ??????????????????
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
//        int versioncode = 0;
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
//            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
        }
        return versionName;
    }

    /**
     * ????????????????????????
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * ??????view
     *
     * @param context ?????????view???content
     * @param id      ??????view?????????id
     * @return ????????????view
     */
    public static View getContentView(Context context, int id) {
        return LayoutInflater.from(context).inflate(id, null);
    }

    /**
     * ?????????????????????????????? salt md5(public_salt+substr(md5(private_salt+pwd),6,15))
     * <p>
     * ??????
     *
     * @param pwd ??????
     * @return ?????????????????????
     */
    public static String setSecret(String pwd) {
        String public_salt = "r1eO*rE1!";
        String private_salt = randompwd(16);
        String y = null;
        try {
            y = Tools.setMD5(private_salt + pwd);// ???????????????
            if (y.length() < 15) {
                y = y + y.substring(0, 15 - y.length());
            }
            y = y + public_salt;
            y = Tools.setMD5(y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return y;
    }

    /**
     * user-agent????????????
     */
    /**
     * agent?????? md5(substr(md5(salt+8???????????????,5,14))+8????????????????????????40????????????
     * <p>
     * ??????
     * ??????
     *
     * @return ?????????????????????
     */
    public static String setAgent(String salt) {
        String f = randompwd(8);
        String hj = Tools.setMD5(salt + f);
        hj = hj.substring(5, 14);
        hj = Tools.setMD5(hj);
        hj += f;
        return hj;
    }

    /**
     * ?????????????????????????????????????????????????????????????????????
     *
     * @return ??????????????????
     */
    public static String randompwd(int count) {
        Random r = new Random();
        int i = 0;
        String str = "";
        String s = null;
        while (i < count) { // ???????????????30?????????????????????????????????????????????30????????????
            switch (r.nextInt(63)) {
                case (0):
                    s = "0";
                    break;
                case (1):
                    s = "1";
                    break;
                case (2):
                    s = "2";
                    break;
                case (3):
                    s = "3";
                    break;
                case (4):
                    s = "4";
                    break;
                case (5):
                    s = "5";
                    break;
                case (6):
                    s = "6";
                    break;
                case (7):
                    s = "7";
                    break;
                case (8):
                    s = "8";
                    break;
                case (9):
                    s = "9";
                    break;
                case (10):
                    s = "a";
                    break;
                case (11):
                    s = "b";
                    break;
                case (12):
                    s = "c";
                    break;
                case (13):
                    s = "d";
                    break;
                case (14):
                    s = "e";
                    break;
                case (15):
                    s = "f";
                    break;
                case (16):
                    s = "g";
                    break;
                case (17):
                    s = "h";
                    break;
                case (18):
                    s = "i";
                    break;
                case (19):
                    s = "j";
                    break;
                case (20):
                    s = "k";
                    break;
                case (21):
                    s = "m";
                    break;
                case (23):
                    s = "n";
                    break;
                case (24):
                    s = "o";
                    break;
                case (25):
                    s = "p";
                    break;
                case (26):
                    s = "q";
                    break;
                case (27):
                    s = "r";
                    break;
                case (28):
                    s = "s";
                    break;
                case (29):
                    s = "t";
                    break;
                case (30):
                    s = "u";
                    break;
                case (31):
                    s = "v";
                    break;
                case (32):
                    s = "w";
                    break;
                case (33):
                    s = "l";
                    break;
                case (34):
                    s = "x";
                    break;
                case (35):
                    s = "y";
                    break;
                case (36):
                    s = "z";
                    break;
                case (37):
                    s = "A";
                    break;
                case (38):
                    s = "B";
                    break;
                case (39):
                    s = "C";
                    break;
                case (40):
                    s = "D";
                    break;
                case (41):
                    s = "E";
                    break;
                case (42):
                    s = "F";
                    break;
                case (43):
                    s = "G";
                    break;
                case (44):
                    s = "H";
                    break;
                case (45):
                    s = "I";
                    break;
                case (46):
                    s = "L";
                    break;
                case (47):
                    s = "J";
                    break;
                case (48):
                    s = "K";
                    break;
                case (49):
                    s = "M";
                    break;
                case (50):
                    s = "N";
                    break;
                case (51):
                    s = "O";
                    break;
                case (52):
                    s = "P";
                    break;
                case (53):
                    s = "Q";
                    break;
                case (54):
                    s = "R";
                    break;
                case (55):
                    s = "S";
                    break;
                case (56):
                    s = "T";
                    break;
                case (57):
                    s = "U";
                    break;
                case (58):
                    s = "V";
                    break;
                case (59):
                    s = "W";
                    break;
                case (60):
                    s = "X";
                    break;
                case (61):
                    s = "Y";
                    break;
                case (62):
                    s = "Z";
                    break;
            }
            i++;
            str = s + str;
        }
        return str;
    }

    /**
     * ??????????????? ?????? ?????????????????????
     *
     * @param context
     * @return
     */
    public static ArrayList<Map<String, String>> getPhoneAddressBook(
            Context context) {
        /** ?????????Phon????????? **/
        final String[] PHONES_PROJECTION = new String[]{Phone.DISPLAY_NAME,
                Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID};
        /** ????????????????????? **/
        final int PHONES_DISPLAY_NAME_INDEX = 0;
        /** ???????????? **/
        final int PHONES_NUMBER_INDEX = 1;
        /** ?????????????????? **/
        ArrayList<Map<String, String>> mContacts = new ArrayList<Map<String, String>>();
        String phoneNumber;// ????????????
        String contactName;// ??????

        ContentResolver resolver = context.getContentResolver();// ??????????????????
        // ??????Sims????????????
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);
        Map<String, String> map;// ?????????

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                map = new HashMap<String, String>();
                // ??????????????????
                phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // ?????????????????????????????????????????? ??????????????????
                if (TextUtils.isEmpty(phoneNumber)) {
                    map.put("phonenum", phoneNumber);
                    continue;
                }
                // ?????????????????????
                contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                map.put("name", contactName);
                // Sim???????????????????????????
                mContacts.add(map);
            }
        }
        // ?????????????????????
        phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION,
                null, null, null);
        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                map = new HashMap<String, String>();
                // ??????????????????
                phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                // ?????????????????????????????????????????? ??????????????????
                if (TextUtils.isEmpty(phoneNumber)) {
                    map.put("phonenum", phoneNumber);
                    continue;
                }
                // ?????????????????????
                contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                map.put("name", contactName);
            }
            phoneCursor.close(); // ??????????????????
        }
        return mContacts;
    }


    /**
     * ???drawable??????bitmap
     *
     * @param drawable
     * @return bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth(); // ???drawable?????????
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565; // ???drawable???????????????
        Bitmap bitmap = Bitmap.createBitmap(width, height, config); // ????????????bitmap
        Canvas canvas = new Canvas(bitmap); // ????????????bitmap?????????
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas); // ???drawable?????????????????????
        return bitmap;
    }

    /**
     * ??????drawable
     *
     * @param drawable
     * @param w        ???
     * @param h        ???
     * @return ????????????drawable
     */
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);// drawable?????????bitmap
        Matrix matrix = new Matrix(); // ????????????????????????Matrix??????
        float scaleWidth = ((float) width / w); // ??????????????????
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight); // ??????????????????
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true); // ????????????bitmap?????????????????????bitmap??????????????????
        Drawable drawable2 = new BitmapDrawable(newbmp);
        // newbmp.recycle();//??????
        return drawable2; // ???bitmap?????????drawable?????????
    }

    /**
     * ??????drawable
     *
     * @param w ???
     * @param h ???
     * @return ????????????drawable
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix(); // ????????????????????????Matrix??????
        float scaleWidth = ((float) w / width); // ??????????????????
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight); // ??????????????????
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true); // ????????????bitmap?????????????????????bitmap??????????????????
        // newbmp.recycle();//??????
        return newbmp; // ???bitmap?????????drawable?????????
    }

    /**
     * ??????????????????drawable
     *
     * @param drawable ???
     *                 ???
     * @return ????????????drawable
     */
    public static Drawable zoomDrawable(Drawable drawable, float scaleWidth,
                                        float scaleHeight) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);// drawable?????????bitmap
        Matrix matrix = new Matrix(); // ????????????????????????Matrix??????
        matrix.postScale(scaleWidth, scaleHeight); // ??????????????????
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true); // ????????????bitmap?????????????????????bitmap??????????????????
        Drawable drawable2 = new BitmapDrawable(newbmp);
        // newbmp.recycle();//??????
        return drawable2; // ???bitmap?????????drawable?????????
    }

    /**
     * ??????????????????
     * 1?????? 2??????
     */
    public static int[] getScreenWH(Activity activity) {
        if (wh != null && wh[0] != 0 && wh[1] != 0) {
            return wh;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = 0;
        int height = 0;
        width = displayMetrics.widthPixels;

        height = displayMetrics.heightPixels - getStatusBarHeight(activity);// ????????????????????????
        int[] is = {width, height};
        wh = is;
        return is;
    }

    /**
     * ??????????????????
     * <p>
     * 1?????? 2??????
     */
    public static int[] getScreenWH(Context context) {
        if (wh != null && wh[0] != 0 && wh[1] != 0) {
            return wh;
        }
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = 0;
        int height = 0;
        width = displayMetrics.widthPixels;

        height = displayMetrics.heightPixels - getStatusBarHeight(context);// ????????????????????????
        int[] is = {width, height};
        wh = is;
        return is;
    }

    /**
     * ????????????????????????
     */
    public static void openWebText(TextView textView, Context activity) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = textView.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) textView.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans(); // should clear old spans
            for (URLSpan url : urls) {
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL(), activity,
                        null);
                style.setSpan(myURLSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                style.setSpan(redSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.setText(style);
        }
    }

    /**
     * ????????????????????????
     */
    public static void openWebText(TextView textView, Context activity,
                                   String title) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = textView.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) textView.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans(); // should clear old spans
            for (URLSpan url : urls) {
                MyURLSpan myURLSpan = new MyURLSpan(url.getURL(), activity,
                        title);
                style.setSpan(myURLSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                style.setSpan(redSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.setText(style);
        }
    }

    /**
     * ????????????html???????????????
     */
    public static void openWebText(TextView textView, Context activity,
                                   ClickableSpan mSpan) {
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = textView.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable sp = (Spannable) textView.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans(); // should clear old spans
            for (URLSpan url : urls) {
                style.setSpan(mSpan, sp.getSpanStart(url), sp.getSpanEnd(url),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
                style.setSpan(redSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            textView.setText(style);
        }
    }

    /**
     * ??????textview????????????????????????
     *
     * @author ??????
     * @date 2014???12???26???
     * @?????? 1.0
     */
    public static class MyURLSpan extends ClickableSpan {
        private String mUrl;
        private Context activity;
        private String title;// ??????

        MyURLSpan(String url, Context activity, String title) {
            this.activity = activity;
            this.mUrl = url;
            this.title = title;
        }

        @Override
        public void onClick(View widget) {
            // Uri uri = Uri.parse(mUrl);
            // Intent it = new Intent(Intent.ACTION_VIEW, uri);
            // Intent it = new Intent(activity, Browsetheweb.class);
            // it.putExtra("url", mUrl);
            // if (title != null) {
            // it.putExtra("title", title);
            // }
            // activity.startActivity(it);
        }
    }

    /**
     * ??????textview???????????????
     *
     * @param start ????????????
     * @param end   ????????????
     * @param color ??????
     */
    public static void changTextViewColor(TextView textView, int start,
                                          int end, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView
                .getText().toString());
        // ForegroundColorSpan ?????????????????????BackgroundColorSpan??????????????????
        ForegroundColorSpan redSpan = new ForegroundColorSpan(color);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    /**
     * ??????????????????
     *
     * @param activity
     * @param view     ?????????view
     * @param sw       ???????????????????????????
     * @param sh       ???????????????????????????
     */
    public static void zoomView(Activity activity, View view, int sw, int sh) {
        view.setLayoutParams(new LinearLayout.LayoutParams(
                getScreenWH(activity)[0] / sw, getScreenWH(activity)[1] / sh));
    }

    /**
     * ???????????????????????????
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((\\+86)|(86))?1(3[0-9]|7[0-9]|8[0-9]|47|5[0-3]|5[5-9])\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * ?????????????????????
     *
     * @return
     */
    public static boolean isHongKongMobile(String mobiles) {
        Pattern p = Pattern
                .compile("^([0-9])\\d{7}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * ??????????????????????????????
     *
     * @param mobiles
     * @return
     */
    public static boolean isPwdNum(String mobiles) {
        Pattern p = Pattern
                .compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * ????????????????????????
     * mobiles
     *
     * @return
     */
    public static boolean isNum(String str) {
        Pattern p = Pattern
                .compile("^[0-9]*$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * ????????????????????????
     * mobiles
     *
     * @return
     */
    public static boolean isAbc(String str) {
        Pattern p = Pattern
                .compile("[a-zA-Z]");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * ?????????email??????
     *
     * @param mobiles
     * @return
     */
    public static boolean isEmailNum(String mobiles) {
        Pattern p = Pattern
                .compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * ???????????????
     *
     * @param content
     * @return
     */
    public static String hintStr(String content) {
        if (content.equals("") || content == null) {
            return content;
        }
        String str = null;
        if (content.length() > 3 && content.length() <= 6) {
            str = "***" + content.substring(3);
        } else if (content.length() <= 3) {
            str = "*" + content.substring(1);
        } else if (content.length() > 6 && content.length() <= 10) {
            str = "******" + content.substring(6);
        } else if (content.length() > 10) {
            str = "**********" + content.substring(9);
        }
        return str;
    }

//    /**
//     * ????????????????????????????????????????????????
//     */
//    public static void cleanCancle(Context context) {
//        try {
////			AbLogUtil.d(context, getFolderSize(new File(AbFileUtil.getCacheDownloadDir(context)))+"?????????????????????");
//            AbFileUtil.deleteFile(new File(AbFileUtil.getCacheDownloadDir(context)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * ?????????????????????
     *
     * @param file File??????
     * @return long ?????????M
     * @throws Exception
     */
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i]);
            } else {
                size = size + fileList[i].length();
            }
        }
        return size / (1024 * 1024);
    }

    /**
     * ????????????TextView??????????????????(??????)
     */
    public static float getTextViewLength(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        // ???????????????paint??????text?????????,???????????????
        float textLength = paint.measureText(text);
        return textLength;
    }

    /**
     * ??????sdk????????????
     */
    public static int getSDKCode() {
        int osVersion;
        try {
            osVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            osVersion = 0;
        }
        return osVersion;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * asset ??? string
     */
    public static String assetToString(Context context, String assetName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(assetName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**
     * ???????????????
     *
     * @return ????????????????????????
     */
    public static String getAppVersion(Context mContext) {
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(mContext.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void cleanSaveUserinfo(Context mContext) {
        cleanXML(mContext, login);
    }

    /**
     * ????????????????????????
     *
     * @return str
     */
    public static String DateToStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));//??????TimeZone???????????????
        Date now = new Date();//??????????????????
        try {
            now = sdf.parse(sdf.format(now));//????????????????????????????????????????????????
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String str = sdf.format(now);
        return str;
    }

    public static long getStringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /*
     * ???????????????????????????
     */
    public static String stampToDate(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    public static File uriToFile(Uri uri, Context context) {
        String path = null;
        if ("file".equals(uri.getScheme())) {
            path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA}, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0) {
                } else {
                    Uri u = Uri.parse("content://media/external/images/media/" + index);
                    System.out.println("temp uri is :" + u);
                }
            }
            if (path != null) {
                return new File(path);
            }
        } else if ("content".equals(uri.getScheme())) {
            // 4.2.2??????
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        } else {
            //Log.i(TAG, "Uri Scheme:" + uri.getScheme());
        }
        return null;
    }

    /**
     * base64??????
     *
     * @param bitmap
     */
    public void encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //???????????????ByteArrayOutputStream
        bitmap.compress(Bitmap.CompressFormat.PNG, 40, baos);
        //???????????????100??????????????????
        byte[] bytes = baos.toByteArray();
        String strbm = Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * base64??????
     *
     * @param bmMsg
     */
    public void sendImage(String bmMsg) {
        byte[] input = Base64.decode(bmMsg, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(input, 0, input.length);
    }
    public static String getValidUA(String userAgent){

        String validUA = "";
        String uaWithoutLine = userAgent.replace("\n", "");
        for (int i = 0, length = uaWithoutLine.length(); i < length; i++){
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                try {
                    validUA = URLEncoder.encode(uaWithoutLine, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return validUA;
            }
        }
        return uaWithoutLine;
    }
    /**
     * ??????????????????????????????????????????
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate){
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if(time != null){
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if(nowDate.equals(timeDate)){
                b = true;
            }
        }
        return b;
    }
    /**
     * ??????????????????????????????
     * @param sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };
//      ["page"] =&gt; string(1) "1"
//            ["order"] =&gt; string(0) ""
//            ["pageSize"] =&gt; string(2) "10"
//            ["get_number"] =&gt; string(1) "0"
//            ["skill_id"] =&gt; string(2) "36"
//}
//</pre><pre>string(41) "VnOOj5nz+a2c7efa9cbf05154cc89e8bbf18b325a"
//</pre><pre>string(10) "1626150031"
//</pre><pre>string(11) "xiaosong666"
//</pre><pre>string(56) "param:get_number=0&amp;order=&amp;page=1&amp;pageSize=10&amp;skill_id=36"
//</pre>string(65) "bef:get_number=0&order=&page=1&pageSize=10&skill_id=36xiaosong666"
//                        get_number=0&page=1&pageSize=10&skill_id=36xiaosong666
//<pre>string(36) "md5:d2965b15a8dc72f0d85307f6a497babf"
//</pre><pre>string(48) "???????????????:02965b15a8dc72f0d85307f6a497babf"
//</pre><pre>string(45) "????????????:02965b1568dc72f0d85307f6a497babf"
//</pre>{"code":0,"msg":"????????????","time":"1626150033","data":null}
    /**
     * URLEncoder??????
     */
    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }
        try {
            String str = new String(paramString.getBytes(), StandardCharsets.UTF_8);
            str = URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (Exception ignored) {
        }
        return "";
    }
    /**
     * sign ?????? ??????????????????????????? +key+MD5???
     *
     * @return
     */
    public static String createSign(SortedMap params, String key,long timeGetTime) {

        StringBuffer sbkey = new StringBuffer();
        // entrySet ??????????????????????????????????????????
        Set es = params.entrySet();
        Iterator it = es.iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            //???????????????????????????????????????
//            if (null != v && !"".equals(v)) {
                if(v.toString().endsWith(" ")||v.toString().startsWith(" ")){
                    v= toURLEncoded(v.toString().trim());
                }else {
                    v= toURLEncoded(v.toString());
                }

                    Log.v("this",k+"=="+v);
                sbkey.append(k + "=" + v + "&");
//            }
        }
        if(sbkey.length()>1){
            sbkey =  new StringBuffer( sbkey.substring(0,sbkey.length()-1));
        }
        //MD5??????,???????????????????????????
        String sign = null;
        if(sbkey.toString().length()<=0){
            Log.v("this","token=="+Tools.getSharedPreferencesValues(AppContext.getContext(), "token")+"===");
                String  token =  OkGo.getInstance().getCommonHeaders().get("token");
            if (!Tools.isEmpty(token)) {
                sbkey = sbkey.append( token);
            }else {
                sbkey = sbkey.append(timeGetTime);
            }
        }
        sbkey = sbkey.append( key);
        //        System.out.println(sbkey);
        Log.v("this","encodeByMD5BF=="+sbkey.toString());
            sign = md5(sbkey.toString());
        Log.v("this","encodeByMD5=="+sign);
        return sign;
    }
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
//        Log.v("this","UTF-8=="+string);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
