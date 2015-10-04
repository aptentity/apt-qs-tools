package com.aptentity.aptqstools.application;

import com.aptentity.aptqstools.db.LocationDBEntity;
import com.aptentity.aptqstools.utils.AptQsLog;
import com.aptentity.aptqstools.utils.Common;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import org.litepal.LitePalApplication;

public class QsApplication extends LitePalApplication{
    public LocationClient mLocationClient;
    public GeofenceClient mGeofenceClient;
    public MyLocationListener mMyLocationListener;
    public static QsApplication instance;

    public static QsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
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
            AptQsLog.v(sb.toString());
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
                entity.saveThrows();
            }catch (Exception e){
                AptQsLog.v(e.toString());
            }

            mLocationClient.stop();
        }
    }
}
