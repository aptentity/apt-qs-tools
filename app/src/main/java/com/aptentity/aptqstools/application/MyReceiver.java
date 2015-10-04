package com.aptentity.aptqstools.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aptentity.aptqstools.utils.AptQsLog;

/**
 * Created by Gulliver(feilong) on 15/10/4.
 */
public class MyReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        AptQsLog.v("MyReceiver --> onReceive: " + intent.getAction());
        Intent i = new Intent("com.apt.PhoneUseService");
        AptQsLog.v(QsApplication.getContext().getPackageName());
        i.setPackage(QsApplication.getContext().getPackageName());
        context.startService(i);
    }
}
