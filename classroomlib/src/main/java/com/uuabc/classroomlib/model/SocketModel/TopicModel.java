package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class TopicModel {
    /**
     * time : 1533772103804
     * action : start
     * value : {"topic_content":"I have ______(12) _______.","topic_img":"https://uutest2.uuabc.com/topic_dry_1500951506606","topic_file_type":"image/png","tasked":false,"topic_content_type":"3","topic_type":"1","topic_type_id":"114","a":"twelve; mangoes","b":"twenty; mangos","c":"twelve; mangos","finished":false,"id":"1575","index":2,"courseware_status":"0","is_record":true}
     * user : {"shield":false,"id":"52","name":"god2","type":2,"photo":"https://uutest2.uuabc.com/teacher_icon_1530695869731"}
     */
    private Object time;
    private String action;
    private Object value;
    private UserBean user;

    public long getTime() {
        return ObjectUtil.getLongValue(time);
    }

    public void setTime(Object time) {
        this.time = time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        /**
         * shield : false
         * id : 52
         * name : god2
         * type : 2
         * photo : https://uutest2.uuabc.com/teacher_icon_1530695869731
         */

        private boolean shield;
        private String id;
        private String name;
        private int type;
        private String photo;

        public boolean isShield() {
            return shield;
        }

        public void setShield(boolean shield) {
            this.shield = shield;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
