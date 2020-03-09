package com.uuabc.classroomlib.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.uuabc.classroomlib.RoomConstant;

public class RCommonResult<T> implements Parcelable {
    private T data;
    private int status;
    private String info;
    private String msg;
    private int code;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    protected RCommonResult(Parcel in) {
        status = in.readInt();
        info = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(info);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RCommonResult> CREATOR = new Creator<RCommonResult>() {
        @Override
        public RCommonResult createFromParcel(Parcel in) {
            return new RCommonResult(in);
        }

        @Override
        public RCommonResult[] newArray(int size) {
            return new RCommonResult[size];
        }
    };

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isResultSuccess() {
        return RoomConstant.RESULT_OK_STATUS == status || RoomConstant.RESULT_OK_CODE == code;
    }

    public boolean isClassOver() {
        return RoomConstant.RESULT_CLASS_OVER_CODE == status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isOut() {
        return RoomConstant.RESULT_CODE_LEARNING == status || RoomConstant.RESULT_CODE_LEARNING == code;
    }

    public boolean isInvalid() {
        return RoomConstant.RESULT_CODE_INVALID == status || RoomConstant.RESULT_CODE_INVALID == code;
    }
}
