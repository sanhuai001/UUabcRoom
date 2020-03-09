package com.uuabc.classroomlib.utils;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.uuabc.classroomlib.custom.ParameterizedGenericsType;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;


/**
 * socketIo中使用的工具类，待以后完善
 * Created by bobi on 2018/3/23.
 */

public class SocketIoUtils {

    /**
     * 生成请求用的json对象
     *s
     * @param map 传进来的map对象
     */
    public static JSONObject getJsonObject(Map map) {
        return new JSONObject(map);
    }

    public static <T> T parseData(Class<T> clazz, String map) throws JsonSyntaxException {
        Type jsonType = new ParameterizedGenericsType(clazz, new Class[]{clazz});
        return new Gson().fromJson(map, jsonType);
    }

    public static <T> T parseData(Class<T> clazz, Map map) throws JsonSyntaxException {
        Gson gson = new Gson();
        String json = gson.toJson(map);
        Type jsonType = new ParameterizedGenericsType(clazz, new Class[]{clazz});
        return new Gson().fromJson(json, jsonType);
    }
}
