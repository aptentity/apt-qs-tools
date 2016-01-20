package com.aptentity.aptqstools.model.dao;

import com.aptentity.aptqstools.model.Env;

import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobObject;

/*
 * 屏幕打开和关闭数据库对象
 */
public class ScreenOnlineDBEntity extends BmobObject{
    private String uuid="";//用户标示符
    private int type=0;//0为亮屏，1为灭屏
    private long timestamp=0;
    private String date="";
    private String phone= Env.PhoneID+":"+ Env.Model;

    public ScreenOnlineDBEntity(){
    }

    public ScreenOnlineDBEntity(ScreenDBEntity entity){
        uuid = entity.getUuid();
        type = entity.getType();
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public class ScreenStatusType{
        public static final int On=1;
        public static final int Off=2;
        public static final int Unlock=3;
    }
}
