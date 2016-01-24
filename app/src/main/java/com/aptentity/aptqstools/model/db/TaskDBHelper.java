package com.aptentity.aptqstools.model.db;

import android.content.Context;

import com.aptentity.aptqstools.application.QsApplication;
import com.aptentity.aptqstools.model.callback.GetCurrentExecuteTaskCallback;
import com.aptentity.aptqstools.model.callback.ResultCallback;
import com.aptentity.aptqstools.model.dao.ProjectEntity;
import com.aptentity.aptqstools.model.dao.TaskDescribe;
import com.aptentity.aptqstools.model.dao.TaskExecuteRecord;
import com.aptentity.aptqstools.model.utils.TimeUtils;
import com.aptentity.aptqstools.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Gulliver(feilong) on 16/1/19.
 * 任务数据辅助类
 */
public class TaskDBHelper {
    private static Context mContext = QsApplication.getAppContext();
    private static final String TAG = TaskDBHelper.class.getSimpleName();

    /**
     * 获取任务
     * @param taskId
     * @param callback
     */
    public static void getTask(String taskId,GetListener<TaskDescribe> callback){
        LogHelper.show(TAG,"getTask");
        BmobQuery<TaskDescribe> query = new BmobQuery<TaskDescribe>();
        //query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.getObject(mContext, taskId, callback);
    }

