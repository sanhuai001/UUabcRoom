package com.uuabc.classroomlib.model.SocketModel;

import android.text.TextUtils;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

public class FlowerModel {
    private int teamdia1;
    private int teamdia2;
    private List<UsersBean> users;

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

    public List<UsersBean> getUsers() {
        return users;
    }

    public void setUsers(List<UsersBean> users) {
        this.users = users;
    }

    public static class UsersBean {
        private String team;
        private Object total;
        private String id;
        private String value;

        public String getTeam() {
            return team;
        }

        public void setTeam(String team) {
            this.team = team;
        }

        public int getTotal() {
            return ObjectUtil.getIntValue(total);
        }

        public void setTotal(Object total) {
            this.total = total;
        }

        public String getId() {
            if (TextUtils.isEmpty(id)) {
                return "0";
            }
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
