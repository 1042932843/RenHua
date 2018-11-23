package com.example.administrator.renhua.entity;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class UploadFiles {
private String fileName;
    private String name;
    private String isNeed;
    private String serviceItemId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsNeed() {
        return isNeed;
    }

    public void setIsNeed(String isNeed) {
        this.isNeed = isNeed;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(String serviceItemId) {
        this.serviceItemId = serviceItemId;
    }
}
