package com.uuabc.classroomlib.model.SocketModel;

/**
 * Created by user on 2018/4/19.
 */

public class CompleteModel {
    private String id;
    private UserModel user;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
