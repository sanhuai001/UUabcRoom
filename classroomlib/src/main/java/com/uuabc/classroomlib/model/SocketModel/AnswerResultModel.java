package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class AnswerResultModel {
    /**
     * id : 291
     * name : Seven
     * type : 1
     * photo : https://uutest2.uuabc.com/student_icon_1490353049901
     * tid : 470
     * time : 359.2
     * option : C
     * correct : A
     * result : false
     * diamond : 0
     * ub : 0
     * rank : 0
     * tasked : false
     * index : 1
     * people : 1
     */

    private int id;
    private String name;
    private int type;
    private String photo;
    private String tid;
    private double time;
    private String option;
    private String correct;
    private boolean result;
    private int diamond;
    private int ub;
    private int rank;
    private boolean tasked;
    private int index;
    private int people;
    private boolean finished;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getTime() {
        return ObjectUtil.getIntValue(time);
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public int getUb() {
        return ub;
    }

    public void setUb(int ub) {
        this.ub = ub;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean isTasked() {
        return tasked;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setTasked(boolean tasked) {
        this.tasked = tasked;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }
}
