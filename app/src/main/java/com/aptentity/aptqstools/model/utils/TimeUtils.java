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
}
