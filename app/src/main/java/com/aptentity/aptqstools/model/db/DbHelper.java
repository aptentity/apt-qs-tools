package com.aptentity.aptqstools.model.db;

import com.aptentity.aptqstools.application.QsApplication;
import com.aptentity.aptqstools.model.dao.AppUseDBEntity;
import com.aptentity.aptqstools.model.dao.AppUseOnlineDBEntity;
import com.aptentity.aptqstools.model.dao.ScreenDBEntity;
import com.aptentity.aptqstools.model.dao.ScreenOnlineDBEntity;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.utils.Common;

import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Gulliver(feilong) on 15/10/4.
 */
public class DbHelper {
    private static final String TAG = DbHelper.class.getSimpleName();
    public static void saveScreenOff(){
        try{
            LogHelper.show(TAG,"saveScreenOff");
            ScreenDBEntity se = new ScreenDBEntity();
            se.setUuid(Common.UUID);
            java.text.DateFormat format = new java.text.SimpleDateFormat(
                    Common.FOMAT);
            se.setDate(format.format(new Date()));
            se.setTimestamp(System.currentTimeMillis());
            se.setType(ScreenDBEntity.ScreenStatusType.Off);
            saveScreen(se);
        }catch (Exception e){
            LogHelper.show("apt-qs screen off exception:" + e.toString());
        }
    }

    public static void saveScreenLockOff(){
        try{
            LogHelper.show(TAG, "saveScreenLockOff");
            ScreenDBEntity se = new ScreenDBEntity();
            se.setUuid(Common.UUID);
            java.text.DateFormat format = new java.text.SimpleDateFormat(
                    Common.FOMAT);
            se.setDate(format.format(new Date()));
            se.setTimestamp(System.currentTimeMillis());
            se.setType(ScreenDBEntity.ScreenStatusType.Unlock);
            saveScreen(se);
        }catch (Exception e){
            LogHelper.show("apt-qs unlock screen exception:" + e.toString());
        }
    }


    public static void saveScreenOn(){
        try{
            LogHelper.show(TAG, "saveScreenOn");
            ScreenDBEntity se = new ScreenDBEntity();
            se.setUuid(Common.UUID);
            java.text.DateFormat format = new java.text.SimpleDateFormat(
                    Common.FOMAT);
            se.setDate(format.format(new Date()));
            se.setTimestamp(System.currentTimeMillis());
            se.setType(ScreenDBEntity.ScreenStatusType.On);
            saveScreen(se);
        }catch (Exception e){
            LogHelper.show("apt-qs screen on exception:" + e.toString());
        }
    }

    private static void saveScreen(final ScreenDBEntity se){
        LogHelper.show(TAG, "saveScreen");
        ScreenOnlineDBEntity entity = new ScreenOnlineDBEntity(se);
        entity.save(QsApplication.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "saveScreen online onSuccess");
                se.save();
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "saveScreen online onFailure");
            }
        });
    }

    /**
     * 保存app关闭
     * @param packageName
     * @param appName
     */
    public static void saveAppClose(String packageName,String appName){
        LogHelper.show("apt-qs close app : " + packageName + ":" + appName);
        AppUseDBEntity ae = new AppUseDBEntity();
        ae.setAppName(appName);
        java.text.DateFormat format = new java.text.SimpleDateFormat(Common.FOMAT);
        ae.setDate(format.format(new Date()));
        ae.setPackageName(packageName);
        ae.setTimestamp(System.currentTimeMillis());
        ae.setType(AppUseDBEntity.AppStatusType.Close);
        saveAppUse(ae);
    }

    /**
     * 保存app打开
     * @param packageName
     * @param appName
     */
    public static void saveAppOpen(String packageName,String appName){
        LogHelper.show(TAG, "saveAppOpen" + packageName + ":" + appName);
        java.text.DateFormat format = new java.text.SimpleDateFormat(Common.FOMAT);
        final AppUseDBEntity ae = new AppUseDBEntity();
        ae.setAppName(appName);
        ae.setDate(format.format(new Date()));
        ae.setPackageName(packageName);
        ae.setTimestamp(System.currentTimeMillis());
        ae.setType(AppUseDBEntity.AppStatusType.Open);
        saveAppUse(ae);
    }

    /**
     * 保存app使用
     * @param ae
     */
    private static void saveAppUse(final AppUseDBEntity ae){
        LogHelper.show(TAG, "saveAppUse");
        AppUseOnlineDBEntity entity = new AppUseOnlineDBEntity(ae);
        entity.save(QsApplication.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "saveAppUse online onSuccess");
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "saveAppUse online onFailure");
                ae.save();
            }
        });
    }
}
