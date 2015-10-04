package com.aptentity.aptqstools.service;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IBinder;

import com.aptentity.aptqstools.application.MyReceiver;
import com.aptentity.aptqstools.db.AppUseDBEntity;
import com.aptentity.aptqstools.db.AppUseDBEntity.AppStatusType;
import com.aptentity.aptqstools.utils.AptQsLog;
import com.aptentity.aptqstools.utils.Common;

import java.util.Date;
import java.util.List;


public class PhoneUseService extends Service {

    private ActivityManager am = null;
    private PackageManager pm;
    private boolean brun = false;
    private String oldTask = "";
    private MyReceiver screenReceiver = new MyReceiver();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AptQsLog.show("PhoneUseService->onCreate");
        am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        pm = this.getPackageManager();
        registerScreenReceiver();
        AptQsLog.show("PhoneUseService->onCreate end");
    }
    
    @Override
    public void onDestroy() {
        unregisterScreenReceiver();
        AptQsLog.show("PhoneUseService->onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AptQsLog.show("PhoneUseService->onStartCommand");
        if (!brun) {
            brun = true;
            Thread th_monitor = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        getAppUse();
                    }
                }
            });
            th_monitor.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void getAppUse() {
        List<RecentTaskInfo> old = am.getRecentTasks(Integer.MAX_VALUE,
                ActivityManager.RECENT_IGNORE_UNAVAILABLE);
        if (old == null || old.size() == 0) {
            return;
        }
        String newTask = old.get(0).baseIntent.getComponent().getPackageName();
        if (!oldTask.equals(newTask)) {
            CharSequence c = "";
            CharSequence oldc = "";
            try {
                c = pm.getApplicationLabel(pm.getApplicationInfo(newTask,
                        PackageManager.GET_META_DATA));
                oldc = pm.getApplicationLabel(pm.getApplicationInfo(oldTask,
                        PackageManager.GET_META_DATA));
            } catch (Exception e) {
            }
            
            // db.insertAppOff(oldTask);
            // db.insertAppOpen(newTask);
            AptQsLog.show("close:" + oldTask);

            AppUseDBEntity ae = new AppUseDBEntity();
            ae.setAppName(oldc.toString());
            java.text.DateFormat format = new java.text.SimpleDateFormat(
                    Common.FOMAT);
            ae.setDate(format.format(new Date()));
            ae.setPackageName(oldTask);
            ae.setTimestamp(System.currentTimeMillis());
            ae.setType(AppStatusType.Close);
            ae.saveThrows();

            AptQsLog.show("open:" + newTask + ":" + c.toString());
            AppUseDBEntity ae1 = new AppUseDBEntity();
            ae1.setAppName(c.toString());
            ae1.setDate(format.format(new Date()));
            ae1.setPackageName(newTask);
            ae1.setTimestamp(System.currentTimeMillis());
            ae1.setType(AppStatusType.Open);
            ae1.saveThrows();
            
            oldTask = newTask;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void registerScreenReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenReceiver, filter);
    }

    private void unregisterScreenReceiver() {
        unregisterReceiver(screenReceiver);
    }
}
