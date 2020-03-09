package com.uuabc.classroomlib.model.SocketModel;

import java.util.List;

/**
 * 用户加入进来的model
 * Created by bobi on 2018/4/19.
 */

public class UserEnterModel {
    private String address;
    private long time;
    private String id;
    private UserModel user;
    private List<String> device;
    private int flower;

    public int getFlower() {
        return flower;
    }

    public void setFlower(int flower) {
        this.flower = flower;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getDevice() {
        return device;
    }

    public void setDevice(List<String> device) {
        this.device = device;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
