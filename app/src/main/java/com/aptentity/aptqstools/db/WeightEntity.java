package com.aptentity.aptqstools.db;
/**
 * 体重数据
 */
public class WeightEntity {
    public String uuid="";//用户标示符
    public float weight=0;//体重
    public long timestamp=0;//时间戳，测试时间
    public long lastmodify=0;//修改时间
    public String type="";//场景
    public String feeling="123";//测试时情绪
    public String note="123";//备注
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(weight).append(';').append(timestamp).append(';')
        .append(type).append(';').append(feeling).append(';')
        .append(note);
        return sb.toString();
    }
}
