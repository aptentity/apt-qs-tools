package com.aptentity.aptqstools.model.dao;

import com.aptentity.aptqstools.model.Env;

import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobObject;

/*
 * 心率数据库对象
 */
public class HeartRateDBEntity extends BmobObject {
    private String uuid="";//用户标示符
    private float bmp=0;
    private long timestamp=0;
    private String type="";
    private String feeling=" 1";
    private String note=" 1";
    private String date="";
    private String phone= Env.PhoneID+":"+ Env.Model;
    
    public HeartRateDBEntity(){
    }
    
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public float getBmp() {
        return bmp;
    }

    public void setBmp(float bmp) {
        this.bmp = bmp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
