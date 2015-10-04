package com.aptentity.aptqstools.db;

import com.aptentity.aptqstools.utils.AptQsLog;
import com.aptentity.aptqstools.utils.Common;

import java.util.Date;

/**
 * Created by Gulliver(feilong) on 15/10/4.
 */
public class DbHelper {
    private static long on = System.currentTimeMillis();
    public static void saveScreenOff(){
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

    public static void saveScreenLockOff(){
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


    public static void saveScreenOn(){
        try{
            on = System.currentTimeMillis();
            AptQsLog.show("apt-qs screen on:");
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

    public static void saveAppClose(String packageName,String appName){
        AptQsLog.show("apt-qs close app : " + packageName+":"+appName);
        AppUseDBEntity ae = new AppUseDBEntity();
        ae.setAppName(appName);
        java.text.DateFormat format = new java.text.SimpleDateFormat(Common.FOMAT);
        ae.setDate(format.format(new Date()));
        ae.setPackageName(packageName);
        ae.setTimestamp(System.currentTimeMillis());
        ae.setType(AppUseDBEntity.AppStatusType.Close);
        ae.saveThrows();
    }

    public static void saveAppOpen(String packageName,String appName){
        AptQsLog.show("apt-qs open app : " + packageName + ":" + appName);
        java.text.DateFormat format = new java.text.SimpleDateFormat(Common.FOMAT);
        AppUseDBEntity ae1 = new AppUseDBEntity();
        ae1.setAppName(appName);
        ae1.setDate(format.format(new Date()));
        ae1.setPackageName(packageName);
        ae1.setTimestamp(System.currentTimeMillis());
        ae1.setType(AppUseDBEntity.AppStatusType.Open);
        ae1.saveThrows();
    }
}
