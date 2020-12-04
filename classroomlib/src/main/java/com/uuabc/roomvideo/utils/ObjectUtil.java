package com.uuabc.roomvideo.utils;

import android.text.TextUtils;

public class ObjectUtil {

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
        } else
            return value instanceof String && TextUtils.equals("true", (CharSequence) value);
    }
}
