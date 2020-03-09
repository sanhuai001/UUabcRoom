package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class SwitchModel {
    private Object id;
    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getId() {
        return ObjectUtil.getIntValue(id);
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isOpen() {
        return ObjectUtil.getBoolean(value);
    }
}
