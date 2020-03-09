package com.uuabc.classroomlib.model;

import java.util.List;

/**
 * Created by user on 2018/4/27.
 */

public class GradeListModel {

    /**
     * stuInfo : {"levelId":"0","levelName":"","levelStatus":"1"}
     * grade : [{"gradeId":"1","gradeName":"KA","desc":null,"knowledge":null,"words":null,"my":2},{"gradeId":"2","gradeName":"KB","desc":null,"knowledge":null,"words":null,"my":2},{"gradeId":"3","gradeName":"JA","desc":null,"knowledge":null,"words":null,"my":2},{"gradeId":"4","gradeName":"JB","desc":null,"knowledge":null,"words":null,"my":2},{"gradeId":"5","gradeName":"PC","desc":null,"knowledge":null,"words":null,"my":2},{"gradeId":"6","gradeName":"PD","desc":null,"knowledge":null,"words":null,"my":2},{"gradeId":"7","gradeName":"PE","desc":null,"knowledge":null,"words":null,"my":2},{"gradeId":"8","gradeName":"PF","desc":null,"knowledge":null,"words":null,"my":2}]
     */

    private StuInfoModel stuInfo;
    private List<GradeModel> grade;

    public StuInfoModel getStuInfo() {
        return stuInfo;
    }

    public void setStuInfo(StuInfoModel stuInfo) {
        this.stuInfo = stuInfo;
    }

    public List<GradeModel> getGrade() {
        return grade;
    }

    public void setGrade(List<GradeModel> grade) {
        this.grade = grade;
    }

    public static class StuInfoModel {
        /**
         * levelId : 0
         * levelName :
         * levelStatus : 1
         */

        private String levelId;
        private String levelName;
        private String levelStatus;

        public String getLevelId() {
            return levelId;
        }

        public void setLevelId(String levelId) {
            this.levelId = levelId;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getLevelStatus() {
            return levelStatus;
        }

        public void setLevelStatus(String levelStatus) {
            this.levelStatus = levelStatus;
        }
    }

    public static class GradeModel {
        /**
         * gradeId : 1
         * gradeName : KA
         * desc : null
         * knowledge : null
         * words : null
         * my : 2
         */

        private String gradeId;
        private String gradeName;
        private String desc;
        private String knowledge;
        private String words;
        private int my;

        public String getGradeId() {
            return gradeId;
        }

        public void setGradeId(String gradeId) {
            this.gradeId = gradeId;
        }

        public String getGradeName() {
            return gradeName;
        }

        public void setGradeName(String gradeName) {
            this.gradeName = gradeName;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getKnowledge() {
            return knowledge;
        }

        public void setKnowledge(String knowledge) {
            this.knowledge = knowledge;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public int getMy() {
            return my;
        }

        public void setMy(int my) {
            this.my = my;
        }
    }
}
