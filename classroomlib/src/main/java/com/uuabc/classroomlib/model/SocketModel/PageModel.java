package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class PageModel {
    private Object total;
    private Object page;

    public int getTotal() {
        return ObjectUtil.getIntValue(total);
    }

    public void setTotal(Object total) {
        this.total = total;
    }

    public int getPage() {
        return ObjectUtil.getIntValue(page);
    }

    public void setPage(Object page) {
        this.page = page;
    }
}
