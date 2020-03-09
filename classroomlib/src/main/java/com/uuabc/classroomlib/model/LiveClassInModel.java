package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class LiveClassInModel {

    /**
     * srvTime : 1528685031
     * beginTime : 1528685100
     * endTime : 1528688700
     * evurl : /previewReview?ac_id=293&cuew_id=336&method=fuxi&course_type=live
     * stuId : 18930
     * stuName : bobi
     * stuPhoto : null
     * balance : 797
     * teaName : bobi
     * teaId : 463
     * teaPhoto : null
     */

    private int srvTime;
    private int beginTime;
    private int endTime;
    private String evurl;
    private String stuId;
    private String stuName;
    private Object stuPhoto;
    private String balance;
    private String teaName;
    private String teaId;
    private Object teaPhoto;
    private Object skuId;

    public int getSrvTime() {
        return srvTime;
    }

    public void setSrvTime(int srvTime) {
        this.srvTime = srvTime;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getEvurl() {
        return evurl;
    }

    public void setEvurl(String evurl) {
        this.evurl = evurl;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public Object getStuPhoto() {
        return stuPhoto;
    }

    public void setStuPhoto(Object stuPhoto) {
        this.stuPhoto = stuPhoto;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public Object getTeaPhoto() {
        return teaPhoto;
    }

    public void setTeaPhoto(Object teaPhoto) {
        this.teaPhoto = teaPhoto;
    }

    public long getSkuId() {
        return ObjectUtil.getLongValue(skuId);
    }

    public void setSkuId(Object skuId) {
        this.skuId = skuId;
    }
}
