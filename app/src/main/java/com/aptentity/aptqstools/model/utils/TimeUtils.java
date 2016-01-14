package com.aptentity.aptqstools.model.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gulliver on 16/1/3.
 */
public class TimeUtils {
    public static final String FOMAT="yyyy/MM/dd HH:mm:ss";
    public static String transferLongToDate(Long millSec) {
        return transferLongToDate(FOMAT,millSec);
    }
    public static String transferLongToDate(String dateFormat,Long millSec){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date= new Date(millSec);
        return sdf.format(date);
    }

    /**
     * 毫秒转换为时分秒
     * @param l
     * @return
     */
    public static String formatLongToTimeStr(Long l) {
        int hour = 0;
        int minute = 0;
        int second = 0;

        second = l.intValue() / 1000;

        if (second > 60) {
            minute = second / 60;
            second = second % 60;
        }
        if (minute > 60) {
            hour = minute / 60;
            minute = minute % 60;
        }
        return (getTwoLength(hour) + ":" + getTwoLength(minute)  + ":"  + getTwoLength(second));
    }

    private static String getTwoLength(final int data) {
        if (data < 10) {
            return "0" + data;
        } else {
            return "" + data;
        }
    }
}
