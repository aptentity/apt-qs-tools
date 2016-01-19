package com.aptentity.aptqstools.model.callback;

/**
 * Created by Gulliver(feilong) on 16/1/19.
 */
public interface ResultCallback {
    public void onSuccess();
    public void onFailed(int code, String msg);
}
