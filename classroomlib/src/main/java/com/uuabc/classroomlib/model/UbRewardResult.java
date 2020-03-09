package com.uuabc.classroomlib.model;

import android.os.Parcel;

import java.util.List;

public class UbRewardResult extends CommonResult<List<UbRewardResult>> {
    private int classId;
    private int classType;
    private int balance;

    public UbRewardResult(Parcel in) {
        super(in);
    }

    public UbRewardResult() {
        super();
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}
