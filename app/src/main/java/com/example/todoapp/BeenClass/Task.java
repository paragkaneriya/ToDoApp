package com.example.todoapp.BeenClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("task_title")
    @Expose
    private String task_title;
    @SerializedName("task_status")
    @Expose
    private String task_status;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("am_pm")
    @Expose
    private String am_pm;
    @SerializedName("isselected")
    @Expose
    private boolean isselected = false;

    @SerializedName("isExpired")
    @Expose
    private boolean isExpired = false;

    public Task(String task_title, String time, String am_pm) {
        this.task_title = task_title;
        this.time = time;
        this.am_pm = am_pm;
    }

    public Task(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }
}
