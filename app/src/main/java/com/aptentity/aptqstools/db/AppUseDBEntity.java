package com.aptentity.aptqstools.db;

import org.litepal.crud.DataSupport;

/*
 * 屏幕打开和关闭数据库对象
 */
public class AppUseDBEntity extends DataSupport{
    private String uuid="";//用户标示符
    private int type=0;
    private String packageName="";
    private String appName="";
    private long timestamp=0;
    private String date="";
    
    public AppUseDBEntity(){
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
}
