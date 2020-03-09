package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class AnswerRankModel {

    /**
     * cid : 37
     * list : [{"cnum":"37","uid":"r66","rank":1,"uname":null},{"cnum":"37","uid":"r134","rank":2,"uname":"null，"},{"cnum":"37","uid":"r83","rank":3,"uname":null},{"cnum":"37","uid":"r14","rank":4,"uname":null},{"cnum":"37","uid":"r123","rank":4,"uname":null},{"cnum":"37","uid":"r90","rank":5,"uname":null},{"cnum":"37","uid":"r118","rank":6,"uname":null},{"cnum":"37","uid":"r186","rank":7,"uname":null},{"cnum":"37","uid":"r73","rank":8,"uname":null},{"cnum":"37","uid":"r84","rank":9,"uname":null},{"cnum":"37","uid":"r11","rank":10,"uname":null},{"cnum":"37","uid":"r130","rank":11,"uname":null},{"cnum":"37","uid":"r147","rank":12,"uname":null},{"cnum":"37","uid":"r79","rank":13,"uname":null},{"cnum":"37","uid":"r74","rank":14,"uname":null},{"cnum":"37","uid":"r77","rank":15,"uname":null},{"cnum":"37","uid":"291","rank":16,"uname":null},{"cnum":"37","uid":"r141","rank":17,"uname":null},{"cnum":"37","uid":"r36","rank":18,"uname":null},{"cnum":"37","uid":"r28","rank":19,"uname":null},{"cnum":"37","uid":"r187","rank":20,"uname":null},{"cnum":"37","uid":"r56","rank":21,"uname":null},{"cnum":"37","uid":"r109","rank":22,"uname":null},{"cnum":"37","uid":"r176","rank":23,"uname":null},{"cnum":"37","uid":"r13","rank":24,"uname":null},{"cnum":"37","uid":"r138","rank":25,"uname":null},{"cnum":"37","uid":"r19","rank":26,"uname":null},{"cnum":"37","uid":"r37","rank":27,"uname":null},{"cnum":"37","uid":"r111","rank":28,"uname":null},{"cnum":"37","uid":"r69","rank":29,"uname":null},{"cnum":"37","uid":"r4","rank":30,"uname":null},{"cnum":"37","uid":"r25","rank":31,"uname":null},{"cnum":"37","uid":"r26","rank":32,"uname":null},{"cnum":"37","uid":"r93","rank":33,"uname":null},{"cnum":"37","uid":"r41","rank":34,"uname":null},{"cnum":"37","uid":"r64","rank":34,"uname":null},{"cnum":"37","uid":"r65","rank":34,"uname":null},{"cnum":"37","uid":"r82","rank":35,"uname":null},{"cnum":"37","uid":"r159","rank":36,"uname":null},{"cnum":"37","uid":"r55","rank":37,"uname":null},{"cnum":"37","uid":"r156","rank":38,"uname":null},{"cnum":"37","uid":"r89","rank":39,"uname":null},{"cnum":"37","uid":"r140","rank":40,"uname":null},{"cnum":"37","uid":"r177","rank":40,"uname":null},{"cnum":"37","uid":"r98","rank":41,"uname":null},{"cnum":"37","uid":"r116","rank":42,"uname":null},{"cnum":"37","uid":"r44","rank":43,"uname":null},{"cnum":"37","uid":"r172","rank":44,"uname":null},{"cnum":"37","uid":"r32","rank":45,"uname":null},{"cnum":"37","uid":"r91","rank":46,"uname":null}]
     */

    private String cid;
    private List<RankListModel> list;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public List<RankListModel> getList() {
        return list;
    }

    public void setList(List<RankListModel> list) {
        this.list = list;
    }

    public static class RankListModel {
        /**
         * cnum : 37
         * uid : r66
         * rank : 1
         * uname : null
         */

        private String cnum;
        private String uid;
        private int rank;
        private String uname;
        private String photo;
        private int dia;

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public int getDia() {
            return dia;
        }

        public void setDia(int dia) {
            this.dia = dia;
        }

        public String getCnum() {
            return cnum;
        }

        public void setCnum(String cnum) {
            this.cnum = cnum;
        }

        public int getUid() {
            return ObjectUtil.getIntValue(uid);
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }
    }
}
