package com.aptentity.aptqstools.presenter;

import com.aptentity.aptqstools.model.dao.TaskEntity;
import com.aptentity.aptqstools.model.utils.TimeUtils;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.view.api.ITaskActivity;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.GetListener;

/**
 * Created by gulliver on 16/1/3.
 */
public class TaskPresenter {
    private final String TAG = TaskPresenter.class.getSimpleName();
    private ITaskActivity activity;
    public TaskPresenter(ITaskActivity activity){
        this.activity = activity;
    }

    public void getTask(String taskId,GetListener<TaskEntity> callback){
        BmobQuery<TaskEntity> query = new BmobQuery<TaskEntity>();
        query.getObject(activity.getContext(), taskId, callback);
    }

    public void updateTask(TaskEntity entity){
        entity.setStatus(TaskEntity.STATUS_RUNNING);
        //entity.update(entity.getBaseObjId());
    }

    /**
     * 创建任务
     */
    public void createTask(){
        TaskEntity entity = activity.getTaskEntityFromUI();
        entity.setStatus(TaskEntity.STATUS_NORMAL);
        entity.setTimestamp(System.currentTimeMillis());
        entity.save(activity.getContext());
    }

    public void deleteTask(){
        TaskEntity entity = activity.getTaskEntity();
        entity.delete(activity.getContext(), new DeleteListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG,"deleteTask onSuccess");
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG,"deleteTask onFailure:"+i+";"+s);
            }
        });
    }

    /**
     * 开始任务
     */
    public void startTask(){
        long time = System.currentTimeMillis();
        TaskEntity entity = activity.getTaskEntity();
        entity.setStatus(TaskEntity.STATUS_RUNNING);
        entity.setTimeStart(time);
        entity.setTimeStartS(TimeUtils.transferLongToDate(time));
        entity.update(activity.getContext());
        //entity.update(entity.getBaseObjId());
    }

    /**
     * 停止任务
     */
    public void stopTask(){
        long time = System.currentTimeMillis();
        TaskEntity entity = activity.getTaskEntity();
        entity.setStatus(TaskEntity.STATUS_COMPLETE);
        entity.setTimeEnd(time);
        entity.setTimeEndS(TimeUtils.transferLongToDate(time));
        entity.setTimeUsed((time - entity.getTimeStart()) / 1000);
        entity.setScore(calculatScore(entity));
        entity.update(activity.getContext());
        //entity.update(entity.getBaseObjId());
    }


    public int calculatScore(TaskEntity entity){
        //重要性系数
        int importantIndex = 50;
        int urgentIndex=50;

        int important = entity.getImportantIdex();
        int urgent = entity.getUrgentIdex();
        int importantScore = importantIndex*important/3;
        int urgentScore = urgentIndex*urgent/3;
        int score = importantScore+urgentScore;
        return score;
    }
    /**
     * 暂停任务
     */
    public void pauseTask(){}
}