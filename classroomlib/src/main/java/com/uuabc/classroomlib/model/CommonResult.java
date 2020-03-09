package com.uuabc.classroomlib.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.utils.ObjectUtil;

public class CommonResult<T> implements Parcelable {
    private T data;
    private Object code;
    private Object msg;
    private Object success;

    protected CommonResult(Parcel in) {
        code = in.readInt();
        msg = in.readString();
    }

    protected CommonResult() {

    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return ObjectUtil.getIntValue(code);
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public String getMsg() {
        return ObjectUtil.getString(msg);
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public boolean getSuccess() {
        return ObjectUtil.getBoolean(success);
    }

    public void setSuccess(Object success) {
        this.success = success;
    }

    public static Creator<CommonResult> getCREATOR() {
        return CREATOR;
    }

    public static final Creator<CommonResult> CREATOR = new Creator<CommonResult>() {
        @Override
        public CommonResult createFromParcel(Parcel in) {
            return new CommonResult(in);
        }

        @Override
        public CommonResult[] newArray(int size) {
            return new CommonResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getCode());
        dest.writeString(getMsg());
    }

    public boolean isResultSuccess() {
        return RoomConstant.RESULT_OK_CODE == getCode();
    }

    public boolean isSuccess() {
        return getSuccess();
    }
}
