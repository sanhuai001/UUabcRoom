package com.uuabc.classroomlib.model;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

public class UserInfoResult extends CommonResult<UserInfoResult> {
    private int userId;
    private String name;
    private String sex;
    private String avatar;
    private String phone;
    private int diamond;
    private int gold;
    private int type;
    private int sawOfficialVideo;
    private int sawGuideVideo;
    private String guideVideoUrl;
    private String officialVideoUrl;
    private int levelLoction;
    @SerializedName("switch")
    private int switchX;

    protected UserInfoResult(Parcel in) {
        super(in);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSawOfficialVideo() {
        return sawOfficialVideo;
    }

    public void setSawOfficialVideo(int sawOfficialVideo) {
        this.sawOfficialVideo = sawOfficialVideo;
    }

    public int getSawGuideVideo() {
        return sawGuideVideo;
    }

    public void setSawGuideVideo(int sawGuideVideo) {
        this.sawGuideVideo = sawGuideVideo;
    }

    public String getGuideVideoUrl() {
        return guideVideoUrl;
    }

    public void setGuideVideoUrl(String guideVideoUrl) {
        this.guideVideoUrl = guideVideoUrl;
    }

    public String getOfficialVideoUrl() {
        return officialVideoUrl;
    }

    public void setOfficialVideoUrl(String officialVideoUrl) {
        this.officialVideoUrl = officialVideoUrl;
    }

    public int getSwitchX() {
        return switchX;
    }

    public void setSwitchX(int switchX) {
        this.switchX = switchX;
    }

    public int getLevelLoction() {
        return levelLoction;
    }

    public void setLevelLoction(int levelLoction) {
        this.levelLoction = levelLoction;
    }
}
