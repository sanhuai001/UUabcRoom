package com.uuabc.classroomlib.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.uuabc.classroomlib.RoomConstant;

import java.util.List;

public class AnimateLogResult implements Parcelable {
    /**
     * list : ["{\"pageIndex\":12,\"cmd\":\"99\",\"type\":\"1\"}","{\"pageIndex\":12,\"cmd\":\"503\",\"type\":\"3\",\"targetIndex\":1,\"x\":205,\"y\":266}","{\"pageIndex\":12,\"cmd\":\"503\",\"type\":\"3\",\"targetIndex\":0,\"x\":405,\"y\":364}","{\"pageIndex\":12,\"cmd\":\"503\",\"type\":\"3\",\"targetIndex\":2,\"x\":835,\"y\":341}"]
     * status : 1
     * info : ok
     */

    private int status;
    private String info;
    private List<String> list;

    protected AnimateLogResult(Parcel in) {
        status = in.readInt();
        info = in.readString();
        list = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(status);
        dest.writeString(info);
        dest.writeStringList(list);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AnimateLogResult> CREATOR = new Creator<AnimateLogResult>() {
        @Override
        public AnimateLogResult createFromParcel(Parcel in) {
            return new AnimateLogResult(in);
        }

        @Override
        public AnimateLogResult[] newArray(int size) {
            return new AnimateLogResult[size];
        }
    };

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

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    public boolean isResultSuccess() {
        return RoomConstant.RESULT_OK_STATUS == status;
    }

    public boolean isOut() {
        return RoomConstant.RESULT_CODE_LEARNING == status;
    }

    public boolean isInvalid() {
        return RoomConstant.RESULT_CODE_INVALID == status;
    }
}
