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

    /**
     * 获取任务
     * @param taskId
     * @param callback
     */
    public void getTask(String taskId,GetListener<TaskDescribe> callback){
        LogHelper.show(TAG,"getTask");
        TaskDBHelper.getTask(taskId,callback);
    }

    /**
     * 创建任务
     */
    public void createTask(ResultCallback callback){
        LogHelper.show(TAG,"createTask");
        TaskDescribe entity = activity.getTaskEntityFromUI();
        TaskDBHelper.createTask(entity, callback);
    }

    /**
     * 删除任务
     * 需要将任务描述和任务执行记录清除
     */
    public void deleteTask(){
        LogHelper.show(TAG,"deleteTask");
        TaskDBHelper.deleteTask(activity.getTaskEntity(), new ResultCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });
    }

    /**
     * 开始任务
     */
    public void startTask(){
        LogHelper.show(TAG,"startTask");
        TaskDBHelper.startTask(activity.getTaskEntity(), new ResultCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });
    }

    /**
     * 停止任务
     */
    public void completTask(){
        LogHelper.show(TAG,"completTask");
        TaskDBHelper.completeTask(activity.getTaskEntity(), new ResultCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });
    }

    /**
     * 继续任务
     */
    public void resumeTask(){
        LogHelper.show(TAG, "resumeTask");
        TaskDBHelper.resumeTask(activity.getTaskEntity(), new ResultCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });
    }

    /**
     * 暂停任务
     */
    public void pauseTask(){
        LogHelper.show(TAG, "pauseTask");
        TaskDBHelper.pauseTask(activity.getTaskEntity(), new ResultCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });
    }

    public void updateTask(){
        LogHelper.show(TAG, "updateTask");
        TaskDBHelper.updateTask(activity.getTaskEntity(), new ResultCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });
    }
}
