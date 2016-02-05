package com.aptentity.aptqstools.model.dao;

import cn.bmob.v3.BmobObject;

/**
 * Created by Gulliver(feilong) on 16/2/5.
 * 语音记录
 */
public class SoundRecord extends BmobObject {
    private String localName="";//本地文件名
    private String remoteName="";//服务器文件名
    private String url="";//下载地址
    private String des="";//描述

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getRemoteName() {
        return remoteName;
    }

    public void setRemoteName(String remoteName) {
        this.remoteName = remoteName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
