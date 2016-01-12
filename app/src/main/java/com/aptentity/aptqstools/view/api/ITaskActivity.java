package com.aptentity.aptqstools.view.api;

import android.content.Context;

import com.aptentity.aptqstools.model.dao.TaskEntity;

/**
 * Created by gulliver on 16/1/3.
 */
public interface ITaskActivity {
    /**
     * 从UI上获取task
     * @return
     */
    public TaskEntity getTaskEntityFromUI();

    /**
     *
     * @return
     */
    public TaskEntity getTaskEntity();

    public Context getContext();
}
