package com.aptentity.aptqstools.view;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.callback.ResultCallback;
import com.aptentity.aptqstools.model.dao.ProjectEntity;
import com.aptentity.aptqstools.model.dao.TaskDescribe;
import com.aptentity.aptqstools.model.db.TaskDBHelper;
import com.aptentity.aptqstools.model.utils.ToastUtils;
import com.aptentity.aptqstools.utils.LogHelper;

import java.util.List;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;

public class ProjectActivity extends BasicActivity {
    public final String TAG = ProjectActivity.class.getSimpleName();
    private int mode=0;//模式，0添加任务模式，1为查看任务模式
    private EditText mEtProjectName,mEtProjectDescription,mEtFatherProject;
    private List<ProjectEntity> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogHelper.show(TAG, "onCreate");
        mode = getIntent().getExtras().getInt("mode");
        LogHelper.show(TAG, "mode=" + mode);
        super.onCreate(savedInstanceState);
    }

    @Override
    int getViewID() {
        return R.layout.activity_project;
    }

    @Override
    int getMenuId() {
        return 0;
    }

    @Override
    void initUI() {
        mEtProjectName = (EditText)findViewById(R.id.borg_et_project_title);
        mEtProjectDescription = (EditText)findViewById(R.id.borg_et_project_description);
        mEtFatherProject = (EditText)findViewById(R.id.borg_et_project);
        if (mode==TaskActivity.MODE_ADD){
            findViewById(R.id.borg_btn_project_save).setVisibility(View.VISIBLE);
            findViewById(R.id.borg_btn_project_complete).setVisibility(View.GONE);
        }else if (TaskActivity.MODE_VIEW==mode){
            findViewById(R.id.borg_btn_project_save).setVisibility(View.GONE);
            findViewById(R.id.borg_btn_project_complete).setVisibility(View.VISIBLE);
            String taskId = getIntent().getExtras().getString("task_id");
            TaskDBHelper.getProject(taskId, new GetListener<ProjectEntity>() {
                @Override
                public void onSuccess(ProjectEntity entity) {
                    mEntity = entity;
                    fillUI(mEntity);
                }

                @Override
                public void onFailure(int i, String s) {

                }
            });
        }
        findViewById(R.id.borg_btn_project_complete).setOnClickListener(this);
        findViewById(R.id.borg_btn_project_save).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.borg_btn_project_save:
                TaskDBHelper.createProject(getProjectFromUI(), new ResultCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort(R.string.save_success);
                        finish();
                    }

                    @Override
                    public void onFailed(int code, String msg) {
                        ToastUtils.showShort(R.string.save_fail);
                    }
                });
                break;
            case R.id.borg_btn_project_complete:
                TaskDBHelper.completeProject(mEntity, new ResultCallback() {
                    @Override
                    public void onSuccess() {
                        ToastUtils.showShort(R.string.save_success);
                        finish();
                    }

                    @Override
                    public void onFailed(int code, String msg) {
                        ToastUtils.showShort(R.string.save_fail);
                    }
                });
                break;
        }
    }

    private ProjectEntity mEntity = new ProjectEntity();
    private ProjectEntity getProjectFromUI(){
        mEntity.setName(mEtProjectName.getText().toString());
        mEntity.setDescription(mEtProjectDescription.getText().toString());
        mEntity.setStatus(TaskDescribe.STATUS_NORMAL);
        return mEntity;
    }

    /**
     * 获取项目列表
     */
    private void getProjects(){
        TaskDBHelper.getProjects(new FindListener<ProjectEntity>() {
            @Override
            public void onSuccess(List<ProjectEntity> list) {
                ToastUtils.showShort(R.string.get_project_success);
                mList = list;
                for (ProjectEntity entity:list) {
                    LogHelper.show(TAG,entity.getName()+":"+entity.getDescription());
                }
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.showShort(R.string.get_project_failed);
            }
        });
    }

    private void fillUI(ProjectEntity entity){
        mEtProjectName.setText(entity.getName());
        mEtProjectDescription.setText(entity.getDescription());
    }
}
