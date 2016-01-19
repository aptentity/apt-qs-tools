package com.aptentity.aptqstools.view;

import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.dao.TaskDescribe;
import com.aptentity.aptqstools.model.utils.ActivitiesUtils;
import com.aptentity.aptqstools.presenter.TaskListPresenter;
import com.aptentity.aptqstools.utils.LogHelper;
import com.aptentity.aptqstools.view.adapter.TaskItemAdapter;

import java.util.List;

import cn.bmob.v3.listener.FindListener;

/**
 * 任务列表
 * 1、显示所有任务
 */
public class TaskListActivity extends BasicActivity {
    private final String TAG = TaskListActivity.class.getSimpleName();
    private TaskListPresenter presenter;
    private List<TaskDescribe> mListTask;
    private ListView mDrawerList;
    private String[] mTitles;
    private DrawerLayout mDrawerLayout;
    private int mDataType=0;

    @Override
    int getViewID() {
        return R.layout.activity_task_list;
    }

    @Override
    int getMenuId() {
        return R.menu.menu_task_list;
    }

    private ListView taskList;
    @Override
    void initUI() {
        taskList = (ListView)findViewById(R.id.borg_lv_task_list);
        taskList.setOnItemClickListener(l);
        presenter = new TaskListPresenter();
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mTitles = getResources().getStringArray(R.array.time_array);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
    }

    /**
     * 任务列表点击
     * 打开任务详情
     */
    private AdapterView.OnItemClickListener l = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            LogHelper.show(TAG,"listview onItemClick");
            TaskDescribe entity = mListTask.get(i);
            ActivitiesUtils.startViewTaskActivity(TaskListActivity.this,entity);
        }
    };


    /**
     * 选择显示的视图
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mDataType = position;
            getData();
            LogHelper.show(TAG,"onItemClick:"+position);
            mDrawerLayout.closeDrawers();
        }
    }

    private void getData(){
        switch (mDataType){
            case 0:
                getNormalTasks();
                break;
            case 1:
                getAllTasks();
                break;
            case 2:
                getCompletedTasks();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    /**
     * 菜单功能
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.borg_action_task_list_add:
                ActivitiesUtils.startAddTaskActivity(TaskListActivity.this);
                break;
            case R.id.borg_action_task_list_show:
                ActivitiesUtils.startFunctionActivity(TaskListActivity.this);
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 取得所有任务
     * @return
     */
    private void getAllTasks(){
        presenter.getAllTask(new FindListener<TaskDescribe>(){
            @Override
            public void onSuccess(final List<TaskDescribe> list) {
                updateTaskList(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 获得完成的任务
     */
    private void getCompletedTasks() {
        presenter.getCompletedTasks(new FindListener<TaskDescribe>() {
            @Override
            public void onSuccess(List<TaskDescribe> list) {
                updateTaskList(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 获得正常状态任务
     */
    private void getNormalTasks() {
        presenter.getNormalTasks(new FindListener<TaskDescribe>() {
            @Override
            public void onSuccess(List<TaskDescribe> list) {
                updateTaskList(list);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
    }

    /**
     * 更新任务列表
     * @param list
     */
    private void updateTaskList(final List<TaskDescribe> list){
        mListTask = list;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TaskItemAdapter adapter = new TaskItemAdapter(TaskListActivity.this);
                adapter.setData(list);
                taskList.setAdapter(adapter);
            }
        });
    }
}
