package com.aptentity.aptqstools.view;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.dao.TaskEntity;
import com.aptentity.aptqstools.model.utils.ActivitiesUtils;
import com.aptentity.aptqstools.presenter.TaskListPresenter;
import com.aptentity.aptqstools.utils.LogHelper;

import java.util.LinkedList;
import java.util.List;

/**
 * 任务列表
 * 1、显示所有任务
 */
public class TaskListActivity extends BasicActivity {
    private final String TAG = TaskListActivity.class.getSimpleName();
    private TaskListPresenter presenter;
    private List<TaskEntity> mListTask;
    private ListView mDrawerList;
    private String[] mTitles;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

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
    private List<String> getData(){
        mListTask = presenter.getAllTask();
        List<String> list = new LinkedList<String>();
        for (TaskEntity task:mListTask) {
            list.add(task.getTitle());
        }
        return list;
    }

    private AdapterView.OnItemClickListener l = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            LogHelper.show(TAG,"listview onItemClick");
            TaskEntity entity = mListTask.get(i);
            ActivitiesUtils.startViewTaskActivity(TaskListActivity.this,entity);
        }
    };

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LogHelper.show(TAG,"onItemClick:"+position);
        }
    }
}
