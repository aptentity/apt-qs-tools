package com.aptentity.aptqstools.application;

import com.aptentity.aptqstools.utils.Common;

import org.litepal.LitePalApplication;

public class QsApplication extends LitePalApplication{
    @Override
    public void onCreate() {
        super.onCreate();

        //启动service
        Common.startPhoneUseService(getApplicationContext());
    }
}
