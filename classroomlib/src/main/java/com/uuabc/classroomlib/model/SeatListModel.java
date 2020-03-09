package com.uuabc.classroomlib.model;

import com.uuabc.classroomlib.model.SocketModel.UserModel;

import java.util.List;

public class SeatListModel {

    private long time;
    private int flower;
    private String id;
    private User user;
    private boolean banned;
    private List<ListSeat> list;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getFlower() {
        return flower;
    }

    public void setFlower(int flower) {
        this.flower = flower;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public List<ListSeat> getList() {
        return list;
    }

    public void setList(List<ListSeat> list) {
        this.list = list;
    }

    public static class User {
        /**
         * id : 1952
         * token : 10001952
         * name : 一对一
         * type : 1
         * newtype : 0
         */

        private int id;
        private String token;
        private String name;
        private int type;
        private int newtype;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
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

        public int getNewtype() {
            return newtype;
        }

        public void setNewtype(int newtype) {
            this.newtype = newtype;
        }
    }

    public static class ListSeat {
        /**
         * device : ["Robot"]
         * time : 1533593894877
         * id : A21C0F6171A04DD78BFB6DBEF958A24A
         * user : {"id":"r197","role":"robot","name":"Juice","type":1,"photo":"/Public/live/photo/user_img_11.png"}
         */

        private long time;
        private String id;
        private UserModel user;
        private List<String> device;

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

        public UserModel getUser() {
            return user;
        }

        public void setUser(UserModel user) {
            this.user = user;
        }

        public List<String> getDevice() {
            return device;
        }

        public void setDevice(List<String> device) {
            this.device = device;
        }
    }
}
