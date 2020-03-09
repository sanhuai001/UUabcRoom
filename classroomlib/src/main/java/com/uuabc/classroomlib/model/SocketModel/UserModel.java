package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class UserModel {
    private Object id;
    private int type;
    private Object token;
    private String name;
    private String photo;
    private int dia;
    private int diamond;
    private int ub;
    private boolean muted = true;
    private boolean drawing = true;
    private int team;
    private boolean stage;
    private String point;
    private String teamname;
    private boolean animate;
    private boolean isOnline;
    private Object entryTime;
    private Object leaveTime;
    private int volume;//用于音量检测
    private int volumeLevel = 100;//用于调节音量用的
    private boolean camera = true;
    private String uuid;
    private Object channel;

    public int getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(int volumeLevel) {
        this.volumeLevel = volumeLevel;
    }

    public void resetVolumeLevel() {
        this.volumeLevel = 100;
    }

    public boolean isCamera() {
        return camera;
    }

    public void setCamera(boolean camera) {
        this.camera = camera;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId() {
        return ObjectUtil.getIntValue(id);
    }

    public String getIdStr() {
        return String.valueOf(ObjectUtil.getIntValue(id));
    }

    public void setId(Object id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToken() {
        return token != null ? String.valueOf(token) : "";
    }

    public void setToken(Object token) {
        this.token = token;
    }

    public int getDia() {
        return dia == 0 ? diamond : dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public int getUb() {
        return ub;
    }

    public void setUb(int ub) {
        this.ub = ub;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isDrawing() {
        return drawing;
    }

    public void setDrawing(boolean drawing) {
        this.drawing = drawing;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public boolean isStage() {
        return stage;
    }

    public void setStage(boolean stage) {
        this.stage = stage;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public boolean isAnimate() {
        return animate;
    }

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getEntryTime() {
        return ObjectUtil.getIntValue(entryTime);
    }

    public void setEntryTime(Object entryTime) {
        this.entryTime = entryTime;
    }

    public int getLeaveTime() {
        return ObjectUtil.getIntValue(leaveTime);
    }

    public void setLeaveTime(Object leaveTime) {
        this.leaveTime = leaveTime;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getChannel() {
        return ObjectUtil.getIntValue(channel);
    }

    public void setChannel(Object channel) {
        this.channel = channel;
    }
}
