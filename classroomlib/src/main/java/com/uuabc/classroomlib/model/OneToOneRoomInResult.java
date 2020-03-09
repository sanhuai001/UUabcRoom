package com.uuabc.classroomlib.model;

import android.os.Parcel;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class OneToOneRoomInResult extends RCommonResult<OneToOneRoomInResult> {
    /**
     * curl :
     * beginTime : 1528354200
     * coursewareId : 174
     * courseware_name : Unit1.1 My Friends
     * endTime : 1528355400
     * srvTime : 1528356839
     * teaId : 554
     * stuId : 48593
     * student_into_time : 1528328011
     * teacher_into_time : 0
     * patriarch_in_time : 0
     * student_words :
     * teacher_words :
     * evurl : /courseTopic.html?acid=44053
     * isPop : false
     * teaName : Python
     * courseType : 1
     * courseHours : 20
     * teaPhoto : https://uutest2.uuabc.com/teacher_icon_1527824849112
     * stuName : celiushisan
     * stuPhoto :
     * roomType : agora
     */

    private String curl;
    private int beginTime;
    private String coursewareId;
    private String courseware_name;
    private int endTime;
    private int srvTime;
    private String teaId;
    private String stuId;
    private int student_into_time;
    private int teacher_into_time;
    private int patriarch_in_time;
    private String student_words;
    private String teacher_words;
    private String evurl;
    private boolean isPop;
    private String teaName;
    private String courseType;
    private int courseHours;
    private String teaPhoto;
    private String stuName;
    private String stuPhoto;
    private String roomType;
    private Object skuId;
    private String serial;
    private String userpwd;

    protected OneToOneRoomInResult(Parcel in) {
        super(in);
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public String getCoursewareId() {
        return coursewareId;
    }

    public void setCoursewareId(String coursewareId) {
        this.coursewareId = coursewareId;
    }

    public String getCourseware_name() {
        return courseware_name;
    }

    public void setCourseware_name(String courseware_name) {
        this.courseware_name = courseware_name;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getSrvTime() {
        return srvTime;
    }

    public void setSrvTime(int srvTime) {
        this.srvTime = srvTime;
    }

    public String getTeaId() {
        return teaId;
    }

    public void setTeaId(String teaId) {
        this.teaId = teaId;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public int getStudent_into_time() {
        return student_into_time;
    }

    public void setStudent_into_time(int student_into_time) {
        this.student_into_time = student_into_time;
    }

    public int getTeacher_into_time() {
        return teacher_into_time;
    }

    public void setTeacher_into_time(int teacher_into_time) {
        this.teacher_into_time = teacher_into_time;
    }

    public int getPatriarch_in_time() {
        return patriarch_in_time;
    }

    public void setPatriarch_in_time(int patriarch_in_time) {
        this.patriarch_in_time = patriarch_in_time;
    }

    public String getStudent_words() {
        return student_words;
    }

    public void setStudent_words(String student_words) {
        this.student_words = student_words;
    }

    public String getTeacher_words() {
        return teacher_words;
    }

    public void setTeacher_words(String teacher_words) {
        this.teacher_words = teacher_words;
    }

    public String getEvurl() {
        return evurl;
    }

    public void setEvurl(String evurl) {
        this.evurl = evurl;
    }

    public boolean isIsPop() {
        return isPop;
    }

    public void setIsPop(boolean isPop) {
        this.isPop = isPop;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public int getCourseHours() {
        return courseHours;
    }

    public void setCourseHours(int courseHours) {
        this.courseHours = courseHours;
    }

    public String getTeaPhoto() {
        return teaPhoto;
    }

    public void setTeaPhoto(String teaPhoto) {
        this.teaPhoto = teaPhoto;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public String getStuPhoto() {
        return stuPhoto;
    }

    public void setStuPhoto(String stuPhoto) {
        this.stuPhoto = stuPhoto;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public long getSkuId() {
        return ObjectUtil.getLongValue(skuId);
    }

    public void setSkuId(Object skuId) {
        this.skuId = skuId;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }
}
