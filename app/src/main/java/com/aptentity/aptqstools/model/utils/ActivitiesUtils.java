package com.aptentity.aptqstools.model.utils;

import android.content.Context;
import android.content.Intent;

import com.aptentity.aptqstools.activity.MainActivity;
import com.aptentity.aptqstools.model.dao.TaskEntity;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.view.TaskActivity;

/**
 * Created by gulliver on 16/1/3.
 */
public class ActivitiesUtils {
    private static final String TAG = ActivitiesUtils.class.getSimpleName();
    public static void startAddTaskActivity(Context context){
        LogHelper.show(TAG,"startAddTaskActivity");
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("mode",TaskActivity.MODE_ADD);
        context.startActivity(intent);
    }

    public static void startViewTaskActivity(Context context,TaskEntity entity){
        LogHelper.show(TAG,"startViewTaskActivity");
        String id = entity.getObjectId();
        LogHelper.show(TAG, "getBaseObjId:"+id);
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("mode",TaskActivity.MODE_VIEW);
        intent.putExtra("task_id", id);
        context.startActivity(intent);
    }

    public static void startFunctionActivity(Context context){
        LogHelper.show(TAG,"startFunctionActivity");
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
