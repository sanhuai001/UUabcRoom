package com.uuabc.classroomlib.model.Event;

import android.text.TextUtils;

import java.util.HashMap;

public class TalkCloudMsgEvent {
    private String event;
    private String uid;
    private int state;
    private HashMap<String, Object> properties;

    public TalkCloudMsgEvent(String event, String uid, HashMap<String, Object> properties) {
        this.event = event;
        this.uid = uid;
        this.properties = properties;
    }

    public TalkCloudMsgEvent(String event) {
        this.event = event;
    }

    public TalkCloudMsgEvent(String event, String uid, int state) {
        this.event = event;
        this.uid = uid;
        this.state = state;
    }

    public HashMap<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, Object> properties) {
        this.properties = properties;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUid() {
        return TextUtils.isEmpty(uid) ? "" : uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
