package com.aptentity.aptqstools.model.utils;

import com.aptentity.aptqstools.R;
import com.aptentity.aptqstools.application.QsApplication;

/**
 * Created by Gulliver(feilong) on 16/1/17.
 */
public class UrgentUtils {
    public static String getUrgentName(int i){
        switch (i){
            case LOW:
                return QsApplication.getInstance().getString(R.string.low);
            case MIDDLE:
                return QsApplication.getInstance().getString(R.string.middle);
            case HIGH:
                return QsApplication.getInstance().getString(R.string.high);
            default:
                return "";
        }
    }

    public static int getUrgentIdex(String name){
        if(name.equals(QsApplication.getInstance().getString(R.string.low))){
            return LOW;
        }else if (name.equals(QsApplication.getInstance().getString(R.string.middle))){
            return MIDDLE;
        }else if (name.equals(QsApplication.getInstance().getString(R.string.high))){
            return HIGH;
        }
        return UNSELECT;
    }

    public static final int LOW=1;
    public static final int MIDDLE=5;
    public static final int HIGH=9;
    public static final int UNSELECT=-1;
}
