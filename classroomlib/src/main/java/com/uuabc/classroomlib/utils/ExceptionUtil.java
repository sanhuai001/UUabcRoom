package com.uuabc.classroomlib.utils;

import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.MicrospotModel;

public class ExceptionUtil {

    public static void sendException(String event, int classType, String message) {
        MicrospotModel model = new MicrospotModel();
        model.setEvent(event);
        model.setClassType(classType);
        model.setId(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        model.setUserName(SPUtils.getInstance().getString(RoomConstant.USER_NAME));
        model.setMessage(message);
        RoomApplication.getInstance().asyncUploadSocketLog(model);
//        CrashReport.postCatchedException(new CustomExceptionModel(JsonUtils.entityToJsonString(model)));
    }
}
