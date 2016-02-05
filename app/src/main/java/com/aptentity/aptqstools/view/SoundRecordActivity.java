package com.aptentity.aptqstools.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.model.dao.SoundRecord;
import com.aptentity.aptqstools.model.utils.ToastUtils;
import com.aptentity.aptqstools.utils.FileUtils;
import com.aptentity.aptqstools.utils.LogHelper;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.DownloadListener;
import com.bmob.btp.callback.UploadListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class SoundRecordActivity extends Activity implements View.OnClickListener{

    final String TAG = SoundRecordActivity.class.getSimpleName();
    Button start,stop;
    ShowRecorderAdpter showRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_recorder);
        start = (Button)findViewById(R.id.start);
        stop = (Button)findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        stop.setEnabled(false);
        initMedia();
        listView = (ListView) findViewById(R.id.list);
        showRecord = new ShowRecorderAdpter();

        listView.setAdapter(showRecord);
        refreshList();
    }
    private ListView listView;
    // 录音文件播放
    private MediaPlayer myPlayer;
    // 录音
    private MediaRecorder myRecorder;
    // 音频文件保存地址
    private String path;
    // 所录音的文件
    //String[] listFile = null;
    /**
     * 初始化音频
     */
    private void initMedia(){
        myPlayer = new MediaPlayer();
        myRecorder = new MediaRecorder();
        // 从麦克风源进行录音
        myRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        // 设置输出格式
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        // 设置编码格式
        myRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                path = Environment.getExternalStorageDirectory()
                        .getCanonicalPath().toString()
                        + "/qstool";
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
            case R.id.start:
                recordSound();
                break;
            case R.id.stop:
                stopRecordSound();
                break;
        }
    }

    private String paths = path;
    private String localName="";
    private File saveFilePath;
    /**
     * 开始录音
     */
    private void recordSound(){
        try {
            localName = new SimpleDateFormat(
                    "yyyyMMddHHmmss").format(System
                    .currentTimeMillis())+ ".amr";
            paths = path
                    + "/"
                    + localName;
            saveFilePath = new File(paths);
            myRecorder.setOutputFile(saveFilePath
                    .getAbsolutePath());
            saveFilePath.createNewFile();
            myRecorder.prepare();
            // 开始录音
            myRecorder.start();
            start.setText("正在录音中。。");
            start.setEnabled(false);
            stop.setEnabled(true);
            // 重新读取 文件
            //File files = new File(path);
            //listFile = files.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    private void stopRecordSound(){
        if (saveFilePath.exists() && saveFilePath != null) {
            final File filePath = saveFilePath;
            myRecorder.stop();
            myRecorder.release();
            // 判断是否保存 如果不保存则删除
            new AlertDialog.Builder(this)
                    .setTitle("是否保存该录音")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LogHelper.show(TAG,"upload file");
                            BmobProFile.getInstance(getApplicationContext()).upload(saveFilePath.getPath(), new UploadListener() {
                                @Override
                                public void onSuccess(String fileName,String url,BmobFile file) {
                                    LogHelper.show(TAG, "upload file onSuccess " + fileName + ";" + url);

                                    SoundRecord record = new SoundRecord();
                                    record.setDes("lala");
                                    record.setLocalName(localName);
                                    record.setRemoteName(fileName);
                                    record.setUrl(url);
                                    record.save(getApplicationContext(), new SaveListener() {
                                        @Override
                                        public void onSuccess() {
                                            LogHelper.show(TAG, "save sound record onSuccess" );
                                            ToastUtils.showShort("保存成功");
                                            refreshList();
                                        }

                                        @Override
                                        public void onFailure(int i, String s) {
                                            LogHelper.show(TAG, "save sound record onFailure" );
                                            ToastUtils.showShort("保存失败");
                                        }
                                    });
                                }

                                @Override
                                public void onProgress(int i) {
                                    LogHelper.show(TAG,"upload file onProgress:"+i);

                                }

                                @Override
                                public void onError(int i, String s) {
                                    LogHelper.show(TAG,"upload file onError "+i+";"+s);
                                }
                            });
                        }
                    })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    saveFilePath.delete();
                                    // 重新读取 文件
