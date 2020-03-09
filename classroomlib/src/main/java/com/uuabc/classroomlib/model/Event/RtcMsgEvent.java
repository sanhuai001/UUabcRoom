package com.uuabc.classroomlib.model.Event;

import android.text.TextUtils;

import java.util.HashMap;

public class RtcMsgEvent {
    private String event;
    private String uid;
    private String errMsg;
    private HashMap<Integer, Integer> map;

    public RtcMsgEvent(String event) {
        this.event = event;
    }

    public RtcMsgEvent(String event, String uid) {
        this.event = event;
        this.uid = uid;
    }

    public RtcMsgEvent(String event, HashMap<Integer, Integer> map) {
        this.event = event;
        this.map = map;

    }

    public HashMap<Integer, Integer> getMap() {
        return map;
    }

    public void setMap(HashMap<Integer, Integer> map) {
        this.map = map;
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

    public String getErrMsg() {
        return TextUtils.isEmpty(errMsg) ? "" : errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}

