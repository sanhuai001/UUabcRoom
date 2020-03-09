package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class SOverClassModel {
    private Object room_id;
    private Object app_id;
    private Object user_id;
    private Object user_type;
    private Object classover;
    private Object redirect_url;

    public int getRoom_id() {
        return ObjectUtil.getIntValue(room_id);
    }

    public void setRoom_id(Object room_id) {
        this.room_id = room_id;
    }

    public int getApp_id() {
        return ObjectUtil.getIntValue(app_id);
    }

    public void setApp_id(Object app_id) {
        this.app_id = app_id;
    }

    public String getUser_id() {
        return ObjectUtil.getString(user_id);
    }

    public void setUser_id(Object user_id) {
        this.user_id = user_id;
    }

    public int getUser_type() {
        return ObjectUtil.getIntValue(user_type);
    }

    public void setUser_type(Object user_type) {
        this.user_type = user_type;
    }

    public boolean isClassover() {
        return ObjectUtil.getBoolean(classover);
    }

    public void setClassover(Object classover) {
        this.classover = classover;
    }

    public Object getClassover() {
        return classover;
    }

    public String getRedirect_url() {
        return ObjectUtil.getString(redirect_url);
    }

    public void setRedirect_url(Object redirect_url) {
        this.redirect_url = redirect_url;
    }
}
