package com.aptentity.aptqstools.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.IBinder;

import com.aptentity.aptqstools.application.MyReceiver;
import com.aptentity.aptqstools.db.DbHelper;
import com.aptentity.aptqstools.utils.AptQsLog;
import com.aptentity.aptqstools.utils.Common;


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
        AptQsLog.show("PhoneUseService->onStartCommand end");
        return super.onStartCommand(intent, flags, startId);
    }

    private void getAppUse() {
        String newTask = Common.getCurrentPkgName(getApplicationContext());
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

            DbHelper.saveAppClose(oldTask,oldc.toString());
            DbHelper.saveAppOpen(newTask,c.toString());
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
