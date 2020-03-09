package com.uuabc.classroomlib.utils;

import android.text.TextUtils;

import com.blankj.utilcode.util.ObjectUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class ObjectUtil {

    public static int getIntValue(Object value) {
        if (value == null) return 0;
        if (value instanceof Integer) {
            return (int) value;
        } else if (value instanceof Long) {
            return (int) Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (int) Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (int) Double.parseDouble(value.toString());
        } else if (value instanceof BigDecimal) {
            return (int) Double.parseDouble(value.toString());
        } else if (value instanceof String) {
            String valueStr = (String) value;
            valueStr = valueStr.replaceAll("[^\\d.]", "");
            return TextUtils.isEmpty(valueStr) ? 0 : (int) Double.parseDouble(valueStr);
        } else {
            return 0;
        }
    }

    public static int getIntValueTwo(Object value) {
        if (value == null) return -1;
        if (value instanceof Integer) {
            return (int) value;
        } else if (value instanceof Long) {
            return (int) Long.parseLong(value.toString());
        } else if (value instanceof Float) {
            return (int) Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (int) Double.parseDouble(value.toString());
        } else if (value instanceof BigDecimal) {
            return (int) Double.parseDouble(value.toString());
        } else if (value instanceof String) {
            String valueStr = (String) value;
            valueStr = valueStr.replaceAll("[^\\d.]", "");
            return TextUtils.isEmpty(valueStr) ? -1 : (int) Double.parseDouble(valueStr);
        } else {
            return 0;
        }
    }

    public static long getLongValue(Object value) {
        if (value == null) return 0;
        if (value instanceof Integer) {
            return Long.parseLong(value.toString());
        } else if (value instanceof Long) {
            return (long) value;
        } else if (value instanceof Float) {
            return (long) Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (long) Double.parseDouble(value.toString());
        } else if (value instanceof BigDecimal) {
            return (long) Double.parseDouble(value.toString());
        } else if (value instanceof String) {
            String valueStr = (String) value;
            valueStr = valueStr.replaceAll("[^\\d.]", "");
            return TextUtils.isEmpty(valueStr) ? 0 : (long) Double.parseDouble(valueStr);
        } else {
            return 0;
        }
    }

    public static boolean getBoolean(Object value) {
        if (value == null) return false;

        if (value instanceof Boolean) {
            return (boolean) value;
        } else if (value instanceof Integer) {
            return (int) value == 1;
        } else if (value instanceof Double) {
            return (double) value == 1;
        } else if (value instanceof Float) {
            return (float) value == 1;
        } else if (value instanceof BigDecimal) {
            return Double.parseDouble(value.toString()) == 1;
        } else
            return value instanceof String && TextUtils.equals("true", (CharSequence) value);
    }

    public static float getFloat(Object value) {
        if (value == null) return 0f;
        if (value instanceof Integer) {
            return Float.parseFloat(value.toString());
        } else if (value instanceof Double) {
            return (float) Double.parseDouble(value.toString());
        } else if (value instanceof Long) {
            return Float.parseFloat(value.toString());
        } else if (value instanceof Float) {
            return (float) value;
        } else if (value instanceof BigDecimal) {
            return (float) Double.parseDouble(value.toString());
        } else if (value instanceof String) {
            String valueStr = (String) value;
            valueStr = valueStr.replaceAll("[^\\d.]", "");
            return TextUtils.isEmpty(valueStr) ? 0f : (float) Double.parseDouble(valueStr);
        } else {
            return 0f;
        }
    }

    public static String getString(Object value) {
        if (value == null) return "";
        return value.toString();
    }

    public static String getJsonStr(JSONObject jsonObj, String key) {
        if (jsonObj == null || TextUtils.isEmpty(key) || !jsonObj.has(key)) return "";
        try {
            Object object = jsonObj.get(key);
            return getString(object);
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getJsonInt(JSONObject jsonObj, String key) {
        if (jsonObj == null || TextUtils.isEmpty(key) || !jsonObj.has(key)) return 0;
        try {
            Object object = jsonObj.get(key);
            return getIntValue(object);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean isEmpty(String str) {
        return ObjectUtils.isEmpty(str) || ObjectUtils.equals(str, "null");
    }

    public static String getPhone(String phoneNum) {
        if (phoneNum.length() != 11) {
            return phoneNum;
        }
        return phoneNum.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }
}
