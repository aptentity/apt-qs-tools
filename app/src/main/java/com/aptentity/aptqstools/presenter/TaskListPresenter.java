package com.aptentity.aptqstools.presenter;

import com.aptentity.aptqstools.model.dao.TaskEntity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by gulliver on 16/1/3.
 */
public class TaskListPresenter {
    public List<TaskEntity> getAllTask(){
        List<TaskEntity> entityList = DataSupport.findAll(TaskEntity.class);
        return entityList;
    }
}
