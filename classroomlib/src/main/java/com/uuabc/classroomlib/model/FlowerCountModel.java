package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class FlowerCountModel {
    private Object count_flowers;
    private Object flowers;

    public int getCount_flowers() {
        return ObjectUtil.getIntValue(count_flowers);
    }

    public void setCount_flowers(Object count_flowers) {
        this.count_flowers = count_flowers;
    }

    public int getFlowers() {
        return ObjectUtil.getIntValue(flowers);
    }

    public void setFlowers(Object flowers) {
        this.flowers = flowers;
    }
}
