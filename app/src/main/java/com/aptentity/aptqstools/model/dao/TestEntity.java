package com.aptentity.aptqstools.model.dao;

import cn.bmob.v3.BmobObject;

/**
 * Created by gulliver on 16/1/6.
 */
public class TestEntity extends BmobObject {
    public String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
