package com.aptentity.aptqstools.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.dao.TaskEntity;

import java.util.List;

/**
 * Created by gulliver on 16/1/12.
 */
public class TaskItemAdapter extends BaseAdapter{
    List<TaskEntity> mList;
    private LayoutInflater mInflater = null;

    public TaskItemAdapter(Context context){
        this.mInflater = LayoutInflater.from(context);
    }

    public void setData(List<TaskEntity> list){
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
            holder.cbFinish = convertView.findViewById(R.id.iv_finish);


            convertView.setTag(holder);
        }else
        {
            holder = (ViewHolder)convertView.getTag();

        }
        //标题
        holder.title.setText(mList.get(i).getTitle());
        //任务状态
        if (mList.get(i).getStatus()==TaskEntity.STATUS_COMPLETE){
            holder.cbFinish.setVisibility(View.VISIBLE);
        }else {
            holder.cbFinish.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class ViewHolder
    {
        public View cbFinish;
        public ImageView img;
        public TextView title;
        public TextView info;
    }
}
