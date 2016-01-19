package com.aptentity.aptqstools.presenter;

import android.text.TextUtils;

import com.aptentity.aptqstools.application.QsApplication;
import com.aptentity.aptqstools.model.callback.ResultCallback;
import com.aptentity.aptqstools.model.dao.TaskDescribe;
import com.aptentity.aptqstools.model.dao.TaskExecuteRecord;
import com.aptentity.aptqstools.model.db.TaskDBHelper;
import com.aptentity.aptqstools.model.utils.TimeUtils;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.view.api.ITaskActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by gulliver on 16/1/3.
 */
public class TaskPresenter {
    private final String TAG = TaskPresenter.class.getSimpleName();
    private ITaskActivity activity;
    public TaskPresenter(ITaskActivity activity){
        this.activity = activity;
    }

    public void getTask(String taskId,GetListener<TaskDescribe> callback){
        BmobQuery<TaskDescribe> query = new BmobQuery<TaskDescribe>();
        //query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.getObject(activity.getContext(), taskId, callback);
    }

    /**
     * 创建任务
     */
    public void createTask(ResultCallback callback){
        TaskDescribe entity = activity.getTaskEntity();
        TaskDBHelper.createTask(entity,callback);
    }

    /**
     * 删除任务
     * 需要将任务描述和任务执行记录清除
     */
    public void deleteTask(){
        //删除任务描述
        TaskDescribe entity = activity.getTaskEntity();
        entity.delete(activity.getContext(), new DeleteListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "deleteTask onSuccess");
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "deleteTask onFailure:" + i + ";" + s);
            }
        });
        //删除任务执行记录
        TaskExecuteRecord execute = new TaskExecuteRecord();
        execute.setObjectId(entity.getObjectId());
        entity.delete(activity.getContext(), new DeleteListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "delete TaskExecuteRecord onSuccess");
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "delete TaskExecuteRecord onFailure:" + i + ";" + s);
            }
        });
    }

    /**
     * 开始任务
     */
    public void startTask(){
        LogHelper.show(TAG,"startTask");
        //更新任务描述
        long time = System.currentTimeMillis();
        TaskDescribe entity = activity.getTaskEntity();
        entity.setStatus(TaskDescribe.STATUS_RUNNING);//更新状态
        entity.setTimeStart(time);//更新开始的时间
        entity.setTimeStartS(TimeUtils.transferLongToDate(time));
        entity.setTimeThisTime(time);
        entity.update(activity.getContext());

        //创建任务执行记录
        final TaskExecuteRecord executeRecord = new TaskExecuteRecord();
        executeRecord.setTaskId(entity.getObjectId());
        executeRecord.setTimeStart(time);
        executeRecord.save(activity.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "startTask save TaskExecuteRecord onSuccess");
                mExecuteRecord = executeRecord;
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "startTask save TaskExecuteRecord onFailure");
            }
        });
    }

    /**
     * 停止任务
     */
    public void completTask(){
        LogHelper.show(TAG,"completTask");
        //更新任务描述
        final long time = System.currentTimeMillis();
        TaskDescribe entity = activity.getTaskEntity();
        entity.setTimeEnd(time);
        entity.setTimeEndS(TimeUtils.transferLongToDate(time));
        //正在计时
        if (entity.getStatus()== TaskDescribe.STATUS_RUNNING){
            entity.setTimeUsed(entity.getTimeUsed()+(time-entity.getTimeThisTime()));
        }
        entity.setStatus(TaskDescribe.STATUS_COMPLETE);
        entity.setScore(calculatScore(entity));
        entity.update(activity.getContext());

        //更新任务执行记录
        getCurrentExecuteTask(new onGetCurrentExecuteTaskCallback() {
            @Override
            public void onResult(TaskExecuteRecord task) {
                LogHelper.show(TAG, "completTask getCurrentExecuteTask:" + task.getObjectId());
                if (task==null) {
                    return;
                }
                LogHelper.show(TAG, "completTask update TaskExecuteRecord");
                task.setTimeStop(time);
                task.update(activity.getContext(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        LogHelper.show(TAG, "completTask update TaskExecuteRecord onSuccess");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        LogHelper.show(TAG, "completTask update TaskExecuteRecord onFailure");
                    }
                });
                mExecuteRecord=null;//置为空
            }
        });
    }

    /**
     * 继续任务
     */
    public void resumeTask(){
        LogHelper.show(TAG, "resumeTask");
        //更新任务描述
        TaskDescribe entity = activity.getTaskEntity();
        entity.setStatus(TaskDescribe.STATUS_RUNNING);
        long time = System.currentTimeMillis();
        entity.setTimeThisTime(time);
        entity.update(activity.getContext());

        //创建任务执行记录
        final TaskExecuteRecord executeRecord = new TaskExecuteRecord();
        executeRecord.setTaskId(entity.getObjectId());
        executeRecord.setTimeStart(time);
        executeRecord.save(activity.getContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "resumeTask save TaskExecuteRecord onSuccess");
                mExecuteRecord = executeRecord;
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "resumeTask save TaskExecuteRecord onFailure");
            }
        });
    }

    /**
     * 暂停任务
     */
    public void pauseTask(){
        LogHelper.show(TAG, "pauseTask");
        TaskDescribe entity = activity.getTaskEntity();
        final long time = System.currentTimeMillis();
        entity.setStatus(TaskDescribe.STATUS_PAUSE);
        entity.setTimeUsed(entity.getTimeUsed() + (time - entity.getTimeThisTime()));
        entity.update(activity.getContext());
        //更新任务执行记录
        getCurrentExecuteTask(new onGetCurrentExecuteTaskCallback() {
            @Override
            public void onResult(TaskExecuteRecord task) {
                LogHelper.show(TAG, "completTask getCurrentExecuteTask:" + task.getObjectId());
                if (task==null) {
                    return;
                }
                LogHelper.show(TAG, "completTask update TaskExecuteRecord");
                task.setTimeStop(time);
                task.update(activity.getContext(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        LogHelper.show(TAG, "completTask update TaskExecuteRecord onSuccess");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        LogHelper.show(TAG, "completTask update TaskExecuteRecord onFailure");
                    }
                });
                mExecuteRecord=null;//置为空
            }
        });
    }

    /**
     * 1-9,9高、5中、1低
     * 预留6档，足够了
     * @param entity
     * @return
     */
    public int calculatScore(TaskDescribe entity){
        //重要性系数
        int importantIndex = 50;
        int urgentIndex=50;

        int important = entity.getImportantIdex();
        int urgent = entity.getUrgentIdex();
        int importantScore = importantIndex*important/9;
        int urgentScore = urgentIndex*urgent/9;
        int score = importantScore+urgentScore;
        return score;
    }

    public void updateTask(){
        TaskDescribe entity = activity.getTaskEntity();
        entity.update(activity.getContext());
    }

    private TaskExecuteRecord mExecuteRecord;
    /**
     * 获取正在执行的任务
     * @return
     */
    private void getCurrentExecuteTask(final onGetCurrentExecuteTaskCallback callback){
        //如果已经存在直接返回
        if (mExecuteRecord!=null){
            callback.onResult(mExecuteRecord);
        }
        //如果没有则去查询
        LogHelper.show(TAG, "getCurrentExecuteTask");
        BmobQuery<TaskExecuteRecord> query = new BmobQuery<TaskExecuteRecord>();
        query.addWhereEqualTo("timeStop", 0);
        query.findObjects(QsApplication.getInstance(), new FindListener<TaskExecuteRecord>() {
            @Override
            public void onSuccess(List<TaskExecuteRecord> list) {
                LogHelper.show(TAG, "getCurrentExecuteTask onSuccess");
                if (list.size()>0){
                    mExecuteRecord = list.get(0);
                }
                callback.onResult(mExecuteRecord);
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.show(TAG, "getCurrentExecuteTask onError");
                callback.onResult(null);
            }
        });
    }

    public interface onGetCurrentExecuteTaskCallback{
        public void onResult(TaskExecuteRecord task);
    }
}
