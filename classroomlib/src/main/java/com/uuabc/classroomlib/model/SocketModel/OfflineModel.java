package com.uuabc.classroomlib.model.SocketModel;

import java.util.List;

/**
 * Created by user on 2018/4/18.
 */

public class OfflineModel {
    private long time;
    private String token;
    private String id;
    private String newSocket;
    private List<String> device;
    private List<String> type;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNewSocket() {
        return newSocket;
    }

    public void setNewSocket(String newSocket) {
        this.newSocket = newSocket;
    }

    public List<String> getDevice() {
        if (device == null) {
            return (type == null || type.size() == 0) ? null : type;
        }
        return device;
    }

    public void setDevice(List<String> device) {
        this.device = device;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }
}
