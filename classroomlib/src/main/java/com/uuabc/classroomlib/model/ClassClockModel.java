package com.uuabc.classroomlib.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ClassClockModel implements Parcelable {
    private String msg;
    private int type;
    private String classId;
    private int classType = 1;
    private String img;
    private int closed;
    private long time;
    private String roomId;

    protected ClassClockModel(Parcel in) {
        msg = in.readString();
        type = in.readInt();
        classId = in.readString();
        roomId = in.readString();
        classType = in.readInt();
        img = in.readString();
        closed = in.readInt();
        time = in.readLong();
    }

    public static final Creator<ClassClockModel> CREATOR = new Creator<ClassClockModel>() {
        @Override
        public ClassClockModel createFromParcel(Parcel in) {
            return new ClassClockModel(in);
        }

        @Override
        public ClassClockModel[] newArray(int size) {
            return new ClassClockModel[size];
        }
    };

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getClosed() {
        return closed;
    }

    public void setClosed(int closed) {
        this.closed = closed;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        time = time;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeInt(type);
        dest.writeString(classId);
        dest.writeString(roomId);
        dest.writeInt(classType);
        dest.writeString(img);
        dest.writeInt(closed);
        dest.writeLong(time);
    }
}
