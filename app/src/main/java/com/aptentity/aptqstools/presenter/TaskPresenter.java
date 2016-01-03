package com.aptentity.aptqstools.presenter;

import com.aptentity.aptqstools.model.dao.TaskEntity;

import org.litepal.crud.DataSupport;

/**
 * Created by gulliver on 16/1/3.
 */
public class TaskPresenter {
    public void saveTask(TaskEntity entity){
        entity.saveThrows();
    }
    public TaskEntity getTask(long taskId){
        TaskEntity entity = DataSupport.find(TaskEntity.class,taskId);
        return entity;
    }
}
