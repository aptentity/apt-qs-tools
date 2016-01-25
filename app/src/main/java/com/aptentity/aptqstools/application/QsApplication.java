package com.aptentity.aptqstools.application;

import android.content.Context;
import android.os.Handler;
import android.telephony.TelephonyManager;

import com.aptentity.aptqstools.model.Env;
import com.aptentity.aptqstools.model.dao.LocationDBEntity;
import com.aptentity.aptqstools.model.db.LocationDBHelper;
import com.aptentity.aptqstools.utils.Common;
import com.aptentity.aptqstools.utils.LogHelper;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;

public class QsApplication extends LitePalApplication{
    public LocationClient mLocationClient;
    public GeofenceClient mGeofenceClient;
    public MyLocationListener mMyLocationListener;
    public static QsApplication instance;
    private static Context mContext;
    public static QsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        instance = this;
        Bmob.initialize(this, "22223a5f4df26be225ba36877a9445ae");
        //启动service
        Common.startPhoneUseService(getApplicationContext());

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);
        option.setCoorType("gcj02");
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mMainThreadHandler = new Handler();

        //获取IMEI
        try{
            TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
            Env.PhoneID = tm.getDeviceId();
        }catch (Exception e){
            LogHelper.show(e.getMessage());
        }

    }
    private static Handler mMainThreadHandler = null;
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }
    public static Context getAppContext() {
        return mContext;
    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\ndirection : ");
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append(location.getDirection());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                location.getCity();
                sb.append(location.getAddrStr());
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
            }
            LogHelper.v(sb.toString());
            try{
                LocationDBEntity entity = new LocationDBEntity();
                entity.setAddress(location.getAddrStr());
                entity.setCity(location.getCity());
                entity.setCityCode(location.getCityCode());
                entity.setDistrict(location.getDistrict());
                entity.setLatitude(location.getLatitude());
                entity.setLongitude(location.getLongitude());
                entity.setProvince(location.getProvince());
                entity.setRadius(location.getRadius());
                entity.setStreet(location.getStreet());
                entity.setStreetNumber(location.getStreetNumber());
                entity.setTime(location.getTime());
                entity.setTimestamp(System.currentTimeMillis());
                LocationDBHelper.save(entity);
            }catch (Exception e){
                LogHelper.v(e.toString());
            }

            mLocationClient.stop();
        }
    }
}
