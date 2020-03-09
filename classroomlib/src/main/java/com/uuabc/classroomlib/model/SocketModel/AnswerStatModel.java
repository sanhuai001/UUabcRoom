package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class AnswerStatModel {
    /**
     * tid : 470
     * opt_a : 0
     * opt_b : 0
     * opt_c : 1
     * opt_d : 0
     * opt : 0
     * correct : A
     * count : 1
     * people : 1
     */
    private String tid;
    private Object opt_a;
    private Object opt_b;
    private Object opt_c;
    private Object opt_d;
    private Object opt_e;
    private Object opt;
    private String correct;
    private Object count;
    private Object people;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public int getOpt_a() {
        return ObjectUtil.getIntValueTwo(opt_a);
    }

    public void setOpt_a(Object opt_a) {
        this.opt_a = opt_a;
    }

    public int getOpt_b() {
        return ObjectUtil.getIntValueTwo(opt_b);
    }

    public void setOpt_b(Object opt_b) {
        this.opt_b = opt_b;
    }

    public int getOpt_c() {
        return ObjectUtil.getIntValueTwo(opt_c);
    }

    public void setOpt_c(Object opt_c) {
        this.opt_c = opt_c;
    }

    public int getOpt_d() {
        return ObjectUtil.getIntValueTwo(opt_d);
    }

    public void setOpt_d(Object opt_d) {
        this.opt_d = opt_d;
    }

    public int getOpt_e() {
        return ObjectUtil.getIntValueTwo(opt_e);
    }

    public void setOpt_e(Object opt_e) {
        this.opt_e = opt_e;
    }

    public int getOpt() {
        return ObjectUtil.getIntValueTwo(opt);
    }

    public void setOpt(int opt) {
        this.opt = opt;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }

    public int getCount() {
        return ObjectUtil.getIntValue(count);
    }

    public void setCount(Object count) {
        this.count = count;
    }

    public int getPeople() {
        return ObjectUtil.getIntValue(people);
    }

    public void setPeople(Object people) {
        this.people = people;
    }
}
