package com.uuabc.classroomlib.utils;

import com.blankj.utilcode.util.DeviceUtils;
import com.uuabc.classroomlib.RoomApplication;

public class UserAgentUtil {

    public static String getUserAgent() {
        return "utv/" + RoomApplication.getInstance().getAppVersionName() +
                "(android " + DeviceUtils.getSDKVersionName() + "; " +
                DeviceUtils.getModel() + "; " +
                (RoomApplication.getInstance().isTable ? "tablet; " : "phone;") +
                RoomApplication.getInstance().getScreenWidth() + "*" + RoomApplication.getInstance().getScreenHeight() + ")";
    }

}
