package com.uuabc.classroomlib.model.SocketModel;

public class SShareModel {
    private String event;
    private Object data;

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public SShareModel(String event) {
        this.event = event;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
