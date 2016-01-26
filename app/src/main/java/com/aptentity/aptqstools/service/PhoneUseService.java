package com.aptentity.aptqstools.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.application.MyReceiver;
import com.aptentity.aptqstools.model.db.DbHelper;
import com.aptentity.aptqstools.model.utils.TimeUtils;
import com.aptentity.aptqstools.utils.Common;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.view.TaskActivity;


public class PhoneUseService extends Service {
    private final String TAG = PhoneUseService.class.getSimpleName();
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
        LogHelper.show(TAG,"PhoneUseService->onCreate");
        am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        pm = this.getPackageManager();
        registerScreenReceiver();
        LogHelper.show(TAG,"PhoneUseService->onCreate end");
    }
    
    @Override
    public void onDestroy() {
        unregisterScreenReceiver();
        LogHelper.show("PhoneUseService->onDestroy");
        super.onDestroy();
    }

    private long timeUsed;
    private long timeThisTime;
    private String title;
    private String id;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogHelper.show(TAG, "PhoneUseService->onStartCommand");
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
        LogHelper.show(TAG, "PhoneUseService->onStartCommand end");

        boolean shown = intent.getBooleanExtra("showNotification",false);
        timeUsed = intent.getLongExtra("timeUsed",0);
        timeThisTime = intent.getLongExtra("timeThisTime",0);
        title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        showNotification(shown);
        return super.onStartCommand(intent, flags, startId);
    }

    private void getAppUse() {
        String newTask = Common.getCurrentPkgName(getApplicationContext());
        LogHelper.show(TAG,"newTask:"+newTask);
        if (oldTask==null){
            oldTask = newTask;
        }else if (!oldTask.equals(newTask)) {
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


    /**
     * 任务进度
     */
    private final int NOTIFICATION_FLAG = 1;
    private void showNotification(){
        LogHelper.show(TAG,"showNotification");
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("mode",TaskActivity.MODE_VIEW);
        intent.putExtra("task_id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        Notification notify = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText("This is the notification message")
                .setContentIntent(pendingIntent).setNumber(1).build(); // 需要注意build()是在API
        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify.flags |= Notification.FLAG_NO_CLEAR; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        manager.notify(NOTIFICATION_FLAG, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提
    }

    private void updateNotification(String text){
        LogHelper.show(TAG,"updateNotification");
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, TaskActivity.class);
        intent.putExtra("mode",TaskActivity.MODE_VIEW);
        intent.putExtra("task_id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API16之后才支持
        Notification notify = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent).setNumber(1).build(); // 需要注意build()是在API
        // level16及之后增加的，API11可以使用getNotificatin()来替代
        notify.flags |= Notification.FLAG_NO_CLEAR; // FLAG_AUTO_CANCEL表明当通知被用户点击时，通知将被清除。
        manager.notify(NOTIFICATION_FLAG, notify);// 步骤4：通过通知管理器来发起通知。如果id不同，则每click，在status哪里增加一个提
        //PendingIntent.FLAG_UPDATE_CURRENT
    }

    /**
     * 定时器
     */
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            handler.postDelayed(this, 1000);
            long time = System.currentTimeMillis();
            updateNotification("running:"+ TimeUtils.formatLongToTimeStr(timeUsed + (time - timeThisTime)));
        }
    };

    private void clearNotification(){
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_FLAG);
    }

    private void showNotification(boolean shown){
        if (shown){
            showNotification();
            handler.postDelayed(runnable, 1000);
        }else {
            handler.removeCallbacks(runnable);
            clearNotification();
        }
    }
}
