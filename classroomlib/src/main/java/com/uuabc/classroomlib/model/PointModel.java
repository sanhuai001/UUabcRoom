package com.uuabc.classroomlib.model;

import java.util.HashMap;

public class PointModel {
    private float x;
    private float y;
    private float t;

    public PointModel() {

    }

    public PointModel(float x, float y, long t) {
        this.x = x;
        this.y = y;
        this.t = t;
    }

    @Override
    public String toString() {
        HashMap hashMap = new HashMap();
        hashMap.put("x", x);
        hashMap.put("y", y);
        hashMap.put("t", t);
        return hashMap.toString();
    }
}
