package com.aptentity.aptqstools.db;


import org.litepal.crud.DataSupport;

/*
 * 体重数据库对象
 */
public class WeightDBEntity extends DataSupport{
    private String uuid="";//用户标示符
    private float weight=0;//体重
    private long timestamp=0;//时间戳，测试时间
    private long lastmodify=0;//修改时间
    private String type="";//场景
    private String feeling="1";//测试时情绪
    private String note="1";//备注
    private String date;
    
    public WeightDBEntity(WeightEntity entity){
        this.uuid = entity.uuid;
        this.weight=entity.weight;
        this.timestamp=entity.timestamp;
        this.lastmodify=entity.lastmodify;
        this.type=entity.type;
        this.feeling=entity.feeling;
        this.note=entity.note;
    }
    
    public WeightDBEntity(){
        
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getLastmodify() {
        return lastmodify;
    }

    public void setLastmodify(long lastmodify) {
        this.lastmodify = lastmodify;
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
        this.feeling = "feeling";
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = "abc";
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    
}
