package com.example.administrator.renhua.entity;

/**
 * Created by K on 2016/4/7.
 */
public class AffairsRecord {

    public String id;
    public String project;
    public int finish_type;
    public String progress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public int getFinish_type() {
        return finish_type;
    }

    public void setFinish_type(int finish_type) {
        this.finish_type = finish_type;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
