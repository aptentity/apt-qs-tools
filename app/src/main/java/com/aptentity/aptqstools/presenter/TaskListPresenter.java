package com.aptentity.aptqstools.presenter;

import com.aptentity.aptqstools.application.QsApplication;
import com.aptentity.aptqstools.model.dao.TaskEntity;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.view.TaskListActivity;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by gulliver on 16/1/3.
 */
public class TaskListPresenter {
    private final String TAG = TaskListActivity.class.getSimpleName();
    public void getAllTask(final FindListener<TaskEntity> findListener){
        BmobQuery<TaskEntity> query = new BmobQuery<TaskEntity>();
        query.findObjects(QsApplication.getInstance(), new FindListener<TaskEntity>() {
            @Override
            public void onSuccess(List<TaskEntity> list) {
                LogHelper.show(TAG,"getAllTask onSuccess");
                findListener.onSuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.show(TAG,"getAllTask onError:"+i+";"+s);
                findListener.onError(i,s);
            }
        });
    }

    /**
     * 获取已经完成的任务
     * @param findListener
     */
    public void getCompletedTasks(final FindListener<TaskEntity> findListener){
        BmobQuery<TaskEntity> query = new BmobQuery<TaskEntity>();
        query.addWhereEqualTo("status",TaskEntity.STATUS_COMPLETE);
        query.findObjects(QsApplication.getInstance(), new FindListener<TaskEntity>() {
            @Override
            public void onSuccess(List<TaskEntity> list) {
                LogHelper.show(TAG,"getAllTask onSuccess");
                findListener.onSuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.show(TAG,"getAllTask onError:"+i+";"+s);
                findListener.onError(i,s);
            }
        });
    }

    /**
     * 获取已经完成的任务
     * @param findListener
     */
    public void getNormalTasks(final FindListener<TaskEntity> findListener){
        BmobQuery<TaskEntity> query = new BmobQuery<TaskEntity>();
        query.addWhereEqualTo("status",TaskEntity.STATUS_NORMAL);
        query.findObjects(QsApplication.getInstance(), new FindListener<TaskEntity>() {
            @Override
            public void onSuccess(List<TaskEntity> list) {
                LogHelper.show(TAG,"getAllTask onSuccess");
                findListener.onSuccess(list);
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.show(TAG,"getAllTask onError:"+i+";"+s);
                findListener.onError(i,s);
            }
        });
    }
}
