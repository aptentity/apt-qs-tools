package com.aptentity.aptqstools.model.dao;

import org.litepal.crud.DataSupport;

/*
 * 屏幕打开和关闭数据库对象
 */
public class ScreenDBEntity extends DataSupport{
    private String uuid="";//用户标示符
    private int type=0;//0为亮屏，1为灭屏
    private long timestamp=0;
    private String date="";
    
    public ScreenDBEntity(){
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

    public class ScreenStatusType{
        public static final int On=1;
        public static final int Off=2;
        public static final int Unlock=3;
    }
}
