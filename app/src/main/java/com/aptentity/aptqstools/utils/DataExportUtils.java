package com.aptentity.aptqstools.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class DataExportUtils {
    public static void exportDB2Sdcard(Context context){
        InputStream inputStream = null;
        try {
            File inputFile = context.getDatabasePath("qsdb.db");
            inputStream = new FileInputStream(inputFile);
            
            String name = "qsdb_"+String.valueOf(System.currentTimeMillis())+".db";
            File desFile = new File(Environment.getExternalStorageDirectory(),
                    name);
            FileUtils.copyStreamToFile(inputStream, desFile);
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            FileUtils.closeQuietly(inputStream);
        }
    }
}
