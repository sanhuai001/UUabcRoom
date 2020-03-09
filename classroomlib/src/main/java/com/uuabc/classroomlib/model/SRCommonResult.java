package com.uuabc.classroomlib.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class SRCommonResult<T> implements Parcelable {
    private boolean isSuccess;
    private T result;
    private Object now;
    private String code;

    private SRCommonResult(Parcel in) {
        isSuccess = in.readByte() != 0;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getNow() {
        return ObjectUtil.getIntValue(now);
    }

    public void setNow(Object now) {
        this.now = now;
    }

    public static Creator<SRCommonResult> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSuccess ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SRCommonResult> CREATOR = new Creator<SRCommonResult>() {
        @Override
        public SRCommonResult createFromParcel(Parcel in) {
            return new SRCommonResult(in);
        }

        @Override
        public SRCommonResult[] newArray(int size) {
            return new SRCommonResult[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
