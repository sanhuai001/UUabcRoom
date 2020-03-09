package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.utils.ObjectUtil;

/**
 * 共享事件的model
 * Created by bobi on 2018/4/19.
 */

public class ShareModel {
    private String id;
    private UserModel user;
    private MessageModel message;
    private String time;

    public static class MessageModel {
        private String action;
        private Object value;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Object getValue() {
            return value;
        }

        public int getIntegerValue() {
            return ObjectUtil.getIntValue(value);
        }

        public void setValue(Object value) {
            this.value = value;
        }
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

    public MessageModel getMessage() {
        return message;
    }

    public void setMessage(MessageModel message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
