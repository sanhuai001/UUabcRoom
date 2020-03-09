package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class CourseDetailsResult {

    /**
     * classType : 2
     * classId : 37122
     * teacherName : it
     * teacherIcon :
     * startTime : 1525328400
     * startTimeTxt : 2018/05/03 周四 14:20
     * classCourseId : 663
     * title : Primary School 1A-一对四课件KA104
     * img : https://uutest2.uuabc.com/courseware_img_1524808599549
     * desc : 课程简介
     * diamonds : null
     * diamondsRank : [{"gid":"0","gname":null,"student":[{"studentId":"1952","my":2,"studentName":"adf","studentAvatar":"https://uutest2.uuabc.com/Fja9IsYTor5yaR5smqW-QRsXX2hi","diamond":"0"}],"diamonds":0}]
     * diamondsMax : 0
     * diamondsTag : 0
     * courseProgress : {"courrentLevel":"JA","nextLevel":"JB","progress":0.87,"status":1}
     * isPreview : 0
     * isLearned : 2
     * isExercises : 1
     * isTopic : 2
     */

    private int classType;
    private String classId;
    private String teacherName;
    private String teacherIcon;
    private Object startTime;
    private String startTimeTxt;
    private String classCourseId;
    private String title;
    private String img;
    private String desc;
    private int diamonds;
    private String diamondsMax;
    private int diamondsTag;
    private CourseProgressBean courseProgress;
    private int isPreview;
    private int isLearned;
    private int isExercises;
    private String isTopic;
    private String costHour;
    private int roomId;
    private List<DiamondsRankBean> diamondsRank;

    private List<String> level;
    private String videoBack;
    private AnswerRankBean answerRank;
    private Object skuId;
    private int roomType;
    private int seatType;
    private int status;

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

    public String getVideoBack() {
        return videoBack;
    }

    public void setVideoBack(String videoBack) {
        this.videoBack = videoBack;
    }

    public List<String> getLevel() {
        return level;
    }

    public void setLevel(List<String> level) {
        this.level = level;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherIcon() {
        return teacherIcon;
    }

    public void setTeacherIcon(String teacherIcon) {
        this.teacherIcon = teacherIcon;
    }

    public long getStartTime() {
        return ObjectUtil.getLongValue(startTime);
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getStartTimeTxt() {
        return startTimeTxt;
    }

    public void setStartTimeTxt(String startTimeTxt) {
        this.startTimeTxt = startTimeTxt;
    }

    public String getClassCourseId() {
        return classCourseId;
    }

    public void setClassCourseId(String classCourseId) {
        this.classCourseId = classCourseId;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }

    public String getDiamondsMax() {
        return diamondsMax;
    }

    public void setDiamondsMax(String diamondsMax) {
        this.diamondsMax = diamondsMax;
    }

    public int getDiamondsTag() {
        return diamondsTag;
    }

    public void setDiamondsTag(int diamondsTag) {
        this.diamondsTag = diamondsTag;
    }

    public CourseProgressBean getCourseProgress() {
        return courseProgress;
    }

    public void setCourseProgress(CourseProgressBean courseProgress) {
        this.courseProgress = courseProgress;
    }

    public int getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(int isPreview) {
        this.isPreview = isPreview;
    }

    public int getIsLearned() {
        return isLearned;
    }

    public void setIsLearned(int isLearned) {
        this.isLearned = isLearned;
    }

    public int getIsExercises() {
        return isExercises;
    }

    public void setIsExercises(int isExercises) {
        this.isExercises = isExercises;
    }

    public String getIsTopic() {
        return isTopic;
    }

    public void setIsTopic(String isTopic) {
        this.isTopic = isTopic;
    }

    public List<DiamondsRankBean> getDiamondsRank() {
        return diamondsRank;
    }

    public void setDiamondsRank(List<DiamondsRankBean> diamondsRank) {
        this.diamondsRank = diamondsRank;
    }

    public int getCostHour() {
        return ObjectUtil.getIntValue(costHour);
    }

    public void setCostHour(String costHour) {
        this.costHour = costHour;
    }

    public AnswerRankBean getAnswerRank() {
        return answerRank;
    }

    public void setAnswerRank(AnswerRankBean answerRank) {
        this.answerRank = answerRank;
    }

    public int getSeatType() {
        return seatType;
    }

    public void setSeatType(int seatType) {
        this.seatType = seatType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class CourseProgressBean {
        /**
         * courrentLevel : JA
         * nextLevel : JB
         * progress : 0.87
         * status : 1
         */

        private String courrentLevel;
        private String nextLevel;
        private double progress;
        private int status;

        public String getCourrentLevel() {
            return courrentLevel;
        }

        public void setCourrentLevel(String courrentLevel) {
            this.courrentLevel = courrentLevel;
        }

        public String getNextLevel() {
            return nextLevel;
        }

        public void setNextLevel(String nextLevel) {
            this.nextLevel = nextLevel;
        }

        public double getProgress() {
            return progress;
        }

        public void setProgress(double progress) {
            this.progress = progress;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class DiamondsRankBean {
        /**
         * gid : 0
         * gname : null
         * student : [{"studentId":"1952","my":2,"studentName":"adf","studentAvatar":"https://uutest2.uuabc.com/Fja9IsYTor5yaR5smqW-QRsXX2hi","diamond":"0"}]
         * diamonds : 0
         */

        private String gid;
        private String gname;
        private int diamonds;
        private List<StudentBean> student;

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public String getGname() {
            return gname;
        }

        public void setGname(String gname) {
            this.gname = gname;
        }

        public int getDiamonds() {
            return diamonds;
        }

        public void setDiamonds(int diamonds) {
            this.diamonds = diamonds;
        }

        public List<StudentBean> getStudent() {
            return student;
        }

        public void setStudent(List<StudentBean> student) {
            this.student = student;
        }

        public static class StudentBean {
            /**
             * studentId : 1952
             * my : 2
             * studentName : adf
             * studentAvatar : https://uutest2.uuabc.com/Fja9IsYTor5yaR5smqW-QRsXX2hi
             * diamond : 0
             */

            private String studentId;
            private int my;
            private String studentName;
            private String studentAvatar;
            private String diamond;

            public String getStudentId() {
                return studentId;
            }

            public void setStudentId(String studentId) {
                this.studentId = studentId;
            }

            public int getMy() {
                return my;
            }

            public void setMy(int my) {
                this.my = my;
            }

            public String getStudentName() {
                return studentName;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }

            public String getStudentAvatar() {
                return studentAvatar;
            }

            public void setStudentAvatar(String studentAvatar) {
                this.studentAvatar = studentAvatar;
            }

            public String getDiamond() {
                return diamond;
            }

            public void setDiamond(String diamond) {
                this.diamond = diamond;
            }
        }
    }

    public static class AnswerRankBean {
        private List<StudentBean> first;
        private List<StudentBean> second;

        public List<StudentBean> getFirst() {
            return first;
        }

        public void setFirst(List<StudentBean> first) {
            this.first = first;
        }

        public List<StudentBean> getSecond() {
            return second;
        }

        public void setSecond(List<StudentBean> second) {
            this.second = second;
        }

        public static class StudentBean {
            private Object studentId;
            private String studentName;
            private String studentAvatar;
            private String trueNum;
            private String rank;
            private String time;

            public String getStudentId() {
                return String.valueOf(ObjectUtil.getIntValue(studentId));
            }

            public void setStudentId(String studentId) {
                this.studentId = studentId;
            }

            public String getStudentName() {
                return studentName;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }

            public String getStudentAvatar() {
                return studentAvatar;
            }

            public void setStudentAvatar(String studentAvatar) {
                this.studentAvatar = studentAvatar;
            }

            public String getTrueNum() {
                return trueNum;
            }

            public void setTrueNum(String trueNum) {
                this.trueNum = trueNum;
            }

            public String getRank() {
                return rank;
            }

            public void setRank(String rank) {
                this.rank = rank;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
