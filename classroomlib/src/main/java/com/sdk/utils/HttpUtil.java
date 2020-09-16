/**
 * Copyright (C) Alibaba Cloud Computing, 2015
 * All rights reserved.
 * <p>
 * 版权所有 （C）阿里巴巴云计算，2015
 */

package com.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class HttpUtil {

    /**
     * Encode a URL segment with special chars replaced.
     * @param value
     * @return
     * @throws UnsupportedEncodingException
     */
    // TODO change the method name to percentageEncode
    public static String urlEncode(String value, String encoding) {
        if (value == null) {
            return "";
        }

        try {
            String encoded = URLEncoder.encode(value, encoding);
            // a b*c~d/e+f  经过URLEncoder.encode 会 转变成 a+b*c%7Ed%2Fe%2Bf
            return encoded.replace("+", "%20").replace("*", "%2A")
                    .replace("%7E", "~").replace("%2F", "/");
        } catch (Exception e) {
            throw new IllegalArgumentException("failed to encode url!", e);
        }
    }
}
