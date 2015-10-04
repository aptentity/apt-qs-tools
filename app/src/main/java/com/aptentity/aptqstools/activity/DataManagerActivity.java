package com.aptentity.aptqstools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.utils.DataExportUtils;

public class DataManagerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manager);
        Button btn = (Button) findViewById(R.id.btnExport);
        btn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DataExportUtils.exportDB2Sdcard(getApplicationContext());
            }
        });
    }
}
