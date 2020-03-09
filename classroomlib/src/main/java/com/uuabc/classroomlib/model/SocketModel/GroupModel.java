package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.model.CommonResult;

import java.util.List;

public class GroupModel extends CommonResult {
    /**
     * team2 : CiCi
     * teamdia1 : 44
     * teamd : true
     * time : 1529040648762
     * id : 6Ksc-8vF-QxnBaekAALQ
     * teamdia2 : 14
     * drawall : true
     * muteall : false
     * team1 : UU
     * user : [{"muted":false,"teamname":"UU","drawing":true,"point":"489,216","stage":false,"ub":1200,"team":1,"id":4243,"dia":35,"name":"douding","animate":false,"photo":"https://uutest2.uuabc.com/Fhpez6Ezu0iwGkyT7p4KqcFuUhyo"},{"teamname":"UU","stage":false,"point":"153,125","token":"100048593","photo":"","muted":false,"drawing":true,"ub":1223,"id":48593,"team":1,"name":"trrtrt","dia":9,"type":1,"animate":false},{"muted":false,"teamname":"CiCi","drawing":true,"point":"742,342","stage":false,"ub":1109,"team":2,"id":48591,"dia":11,"name":"rtrt","animate":false,"photo":""},{"teamname":"CiCi","stage":false,"point":"64,85","token":100048592,"photo":"","muted":false,"drawing":true,"ub":246,"id":48592,"team":2,"name":"celiushiyi","dia":3,"type":1,"animate":false}]
     * animate : false
     */
    private String team2;
    private int teamdia1;
    private boolean teamd;
    private long time;
    private String id;
    private int teamdia2;
    private boolean drawall;
    private boolean muteall;
    private String team1;
    private boolean animate;
    private List<UserModel> user;

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public int getTeamdia1() {
        return teamdia1;
    }

    public void setTeamdia1(int teamdia1) {
        this.teamdia1 = teamdia1;
    }

    public boolean isTeamd() {
        return teamd;
    }

    public void setTeamd(boolean teamd) {
        this.teamd = teamd;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTeamdia2() {
        return teamdia2;
    }

    public void setTeamdia2(int teamdia2) {
        this.teamdia2 = teamdia2;
    }

    public boolean isDrawall() {
        return drawall;
    }

    public void setDrawall(boolean drawall) {
        this.drawall = drawall;
    }

    public boolean isMuteall() {
        return muteall;
    }

    public void setMuteall(boolean muteall) {
        this.muteall = muteall;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public List<UserModel> getUser() {
        return user;
    }

    public void setUser(List<UserModel> user) {
        this.user = user;
    }
}
