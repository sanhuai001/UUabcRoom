package com.uuabc.classroomlib.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;

import java.util.List;

/**
 * FastJSON工具类，可防止解析时异常
 */
public class JsonUtils {
    private static final String TAG = "JsonUtils";

    /**
     * 判断json格式是否正确
     *
     * @param jsonStr Json字符串
     * @return {@code true}: 正确的Json格式<br> {@code false}: 错误的Json格式
     */
    public static boolean isJsonCorrect(String jsonStr) {
        return jsonStr != null && !jsonStr.equals("[]") &&
                !jsonStr.equals("{}") &&
                !jsonStr.equals("") &&
                !jsonStr.equals("[null]") &&
                !jsonStr.equals("{null}") &&
                !jsonStr.equals("null");
    }

    /**
     * 获取有效的json
     *
     * @param jsonStr Json字符串
     * @return 有效的Json字符串
     */
    public static String getCorrectJson(String jsonStr) {
        return isJsonCorrect(jsonStr) ? jsonStr : "";
    }

    /**
     * Object 转 JSONObject
     *
     * @param obj Object
     * @return JSONObject
     */
    public static JSONObject parseObject(Object obj) {
        return parseObject(toJSONString(obj));
    }

    /**
     * json转JSONObject
     *
     * @param jsonStr json字符串
     * @return JSONObject
     */
    public static JSONObject parseObject(String jsonStr) {
        try {
            return JSON.parseObject(getCorrectJson(jsonStr));
        } catch (Exception e) {
            Log.e(TAG, "parseObject  catch \n" + e.getMessage());
        }
        return null;
    }

    /**
     * JSONObject转实体类
     *
     * @param jsonObject JSONObject
     * @param clazz      要转化的实体类型
     * @return 实体类
     */
    public static <T> T parseObject(JSONObject jsonObject, Class<T> clazz) {
        return parseObject(toJSONString(jsonObject), clazz);
    }

    /**
     * json转实体类
     *
     * @param jsonStr Json字符串
     * @param clazz   要转化的实体类型
     * @return 实体类
     */
    public static <T> T parseObject(String jsonStr, Class<T> clazz) {
        try {
            return JSON.parseObject(getCorrectJson(jsonStr), clazz);
        } catch (Exception e) {
            Log.e(TAG, "parseObject  catch \n" + e.getMessage());
        }
        return null;
    }

    /**
     * Object转Json字符串
     *
     * @param obj Object
     * @return Json字符串
     */
    public static String toJSONString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            Log.e(TAG, "toJSONString  catch \n" + e.getMessage());
        }
        return null;
    }

    /**
     * Json转JSONArray
     *
     * @param jsonStr Json字符串
     * @return JSONArray
     */
    public static JSONArray parseArray(String jsonStr) {
        try {
            return JSON.parseArray(getCorrectJson(jsonStr));
        } catch (Exception e) {
            Log.e(TAG, "parseArray  catch \n" + e.getMessage());
        }
        return null;
    }

    /**
     * Json字符串转List
     *
     * @param jsonStr Json字符串
     * @param clazz   要转化的Json类型
     * @return List
     */
    public static <T> List<T> parseArray(String jsonStr, Class<T> clazz) {
        try {
            return JSON.parseArray(getCorrectJson(jsonStr), clazz);
        } catch (Exception e) {
            Log.e(TAG, "parseArray  catch \n" + e.getMessage());
        }
        return null;
    }

    /**
     * 格式化，显示更好看
     *
     * @param object Object
     * @return 格式化的Json字符串
     */
    public static String format(Object object) {
        try {
            return JSON.toJSONString(object, true);
        } catch (Exception e) {
            Log.e(TAG, "format  catch \n" + e.getMessage());
        }
        return null;
    }

    /**
     * 判断是否为JSONObject
     *
     * @param obj instanceof String ? parseObject
     * @return {@code true}: JSONObject<br> {@code false}: 不是JSONObject
     */
    public static boolean isJSONObject(Object obj) {
        if (obj instanceof JSONObject) {
            return true;
        }
        if (obj instanceof String) {
            try {
                JSONObject json = parseObject((String) obj);
                return json != null && !json.isEmpty();
            } catch (Exception e) {
                Log.e(TAG, "isJSONObject  catch \n" + e.getMessage());
            }
        }
        return false;
    }

    /**
     * 判断是否为JSONArray
     *
     * @param obj instanceof String ? parseArray
     * @return {@code true}: JSONArray<br> {@code false}: 不是JSONArray
     */
    public static boolean isJSONArray(Object obj) {
        if (obj instanceof JSONArray) {
            return true;
        }
        if (obj instanceof String) {
            try {
                JSONArray json = parseArray((String) obj);
                return json != null && !json.isEmpty();
            } catch (Exception e) {
                Log.e(TAG, "isJSONArray  catch \n" + e.getMessage());
            }
        }
        return false;
    }

    public static <T> String entityToJsonString(T obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T jsonStringToEntity(String res, Class<T> c) {
        return JSON.parseObject(res, c);
    }

    public static <T> JsonArray entityToJsonArray(T obj) {
        JsonArray josn = new JsonArray();
        josn.add(entityToJsonString(obj));
        return josn;
    }

    public static String getStringByKey(@NonNull JSONObject jsonObject, @NonNull String key) {
        if (jsonObject.containsKey(key)) {
            return jsonObject.getString(key);
        }
        return null;
    }
}
