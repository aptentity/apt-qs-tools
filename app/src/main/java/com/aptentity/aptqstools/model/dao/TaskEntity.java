package com.aptentity.aptqstools.model.dao;

import org.litepal.crud.DataSupport;

/**
 * Created by gulliver on 16/1/3.
 * 任务
 */
public class TaskEntity  extends DataSupport {
    public static final int STATUS_NORMAL=0;//正常状态
    public static final int STATUS_DELETED=1;//被删除
    public static final int STATUS_COMPLETE=2;//完成
    public static final int STATUS_RUNNING=3;//正在计时
    public static final int STATUS_PAUSE=4;//暂停
    private String title="";//标题
    private String description="";//描述
    private String target="";//目标
    private String step="";//步骤
    private long importantIdex=0;//重要程度,1~3,1-低、2-中、3-高
    private long urgentIdex=0;//紧急程度,1~3,1-低、2-中、3-高
    private long timeUsed=0;//使用的时长
    private long timeEstimated=0;//估计的时长
    private long timeStart=0;//开始时间
    private long timeEnd=0;//结束时间
    private long timeStartEstimated=0;
    private long timeEndEstimated=0;
    private String timeStartS="";//开始时间
    private String timeEndS="";//结束时间
    private int score=0;//分数
    private int status=0;//状态
    private long timestamp=0;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descripe) {
        this.description = descripe;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public long getImportantIdex() {
        return importantIdex;
    }

    public void setImportantIdex(long importantIdex) {
        this.importantIdex = importantIdex;
    }

    public long getUrgentIdex() {
        return urgentIdex;
    }

    public void setUrgentIdex(long urgentIdex) {
        this.urgentIdex = urgentIdex;
    }

    public long getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(long timeUsed) {
        this.timeUsed = timeUsed;
    }

    public long getTimeEstimated() {
        return timeEstimated;
    }

    public void setTimeEstimated(long timeEstimated) {
        this.timeEstimated = timeEstimated;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public long getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(long timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTimeStartS() {
        return timeStartS;
    }

    public void setTimeStartS(String timeStartS) {
        this.timeStartS = timeStartS;
    }

    public String getTimeEndS() {
        return timeEndS;
    }

    public void setTimeEndS(String timeEndS) {
        this.timeEndS = timeEndS;
    }

    public long getTimeStartEstimated() {
        return timeStartEstimated;
    }

    public void setTimeStartEstimated(long timeStartEstimated) {
        this.timeStartEstimated = timeStartEstimated;
    }

    public long getTimeEndEstimated() {
        return timeEndEstimated;
    }

    public void setTimeEndEstimated(long timeEndEstimated) {
        this.timeEndEstimated = timeEndEstimated;
    }

    @Override
    public long getBaseObjId() {
        return super.getBaseObjId();
    }

    @Override
    public String toString() {
        return title+";"+description+";"+target+";"+step;
    }
}
