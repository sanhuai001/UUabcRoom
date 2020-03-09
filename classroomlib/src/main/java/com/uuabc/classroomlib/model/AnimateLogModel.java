package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class AnimateLogModel {
    private Object pageIndex;
    private String animateLog;

    public int getPageIndex() {
        return ObjectUtil.getIntValue(pageIndex);
    }

    public void setPageIndex(Object pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getAnimateLog() {
        return animateLog;
    }

    public void setAnimateLog(String animateLog) {
        this.animateLog = animateLog;
    }
}
