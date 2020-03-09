package com.uuabc.classroomlib.model;

import java.util.List;

/**
 * Created by user on 2018/5/12.
 */

public class CenterModel {


    /**
     * lists : [{"classId":"36916","classType":2,"startTime":1523005800,"status":1},{"classId":"192","classType":"3","startTime":1523018700,"status":1},{"classId":"36920","classType":2,"startTime":1523092200,"status":1},{"classId":"193","classType":"3","startTime":1523513580,"status":1},{"classId":"36926","classType":2,"startTime":1523667600,"status":1},{"classId":"36954","classType":2,"startTime":1524204900,"status":1},{"classId":"184","classType":"3","startTime":1524304200,"status":1},{"classId":"37034","classType":2,"startTime":1524883500,"status":1},{"classId":"37076","classType":2,"startTime":1525243800,"status":1},{"classId":"37105","classType":2,"startTime":1525315500,"status":1},{"classId":"37144","classType":"1","startTime":1525480200,"status":1},{"classId":"37148","classType":"1","startTime":1525570800,"status":1},{"classId":"37151","classType":"1","startTime":1525572900,"status":1},{"classId":"37153","classType":"1","startTime":1525575000,"status":1},{"classId":"37154","classType":"1","startTime":1525577100,"status":1},{"classId":"37155","classType":"1","startTime":1525579200,"status":1},{"classId":"37203","classType":2,"startTime":1525770600,"status":1},{"classId":"37263","classType":"1","startTime":1525930800,"status":1},{"classId":"37265","classType":"1","startTime":1526000700,"status":1},{"classId":"236","classType":"3","startTime":1526020200,"status":1},{"classId":"231","classType":"3","startTime":1526024580,"status":1},{"classId":"37286","classType":2,"startTime":1526027700,"status":1},{"classId":"37261","classType":"1","startTime":1526173500,"status":1}]
     * pageInfo : {"pageSize":100,"lastTime":1526115900}
     */

    private PageInfoModel pageInfo;
    private List<ClassModel> lists;

    public PageInfoModel getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoModel pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ClassModel> getLists() {
        return lists;
    }

    public void setLists(List<ClassModel> lists) {
        this.lists = lists;
    }

    public static class PageInfoModel {
        /**
         * pageSize : 100
         * lastTime : 1526115900
         */

        private int pageSize;
        private int lastTime;

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getLastTime() {
            return lastTime;
        }

        public void setLastTime(int lastTime) {
            this.lastTime = lastTime;
        }
    }

    public static class ClassModel {
        /**
         * classId : 36916
         * classType : 2
         * startTime : 1523005800
         * status : 1
         */

        private String classId;
        private int classType;
        private long startTime;
        private int status;

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        public int getClassType() {
            return classType;
        }

        public void setClassType(int classType) {
            this.classType = classType;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
