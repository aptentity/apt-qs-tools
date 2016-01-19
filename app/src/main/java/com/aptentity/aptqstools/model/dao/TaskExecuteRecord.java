package com.aptentity.aptqstools.model.dao;

import cn.bmob.v3.BmobObject;

/**
 * Created by Gulliver(feilong) on 16/1/18.
 * 任务执行记录
 */
public class TaskExecuteRecord extends BmobObject {
    private String taskId="";//任务的id
    private long timeStart=0;//开始时间
    private long timeStop=0;//停止时间
    private long timeUsed=0;//使用时间

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeStop() {
        return timeStop;
    }

    public void setTimeStop(long timeStop) {
        this.timeStop = timeStop;
    }

    public long getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(long timeUsed) {
        this.timeUsed = timeUsed;
    }
}