    public static void updateTask(TaskDescribe entity,final ResultCallback callback){
        LogHelper.show(TAG, "updateTask");
        entity.update(mContext, new UpdateListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "updateTask onSuccess");
                callback.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "updateTask onFailure");
                callback.onFailed(i, s);
            }
        });
    }

    /**
     * 创建任务
     * 只创建任务描述表
     */
    public static void createTask(final TaskDescribe entity, final ResultCallback callback){
        LogHelper.show(TAG,"createTask");
        //创建任务描述表
        entity.setStatus(TaskDescribe.STATUS_NORMAL);
        entity.setTimestamp(System.currentTimeMillis());
        entity.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "createTask onSuccess");
                callback.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "createTask onFailure:" + i + ";" + s);
                callback.onFailed(i, s);
            }
        });
    }

    /**
     * 删除任务
     * 两个步骤：第一步删除任务描述表，第二步删除任务执行记录表
     */
    public static void deleteTask(final TaskDescribe entity, final ResultCallback callback){
        LogHelper.show(TAG, "deleteTask");
        //1.删除任务描述表
        entity.delete(mContext, new DeleteListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "deleteTask TaskDescribe onSuccess");
                deleteExecuteTask(entity.getObjectId(), callback);
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "deleteTask TaskDescribe onFailure:" + i + ";" + s);
                callback.onFailed(i, s);
            }
        });
    }

    private static TaskExecuteRecord mExecuteRecord;//当前的执行记录
    /**
     * 开始任务
     * 1.更新任务描述
     * 2.创建任务执行记录
     * @param entity
     * @param callback
     */
    public static void startTask(final TaskDescribe entity,final ResultCallback callback){
        LogHelper.show(TAG,"startTask");
        //更新任务描述
        long time = System.currentTimeMillis();
        entity.setStatus(TaskDescribe.STATUS_RUNNING);//更新状态
        entity.setTimeStart(time);//更新开始的时间
        entity.setTimeStartS(TimeUtils.transferLongToDate(time));
        entity.setTimeThisTime(time);
        entity.update(mContext);

        //创建任务执行记录
        createExecuteTask(entity.getObjectId(),callback);
    }
    /**
     * 暂停任务
     * 1.更新任务描述
     * 2.更新任务执行记录）
     */
    public static void pauseTask(final TaskDescribe entity,final ResultCallback callback){
        LogHelper.show(TAG, "pauseTask");
        //更新任务描述
        final long time = System.currentTimeMillis();
        entity.setStatus(TaskDescribe.STATUS_PAUSE);
        entity.setTimeUsed(entity.getTimeUsed() + (time - entity.getTimeThisTime()));
        entity.update(mContext);
        //更新任务执行记录
        completeExecuteTask();
    }

    /**
     * 继续任务
     * 1.更新任务描述
     * 2.创建任务执行记录
     */
    public static void resumeTask(final TaskDescribe entity, final ResultCallback callback){
        LogHelper.show(TAG, "resumeTask");
        //更新任务描述
        entity.setStatus(TaskDescribe.STATUS_RUNNING);
        long time = System.currentTimeMillis();
        entity.setTimeThisTime(time);
        entity.update(mContext);

        //创建任务执行记录
        createExecuteTask(entity.getObjectId(), callback);
    }

    /**
     * 完成任务

     */
    public static void completeTask(final TaskDescribe entity, final ResultCallback callback){
        LogHelper.show(TAG,"completTask");
        //更新任务描述
        final long time = System.currentTimeMillis();
        entity.setTimeEnd(time);
        entity.setTimeEndS(TimeUtils.transferLongToDate(time));
        //正在计时
        if (entity.getStatus()== TaskDescribe.STATUS_RUNNING){
            entity.setTimeUsed(entity.getTimeUsed()+(time-entity.getTimeThisTime()));
        }
        entity.setStatus(TaskDescribe.STATUS_COMPLETE);
        entity.setScore(calculatScore(entity));
        entity.update(mContext);

        //更新任务执行记录
        completeExecuteTask();
    }

    /**
     * 获取正在执行的任务执行记录
     * @return
     */
    private static void getCurrentExecuteTask(final GetCurrentExecuteTaskCallback callback){
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
                if (list.size() > 0) {
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

    /**
     * 创建任务执行记录
     * @param taskId
     * @param callback
     */
    private static void createExecuteTask(String taskId,final ResultCallback callback){
        long time = System.currentTimeMillis();
        final TaskExecuteRecord executeRecord = new TaskExecuteRecord();
        executeRecord.setTaskId(taskId);
        executeRecord.setTimeStart(time);
        executeRecord.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                LogHelper.show(TAG, "resumeTask save TaskExecuteRecord onSuccess");
                mExecuteRecord = executeRecord;
                callback.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "resumeTask save TaskExecuteRecord onFailure");
                callback.onFailed(i, s);
            }
        });
    }

    /**
     * 删除与任务描述相关的任务执行记录
     * @param taskId
     * @param callback
     */
    private static void deleteExecuteTask(String taskId,final ResultCallback callback){
        //1.查询任务执行记录
        BmobQuery<TaskExecuteRecord> query = new BmobQuery<TaskExecuteRecord>();
        query.addWhereEqualTo("taskId", taskId);
        query.findObjects(mContext, new FindListener<TaskExecuteRecord>() {
            @Override
            public void onSuccess(List<TaskExecuteRecord> list) {
                //2.批量删除
                LogHelper.show(TAG, "deleteTask find TaskExecuteRecord onSuccess");
                List<BmobObject> taskList = new ArrayList<BmobObject>();
                for (TaskExecuteRecord item : list) {
                    taskList.add(item);
                }
                new TaskExecuteRecord().deleteBatch(mContext, taskList, new DeleteListener() {
                    @Override
                    public void onSuccess() {
                        LogHelper.show(TAG, "deleteTask delete TaskExecuteRecord onSuccess");
                        callback.onSuccess();
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        LogHelper.show(TAG, "deleteTask delete TaskExecuteRecord onFailures");
                        callback.onFailed(i, s);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.show(TAG, "deleteTask find TaskExecuteRecord onFailure:" + i + ";" + s);
                callback.onFailed(i, s);
            }
        });
    }

    /**
     * 完成一条任务执行记录
     */
    private static void completeExecuteTask(){
        LogHelper.show(TAG, "completeExecuteTask");
        getCurrentExecuteTask(new GetCurrentExecuteTaskCallback() {
            @Override
            public void onResult(TaskExecuteRecord task) {
                if (task == null) {
                    return;
                }
                LogHelper.show(TAG, "completTask getCurrentExecuteTask:" + task.getObjectId());

                LogHelper.show(TAG, "completTask update TaskExecuteRecord");
                long time = System.currentTimeMillis();
                task.setTimeStop(time);
                task.setTimeUsed(task.getTimeStop() - task.getTimeStart());
                task.update(mContext, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        LogHelper.show(TAG, "completTask update TaskExecuteRecord onSuccess");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        LogHelper.show(TAG, "completTask update TaskExecuteRecord onFailure");
                    }
                });
                mExecuteRecord = null;//置为空
            }
        });
    }

    /**
     * 1-9,9高、5中、1低
     * 预留6档，足够了
     * @param entity
     * @return
     */
    private static int calculatScore(TaskDescribe entity){
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

    /**
     * 创建项目
     */
    public static void createProject(ProjectEntity entity,final ResultCallback callback){
        LogHelper.show(TAG,"createProject");
        entity.setStatus(TaskDescribe.STATUS_NORMAL);
        entity.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                callback.onFailed(i,s);
            }
        });
    }

    /**
     * 删除项目
     * @param entity
     * @param callback
     */
    public static void deleteProject(ProjectEntity entity,final ResultCallback callback){
        LogHelper.show(TAG, "deleteProject");
        entity.delete(mContext, new DeleteListener() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                callback.onFailed(i, s);
            }
        });
    }

    /**
     * 更新项目
     * @param entity
     * @param callback
     */
    public static void updateProject(ProjectEntity entity,final ResultCallback callback){
        LogHelper.show(TAG,"updateProject");
        entity.update(mContext, new UpdateListener() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                callback.onFailed(i, s);
            }
        });
    }

    /**
     * 完成项目
     * @param entity
     * @param callback
     */
    public static void completeProject(ProjectEntity entity, final ResultCallback callback){
        entity.setStatus(TaskDescribe.STATUS_COMPLETE);
        entity.save(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                callback.onFailed(i,s);
            }
        });
    }
    /**
     * 获取项目
     * @param taskId
     * @param callback
     */
    public static void getProject(String taskId,GetListener<ProjectEntity> callback){
        LogHelper.show(TAG,"getProject");
        BmobQuery<ProjectEntity> query = new BmobQuery<ProjectEntity>();
        query.getObject(mContext, taskId, callback);
    }

    /**
     * 获取所有项目
     */
    public static void getProjects(FindListener<ProjectEntity> callback){
        LogHelper.show(TAG,"getProjects");
        BmobQuery<ProjectEntity> query = new BmobQuery<ProjectEntity>();
        query.findObjects(mContext,callback);
    }
}
