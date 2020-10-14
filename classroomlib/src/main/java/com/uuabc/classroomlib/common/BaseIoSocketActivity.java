package com.uuabc.classroomlib.common;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.classroom.BaseWifiListenerActivity;
import com.uuabc.classroomlib.model.Event.MessageEvent;
import com.uuabc.classroomlib.model.SUserModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.utils.JsonUtils;
import com.uuabc.classroomlib.utils.SocketIoUtils;
import com.uuabc.classroomlib.utils.UserAgentUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("Registered")
public class BaseIoSocketActivity extends BaseWifiListenerActivity {

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveIoSocketMsg(MessageEvent eventModel) {
        mMainHandler.post(() -> {
            LogUtils.i("receiveIoSocketMsg", eventModel.getEvent() + ":" + eventModel.getData());
            if (isDestroyed()) return;
            onIoSocketMsgReceived(eventModel.getEvent(), eventModel.getData(), eventModel.getData2());
        });
    }

    public void login(int classId, int classType, String userName) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        userMap.put("type", 1);
        userMap.put("token", "1000" + SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        userMap.put("newtype", classType == 4 ? 1 : 0);
        userMap.put("name", classType == RoomConstant.CLASS_TYPE_ONE_TO_FOUR ? "一对四"
                : (classType == RoomConstant.CLASS_TYPE_ONE_TO_ONE ? "一对一"
                : (classType == RoomConstant.CLASS_TYPE_LIVE ? userName : "包场")));
        userMap.put("photo", SPUtils.getInstance().getString(RoomConstant.USER_AVATAR));

        Map<String, Object> map = new HashMap<>();
        map.put("room", classId);
        map.put("user", userMap);
        map.put("ua", UserAgentUtil.getUserAgent());
        RoomApplication.getInstance().sendMessage(RoomConstant.LOGIN, SocketIoUtils.getJsonObject(map));
    }

    public void authenticate(int userType) {
        RoomApplication.getInstance().sendMessage(RoomConstant.AUTHENTICATE, String.valueOf(userType) + SPUtils.getInstance().getInt(RoomConstant.USER_ID));
    }

    public void join(int roomId, SUserModel userModel, int userType, boolean camera) {
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("token", String.valueOf(userType) + SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        infoMap.put("name", userModel != null ? userModel.getNickname() : "");
        infoMap.put("photo", userModel != null ? userModel.getAvatar() : "");
        infoMap.put("camera", camera);
        String uuid = userModel != null ? userModel.getExternal_id() : "0";
        infoMap.put("uuid", TextUtils.isEmpty(uuid) ? "0" : uuid);
        Map<String, Object> map = new HashMap<>();
        map.put("room_id", roomId);
        map.put("user_id", SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        map.put("role", userType);
        map.put("info", infoMap);
        RoomApplication.getInstance().sendMessage(RoomConstant.JOIN, SocketIoUtils.getJsonObject(map));
    }

    public void sendStudents(List<UserModel> users, int classType) {
        if (users != null) {
            Map<String, Object> map = new HashMap<>();
            Map<String, Object> infoMap = new HashMap<>();
            for (int i = 0; i < users.size(); i++) {
                users.get(i).setType(RoomConstant.STUDENT_TYPE);
                users.get(i).setToken("1" + users.get(i).getId());
                if (classType == RoomConstant.CLASS_TYPE_ONE_TO_ONE) {
                    users.get(i).setMuted(false);
                }
                infoMap.put(String.valueOf(users.get(i).getId()), JsonUtils.parseObject(users.get(i)));
            }
            map.put("event", RoomConstant.STUDENTS);
            map.put("data", infoMap);
            RoomApplication.getInstance().sendMessage(RoomConstant.SHARE, SocketIoUtils.getJsonObject(map));
        }
    }

    public void loginOut() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("list", UserAgentUtil.getUserAgent());
//        RoomApplication.getInstance().sendMessage(RoomConstant.LOGOUT, SocketIoUtils.getJsonObject(map));
    }

    public void onIoSocketMsgReceived(String event, String... data) {

    }
}
