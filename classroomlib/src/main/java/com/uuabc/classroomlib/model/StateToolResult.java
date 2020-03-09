package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class StateToolResult {
    private Object pageSetting;
    private Object toolSetting;
    private List<Object> animationSetting;

    public int getPageSetting() {
        return ObjectUtil.getIntValue(pageSetting);
    }

    public void setPageSetting(Object pageSetting) {
        this.pageSetting = pageSetting;
    }

    public Object getToolSetting() {
        return toolSetting;
    }

    public void setToolSetting(Object toolSetting) {
        this.toolSetting = toolSetting;
    }

    public List<Object> getAnimationSetting() {
        return animationSetting;
    }

    public void setAnimationSetting(List<Object> animationSetting) {
        this.animationSetting = animationSetting;
    }
}
