package com.aptentity.aptqstools.model.db;

import android.content.Context;

import com.aptentity.aptqstools.application.QsApplication;
import com.aptentity.aptqstools.model.callback.ResultCallback;
import com.aptentity.aptqstools.model.dao.TaskDescribe;
import com.aptentity.aptqstools.model.dao.TaskExecuteRecord;
import com.aptentity.aptqstools.utils.LogHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Gulliver(feilong) on 16/1/19.
 * 任务数据辅助类
 */
public class TaskDBHelper {
    private static Context mContext = QsApplication.getAppContext();
    private static final String TAG = TaskDBHelper.class.getSimpleName();
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
                LogHelper.show(TAG,"createTask onSuccess");
                callback.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG,"createTask onFailure:"+i+";"+s);
                callback.onFailed(i,s);
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
                //2.查询任务执行记录
                BmobQuery<TaskExecuteRecord> query = new BmobQuery<TaskExecuteRecord>();
                query.addWhereEqualTo("taskId", entity.getObjectId());
                query.findObjects(mContext, new FindListener<TaskExecuteRecord>() {
                    @Override
                    public void onSuccess(List<TaskExecuteRecord> list) {
                        //3.批量删除
                        LogHelper.show(TAG, "deleteTask find TaskExecuteRecord onSuccess");
                        List<BmobObject> taskList = new ArrayList<BmobObject>();
                        for (TaskExecuteRecord item:list) {
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
                                callback.onFailed(i,s);
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

            @Override
            public void onFailure(int i, String s) {
                LogHelper.show(TAG, "deleteTask TaskDescribe onFailure:" + i + ";" + s);
                callback.onFailed(i, s);
            }
        });
    }

    /**
     * 暂停任务
     */
    public static void pauseTask(final TaskDescribe entity,final ResultCallback callback){

    }

    /**
     * 继续任务
     */
    public static void resumeTask(final TaskDescribe entity, final ResultCallback callback){

    }

    /**
     * 完成任务
     */
    public static void completeTask(final TaskDescribe entity, final ResultCallback callback){

    }
}
