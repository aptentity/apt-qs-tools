package com.aptentity.aptqstools.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.dao.ProjectEntity;
import com.aptentity.aptqstools.model.dao.TaskDescribe;

import java.util.List;

/**
 * Created by gulliver on 16/1/12.
 */
public class TaskItemAdapter extends BaseAdapter{
    private List<TaskDescribe> mList;
    private List<ProjectEntity> mProjectList;
    private int type = 0;
    private LayoutInflater mInflater = null;

    public int getType(){
        return type;
    }

    public TaskItemAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<TaskDescribe> list){
        mList = list;
        type = 0;
    }

    public void setPrjectData(List<ProjectEntity> list){
        mProjectList = list;
        type = 1;
    }
    @Override
    public int getCount() {
        if (type == 0){
            return mList.size();
        }else {
            return  mProjectList.size();
        }
    }

    @Override
    public Object getItem(int i) {
        if (type == 1){
            return mProjectList.get(i);
        }
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.task_item, null);
            holder.title = (TextView)convertView.findViewById(R.id.tv_task_name);
            holder.cbFinish = (ImageView)convertView.findViewById(R.id.iv_finish);
            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();

        }

        String title;
        int status;
        if (type==1){
            title = mProjectList.get(i).getName();
            status = mProjectList.get(i).getStatus();
        }else {
            title = mList.get(i).getTitle();
            status = mList.get(i).getStatus();
        }

        //标题
        holder.title.setText(title);
        //任务状态
        holder.cbFinish.setVisibility(View.VISIBLE);
        if (status== TaskDescribe.STATUS_COMPLETE){
            holder.cbFinish.setImageResource(R.mipmap.ic_clear);
        }else if (status== TaskDescribe.STATUS_PAUSE){
            holder.cbFinish.setImageResource(R.mipmap.pause);
        }else if (status== TaskDescribe.STATUS_RUNNING){
            holder.cbFinish.setImageResource(R.mipmap.playing);
        }else {
            holder.cbFinish.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder
    {
        public ImageView cbFinish;
        public TextView title;
        public TextView info;
    }
}
