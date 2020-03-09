package com.uuabc.classroomlib.model;

import java.util.List;

public class PubInfoModel {
    Object list;
    List<TopModel.RankBean> rank;

    public List<TopModel.RankBean> getRank() {
        return rank;
    }

    public void setRank(List<TopModel.RankBean> rank) {
        this.rank = rank;
    }
}
