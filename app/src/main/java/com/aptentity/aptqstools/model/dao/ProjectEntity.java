package com.aptentity.aptqstools.model.dao;

import cn.bmob.v3.BmobObject;

/**
 * Created by gulliver on 16/1/3.
 * 项目
 */
public class ProjectEntity extends BmobObject{
    private String name;//项目名称
    private String description;//描述
    private String parentId;//父项目
    private int status=0;//状态

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
