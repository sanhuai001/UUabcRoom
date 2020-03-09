package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class SUserModel {
    private Object user_id;
    private Object user_type;
    private Object nickname;
    private Object avatar;
    private Object diamond;
    private Object entry_time;
    private Object leave_time;
    private Object message;
    private Object child_id;
    private Object external_id;

    public String getUser_id() {
        return String.valueOf(ObjectUtil.getIntValue(user_id));
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

    public String getNickname() {
        return ObjectUtil.getString(nickname);
    }

    public void setNickname(Object nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return ObjectUtil.getString(avatar);
    }

    public void setAvatar(Object avatar) {
        this.avatar = avatar;
    }

    public int getDiamond() {
        return ObjectUtil.getIntValue(diamond);
    }

    public void setDiamond(Object diamond) {
        this.diamond = diamond;
    }

    public Object getEntry_time() {
        return entry_time;
    }

    public void setEntry_time(Object entry_time) {
        this.entry_time = entry_time;
    }

    public int getLeave_time() {
        return ObjectUtil.getIntValue(leave_time);
    }

    public void setLeave_time(Object leave_time) {
        this.leave_time = leave_time;
    }

    public String getMessage() {
        return ObjectUtil.getString(message);
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public int getChild_id() {
        return ObjectUtil.getIntValue(child_id);
    }

    public void setChild_id(Object child_id) {
        this.child_id = child_id;
    }

    public String getExternal_id() {
        return ObjectUtil.getString(external_id);
    }

    public void setExternal_id(Object external_id) {
        this.external_id = external_id;
    }
}
