package com.aptentity.aptqstools.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.dao.WeightDBEntity;
import com.aptentity.aptqstools.model.dao.WeightEntity;
import com.aptentity.aptqstools.model.dao.WeightSceneType;
import com.aptentity.aptqstools.model.utils.ToastUtils;
import com.aptentity.aptqstools.utils.Common;

import java.util.Date;

import cn.bmob.v3.listener.SaveListener;

public class WeightActivity extends Activity {

    private EditText etWeight, etNote;
    private Button btnType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        initView();
    }
    
    private void initView() {
        etWeight = (EditText) findViewById(R.id.editTextWeight);
        etNote = (EditText) findViewById(R.id.editTextNote);
        // 确定
        Button btn = (Button) findViewById(R.id.buttonOK);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float weight = 0;
                // 检查体重的输入
                try {
                    weight = Float.parseFloat(etWeight.getText().toString()
                            .trim());
                } catch (Exception e) {
                    Toast.makeText(WeightActivity.this, "体重输入错误",
                            Toast.LENGTH_SHORT).show();
                    return;
                } finally {
                    WeightEntity entity = new WeightEntity();
                    entity.uuid = Common.UUID;
                    entity.timestamp = System.currentTimeMillis();
                    entity.weight = weight;
                    entity.note = "abc";// etNote.getText().toString();
                    entity.type = btnType.getText().toString();
                    WeightDBEntity e = new WeightDBEntity(entity);
                    java.text.DateFormat format = new java.text.SimpleDateFormat(
                            Common.FOMAT);
                    e.setDate(format.format(new Date()));
                    e.save(WeightActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            ToastUtils.showShort(R.string.save_success);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            ToastUtils.showShort(R.string.save_fail);
                        }
                    });
                }

            }
        });

        // 取消
        Button btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 场景选择
        btnType = (Button) findViewById(R.id.buttonType);
        btnType.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showTypeDialog();
            }
        });

        findViewById(R.id.buttonHistory).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                    }
                });
    }

    /**
     * 创建场景选择对话框
     */
    private void showTypeDialog() {
        Dialog dialog = null;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("选择场景");
        builder.setSingleChoiceItems(WeightSceneType.SCENETYPE, 0,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = WeightSceneType.SCENETYPE[which];
                        btnType.setText(str);
                        dialog.dismiss();
                    }
                });
        dialog = builder.create();
        dialog.show();
    }
}
