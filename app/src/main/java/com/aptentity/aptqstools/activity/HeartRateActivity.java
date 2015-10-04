package com.aptentity.aptqstools.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.db.HeartRateDBEntity;
import com.aptentity.aptqstools.utils.Common;

import java.util.Date;

public class HeartRateActivity extends Activity {
    private EditText etHR,etNote;
    private Button btnType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        initView();
    }

    private void initView() {
        etHR = (EditText) findViewById(R.id.editTextWeight);
        etNote = (EditText) findViewById(R.id.editTextNote);
        // 确定
        Button btn = (Button) findViewById(R.id.buttonOK);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float heartRate = 0;
                // 检查体重的输入
                try {
                    heartRate = Float.parseFloat(etHR.getText().toString().trim());
                } catch (Exception e) {
                    Toast.makeText(HeartRateActivity.this, "心率输入错误", Toast.LENGTH_SHORT).show();
                    return;
                } finally {
                    HeartRateDBEntity hre = new HeartRateDBEntity();
                    hre.setBmp(heartRate);
                    java.text.DateFormat format = new java.text.SimpleDateFormat(Common.FOMAT);
                    hre.setDate(format.format(new Date()));
                    hre.setNote(etNote.getText().toString());
                    hre.setTimestamp(System.currentTimeMillis());
                    hre.setUuid(Common.UUID);
                    hre.setType(btnType.getText().toString());
                    hre.saveThrows();
                }
                finish();
            }
        });

        // 取消
        Button btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnType = (Button) findViewById(R.id.buttonType);

        findViewById(R.id.buttonHistory).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                    }
                });
    }
}
