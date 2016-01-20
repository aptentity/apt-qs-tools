package com.aptentity.aptqstools.model;

import android.os.Build;

/**
 * Created by Gulliver(feilong) on 16/1/20.
 */
public class Env {
    public static final String Model = Build.MODEL;
    public static String PhoneID="";//唯一的设备ID，GSM手机的 IMEI 和 CDMA手机的 MEID
}
