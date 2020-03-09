package com.uuabc.classroomlib.model;

import android.os.Parcel;

import com.uuabc.classroomlib.model.SocketModel.UserModel;

import java.util.List;

public class RStudentResult extends RCommonResult<RStudentResult> {
    private boolean muteall;
    private boolean drawall;
    private boolean teamd;
    private int teamdia1;
    private int teamdia2;
    private List<UserModel> user;

    protected RStudentResult(Parcel in) {
        super(in);
    }

    public boolean isMuteall() {
        return muteall;
    }

    public void setMuteall(boolean muteall) {
        this.muteall = muteall;
    }

    public boolean isDrawall() {
        return drawall;
    }

    public void setDrawall(boolean drawall) {
        this.drawall = drawall;
    }

    public boolean isTeamd() {
        return teamd;
    }

    public void setTeamd(boolean teamd) {
        this.teamd = teamd;
    }

    public int getTeamdia1() {
        return teamdia1;
    }

    public void setTeamdia1(int teamdia1) {
        this.teamdia1 = teamdia1;
    }

    public int getTeamdia2() {
        return teamdia2;
    }

    public void setTeamdia2(int teamdia2) {
        this.teamdia2 = teamdia2;
    }

    public List<UserModel> getUser() {
        return user;
    }

    public void setUser(List<UserModel> user) {
        this.user = user;
    }
}
