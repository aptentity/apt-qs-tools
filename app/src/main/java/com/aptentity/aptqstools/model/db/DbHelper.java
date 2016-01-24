package com.aptentity.aptqstools.model.db;

import android.text.TextUtils;

import com.aptentity.aptqstools.application.QsApplication;
import com.aptentity.aptqstools.model.dao.AppUseDBEntity;
import com.aptentity.aptqstools.model.dao.AppUseOnlineDBEntity;
import com.aptentity.aptqstools.model.dao.ScreenDBEntity;
import com.aptentity.aptqstools.model.dao.ScreenOnlineDBEntity;
import com.aptentity.aptqstools.utils.Common;
import com.aptentity.aptqstools.utils.LogHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobObject;
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
            LogHelper.show(TAG,"apt-qs screen on exception:" + e.toString());
        }
    }

    private static void saveScreen(final ScreenDBEntity se){
        LogHelper.show(TAG, "saveScreen");
        ScreenOnlineDBEntity entity = new ScreenOnlineDBEntity(se);
        entity.save(QsApplication.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "saveScreen online onSuccess");

            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "saveScreen online onFailure");
                se.save();
            }
        });
    }

    /**
     * 保存app关闭
     * @param packageName
     * @param appName
     */
    public static void saveAppClose(String packageName,String appName){
        LogHelper.show(TAG,"apt-qs close app : " + packageName + ":" + appName);
        if (TextUtils.isEmpty(packageName))
            return;
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
        LogHelper.show(TAG, "saveAppOpen " + packageName + ":" + appName);
        if (TextUtils.isEmpty(packageName))
            return;
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

    /**
     * 将本地数据上传
     */
    public static void upload(){
        uploadScreenData();
        uploadAppData();
    }
    private static boolean isUploadScreenData = false;
    private static void uploadScreenData(){
        LogHelper.show(TAG, "uploadScreenData");
        //有可能同时受到多个广播，会执行多次
        if (isUploadScreenData){
            LogHelper.show(TAG, "ScreenData isUploading");
            return;
        }

        final List<ScreenDBEntity> allNews = DataSupport.limit(50).find(ScreenDBEntity.class);
        if (allNews==null||allNews.size()<1){
            LogHelper.show(TAG,"ScreenData no data to upload");
            return;
        }
        isUploadScreenData = true;
        List<BmobObject> locations = new ArrayList<BmobObject>();
        for (ScreenDBEntity entity:allNews) {
            ScreenOnlineDBEntity online = new ScreenOnlineDBEntity(entity);
            locations.add(online);
        }

        new BmobObject().insertBatch(QsApplication.getContext(), locations, new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "ScreenData uploade onSuccess");
                for (ScreenDBEntity entity : allNews) {
                    entity.delete();
                }
                isUploadScreenData = false;
                uploadScreenData();
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "ScreenData uploade onFailure:" + i + ":" + s);
                isUploadScreenData = false;
            }
        });
    }

    private static boolean isUploadAppData = false;
    private static void uploadAppData(){
        LogHelper.show(TAG, "uploadAppData");
        //有可能同时受到多个广播，会执行多次
        if (isUploadAppData){
            LogHelper.show(TAG, "AppData isUploading");
            return;
        }
        final List<AppUseDBEntity> allNews = DataSupport.limit(50).find(AppUseDBEntity.class);
        if (allNews==null||allNews.size()<1){
            LogHelper.show(TAG,"AppData no data to upload");
            return;
        }
        isUploadAppData = true;
        List<BmobObject> locations = new ArrayList<BmobObject>();
        for (AppUseDBEntity entity:allNews) {
            AppUseOnlineDBEntity online = new AppUseOnlineDBEntity(entity);
            locations.add(online);
        }

        new BmobObject().insertBatch(QsApplication.getContext(), locations, new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "AppData uploade onSuccess");
                for (AppUseDBEntity entity : allNews) {
                    entity.delete();
                }
                isUploadAppData = false;
                uploadAppData();
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "AppData uploade onFailure:" + i + ":" + s);
                isUploadAppData = false;
            }
        });
    }
}
