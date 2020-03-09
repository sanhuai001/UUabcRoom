package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.model.SocketModel.AnswerResultModel;
import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class TopThreeModel {

    /**
     * time : 46.021
     * rank : [{"ub":1,"diamond":3,"time":3.1,"id":1952,"name":"一对一","type":1},{"ub":1,"diamond":2,"time":41.2,"id":"r92","name":"Winni","type":1,"photo":"/Public/live/photo/user_img_153.png"},{"ub":1,"diamond":1,"time":42.2,"id":"r69","name":"Olivia","type":1,"photo":"/Public/live/photo/user_img_109.png"}]
     * item : {"time":3.1,"diamond":3,"rank":1,"tasked":true,"option":"A","ub":1,"finished":true,"id":1952,"tid":"2236","name":"一对一","index":1,"type":1,"correct":"A","result":true,"people":184}
     */

    private double time;
    private AnswerResultModel item;
    private List<RankBean> rank;

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public AnswerResultModel getItem() {
        return item;
    }

    public void setItem(AnswerResultModel item) {
        this.item = item;
    }

    public List<RankBean> getRank() {
        return rank;
    }

    public void setRank(List<RankBean> rank) {
        this.rank = rank;
    }


    public static class RankBean {
        /**
         * ub : 1
         * diamond : 3
         * time : 3.1
         * id : 1952
         * name : 一对一
         * type : 1
         * photo : /Public/live/photo/user_img_153.png
         */

        private Object ub;
        private Object diamond;
        private double time;
        private Object id;
        private String name;
        private int type;
        private String photo;
        private int cnum;

        public int getUb() {
            return ObjectUtil.getIntValue(ub);
        }

        public void setUb(Object ub) {
            this.ub = ub;
        }

        public int getDiamond() {
            return ObjectUtil.getIntValue(diamond);
        }

        public void setDiamond(Object diamond) {
            this.diamond = diamond;
        }

        public String getTime() {
            return String.valueOf(time);
        }

        public void setTime(double time) {
            this.time = time;
        }

        public int getId() {
            return ObjectUtil.getIntValue(id);
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getCnum() {
            return cnum;
        }

        public void setCnum(int cnum) {
            this.cnum = cnum;
        }
    }
}
