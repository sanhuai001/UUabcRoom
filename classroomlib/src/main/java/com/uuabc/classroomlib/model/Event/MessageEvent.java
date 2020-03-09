package com.uuabc.classroomlib.model.Event;

public class MessageEvent {
    private String event;
    private String data;
    private String data2;

    public MessageEvent(String event, String data) {
        this.event = event;
        this.data = data;
    }

    public MessageEvent(String event, String data, String data2) {
        this.event = event;
        this.data = data;
        this.data2 = data2;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public MessageEvent(String event) {
        this.event = event;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
