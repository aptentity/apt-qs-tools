package com.aptentity.aptqstools.view;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

/**
 * Created by gulliver on 16/1/3.
 */
public abstract class BasicActivity extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewID());
        ActionBar actionBar = getActionBar();
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getMenuId()==0){
            return false;
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(getMenuId(), menu);
        return true;
    }

    /**
     * 获取资源id
     * @return
     */
    abstract int getViewID();
    abstract int getMenuId();
    abstract void initUI();
}
