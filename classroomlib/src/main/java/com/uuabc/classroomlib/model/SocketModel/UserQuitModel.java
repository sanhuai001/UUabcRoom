package com.uuabc.classroomlib.model.SocketModel;

/**
 * 用户退出的model
 * Created by bobi on 2018/4/19.
 */

public class UserQuitModel {

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
