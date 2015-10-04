package com.aptentity.aptqstools.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aptentity.aptqstools.db.ScreenDBEntity;
import com.aptentity.aptqstools.utils.AptQsLog;
import com.aptentity.aptqstools.utils.Common;

import java.util.Date;

/**
 * Created by Gulliver(feilong) on 15/10/4.
 */
public class MyReceiver extends BroadcastReceiver{
    private long on = 0;
    @Override
    public void onReceive(Context context, Intent intent) {
        AptQsLog.v("MyReceiver --> onReceive: " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            saveScreenOn();
            //get location
            //((MyApplication)getApplication()).mLocationClient.start();

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            saveScreenOff();
        }else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            saveScreenLockOff();
        }
    }

    private void saveScreenOn(){
        try{
            on = System.currentTimeMillis();
            AptQsLog.show("apt-qs screen on");
            ScreenDBEntity se = new ScreenDBEntity();
            se.setUuid(Common.UUID);
            java.text.DateFormat format = new java.text.SimpleDateFormat(
                    Common.FOMAT);
            se.setDate(format.format(new Date()));
            se.setTimestamp(System.currentTimeMillis());
            se.setType(ScreenDBEntity.ScreenStatusType.On);
            se.saveThrows();
        }catch (Exception e){
            AptQsLog.show("apt-qs screen on exception:"+e.toString());
        }
    }

    private void saveScreenOff(){
        try{
            AptQsLog.show("apt-qs screen off:" + (System.currentTimeMillis() - on));
            ScreenDBEntity se = new ScreenDBEntity();
            se.setUuid(Common.UUID);
            java.text.DateFormat format = new java.text.SimpleDateFormat(
                    Common.FOMAT);
            se.setDate(format.format(new Date()));
            se.setTimestamp(System.currentTimeMillis());
            se.setType(ScreenDBEntity.ScreenStatusType.Off);
            se.saveThrows();
        }catch (Exception e){
            AptQsLog.show("apt-qs screen off exception:"+e.toString());
        }
    }

    private void saveScreenLockOff(){
        try{
            AptQsLog.show("apt-qs unlock screen:" + (System.currentTimeMillis() - on));
            ScreenDBEntity se = new ScreenDBEntity();
            se.setUuid(Common.UUID);
            java.text.DateFormat format = new java.text.SimpleDateFormat(
                    Common.FOMAT);
            se.setDate(format.format(new Date()));
            se.setTimestamp(System.currentTimeMillis());
            se.setType(ScreenDBEntity.ScreenStatusType.Unlock);
            se.saveThrows();
        }catch (Exception e){
            AptQsLog.show("apt-qs unlock screen exception:"+e.toString());
        }
    }
}
