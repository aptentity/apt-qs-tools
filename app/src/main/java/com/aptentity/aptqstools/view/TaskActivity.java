package com.aptentity.aptqstools.view;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.dao.TaskEntity;
import com.aptentity.aptqstools.model.utils.TimeUtils;
import com.aptentity.aptqstools.presenter.TaskPresenter;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.view.api.ITaskActivity;

import cn.bmob.v3.listener.GetListener;

public class TaskActivity extends BasicActivity implements ITaskActivity{
    public final String TAG = TaskActivity.class.getSimpleName();
    public final static int MODE_ADD=0;
    public final static int MODE_VIEW=1;
    private int mode=0;//模式，0添加任务模式，1为查看任务模式
    private TaskPresenter presenter;
    private TaskEntity mEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.show(TAG, "onCreate");
        mode = getIntent().getExtras().getInt("mode");
        LogHelper.show(TAG,"mode="+mode);
        presenter = new TaskPresenter(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    int getViewID() {
        return R.layout.activity_task;
    }

    @Override
    int getMenuId() {
        return 0;
    }

    private EditText mEtTitle,mEtDescription,mEtTarget,mEtStep,mEtTimeCreated,mEtTimeStart,mEtTimeStop,mEtTimeUsed,
            mEtScore;
    @Override
    void initUI() {
        mEtTitle = (EditText)findViewById(R.id.borg_et_task_title);
        mEtDescription = (EditText)findViewById(R.id.borg_et_task_description);
        mEtTarget = (EditText)findViewById(R.id.borg_et_task_target);
        mEtStep = (EditText)findViewById(R.id.borg_et_task_step);
        mEtTimeCreated = (EditText)findViewById(R.id.borg_et_task_timeCreate);
        mEtTimeStart = (EditText)findViewById(R.id.borg_et_task_timeStart);
        mEtTimeStop = (EditText)findViewById(R.id.borg_et_task_timeStop);
        mEtTimeUsed = (EditText)findViewById(R.id.borg_et_task_timeUsed);
        mEtScore = (EditText)findViewById(R.id.borg_et_task_score);
        findViewById(R.id.borg_btn_task_save).setOnClickListener(this);
        findViewById(R.id.borg_btn_task_start).setOnClickListener(this);
        findViewById(R.id.borg_btn_task_stop).setOnClickListener(this);
        if (mode==MODE_ADD){
            findViewById(R.id.borg_view_task_timeStop).setVisibility(View.GONE);
            findViewById(R.id.borg_view_task_timeStart).setVisibility(View.GONE);
            findViewById(R.id.borg_view_task_timeUsed).setVisibility(View.GONE);
            findViewById(R.id.borg_btn_task_start).setVisibility(View.GONE);
            findViewById(R.id.borg_btn_task_stop).setVisibility(View.GONE);
        }else if (MODE_VIEW==mode){
            String taskId = getIntent().getExtras().getString("task_id");
            presenter.getTask(taskId, new GetListener<TaskEntity>() {
                @Override
                public void onSuccess(TaskEntity entity) {
                    mEntity = entity;
                    fillUI(mEntity);
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });

            findViewById(R.id.borg_btn_task_save).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.borg_btn_task_save:
                presenter.createTask();
                finish();
                break;
            case R.id.borg_btn_task_start:
                long time = System.currentTimeMillis();
                presenter.startTask();
                mEtTimeStart.setText(TimeUtils.transferLongToDate(time));
                break;
            case R.id.borg_btn_task_stop:
                presenter.stopTask();
                fillUI(mEntity);
                break;
            default:
                break;
        }
    }

    /**
     *
     * @return
     */
    public TaskEntity getTaskEntityFromUI(){
        TaskEntity entity = new TaskEntity();
        entity.setTitle(mEtTitle.getText().toString());
        entity.setDescription(mEtDescription.getText().toString());
        entity.setTarget(mEtTarget.getText().toString());
        entity.setStep(mEtStep.getText().toString());
        return entity;
    }

    @Override
    public TaskEntity getTaskEntity() {
        return mEntity;
    }

    /**
     * 填充字段
     * @param entity
     */
    private void fillUI(TaskEntity entity){
        LogHelper.show(TAG,"fill ui:"+entity.toString());
        mEtTitle.setText(entity.getTitle());
        mEtDescription.setText(entity.getDescription());
        mEtTarget.setText(entity.getTarget());
        mEtStep.setText(entity.getStep());
        mEtTimeCreated.setText(TimeUtils.transferLongToDate(entity.getTimestamp()));
        mEtTimeStart.setText(TimeUtils.transferLongToDate(entity.getTimeStart()));
        mEtScore.setText(String.valueOf(entity.getScore()));
        mEtTimeUsed.setText(String.valueOf(entity.getTimeUsed()));
        mEtTimeStop.setText(TimeUtils.transferLongToDate(entity.getTimeEnd()));
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
