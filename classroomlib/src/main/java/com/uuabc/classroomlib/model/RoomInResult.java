package com.uuabc.classroomlib.model;

import android.os.Parcel;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class RoomInResult extends RCommonResult<RoomInResult> {
    private int beginTime;
    private String evurl;
    private CourselistBean courselist;
    private String courseware_name;
    private int endTime;
    private int srvTime;
    private String intoTime;
    private String camera;
    private String mic;
    private Object id;
    private String name;
    private String photo;
    private Object courseType;
    private Object courseHours;
    private String words;
    private String page;
    private int token;
    private String serial;
    private String userpwd;
    private int roomType;
    private Object skuId;

    protected RoomInResult(Parcel in) {
        super(in);
    }

    public int getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(int beginTime) {
        this.beginTime = beginTime;
    }

    public String getEvurl() {
        return evurl;
    }

    public void setEvurl(String evurl) {
        this.evurl = evurl;
    }

    public CourselistBean getCourselist() {
        return courselist;
    }

    public void setCourselist(CourselistBean courselist) {
        this.courselist = courselist;
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

    public String getIntoTime() {
        return intoTime;
    }

    public void setIntoTime(String intoTime) {
        this.intoTime = intoTime;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getMic() {
        return mic;
    }

    public void setMic(String mic) {
        this.mic = mic;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
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

    public Object getCourseType() {
        return courseType;
    }

    public void setCourseType(Object courseType) {
        this.courseType = courseType;
    }

    public int getCourseHours() {
        return ObjectUtil.getIntValue(courseHours);
    }

    public void setCourseHours(Object courseHours) {
        this.courseHours = courseHours;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
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

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public long getSkuId() {
        return ObjectUtil.getLongValue(skuId);
    }

    public void setSkuId(Object skuId) {
        this.skuId = skuId;
    }

    public static class CourselistBean {
        private Object total;
        private String url;
        private List<Integer> page;

        public Object getTotal() {
            return total;
        }

        public void setTotal(Object total) {
            this.total = total;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<Integer> getPage() {
            return page;
        }

        public void setPage(List<Integer> page) {
            this.page = page;
        }
    }
}
