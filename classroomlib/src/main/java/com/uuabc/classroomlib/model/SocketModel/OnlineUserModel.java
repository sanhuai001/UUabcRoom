package com.uuabc.classroomlib.model.SocketModel;

public class OnlineUserModel {
    private int user_id;
    private int role;
    private UserModel info;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public UserModel getInfo() {
        return info;
    }

    public void setInfo(UserModel info) {
        this.info = info;
    }
}
