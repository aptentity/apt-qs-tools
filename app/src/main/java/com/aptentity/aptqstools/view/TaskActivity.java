package com.aptentity.aptqstools.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.dao.TaskEntity;
import com.aptentity.aptqstools.presenter.TaskPresenter;
import com.aptentity.aptqstools.utils.LogHelper;

public class TaskActivity extends BasicActivity {
    public final String TAG = TaskActivity.class.getSimpleName();
    public final static int MODE_ADD=0;
    public final static int MODE_VIEW=1;
    private int mode=0;//模式，0添加任务模式，1为查看任务模式
    private TaskPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.show(TAG, "onCreate");
        mode = getIntent().getExtras().getInt("mode");
        LogHelper.show(TAG,"mode="+mode);
        presenter = new TaskPresenter();
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

    private EditText mEtTitle,mEtDescription,mEtTarget,mEtStep;
    @Override
    void initUI() {
        mEtTitle = (EditText)findViewById(R.id.borg_et_task_title);
        mEtDescription = (EditText)findViewById(R.id.borg_et_task_description);
        mEtTarget = (EditText)findViewById(R.id.borg_et_task_target);
        mEtStep = (EditText)findViewById(R.id.borg_et_task_step);
        findViewById(R.id.borg_btn_task_save).setOnClickListener(this);
        if (mode==MODE_ADD){
            findViewById(R.id.borg_view_task_timeEnd).setVisibility(View.GONE);
            findViewById(R.id.borg_view_task_timeStart).setVisibility(View.GONE);
            findViewById(R.id.borg_view_task_timeUsed).setVisibility(View.GONE);
        }else if (MODE_VIEW==mode){
            long taskId = getIntent().getExtras().getLong("task_id");
            TaskEntity entity = presenter.getTask(taskId);
            fillUI(entity);
            findViewById(R.id.borg_btn_task_save).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.borg_btn_task_save:
                presenter.saveTask(getTaskEntity());
                finish();
                break;
            default:
                break;
        }
    }

    /**
     *
     * @return
     */
    private TaskEntity getTaskEntity(){
        TaskEntity entity = new TaskEntity();
        entity.setTitle(mEtTitle.getText().toString());
        entity.setDescription(mEtDescription.getText().toString());
        entity.setTarget(mEtTarget.getText().toString());
        entity.setStep(mEtStep.getText().toString());
        return entity;
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
    }
}
