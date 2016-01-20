package com.aptentity.aptqstools.application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.aptentity.aptqstools.model.db.DbHelper;
import com.aptentity.aptqstools.model.db.LocationDBHelper;
import com.aptentity.aptqstools.model.utils.NetworkUtils;
import com.aptentity.aptqstools.utils.LogHelper;

/**
 * Created by Gulliver(feilong) on 16/1/20.
 * 网络连通后检查是否有本地数据，如果有则上传
 */
public class NetworkReceiver extends BroadcastReceiver{
    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private final String TAG = NetworkReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogHelper.show(TAG, "NetworkReceiver receive action " + action);
        if(TextUtils.equals(action, CONNECTIVITY_CHANGE_ACTION)){ //网络变化的时候会发送通知
            LogHelper.show(TAG,"net work changed");
            if (NetworkUtils.isWifiConnected(context)){
                LocationDBHelper.upload();
                DbHelper.upload();
            }
            return;
        }
    }
}
