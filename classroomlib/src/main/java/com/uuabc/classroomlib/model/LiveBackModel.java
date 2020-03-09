package com.uuabc.classroomlib.model;

import java.util.ArrayList;
import java.util.List;

public class LiveBackModel {

    private PageInfo pageInfo;
    private ArrayList<Lists> lists;

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public ArrayList<Lists> getLists() {
        return lists;
    }

    public void setLists(ArrayList<Lists> lists) {
        this.lists = lists;
    }

    public static class PageInfo {
        /**
         * pageSize : 6
         * lastTime : 1494205800
         */

        private int pageSize;
        private long lastTime;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public long getLastTime() {
            return lastTime;
        }

        public void setLastTime(long lastTime) {
            this.lastTime = lastTime;
        }
    }

    public static class Lists {
        /**
         * classId : 1092
         * classType : 3
         * videoBack : www.baidu.com
         * title : iPhone直播课
         * img : https://uutest2.uuabc.com/courseware_centent_1517283820179
         * startTime : 1519960805
         * startTimeTxt : 2018-03-02 周五 11:20
         * teacherIcon :
         * teacherName : 111
         * level : []
         * state : 0
         * status : 2
         */

        private String classId;
        private int classType;
        private String videoBack;
        private String title;
        private String img;
        private int startTime;
        private String startTimeTxt;
        private String teacherIcon;
        private String teacherName;
        private int state;
        private int status;
        private List<String> level;

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

        public String getVideoBack() {
            return videoBack;
        }

        public void setVideoBack(String videoBack) {
            this.videoBack = videoBack;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public String getStartTimeTxt() {
            return startTimeTxt;
        }

        public void setStartTimeTxt(String startTimeTxt) {
            this.startTimeTxt = startTimeTxt;
        }

        public String getTeacherIcon() {
            return teacherIcon;
        }

        public void setTeacherIcon(String teacherIcon) {
            this.teacherIcon = teacherIcon;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public List<String> getLevel() {
            return level;
        }

        public void setLevel(List<String> level) {
            this.level = level;
        }
    }
}
