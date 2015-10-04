package com.aptentity.aptqstools.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aptentity.aptqstools.db.DbHelper;
import com.aptentity.aptqstools.utils.AptQsLog;

/**
 * Created by Gulliver(feilong) on 15/10/4.
 */
public class MyReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        AptQsLog.v("MyReceiver --> onReceive: " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            DbHelper.saveScreenOn();
            //get location
            QsApplication.getInstance().mLocationClient.start();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            DbHelper.saveScreenOff();
        }else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            DbHelper.saveScreenLockOff();
        }
    }
}
