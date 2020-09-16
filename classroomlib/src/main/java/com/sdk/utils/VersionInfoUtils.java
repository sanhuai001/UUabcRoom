/**
 * Copyright (C) Alibaba Cloud Computing, 2015
 * All rights reserved.
 * <p>
 * 版权所有 （C）阿里巴巴云计算，2015
 */

package com.sdk.utils;

import android.os.Build;
import android.text.TextUtils;

import com.sdk.Constants;

public class VersionInfoUtils {
    private static String userAgent = null;


    public static String getUserAgent() {
        if (userAgent == null) {
            userAgent = "aliyun-log-sdk-android/" + getVersion() + "/" + getDefaultUserAgent();
        }
        return userAgent;
    }

    public static String getVersion() {
        return Constants.SDK_VERSION;
    }

    /**
     * 获取系统UA值
     *
     * @return
     */
    public static String getDefaultUserAgent() {
        String result = System.getProperty("http.agent");
        if (!TextUtils.isEmpty(result)) {
            result = "(" + System.getProperty("os.name") + "/Android " + Build.VERSION.RELEASE + "/" +
                    Build.MODEL + "/" + Build.ID + ")";
        }
        return result.replaceAll("[^\\p{ASCII}]", "?");
    }


}
