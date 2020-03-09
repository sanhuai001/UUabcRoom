package com.uuabc.classroomlib.utils;

import com.blankj.utilcode.util.SPUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.CustomExceptionModel;
import com.uuabc.classroomlib.model.MicrospotModel;

public class ExceptionUtil {

    public static void sendException(String event, int classType, String message) {
        sendException(event, classType, message, false);
    }

    public static void sendException(String event, int classType, String message, boolean isOldClssRoom) {
        MicrospotModel model = new MicrospotModel();
        model.setEvent(event);
        model.setClassType(classType);
        model.setId(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        model.setUserName(SPUtils.getInstance().getString(RoomConstant.USER_NAME));
        model.setMessage(message);
        model.setOldRoom(isOldClssRoom);

        CrashReport.postCatchedException(new CustomExceptionModel(JsonUtils.entityToJsonString(model)));
    }
}
