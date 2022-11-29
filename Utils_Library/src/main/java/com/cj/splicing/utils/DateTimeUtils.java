package com.cj.splicing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间
 */
public final class DateTimeUtils {

    /**
     * 获取当前详细时间
     * @return 2022-10-19 14:14:15
     */
    public static String getDateTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String time = simpleDateFormat.format(new Date()).toString();
        return time;
    }


    /**
     * 获取当前年月
     * @return 2022-10-19
     */
    public static String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = simpleDateFormat.format(new Date()).toString();
        return time;
    }

    /**
     * 获取当前年月
     * @return 2022-10-19
     */
    public static String getDate2(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String time = simpleDateFormat.format(new Date()).toString();
        return time;
    }

    /**
     * 时间戳生成Id
     * @return 20221019141313
     */
    public static String getId(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        String id = simpleDateFormat.format(new Date()).toString();
        return id;
    }
}
