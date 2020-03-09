package com.uuabc.classroomlib.model.SocketModel;

public class CourseActionModel {
    /**
     * time : 1532004245619
     * id : MFcgtKuByUIqWUWCAABu
     * message : {"pageIndex":7,"cmd":"505","type":"4","targetIndex":0,"x":341,"y":619}
     * page : 7
     * user : {"id":886,"token":2000886,"name":"bbcat","animate":true,"type":2,"photo":"https://courseware.uuabc.com/teacher_icon_1531879946583"}
     */
    private long time;
    private String id;
    private String message;
    private int page;
    private UserBean user;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * id : 886
         * token : 2000886
         * name : bbcat
         * animate : true
         * type : 2
         * photo : https://courseware.uuabc.com/teacher_icon_1531879946583
         */

        private int id;
        private int token;
        private String name;
        private boolean animate;
        private int type;
        private String photo;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getToken() {
            return token;
        }

        public void setToken(int token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isAnimate() {
            return animate;
        }

        public void setAnimate(boolean animate) {
            this.animate = animate;
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
    }
}
