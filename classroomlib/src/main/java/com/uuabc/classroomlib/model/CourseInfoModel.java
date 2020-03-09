package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class CourseInfoModel {
    /**
     * liveInfo : {"id":"314","live_name":"直播课测试2018/7/23 17:55","cid":"a7d1ab043ef742809ab790c216278bcf","courseware_img":"https://uutest2.uuabc.com/courseware_centent_1532339544684","synopsis":"直播课测试2018/7/23 17:55","teacher_user_id":"25","start_time":1533535200,"course_type":"144","courseware":"888","difficult":"初级","remarks":"","rtmppullurl":"rtmp://vdl471bd653.live.126.net/live/a7d1ab043ef742809ab790c216278bcf","hlspullurl":"http://pullhlsdl471bd653.live.126.net/live/a7d1ab043ef742809ab790c216278bcf/playlist.m3u8","httppullurl":"http://flvdl471bd653.live.126.net/live/a7d1ab043ef742809ab790c216278bcf.flv?netease=flvdl471bd653.live.126.net","pushurl":"rtmp://pdl471bd653.live.126.net/live/a7d1ab043ef742809ab790c216278bcf?wsSecret=63ecdb4c0a4641e17b6155147da53182&wsTime=1532339663","flowers":"0","class_time":"20","comments":"","status":"1","price":"0"}
     * forbidden : false
     * courselist : [{"url":"https://uutest2.uuabc.com/courseware_centent_1510111881629","type":"image","page":1},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111881843","type":"image","page":2},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111881939","type":"image","page":3},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111882040","type":"image","page":4},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111882293","type":"image","page":5},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111882498","type":"image","page":6},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111882699","type":"image","page":7},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111882875","type":"image","page":8},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111883078","type":"image","page":9},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111883246","type":"image","page":10},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111883526","type":"image","page":11},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111883779","type":"image","page":12},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111883956","type":"image","page":13},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111884123","type":"image","page":14},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111884326","type":"image","page":15},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111884496","type":"image","page":16},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111884723","type":"image","page":17},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111884889","type":"image","page":18},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111885065","type":"image","page":19},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111885246","type":"image","page":20},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111885476","type":"image","page":21},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111885702","type":"image","page":22},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111885865","type":"image","page":23},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111886022","type":"image","page":24},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111886273","type":"image","page":25},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111886495","type":"image","page":26},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111886696","type":"image","page":27},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111887042","type":"image","page":28},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111887353","type":"image","page":29},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111887622","type":"image","page":30},{"url":"https://uutest2.uuabc.com/courseware_centent_1510111887904","type":"image","page":31}]
     * page : 1
     * srvurl : rtmp://ws.uuabc.com.cdn20.com/uuabc
     * student_flowers : 5
     * student_balance : 0
     * cam : {"deviceId":"efea90a3255e8f9df941c03cd6a6a057596a5679a18d81ab67eb1ce7afdd3881","label":"USB 2.0 1.3M UVC WebCam (04f2:b140)"}
     * mic : {"deviceId":"default","label":"默认"}
     * gain : 0
     * volume : 0
     * teacher_flowers : 0
     * student_dia : 0
     * courseware_name : 1. Schools in America
     * teacher_name : bobi
     */
    private LiveInfoBean liveInfo;
    private boolean forbidden;
    private String page;
    private String srvurl;
    private String student_flowers;
    private String student_balance;
    private String cam;
    private String mic;
    private int gain;
    private int volume;
    private String teacher_flowers;
    private String student_dia;
    private String courseware_name;
    private String teacher_name;
    private List<CourselistBean> courselist;

    public LiveInfoBean getLiveInfo() {
        return liveInfo;
    }

    public void setLiveInfo(LiveInfoBean liveInfo) {
        this.liveInfo = liveInfo;
    }

    public boolean isForbidden() {
        return forbidden;
    }

    public void setForbidden(boolean forbidden) {
        this.forbidden = forbidden;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSrvurl() {
        return srvurl;
    }

    public void setSrvurl(String srvurl) {
        this.srvurl = srvurl;
    }

    public int getStudent_flowers() {
        return ObjectUtil.getIntValue(student_flowers);
    }

    public void setStudent_flowers(String student_flowers) {
        this.student_flowers = student_flowers;
    }

    public int getStudent_balance() {
        return ObjectUtil.getIntValue(student_balance);
    }

    public void setStudent_balance(String student_balance) {
        this.student_balance = student_balance;
    }

    public String getCam() {
        return cam;
    }

    public void setCam(String cam) {
        this.cam = cam;
    }

    public String getMic() {
        return mic;
    }

    public void setMic(String mic) {
        this.mic = mic;
    }

    public int getGain() {
        return gain;
    }

    public void setGain(int gain) {
        this.gain = gain;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getTeacher_flowers() {
        return ObjectUtil.getIntValue(teacher_flowers);
    }

    public void setTeacher_flowers(String teacher_flowers) {
        this.teacher_flowers = teacher_flowers;
    }

    public int getStudent_dia() {
        return ObjectUtil.getIntValue(student_dia);
    }

    public void setStudent_dia(String student_dia) {
        this.student_dia = student_dia;
    }

    public String getCourseware_name() {
        return courseware_name;
    }

    public void setCourseware_name(String courseware_name) {
        this.courseware_name = courseware_name;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public List<CourselistBean> getCourselist() {
        return courselist;
    }

    public void setCourselist(List<CourselistBean> courselist) {
        this.courselist = courselist;
    }

    public static class LiveInfoBean {
        /**
         * id : 314
         * live_name : 直播课测试2018/7/23 17:55
         * cid : a7d1ab043ef742809ab790c216278bcf
         * courseware_img : https://uutest2.uuabc.com/courseware_centent_1532339544684
         * synopsis : 直播课测试2018/7/23 17:55
         * teacher_user_id : 25
         * start_time : 1533535200
         * course_type : 144
         * courseware : 888
         * difficult : 初级
         * remarks :
         * rtmppullurl : rtmp://vdl471bd653.live.126.net/live/a7d1ab043ef742809ab790c216278bcf
         * hlspullurl : http://pullhlsdl471bd653.live.126.net/live/a7d1ab043ef742809ab790c216278bcf/playlist.m3u8
         * httppullurl : http://flvdl471bd653.live.126.net/live/a7d1ab043ef742809ab790c216278bcf.flv?netease=flvdl471bd653.live.126.net
         * pushurl : rtmp://pdl471bd653.live.126.net/live/a7d1ab043ef742809ab790c216278bcf?wsSecret=63ecdb4c0a4641e17b6155147da53182&wsTime=1532339663
         * flowers : 0
         * class_time : 20
         * comments :
         * status : 1
         * price : 0
         */

        private String id;
        private String live_name;
        private String cid;
        private String courseware_img;
        private String synopsis;
        private String teacher_user_id;
        private int start_time;
        private String course_type;
        private String courseware;
        private String difficult;
        private String remarks;
        private String rtmppullurl;
        private String hlspullurl;
        private String httppullurl;
        private String pushurl;
        private String flowers;
        private String class_time;
        private String comments;
        private String status;
        private String price;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLive_name() {
            return live_name;
        }

        public void setLive_name(String live_name) {
            this.live_name = live_name;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getCourseware_img() {
            return courseware_img;
        }

        public void setCourseware_img(String courseware_img) {
            this.courseware_img = courseware_img;
        }

        public String getSynopsis() {
            return synopsis;
        }

        public void setSynopsis(String synopsis) {
            this.synopsis = synopsis;
        }

        public String getTeacher_user_id() {
            return teacher_user_id;
        }

        public void setTeacher_user_id(String teacher_user_id) {
            this.teacher_user_id = teacher_user_id;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public String getCourse_type() {
            return course_type;
        }

        public void setCourse_type(String course_type) {
            this.course_type = course_type;
        }

        public String getCourseware() {
            return courseware;
        }

        public void setCourseware(String courseware) {
            this.courseware = courseware;
        }

        public String getDifficult() {
            return difficult;
        }

        public void setDifficult(String difficult) {
            this.difficult = difficult;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getRtmppullurl() {
            return rtmppullurl;
        }

        public void setRtmppullurl(String rtmppullurl) {
            this.rtmppullurl = rtmppullurl;
        }

        public String getHlspullurl() {
            return hlspullurl;
        }

        public void setHlspullurl(String hlspullurl) {
            this.hlspullurl = hlspullurl;
        }

        public String getHttppullurl() {
            return httppullurl;
        }

        public void setHttppullurl(String httppullurl) {
            this.httppullurl = httppullurl;
        }

        public String getPushurl() {
            return pushurl;
        }

        public void setPushurl(String pushurl) {
            this.pushurl = pushurl;
        }

        public String getFlowers() {
            return flowers;
        }

        public void setFlowers(String flowers) {
            this.flowers = flowers;
        }

        public String getClass_time() {
            return class_time;
        }

        public void setClass_time(String class_time) {
            this.class_time = class_time;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public static class CourselistBean {
        /**
         * url : https://uutest2.uuabc.com/courseware_centent_1510111881629
         * type : image
         * page : 1
         */

        private String url;
        private String type;
        private int page;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }
    }
}
