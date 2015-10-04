package com.aptentity.aptqstools.db;
/**
 * 心跳场景类型
 * @author pc
 *
 */
public class HeartRateSceneType {
    public static final String[] SCENETYPE={"常规","饭前","饭后","睡觉前","运动前","运动后","起床后"};
    public final static int UNKNOWN=0;//未知
    public final static int ROUTINE =1;//常规
    public final static int RESTING=2;//静息心率
    public final static int BEFORESPORT=3;//运动前
    public final static int AFTERSPORT=4;//运动后
    public final static int MAX=5;//最大心率
}
