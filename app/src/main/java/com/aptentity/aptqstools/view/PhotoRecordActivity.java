package com.aptentity.aptqstools.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.aptentity.aptqstools.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class PhotoRecordActivity extends AppCompatActivity implements View.OnClickListener{
    // 文件保存地址
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_record);
        findViewById(R.id.take_photo).setOnClickListener(this);

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                path = Environment.getExternalStorageDirectory()
                        .getCanonicalPath().toString()
                        + "/qstool/pic/";
                File files = new File(path);
                if (!files.exists()) {
                    files.mkdir();
                }
                //listFile = files.list();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_photo:
                break;
        }
    }

    final int TAKE_PICTURE = 1;//为了表示返回方法中辨识你的程序打开的相机
    /**
     * 调用系统相机进行拍照
     */
    private void takePhoto(){
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), TAKE_PICTURE);
    }

    private String paths = path;
    private String localName="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bm = (Bitmap) data.getExtras().get("data");
                localName = new SimpleDateFormat(
                        "yyyyMMddHHmmss").format(System
                        .currentTimeMillis())+ ".jpg";
                paths = path
                        + "/"
                        + localName;
                File myCaptureFile = new File(paths);
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
                    /* 采用压缩转档方法 */
                    bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
                    /* 调用flush()方法，更新BufferStream */
                    bos.flush();
                    /* 结束OutputStream */
                    bos.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 保存文件
     */
    private void savePic(String pic){

    }
}
