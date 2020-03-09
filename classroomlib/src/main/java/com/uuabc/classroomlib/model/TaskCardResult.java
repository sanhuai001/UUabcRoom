package com.uuabc.classroomlib.model;

import android.os.Parcel;

import java.util.List;

public class TaskCardResult extends CommonResult<TaskCardResult> {
    private int levelTest;
    private int deviceTest;
    private List<CourseModel> courses;

    protected TaskCardResult(Parcel in) {
        super(in);
    }

    public int getLevelTest() {
        return levelTest;
    }

    public void setLevelTest(int levelTest) {
        this.levelTest = levelTest;
    }

    public int getDeviceTest() {
        return deviceTest;
    }

    public void setDeviceTest(int deviceTest) {
        this.deviceTest = deviceTest;
    }

    public List<CourseModel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseModel> courses) {
        this.courses = courses;
    }
}
