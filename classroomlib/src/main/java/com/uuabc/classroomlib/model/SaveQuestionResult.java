package com.uuabc.classroomlib.model;

import android.os.Parcel;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class SaveQuestionResult extends RCommonResult<SaveQuestionResult> {
    private Object correct;
    private Object balance;
    private Object gold_number;
    private Object dia;

    protected SaveQuestionResult(Parcel in) {
        super(in);
    }

    public boolean isCorrect() {
        return ObjectUtil.getBoolean(correct);
    }

    public void setCorrect(Object correct) {
        this.correct = correct;
    }

    public int getBalance() {
        return ObjectUtil.getIntValue(balance);
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getGold_number() {
        return ObjectUtil.getIntValue(gold_number);
    }

    public void setGold_number(String gold_number) {
        this.gold_number = gold_number;
    }

    public int getDia() {
        return ObjectUtil.getIntValue(dia);
    }

    public void setDia(String dia) {
        this.dia = dia;
    }
}
