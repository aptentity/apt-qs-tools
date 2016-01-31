package com.aptentity.aptqstools.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.callback.ResultCallback;
import com.aptentity.aptqstools.model.dao.TaskDescribe;
import com.aptentity.aptqstools.model.utils.TimeUtils;
import com.aptentity.aptqstools.model.utils.ToastUtils;
import com.aptentity.aptqstools.model.utils.UrgentUtils;
import com.aptentity.aptqstools.presenter.TaskPresenter;
import com.aptentity.aptqstools.service.PhoneUseService;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.view.api.ITaskActivity;
import com.aptentity.aptqstools.view.widget.UrgentSelectDlg;

import cn.bmob.v3.listener.GetListener;

public class TaskActivity extends BasicActivity implements ITaskActivity{
    public final String TAG = TaskActivity.class.getSimpleName();
    public final static int MODE_ADD=0;
    public final static int MODE_VIEW=1;
    private int mode=0;//模式，0添加任务模式，1为查看任务模式
    private TaskPresenter presenter;
    private TaskDescribe mEntity=new TaskDescribe();
    private String taskId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.show(TAG, "onCreate");
        mode = getIntent().getExtras().getInt("mode");
        LogHelper.show(TAG,"mode="+mode);
        taskId = getIntent().getExtras().getString("task_id");
        LogHelper.show(TAG,"task_id="+taskId);
        presenter = new TaskPresenter(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    int getViewID() {
        return R.layout.activity_task;
    }

    @Override
    int getMenuId() {
        if (mode==MODE_ADD)
            return 0;
        return R.menu.menu_task;
    }

    private EditText mEtTitle,mEtDescription,mEtTarget,mEtStep,mEtTimeCreated,mEtTimeStart,mEtTimeStop,mEtTimeUsed,
            mEtScore,mEtImportant,mEtUrgent,mEtTimeEstimated;
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
        mEtImportant = (EditText)findViewById(R.id.borg_et_task_importantIdex);
        mEtUrgent = (EditText)findViewById(R.id.borg_et_task_urgentIdex);
        mEtImportant.setOnClickListener(this);
        mEtUrgent.setOnClickListener(this);
        mEtTimeEstimated = (EditText)findViewById(R.id.borg_et_task_timeEstimated);
        findViewById(R.id.borg_btn_task_save).setOnClickListener(this);
        findViewById(R.id.borg_btn_task_start).setOnClickListener(this);
        findViewById(R.id.borg_btn_task_complete).setOnClickListener(this);
        findViewById(R.id.borg_btn_task_pause).setOnClickListener(this);
        findViewById(R.id.borg_btn_task_resume).setOnClickListener(this);
        if (mode==MODE_ADD){
            //填写默认值
            mEtImportant.setText(R.string.middle);
            mEtUrgent.setText(R.string.middle);
            //按钮的状态
            findViewById(R.id.borg_view_task_timeStop).setVisibility(View.GONE);
            findViewById(R.id.borg_view_task_timeStart).setVisibility(View.GONE);
            findViewById(R.id.borg_view_task_timeUsed).setVisibility(View.GONE);
            findViewById(R.id.borg_btn_task_start).setVisibility(View.GONE);
            findViewById(R.id.borg_btn_task_complete).setVisibility(View.GONE);
            findViewById(R.id.borg_btn_task_pause).setVisibility(View.GONE);
            findViewById(R.id.borg_btn_task_resume).setVisibility(View.GONE);
        }else if (MODE_VIEW==mode){

            LogHelper.show(TAG, "task_id:"+taskId);
            presenter.getTask(taskId, new GetListener<TaskDescribe>() {
                @Override
                public void onSuccess(TaskDescribe entity) {
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
                presenter.createTask(new ResultCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort(R.string.save_success);
                        finish();
                    }

                    @Override
                    public void onFailed(int i, String s) {
                        ToastUtils.showShort(R.string.save_fail);
                    }
                });
                break;
            case R.id.borg_btn_task_start:
                long time = System.currentTimeMillis();
                presenter.startTask();
                //mEtTimeStart.setText(TimeUtils.transferLongToDate(time));
                fillUI(mEntity);
                break;
            case R.id.borg_btn_task_complete:
                presenter.completTask();
                fillUI(mEntity);
                break;
            case R.id.borg_btn_task_pause:
                presenter.pauseTask();
                fillUI(mEntity);
                break;
            case R.id.borg_btn_task_resume:
                presenter.resumeTask();
                fillUI(mEntity);
                break;
            case R.id.borg_et_task_importantIdex:
                selectImportantIdex();
                break;
            case R.id.borg_et_task_urgentIdex:
                selectUrgentInde();
                break;
            default:
                break;
        }
    }

    public void selectImportantIdex(){
        UrgentSelectDlg dlg = new UrgentSelectDlg(this);
        dlg.setGenderSelectListener(new UrgentSelectDlg.onIndexSelectListener() {
            @Override
            public void select(int i) {
                mEtImportant.setText(UrgentUtils.getUrgentName(i));
            }
        });
    }

    public void selectUrgentInde(){
        UrgentSelectDlg dlg = new UrgentSelectDlg(this);
        dlg.setGenderSelectListener(new UrgentSelectDlg.onIndexSelectListener() {
            @Override
            public void select(int i) {
                mEtUrgent.setText(UrgentUtils.getUrgentName(i));
            }
        });
    }

    /**
     *
     * @return
     */
    public TaskDescribe getTaskEntityFromUI(){
        mEntity.setTitle(mEtTitle.getText().toString());
        mEntity.setDescription(mEtDescription.getText().toString());
        mEntity.setTarget(mEtTarget.getText().toString());
        mEntity.setStep(mEtStep.getText().toString());
        try {
            mEntity.setTimeEstimated(Long.parseLong(mEtTimeEstimated.getText().toString()));
        }catch (Exception e){}
        mEntity.setImportantIdex(UrgentUtils.getUrgentIdex(mEtImportant.getText().toString()));
        mEntity.setUrgentIdex(UrgentUtils.getUrgentIdex(mEtUrgent.getText().toString()));
        return mEntity;
    }

    @Override
    public TaskDescribe getTaskEntity() {
        return mEntity;
    }

    /**
     * 填充字段
     * @param entity
     */
    private void fillUI(TaskDescribe entity){
        LogHelper.show(TAG,"fill ui:"+entity.toString()+";"+entity.getObjectId());
        mEtTitle.setText(entity.getTitle());
        mEtDescription.setText(entity.getDescription());
        mEtTarget.setText(entity.getTarget());
        mEtStep.setText(entity.getStep());
        mEtTimeCreated.setText(TimeUtils.transferLongToDate(entity.getTimestamp()));
        mEtTimeStart.setText(TimeUtils.transferLongToDate(entity.getTimeStart()));
        mEtScore.setText(String.valueOf(entity.getScore()));
        mEtTimeUsed.setText(TimeUtils.formatLongToTimeStr(entity.getTimeUsed()));
        mEtTimeStop.setText(TimeUtils.transferLongToDate(entity.getTimeEnd()));
        switch (entity.getStatus()){
            case TaskDescribe.STATUS_NORMAL:
                normalUI();
                break;
            case TaskDescribe.STATUS_RUNNING:
                runningUI();
                break;
            case TaskDescribe.STATUS_PAUSE:
                pauseUI();
                break;
            case TaskDescribe.STATUS_COMPLETE:
                completeUI();
                break;
        }
        mEtUrgent.setText(UrgentUtils.getUrgentName(entity.getUrgentIdex()));
        mEtImportant.setText(UrgentUtils.getUrgentName(entity.getImportantIdex()));
        mEtTimeEstimated.setText(String.valueOf(entity.getTimeEstimated()));
    }

    private void normalUI(){
        findViewById(R.id.borg_btn_task_start).setVisibility(View.VISIBLE);
        findViewById(R.id.borg_btn_task_complete).setVisibility(View.GONE);
        findViewById(R.id.borg_btn_task_pause).setVisibility(View.GONE);
        findViewById(R.id.borg_btn_task_resume).setVisibility(View.GONE);
    }

    private void runningUI(){
        findViewById(R.id.borg_btn_task_start).setVisibility(View.GONE);
        findViewById(R.id.borg_btn_task_complete).setVisibility(View.VISIBLE);
        findViewById(R.id.borg_btn_task_pause).setVisibility(View.VISIBLE);
        findViewById(R.id.borg_btn_task_resume).setVisibility(View.GONE);
        showNotification(true);
    }
    private void pauseUI(){
        findViewById(R.id.borg_btn_task_start).setVisibility(View.GONE);
        findViewById(R.id.borg_btn_task_complete).setVisibility(View.VISIBLE);
        findViewById(R.id.borg_btn_task_pause).setVisibility(View.GONE);
        findViewById(R.id.borg_btn_task_resume).setVisibility(View.VISIBLE);
        showNotification(false);
    }

    private void completeUI(){
        findViewById(R.id.borg_btn_task_start).setVisibility(View.GONE);
        findViewById(R.id.borg_btn_task_complete).setVisibility(View.GONE);
        findViewById(R.id.borg_btn_task_pause).setVisibility(View.GONE);
        findViewById(R.id.borg_btn_task_resume).setVisibility(View.GONE);
        showNotification(false);
    }

    private void showNotification(boolean shown){
        Intent intent = new Intent(this, PhoneUseService.class);
        intent.putExtra("showNotification",shown);
        intent.putExtra("timeUsed",mEntity.getTimeUsed());
        intent.putExtra("timeThisTime",mEntity.getTimeThisTime());
        intent.putExtra("title",mEntity.getTitle());
        intent.putExtra("id",mEntity.getObjectId());
        intent.putExtra("notification",true);
        startService(intent);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    /**
     * 菜单功能
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.borg_action_task_delete://删除任务
                presenter.deleteTask();
                finish();
                break;
            case R.id.borg_action_task_update://修改任务
                getTaskEntityFromUI();
                presenter.updateTask();
        }
        return true;
    }
}
