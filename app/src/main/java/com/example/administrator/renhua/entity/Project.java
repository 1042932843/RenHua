package com.example.administrator.renhua.entity;

/**
 * Created by K on 2016/3/25.
 */
public class Project {

    public String id;
    public String project;
    public String hall;
    public String time;
    public String progress;

    public Project() {

    }

    public Project(String id, String project, String hall, String time, String progress) {
        this.id = id;
        this.project = project;
        this.hall = hall;
        this.time = time;
        this.progress = progress;
    }

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

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

}
