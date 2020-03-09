package com.uuabc.classroomlib.model.SocketModel;


/**
 * 最外层的socket的model
 * Created by bobi on 2018/3/23.
 */

public class BaseSocketModel {
    private String event;
    private Object data;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
