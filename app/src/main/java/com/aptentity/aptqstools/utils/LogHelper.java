package com.aptentity.aptqstools.utils;

import android.os.Environment;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Created by Gulliver(feilong) on 15/10/4.
 */
public class LogHelper {
    static FileWriter writer = null;
    static FileWriter writer2 = null;
    private static boolean debug = true;
    static String tag = "borg_qs_";
    private static final String LOG_FILE_NAME = "/qslog%g.txt";
    private static final int LOG_FILE_SIZE = 500 * 1024;// 500K
    private static final int LOG_FILE_NUMBER = 2;
    private static Logger sLogger = null;
    private static ThreadLogger mThreadLogger = null;

    /**
     * 如果是debug状态，则将log写入文件
     */
    static {
        if(debug){
            boolean sdCardExist = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
            if (sdCardExist) {
                String strFilePath2 = Environment.getExternalStorageDirectory()
                        + "/qslog1.txt";
                try {
                    writer = new FileWriter(strFilePath2, true);
                } catch (Exception e) {
                }

                String strFilePath = Environment.getExternalStorageDirectory()
                        + "/qslog2.txt";
                try {
                    writer2 = new FileWriter(strFilePath, true);
                } catch (Exception e) {
                }
            }
        }
    }
    public static void i(String log){
        Log.i(tag,log);
    }

    public static void v(String log){
        Log.v(tag, log);
    }
    public static void e(String log){
        Log.e(tag, log);
    }

    public static void show(String log){
        Log.v(tag, log);
        if(!debug){
            return;
        }
        if (mThreadLogger == null) {
            mThreadLogger = new ThreadLogger();
        }

        try {
            mThreadLogger.info(log);
        } catch (Exception e) {
            Log.v(tag, e.toString());
        }
    }

    public static void show(String TAG,String log){
        Log.v(tag+TAG, log);
        if(!debug){
            return;
        }
        if (mThreadLogger == null) {
            mThreadLogger = new ThreadLogger();
        }

        try {
            mThreadLogger.info(log);
        } catch (Exception e) {
            Log.v(tag, e.toString());
        }
    }

    private static void init() {
        Log.v(tag, "Init Debug:" + debug);

        if (!debug) {
            return;
        }

        if (sLogger != null) {
            return;
        }

        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            final String sdDir = Environment.getExternalStorageDirectory().toString();
            try {

                // suppress the logging output to the console
                Logger rootLogger = Logger.getLogger("");
                Handler[] handlers = rootLogger.getHandlers();
                if (handlers != null && handlers.length >= 1 && handlers[0] instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handlers[0]);
                }

                sLogger = Logger.getLogger(LogHelper.class.getName());
                sLogger.setLevel(Level.INFO);

                FileHandler fileTxt = new FileHandler(sdDir + LOG_FILE_NAME, LOG_FILE_SIZE, LOG_FILE_NUMBER, true);
                fileTxt.setFormatter(new Formatter() {
                    static final String DT_FORMAT = "yyyy-MM-dd HH:mm:ss";
                    final SimpleDateFormat formatter = new SimpleDateFormat(DT_FORMAT);

                    @Override
                    public String format(LogRecord r) {
                        return String.format("%s\t%s\n", formatter.format(System.currentTimeMillis()), r.getMessage());
                    }
                });
                sLogger.addHandler(fileTxt);
            } catch (Exception e) {
                Log.v(tag, e.toString());
            }
        } else {
            Log.v(tag, "sdCardExist does not exist");
        }
    }

    private static class MyLogHandler extends android.os.Handler {
        static final int INIT = 1;
        static final int LOG = 2;
        public MyLogHandler(Looper l) {
            super(l);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case INIT:
                    init();
                    break;
                case LOG:
                    if(msg.obj != null && sLogger != null){
                        sLogger.info((String)msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private static class ThreadLogger {
        private android.os.Handler mHandler;
        private HandlerThread mThread;

        ThreadLogger() {
            mThread = new HandlerThread("QsLogThread");
            mThread.start();
            mHandler = new MyLogHandler(mThread.getLooper());
            mHandler.obtainMessage(MyLogHandler.INIT).sendToTarget();
        }

        public void info(String msg){
            mHandler.obtainMessage(MyLogHandler.LOG, msg).sendToTarget();
        }
    }
}
