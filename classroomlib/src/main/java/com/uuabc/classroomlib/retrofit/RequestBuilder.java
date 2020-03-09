package com.uuabc.classroomlib.retrofit;

import android.annotation.SuppressLint;
import android.os.Build;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.utils.JsonUtils;

import java.util.HashMap;

/**
 * RequestBuilder
 * Created by wb on 2017/11/3.
 */

public class RequestBuilder {
    private HashMap<String, Object> mHashMap = new HashMap<>();

    public RequestBuilder build(String key, Object value) {
        mHashMap.put(key, value);
        return this;
    }

    public String create() {
        getRequestParams(mHashMap);
        String requestStr = JsonUtils.toJSONString(mHashMap);
        LogUtils.d("NetRequest", "请求参数：" + requestStr);
        return requestStr;
    }

    public String sCreate() {
        String requestStr = JsonUtils.toJSONString(mHashMap);
        LogUtils.d("NetRequest", "请求参数：" + requestStr);
        return requestStr;
    }


    @SuppressLint("HardwareIds")
    private void getRequestParams(HashMap<String, Object> requestParmsMap) {
        if (requestParmsMap == null) return;
        requestParmsMap.put("version", RoomConstant.ApiVersion);
        requestParmsMap.put("appVersion", AppUtils.getAppVersionName());
        requestParmsMap.put("deviceType", DeviceUtils.getModel());
        requestParmsMap.put("systemVersion", DeviceUtils.getSDKVersionName());
        requestParmsMap.put("deviceUUID", ObjectUtils.isEmpty(Build.SERIAL) ? DeviceUtils.getAndroidID() : Build.SERIAL);
        requestParmsMap.put("system", RoomConstant.SYSTEM);
        requestParmsMap.put("appType", 1);
    }
}
