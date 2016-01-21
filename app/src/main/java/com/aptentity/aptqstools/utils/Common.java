package com.aptentity.aptqstools.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Field;
import java.util.List;

public class Common {
    public static final String UUID="aptentity";
    public static final String FOMAT="yyyyMMddHHmmss";
    public static final String FOMAT2="yyyyMMdd";
    private final String TAG = Common.class.getSimpleName();

    public static void startPhoneUseService(Context context){
        LogHelper.show("Common --> startPhoneUseService");
        Intent i = new Intent("com.apt.PhoneUseService");
        i.setPackage(context.getPackageName());
        context.startService(i);
        LogHelper.show("Common --> startPhoneUseService end");
    }

    /**
     * mx4 pro 5.6.1.19无法获取
     * @param context
     * @return
     */
    public static String getCurrentPkgName(Context context) {
        if (getSDKVersionNumber()>20){
            ActivityManager.RunningAppProcessInfo currentInfo = null;
            Field field = null;
            int START_TASK_TO_FRONT = 2;
            String pkgName = null;
            try {
                field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            } catch (Exception e) { e.printStackTrace(); }
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo app : appList) {
                if (app.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Integer state = null;
                    try {
                        state = field.getInt( app );
                    } catch (Exception e) { e.printStackTrace(); }
                    if (state != null && state == START_TASK_TO_FRONT) {
                        currentInfo = app;
                        break;
                    }
                }
            }
            if (currentInfo != null) {
                pkgName = currentInfo.processName;
            }
            return pkgName;
        }else{
            return getCurrentPkgName2(context);
        }

    }

    /**
     *
     * @param context
     * @return
     */
    public static String getCurrentPkgName2(Context context){
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RecentTaskInfo> old = am.getRecentTasks(Integer.MAX_VALUE,
                ActivityManager.RECENT_IGNORE_UNAVAILABLE);
        if (old == null || old.size() == 0) {
            return "";
        }
        String newTask = old.get(0).baseIntent.getComponent().getPackageName();
        return newTask;
    }

    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = android.os.Build.VERSION.SDK_INT;
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }
}