//                                    File files = new File(path);
//                                    listFile = files.list();
                                }
                            }).show();

        }
        start.setText("录音");
        start.setEnabled(true);
    }


    @Override
    protected void onDestroy() {
        // 释放资源
        if (myPlayer.isPlaying()) {
            myPlayer.stop();
            myPlayer.release();
        }
        myPlayer.release();
        myRecorder.release();
        super.onDestroy();
    }

    List<SoundRecord> mDatalist=new ArrayList<SoundRecord>();
    /**
     * 更新列表
     * 从网络查询
     */
    private void refreshList(){
        LogHelper.show(TAG,"refreshList");
        BmobQuery<SoundRecord> query = new BmobQuery<SoundRecord>();
        query.findObjects(getApplicationContext(), new FindListener<SoundRecord>() {
            @Override
            public void onSuccess(List<SoundRecord> list) {
                LogHelper.show(TAG,"refreshList onSuccess");
                mDatalist = list;
                showRecord.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                LogHelper.show(TAG,"refreshList onError:"+i+";"+s);
            }
        });
    }

    class ShowRecorderAdpter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDatalist.size();
        }

        @Override
        public Object getItem(int arg0) {
            return arg0;
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;

        }

        @Override
        public View getView(final int postion, View arg1, ViewGroup arg2) {

            View views = LayoutInflater.from(SoundRecordActivity.this).inflate(
                    R.layout.list_show_filerecorder, null);
            TextView filename = (TextView) views
                    .findViewById(R.id.show_file_name);
            Button plays = (Button) views.findViewById(R.id.bt_list_play);
            Button stop = (Button) views.findViewById(R.id.bt_list_stop);
            Button download = (Button) views.findViewById(R.id.bt_list_download);
            String name = mDatalist.get(postion).getLocalName();//文件名
            String filepath = path + "/" +name;
            LogHelper.show(TAG,filepath);
            //检查该文件是否存在
            if (!FileUtils.fileIsExists(path + "/" +name)){
                plays.setVisibility(View.GONE);
                stop.setVisibility(View.GONE);
                download.setVisibility(View.VISIBLE);
            }else {
                plays.setVisibility(View.VISIBLE);
                stop.setVisibility(View.VISIBLE);
                download.setVisibility(View.GONE);
            }
            filename.setText(mDatalist.get(postion).getLocalName());

            // 播放录音
            plays.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        myPlayer.reset();
                        myPlayer.setDataSource(path + "/" + mDatalist.get(postion).getLocalName());
                        if (!myPlayer.isPlaying()) {

                            myPlayer.prepare();
                            myPlayer.start();
                        } else {
                            myPlayer.pause();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            // 停止播放
            stop.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (myPlayer.isPlaying()) {
                        myPlayer.stop();
                    }
                }
            });

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BmobProFile.getInstance(getApplicationContext()).download(mDatalist.get(postion).getRemoteName(), new DownloadListener() {
                        @Override
                        public void onSuccess(String s) {
                            LogHelper.show(TAG,"download onSuccess:"+s);
                            String name = mDatalist.get(postion).getLocalName();//文件名
                            String filepath = path + "/" +name;
                            FileUtils.copyFile(s,filepath);
                            showRecord.notifyDataSetChanged();
                        }

                        @Override
                        public void onProgress(String s, int i) {
                            LogHelper.show(TAG,"download onProgress:"+s+";"+i);
                        }

                        @Override
                        public void onError(int i, String s) {
                            LogHelper.show(TAG,"download onError:"+i+";"+s);
                        }
                    });
                }
            });
            return views;
        }

    }
}
