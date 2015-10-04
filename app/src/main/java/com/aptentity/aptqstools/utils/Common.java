package com.aptentity.aptqstools.utils;

import android.content.Context;
import android.content.Intent;

public class Common {
    public static final String UUID="aptentity";
    public static final String FOMAT="yyyyMMddHHmmss";
    public static final String FOMAT2="yyyyMMdd";

    public static void startPhoneUseService(Context context){
        AptQsLog.v("Common --> startPhoneUseService");
        Intent i = new Intent("com.apt.PhoneUseService");
        i.setPackage(context.getPackageName());
        context.startService(i);
        AptQsLog.v("Common --> startPhoneUseService end");
    }
}
