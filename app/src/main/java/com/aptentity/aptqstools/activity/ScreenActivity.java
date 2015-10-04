package com.aptentity.aptqstools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.db.ScreenDBEntity;
import com.aptentity.aptqstools.utils.Common;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScreenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        showScreenOn();
        showScreenOff();
        showUnlock();
    }
    
    private void showScreenOn(){
        TextView tv = (TextView) findViewById(R.id.tvSreenOn);
        tv.setText("");
        for (int i = 0; i < 10; i++) {
            int count = DataSupport.where("date>? and date<? and type=?",getDay(-i),getDay(-i+1),"1").count(ScreenDBEntity.class);
            String str = getDay(-i)+":"+count;
            tv.setText(tv.getText()+str+"\n");
            
        }
        
    }
    
    private void showUnlock(){
        TextView tv = (TextView) findViewById(R.id.tvUnlock);
        tv.setText("");
        for (int i = 0; i < 10; i++) {
            int count = DataSupport.where("date>? and date<? and type=?",getDay(-i),getDay(-i+1),"3").count(ScreenDBEntity.class);
            String str = getDay(-i)+":"+count;
            tv.setText(tv.getText()+str+"\n");
            
        }
    }
    
    private void showScreenOff(){
        TextView tv = (TextView) findViewById(R.id.tvSreenOff);
        tv.setText("");
        for (int i = 0; i < 10; i++) {
            int count = DataSupport.where("date>? and date<? and type=?",getDay(-i),getDay(-i+1),"2").count(ScreenDBEntity.class);
            String str = getDay(-i)+":"+count;
            tv.setText(tv.getText()+str+"\n");
            
        }
    }
    
    private String getDay(int n) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, n);
        return new SimpleDateFormat(Common.FOMAT2).format(cal.getTime());
    }
}
