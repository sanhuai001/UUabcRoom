package com.uuabc.classroomlib.model;


import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class TopModel {

    /**
     * list : [{"timer":22.6,"ub":2,"cnum":2,"rank":1,"dia":6,"uid":"1952","uname":"一对一"},{"timer":9.6,"ub":"0","cnum":0,"rank":2,"dia":"0","uid":"r56","photo":"/Public/live/photo/user_img_91.png","uname":"Lindsay"},{"timer":15.6,"ub":"0","cnum":0,"rank":2,"dia":"0","uid":"r26","photo":"/Public/live/photo/user_img_49.png","uname":"Emily"},{"timer":17.6,"ub":"0","cnum":0,"rank":2,"dia":"0","uid":"r110","photo":"/Public/live/photo/user_img_198.png","uname":"Bard"},{"timer":17.6,"ub":"0","cnum":0,"rank":2,"dia":"0","uid":"r164","photo":"/Public/live/photo/user_img_150.png","uname":"Marlon"}]
     * ub : 2
     * dia : 6
     */

    private int ub;
    private int dia;
    private List<RankBean> list;

    public int getUb() {
        return ub;
    }

    public void setUb(int ub) {
        this.ub = ub;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public List<RankBean> getList() {
        return list;
    }

    public void setList(List<RankBean> list) {
        this.list = list;
    }

    public static class RankBean {
        /**
         * timer : 22.6
         * ub : 2
         * cnum : 2
         * rank : 1
         * dia : 6
         * uid : 1952
         * uname : 一对一
         * photo : /Public/live/photo/user_img_91.png
         */

        private double timer;
        private Object ub;
        private Object cnum;
        private Object rank;
        private Object dia;
        private Object uid;
        private String uname;
        private String photo;

        public double getTimer() {
            return timer;
        }

        public void setTimer(double timer) {
            this.timer = timer;
        }

        public int getUb() {
            return ObjectUtil.getIntValue(ub);
        }

        public void setUb(Object ub) {
            this.ub = ub;
        }

        public int getCnum() {
            return ObjectUtil.getIntValue(cnum);
        }

        public void setCnum(Object cnum) {
            this.cnum = cnum;
        }

        public int getRank() {
            return ObjectUtil.getIntValue(rank);
        }

        public void setRank(Object rank) {
            this.rank = rank;
        }

        public int getDia() {
            return ObjectUtil.getIntValue(dia);
        }

        public void setDia(Object dia) {
            this.dia = dia;
        }

        public int getUid() {
            return ObjectUtil.getIntValue(uid);
        }

        public void setUid(Object uid) {
            this.uid = uid;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
