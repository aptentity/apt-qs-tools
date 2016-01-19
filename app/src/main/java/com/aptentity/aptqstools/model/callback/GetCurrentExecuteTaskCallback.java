package com.aptentity.aptqstools.model.callback;

import com.aptentity.aptqstools.model.dao.TaskExecuteRecord;

/**
 * Created by Gulliver(feilong) on 16/1/19.
 */
public interface GetCurrentExecuteTaskCallback {
    public void onResult(TaskExecuteRecord task);
}
