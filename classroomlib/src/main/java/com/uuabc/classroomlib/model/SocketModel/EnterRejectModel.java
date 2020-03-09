package com.uuabc.classroomlib.model.SocketModel;

/**
 * Created by user on 2018/4/19.
 */

public class EnterRejectModel {

    /**
     * info : error
     * id : this.id
     * time : Date.now()
     */

    private String info;
    private String id;
    private String time;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
