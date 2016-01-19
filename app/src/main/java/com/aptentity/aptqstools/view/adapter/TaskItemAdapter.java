package com.aptentity.aptqstools.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.dao.TaskDescribe;

import java.util.List;

/**
 * Created by gulliver on 16/1/12.
 */
public class TaskItemAdapter extends BaseAdapter{
    List<TaskDescribe> mList;
    private LayoutInflater mInflater = null;

    public TaskItemAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<TaskDescribe> list){
        mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
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
        //标题
        holder.title.setText(mList.get(i).getTitle());
        //任务状态
        holder.cbFinish.setVisibility(View.VISIBLE);
        if (mList.get(i).getStatus()== TaskDescribe.STATUS_COMPLETE){
            holder.cbFinish.setImageResource(R.mipmap.ic_clear);
        }else if (mList.get(i).getStatus()== TaskDescribe.STATUS_PAUSE){
            holder.cbFinish.setImageResource(R.mipmap.pause);
        }else if (mList.get(i).getStatus()== TaskDescribe.STATUS_RUNNING){
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
