package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class RSocketModel {
    private Object url;
    private Object path;

    public String getUrl() {
        return ObjectUtil.getString(url);
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public String getPath() {
        return ObjectUtil.getString(path);
    }

    public void setPath(Object path) {
        this.path = path;
    }
}
