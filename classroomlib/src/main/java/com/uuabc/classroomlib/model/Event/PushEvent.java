package com.uuabc.classroomlib.model.Event;

import com.uuabc.classroomlib.model.ClassClockModel;

/**
 * Created by user on 2018/5/2.
 */

public class PushEvent {
    private ClassClockModel model;

    public PushEvent() {
    }

    public PushEvent(ClassClockModel model) {
        this.model = model;
    }

    public ClassClockModel getModel() {
        return model;
    }

    public void setModel(ClassClockModel model) {
        this.model = model;
    }
}
