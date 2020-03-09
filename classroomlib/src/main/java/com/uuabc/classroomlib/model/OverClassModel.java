package com.uuabc.classroomlib.model;

import java.util.List;

public class OverClassModel {

    /**
     * id : 0NOgk1vGknVMeDfAAAPl
     * user : {"token":2000568,"id":568,"type":2,"name":"","newtype":null,"photo":"/Public/live/image/photo_tea.png","animate":false}
     * value : {"muteall":false,"drawall":true,"animate":false,"teamd":true,"user":[{"id":48577,"name":"liusi","photo":"","dia":4,"ub":274,"muted":false,"drawing":true,"team":1,"stage":false,"point":"451,211","teamname":"UU","animate":false},{"id":48575,"name":"liusan","photo":"","dia":18,"ub":420,"muted":false,"drawing":true,"team":1,"stage":false,"point":"326,301","teamname":"UU","animate":false},{"id":48570,"name":"liuyi","photo":"https://uutest2.uuabc.com/FqXkCdvkpkV0DgWw68LH2UbZivRg","dia":0,"ub":2362,"muted":false,"drawing":true,"team":2,"stage":true,"point":"239,82","teamname":"CiCi","animate":false},{"id":48573,"name":"liuer","photo":"","dia":0,"ub":156,"muted":false,"drawing":true,"team":2,"stage":false,"point":"279,260","teamname":"CiCi","animate":false}],"teamdia1":22,"teamdia2":0,"team1":"UU","team2":"CiCi"}
     * time : 1529493832251
     */

    private String id;
    private UserModel user;
    private ValueModel value;
    private long time;

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

    public ValueModel getValue() {
        return value;
    }

    public void setValue(ValueModel value) {
        this.value = value;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class UserModel {
        /**
         * token : 2000568
         * id : 568
         * type : 2
         * name :
         * newtype : null
         * photo : /Public/live/image/photo_tea.png
         * animate : false
         */

        private int token;
        private int id;
        private int type;
        private String name;
        private Object newtype;
        private String photo;
        private boolean animate;

        public int getToken() {
            return token;
        }

        public void setToken(int token) {
            this.token = token;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getNewtype() {
            return newtype;
        }

        public void setNewtype(Object newtype) {
            this.newtype = newtype;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public boolean isAnimate() {
            return animate;
        }

        public void setAnimate(boolean animate) {
            this.animate = animate;
        }
    }

    public static class ValueModel {
        /**
         * muteall : false
         * drawall : true
         * animate : false
         * teamd : true
         * user : [{"id":48577,"name":"liusi","photo":"","dia":4,"ub":274,"muted":false,"drawing":true,"team":1,"stage":false,"point":"451,211","teamname":"UU","animate":false},{"id":48575,"name":"liusan","photo":"","dia":18,"ub":420,"muted":false,"drawing":true,"team":1,"stage":false,"point":"326,301","teamname":"UU","animate":false},{"id":48570,"name":"liuyi","photo":"https://uutest2.uuabc.com/FqXkCdvkpkV0DgWw68LH2UbZivRg","dia":0,"ub":2362,"muted":false,"drawing":true,"team":2,"stage":true,"point":"239,82","teamname":"CiCi","animate":false},{"id":48573,"name":"liuer","photo":"","dia":0,"ub":156,"muted":false,"drawing":true,"team":2,"stage":false,"point":"279,260","teamname":"CiCi","animate":false}]
         * teamdia1 : 22
         * teamdia2 : 0
         * team1 : UU
         * team2 : CiCi
         */

        private boolean muteall;
        private boolean drawall;
        private boolean animate;
        private boolean teamd;
        private int teamdia1;
        private int teamdia2;
        private String team1;
        private String team2;
        private List<User> user;

        public boolean isMuteall() {
            return muteall;
        }

        public void setMuteall(boolean muteall) {
            this.muteall = muteall;
        }

        public boolean isDrawall() {
            return drawall;
        }

        public void setDrawall(boolean drawall) {
            this.drawall = drawall;
        }

        public boolean isAnimate() {
            return animate;
        }

        public void setAnimate(boolean animate) {
            this.animate = animate;
        }

        public boolean isTeamd() {
            return teamd;
        }

        public void setTeamd(boolean teamd) {
            this.teamd = teamd;
        }

        public int getTeamdia1() {
            return teamdia1;
        }

        public void setTeamdia1(int teamdia1) {
            this.teamdia1 = teamdia1;
        }

        public int getTeamdia2() {
            return teamdia2;
        }

        public void setTeamdia2(int teamdia2) {
            this.teamdia2 = teamdia2;
        }

        public String getTeam1() {
            return team1;
        }

        public void setTeam1(String team1) {
            this.team1 = team1;
        }

        public String getTeam2() {
            return team2;
        }

        public void setTeam2(String team2) {
            this.team2 = team2;
        }

        public List<User> getUser() {
            return user;
        }

        public void setUser(List<User> user) {
            this.user = user;
        }

        public static class User {
            /**
             * id : 48577
             * name : liusi
             * photo :
             * dia : 4
             * ub : 274
             * muted : false
             * drawing : true
             * team : 1
             * stage : false
             * point : 451,211
             * teamname : UU
             * animate : false
             */

            private int id;
            private String name;
            private String photo;
            private int dia;
            private int ub;
            private boolean muted;
            private boolean drawing;
            private int team;
            private boolean stage;
            private String point;
            private String teamname;
            private boolean animate;
            private boolean isMe;

            public boolean isMe() {
                return isMe;
            }

            public void setMe(boolean me) {
                isMe = me;
            }

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

            public String getPhoto() {
                return photo;
            }

            public void setPhoto(String photo) {
                this.photo = photo;
            }

            public int getDia() {
                return dia;
            }

            public void setDia(int dia) {
                this.dia = dia;
            }

            public int getUb() {
                return ub;
            }

            public void setUb(int ub) {
                this.ub = ub;
            }

            public boolean isMuted() {
                return muted;
            }

            public void setMuted(boolean muted) {
                this.muted = muted;
            }

            public boolean isDrawing() {
                return drawing;
            }

            public void setDrawing(boolean drawing) {
                this.drawing = drawing;
            }

            public int getTeam() {
                return team;
            }

            public void setTeam(int team) {
                this.team = team;
            }

            public boolean isStage() {
                return stage;
            }

            public void setStage(boolean stage) {
                this.stage = stage;
            }

            public String getPoint() {
                return point;
            }

            public void setPoint(String point) {
                this.point = point;
            }

            public String getTeamname() {
                return teamname;
            }

            public void setTeamname(String teamname) {
                this.teamname = teamname;
            }

            public boolean isAnimate() {
                return animate;
            }

            public void setAnimate(boolean animate) {
                this.animate = animate;
            }
        }
    }
}
