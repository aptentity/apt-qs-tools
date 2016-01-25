package com.aptentity.aptqstools.utils;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Field;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

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
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    String pkgName = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                    return pkgName;
                }
            }
        }
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
