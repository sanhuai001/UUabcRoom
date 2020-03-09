package com.uuabc.classroomlib.utils;

import android.content.Context;

import com.umeng.analytics.MobclickAgent;

public class PointUtil {
    public static void onEvent(Context context, String event) {
        MobclickAgent.onEvent(context, event);
    }
}
