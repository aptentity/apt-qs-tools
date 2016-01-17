package com.aptentity.aptqstools.model.utils;

import android.widget.Toast;

import com.aptentity.aptqstools.application.QsApplication;

/**
 * Created by Gulliver(feilong) on 16/1/17.
 */
public class ToastUtils {
    private static Toast sToast;

    /**
     * long Toast
     *
     * @param text
     */
    public static void showLong(final String text) {
        showBase(text, Toast.LENGTH_LONG);
    }
    public static void showLong(final int id) {
        showBase(id, Toast.LENGTH_LONG);
    }
    /**
     * short Toast
     *
     * @param text
     */
    public static void showShort(final String text) {
        showBase(text, Toast.LENGTH_SHORT);
    }
    public static void showShort(final int id) {
        showBase(id, Toast.LENGTH_SHORT);
    }

    private static void showBase(final String text, final int length) {
        QsApplication.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                if (sToast == null) {
                    sToast = Toast.makeText(QsApplication.getAppContext(), text, length);
                }
                sToast.setText(text);
                sToast.show();
            }
        });
    }

    private static void showBase(final int id, final int length) {
        QsApplication.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                if (sToast == null) {
                    sToast = Toast.makeText(QsApplication.getAppContext(), id, length);
                }
                sToast.setText(id);
                sToast.show();
            }
        });
    }
}
