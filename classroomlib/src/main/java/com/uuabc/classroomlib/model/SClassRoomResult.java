package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class SClassRoomResult {
    private Object app_id;
    private Object room_id;
    private Object start_time;
    private Object end_time;
    private Object courseware;
    private Object type;
    private Object member_count;
    private Object sku_num;
    private Object course_num;
    private SettingBean setting;
    private List<SUserModel> users;
    private Object courseware_name;
    private String courseWareUrl;

    public int getApp_id() {
        return ObjectUtil.getIntValue(app_id);
    }

    public void setApp_id(Object app_id) {
        this.app_id = app_id;
    }

    public int getRoom_id() {
        return ObjectUtil.getIntValue(room_id);
    }

    public void setRoom_id(Object room_id) {
        this.room_id = room_id;
    }

    public int getStart_time() {
        return ObjectUtil.getIntValue(start_time);
    }

    public void setStart_time(Object start_time) {
        this.start_time = start_time;
    }

    public int getEnd_time() {
        return ObjectUtil.getIntValue(end_time);
    }

    public void setEnd_time(Object end_time) {
        this.end_time = end_time;
    }

    public String getCourseware() {
        return ObjectUtil.getString(courseware);
    }

    public void setCourseware(Object courseware) {
        this.courseware = courseware;
    }

    public String getCoursewareUrl() {
        return courseWareUrl;
    }

    public void setCoursewareUrl(String courseWareUrl) {
        this.courseWareUrl = courseWareUrl;
    }

    public int getType() {
        return ObjectUtil.getIntValue(type);
    }

    public void setType(Object type) {
        this.type = type;
    }

    public int getMember_count() {
        return ObjectUtil.getIntValue(member_count);
    }

    public void setMember_count(Object member_count) {
        this.member_count = member_count;
    }

    public int getSku_num() {
        return ObjectUtil.getIntValue(sku_num);
    }

    public void setSku_num(String sku_num) {
        this.sku_num = sku_num;
    }

    public SettingBean getSetting() {
        return setting;
    }

    public void setSetting(SettingBean setting) {
        this.setting = setting;
    }

    public List<SUserModel> getUsers() {
        return users;
    }

    public void setUsers(List<SUserModel> users) {
        this.users = users;
    }

    public String getCourse_num() {
        return ObjectUtil.getString(course_num);
    }

    public void setCourse_num(Object course_num) {
        this.course_num = course_num;
    }

    public String getCourseware_name() {
        return ObjectUtil.getString(courseware_name);
    }

    public void setCourseware_name(Object courseware_name) {
        this.courseware_name = courseware_name;
    }

    public static class SettingBean {
        /**
         * socket_url : wss://ws.uuabc.com:10000
         * agora_key : d203027df5414201af896d5b3f3f08f7
         * return_url :
         * reward_url :
         * leave_url :
         * im :
         */

        private RSocketModel socket;
        private Object agora_key;
        private Object return_url;
        private Object reward_url;
        private Object leave_url;
        private IMModel im;

        public RSocketModel getSocket() {
            return socket;
        }

        public void setSocket(RSocketModel socket) {
            this.socket = socket;
        }

        public String getAgora_key() {
            return ObjectUtil.getString(agora_key);
        }

        public void setAgora_key(Object agora_key) {
            this.agora_key = agora_key;
        }

        public String getReturn_url() {
            return ObjectUtil.getString(return_url);
        }

        public void setReturn_url(Object return_url) {
            this.return_url = return_url;
        }

        public String getReward_url() {
            return ObjectUtil.getString(reward_url);
        }

        public void setReward_url(Object reward_url) {
            this.reward_url = reward_url;
        }

        public String getLeave_url() {
            return ObjectUtil.getString(leave_url);
        }

        public void setLeave_url(Object leave_url) {
            this.leave_url = leave_url;
        }

        public IMModel getIm() {
            return im;
        }

        public void setIm(IMModel im) {
            this.im = im;
        }
    }
}
