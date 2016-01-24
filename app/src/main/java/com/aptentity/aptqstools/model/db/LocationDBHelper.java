package com.aptentity.aptqstools.model.db;

import com.aptentity.aptqstools.application.QsApplication;
import com.aptentity.aptqstools.model.dao.LocationDBEntity;
import com.aptentity.aptqstools.model.dao.LocationOnlineDBEntity;
import com.aptentity.aptqstools.utils.LogHelper;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Gulliver(feilong) on 16/1/20.
 */
public class LocationDBHelper {
    private static final String TAG = LocationDBHelper.class.getSimpleName();

    /**
     * 保存位置信息，首先保存到服务器，如果保存失败则保存到本地
     * @param entity
     */
    public static void save(final LocationDBEntity entity){
        LogHelper.show(TAG,"save");
        LocationOnlineDBEntity onlinDBEntity = new LocationOnlineDBEntity(entity);
        onlinDBEntity.save(QsApplication.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG,"save online onSuccess");
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG,"save online onFailure");
                if (entity.save()){
                    LogHelper.show(TAG,"save location onSuccess");
                }else {
                    LogHelper.show(TAG,"save location onFailure");
                }
            }
        });
    }

    /**
     * 将本地数据上传
     */
    public static boolean isUpload = false;
    public static void upload(){
        LogHelper.show(TAG, "upload");
        //有可能同时受到多个广播，会执行多次
        if (isUpload){
            LogHelper.show(TAG, "isUploading");
            return;
        }
        final List<LocationDBEntity> allNews = DataSupport.limit(50).find(LocationDBEntity.class);
        if (allNews==null||allNews.size()<1){
            LogHelper.show(TAG,"no data to upload");
            return;
        }
        isUpload = true;
        List<BmobObject> locations = new ArrayList<BmobObject>();
        for (LocationDBEntity entity:allNews) {
            LocationOnlineDBEntity online = new LocationOnlineDBEntity(entity);
            locations.add(online);
        }

        new BmobObject().insertBatch(QsApplication.getContext(), locations, new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "uploade onSuccess");
                for (LocationDBEntity entity : allNews) {
                    entity.delete();
                }
                isUpload = false;
                upload();
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "uploade onFailure:" + i + ":" + s);
                isUpload = false;
            }
        });
    }
}
