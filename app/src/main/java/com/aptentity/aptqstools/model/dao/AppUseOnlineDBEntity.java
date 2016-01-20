package com.aptentity.aptqstools.model.dao;

import com.aptentity.aptqstools.model.Env;

import cn.bmob.v3.BmobObject;

/**
 * Created by Gulliver(feilong) on 16/1/20.
 */
public class AppUseOnlineDBEntity extends BmobObject{
    private String uuid="";//用户标示符
    private int type=0;
    private String packageName="";
    private String appName="";
    private long timestamp=0;
    private String date="";
    private String phone= Env.PhoneID+":"+ Env.Model;

    public AppUseOnlineDBEntity(){
    }

    public AppUseOnlineDBEntity(AppUseDBEntity entity){
        uuid = entity.getUuid();
        type = entity.getType();
        packageName = entity.getPackageName();
        appName = entity.getAppName();
        timestamp = entity.getTimestamp();
        date = entity.getDate();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public class AppStatusType{
        public static final int Open=1;
        public static final int Close=2;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
