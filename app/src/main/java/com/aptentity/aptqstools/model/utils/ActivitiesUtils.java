package com.aptentity.aptqstools.model.utils;

import android.content.Context;
import android.content.Intent;

import com.aptentity.aptqstools.activity.MainActivity;
import com.aptentity.aptqstools.model.dao.ProjectEntity;
import com.aptentity.aptqstools.model.dao.TaskDescribe;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.view.PhotoRecordActivity;
import com.aptentity.aptqstools.view.ProjectActivity;
import com.aptentity.aptqstools.view.SoundRecordActivity;
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

    public static void startViewTaskActivity(Context context,TaskDescribe entity){
        LogHelper.show(TAG,"startViewTaskActivity");
        String id = entity.getObjectId();
        LogHelper.show(TAG, "getBaseObjId:"+id);
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra("mode",TaskActivity.MODE_VIEW);
        intent.putExtra("task_id", id);
        context.startActivity(intent);
    }

    public static void startFunctionActivity(Context context){
        LogHelper.show(TAG, "startFunctionActivity");
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startAddProjectActivity(Context context){
        LogHelper.show(TAG,"startAddProjectActivity");
        Intent intent = new Intent(context, ProjectActivity.class);
        intent.putExtra("mode",TaskActivity.MODE_ADD);
        context.startActivity(intent);
    }

    public static void startViewProjectActivity(Context context,ProjectEntity entity){
        LogHelper.show(TAG,"startViewProjectActivity");
        String id = entity.getObjectId();
        LogHelper.show(TAG, "getBaseObjId:"+id);
        Intent intent = new Intent(context, ProjectActivity.class);
        intent.putExtra("mode",TaskActivity.MODE_VIEW);
        intent.putExtra("task_id", id);
        context.startActivity(intent);
    }

    public static void startSoundRecordActivity(Context context){
        LogHelper.show(TAG, "startSoundRecordActivity");
        Intent intent = new Intent(context, SoundRecordActivity.class);
        context.startActivity(intent);
    }

    public static void startPhotoRecordActivity(Context context){
        LogHelper.show(TAG,"startSoundRecordActivity");
        Intent intent = new Intent(context, PhotoRecordActivity.class);
        context.startActivity(intent);
    }
}
