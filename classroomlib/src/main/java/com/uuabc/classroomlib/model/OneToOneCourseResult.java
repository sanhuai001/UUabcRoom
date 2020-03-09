package com.uuabc.classroomlib.model;

import android.os.Parcel;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class OneToOneCourseResult extends RCommonResult<OneToOneCourseResult> {
    /**
     * relationCourse : {"prev":null,"current":{"url":"https://uutest2.uuabc.com/courseware_centent_1480300383581","id":"174","total":33},"next":{"id":"175","url":"https://uutest2.uuabc.com/courseware_centent_1480300820481","total":28}}
     * courselist : [{"url":"https://uutest2.uuabc.com/courseware_centent_1480300383581","type":"image","page":1},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300383932","type":"image","page":2},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300384254","type":"image","page":3},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300384845","type":"image","page":4},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300385318","type":"image","page":5},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300385801","type":"image","page":6},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300386268","type":"image","page":7},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300386681","type":"image","page":8},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300387237","type":"image","page":9},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300387667","type":"image","page":10},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300388179","type":"image","page":11},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300388926","type":"image","page":12},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300389320","type":"image","page":13},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300389778","type":"image","page":14},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300390213","type":"image","page":15},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300390476","type":"image","page":16},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300391018","type":"image","page":17},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300391372","type":"image","page":18},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300391705","type":"image","page":19},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300392067","type":"image","page":20},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300392416","type":"image","page":21},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300392781","type":"image","page":22},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300393039","type":"image","page":23},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300393299","type":"image","page":24},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300393692","type":"image","page":25},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300394022","type":"image","page":26},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300394374","type":"image","page":27},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300394647","type":"image","page":28},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300395024","type":"image","page":29},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300395324","type":"image","page":30},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300395749","type":"image","page":31},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300396122","type":"image","page":32},{"url":"https://uutest2.uuabc.com/courseware_centent_1480300396605","type":"image","page":33}]
     * student_stream : pad
     * teacher_stream : null
     * srvurl : rtmp://ws.uuabc.com/uuabc
     * cam :
     * mic :
     * gain : 0
     * volume : 0
     * page : 1
     * harvest : 0
     */

    private RelationCourseBean relationCourse;
    private String student_stream;
    private Object teacher_stream;
    private String srvurl;
    private String cam;
    private String mic;
    private int gain;
    private int volume;
    private String page;
    private int harvest;
    private List<CourselistBean> courselist;

    protected OneToOneCourseResult(Parcel in) {
        super(in);
    }

    public RelationCourseBean getRelationCourse() {
        return relationCourse;
    }

    public void setRelationCourse(RelationCourseBean relationCourse) {
        this.relationCourse = relationCourse;
    }

    public String getStudent_stream() {
        return student_stream;
    }

    public void setStudent_stream(String student_stream) {
        this.student_stream = student_stream;
    }

    public Object getTeacher_stream() {
        return teacher_stream;
    }

    public void setTeacher_stream(Object teacher_stream) {
        this.teacher_stream = teacher_stream;
    }

    public String getSrvurl() {
        return srvurl;
    }

    public void setSrvurl(String srvurl) {
        this.srvurl = srvurl;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public int getHarvest() {
        return harvest;
    }

    public void setHarvest(int harvest) {
        this.harvest = harvest;
    }

    public List<CourselistBean> getCourselist() {
        return courselist;
    }

    public void setCourselist(List<CourselistBean> courselist) {
        this.courselist = courselist;
    }

    public static class RelationCourseBean {
        /**
         * prev : null
         * current : {"url":"https://uutest2.uuabc.com/courseware_centent_1480300383581","id":"174","total":33}
         * next : {"id":"175","url":"https://uutest2.uuabc.com/courseware_centent_1480300820481","total":28}
         */

        private Object prev;
        private CurrentBean current;
        private NextBean next;

        public Object getPrev() {
            return prev;
        }

        public void setPrev(Object prev) {
            this.prev = prev;
        }

        public CurrentBean getCurrent() {
            return current;
        }

        public void setCurrent(CurrentBean current) {
            this.current = current;
        }

        public NextBean getNext() {
            return next;
        }

        public void setNext(NextBean next) {
            this.next = next;
        }

        public static class CurrentBean {
            /**
             * url : https://uutest2.uuabc.com/courseware_centent_1480300383581
             * id : 174
             * total : 33
             */

            private String url;
            private String id;
            private int total;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }

        public static class NextBean {
            /**
             * id : 175
             * url : https://uutest2.uuabc.com/courseware_centent_1480300820481
             * total : 28
             */

            private String id;
            private String url;
            private int total;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }
    }

    public static class CourselistBean {
        /**
         * url : https://uutest2.uuabc.com/courseware_centent_1480300383581
         * type : image
         * page : 1
         */

        private String url;
        private String type;
        private Object page;

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
            return ObjectUtil.getIntValue(page);
        }

        public void setPage(Object page) {
            this.page = page;
        }
    }
}
