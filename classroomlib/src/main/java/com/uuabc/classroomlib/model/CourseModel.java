package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class CourseModel {

    /**
     * classId : 45
     * skuId : 670845
     * classType : 4
     * startTime : 1529975100
     * seatType : 1
     * startTimeTxt : 2018/06/26 周二 09:05
     * status : 8
     * teacherId : 670
     * classLevel :
     * roomId : 21
     * progress : {"total":13,"index":1}
     * audit : {"preview":1,"review":1,"student_feedback_status":1,"teacher_feedback_status":1,"homework_status":1}
     * teacherName : Egret_1
     * teacherAvatar :
     * courseWareId : 1331
     * courseWareImg : https://uutest2.uuabc.com/courseware_img_1526284219292
     * courseWareName : 一对四课件KB_1_4.2
     * courseTypeName : KB上
     * isPreview : 0
     * isLearned : 1
     * isExercises : 0
     */
    private int classId;
    private String skuId;
    private int classType;
    private int startTime;
    private int seatType;
    private String startTimeTxt;
    private int status;
    private Object teacherId;
    private Object classLevel;
    private Object roomId;
    private ProgressBean progress;
    private AuditBean audit;
    private String teacherName;
    private String teacherAvatar;
    private int courseWareId;
    private String courseWareImg;
    private String courseWareName;
    private String courseTypeName;
    private int isPreview;
    private int isLearned;
    private int isExercises;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public int getClassType() {
        return classType;
    }

    public void setClassType(int classType) {
        this.classType = classType;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getSeatType() {
        return seatType;
    }

    public void setSeatType(int seatType) {
        this.seatType = seatType;
    }

    public String getStartTimeTxt() {
        return startTimeTxt;
    }

    public void setStartTimeTxt(String startTimeTxt) {
        this.startTimeTxt = startTimeTxt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTeacherId() {
        return ObjectUtil.getIntValue(teacherId);
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public List<String> getClassLevel() {
        if (classLevel == null || classLevel instanceof String) return null;
        if (classLevel instanceof List) {
            return (List<String>) classLevel;
        }
        return null;
    }

    public void setClassLevel(List<String> classLevel) {
        this.classLevel = classLevel;
    }

    public int getRoomId() {
        return ObjectUtil.getIntValue(roomId);
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public ProgressBean getProgress() {
        return progress;
    }

    public void setProgress(ProgressBean progress) {
        this.progress = progress;
    }

    public AuditBean getAudit() {
        return audit;
    }

    public void setAudit(AuditBean audit) {
        this.audit = audit;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherAvatar() {
        return teacherAvatar;
    }

    public void setTeacherAvatar(String teacherAvatar) {
        this.teacherAvatar = teacherAvatar;
    }

    public int getCourseWareId() {
        return courseWareId;
    }

    public void setCourseWareId(int courseWareId) {
        this.courseWareId = courseWareId;
    }

    public String getCourseWareImg() {
        return courseWareImg;
    }

    public void setCourseWareImg(String courseWareImg) {
        this.courseWareImg = courseWareImg;
    }

    public String getCourseWareName() {
        return courseWareName;
    }

    public void setCourseWareName(String courseWareName) {
        this.courseWareName = courseWareName;
    }

    public String getCourseTypeName() {
        return courseTypeName;
    }

    public void setCourseTypeName(String courseTypeName) {
        this.courseTypeName = courseTypeName;
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

    public static class ProgressBean {
        /**
         * total : 13
         * index : 1
         */

        private int total;
        private int index;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }

    public static class AuditBean {
        /**
         * preview : 1
         * review : 1
         * student_feedback_status : 1
         * teacher_feedback_status : 1
         * homework_status : 1
         */

        private int preview;
        private int review;
        private int student_feedback_status;
        private int teacher_feedback_status;
        private int homework_status;

        public int getPreview() {
            return preview;
        }

        public void setPreview(int preview) {
            this.preview = preview;
        }

        public int getReview() {
            return review;
        }

        public void setReview(int review) {
            this.review = review;
        }

        public int getStudent_feedback_status() {
            return student_feedback_status;
        }

        public void setStudent_feedback_status(int student_feedback_status) {
            this.student_feedback_status = student_feedback_status;
        }

        public int getTeacher_feedback_status() {
            return teacher_feedback_status;
        }

        public void setTeacher_feedback_status(int teacher_feedback_status) {
            this.teacher_feedback_status = teacher_feedback_status;
        }

        public int getHomework_status() {
            return homework_status;
        }

        public void setHomework_status(int homework_status) {
            this.homework_status = homework_status;
        }
    }
}
