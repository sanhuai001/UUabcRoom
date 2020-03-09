package com.uuabc.classroomlib.model.SocketModel;

import java.util.List;

/**
 * 登录成功
 * 的model
 * Created by user on 2018/3/23.
 */

public class EnterSuccessModel {

    private String id;
    private UserModel user;
    private String address;
    private long time;
    private List<String> device;
    private List<EnterSuccessModel> list;

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

    public List<String> getDevice() {
        return device;
    }

    public void setDevice(List<String> device) {
        this.device = device;
    }

    public List<EnterSuccessModel> getList() {
        return list;
    }

    public void setList(List<EnterSuccessModel> list) {
        this.list = list;
    }
}
