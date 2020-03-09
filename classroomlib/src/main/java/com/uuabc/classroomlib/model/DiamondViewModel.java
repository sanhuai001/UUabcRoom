package com.uuabc.classroomlib.model;

import android.widget.ImageView;

public class DiamondViewModel {
    private ImageView ivDiamond;
    private int userId;
    private int total;

    public ImageView getIvDiamond() {
        return ivDiamond;
    }

    public void setIvDiamond(ImageView ivDiamond) {
        this.ivDiamond = ivDiamond;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
