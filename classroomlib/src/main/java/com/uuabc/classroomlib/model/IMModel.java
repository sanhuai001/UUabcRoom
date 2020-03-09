package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class IMModel {
    private Object tenantId;
    private Object configId;
    private Object to;
    private Object appKey;

    public String getTenantId() {
        return ObjectUtil.getString(tenantId);
    }

    public void setTenantId(Object tenantId) {
        this.tenantId = tenantId;
    }

    public String getConfigId() {
        return ObjectUtil.getString(configId);
    }

    public void setConfigId(Object configId) {
        this.configId = configId;
    }

    public String getTo() {
        return ObjectUtil.getString(to);
    }

    public void setTo(Object to) {
        this.to = to;
    }

    public String getAppKey() {
        return ObjectUtil.getString(appKey);
    }

    public void setAppKey(Object appKey) {
        this.appKey = appKey;
    }
}
