package com.uuabc.classroomlib.utils;

import java.math.BigDecimal;

public class UtilsBigDecimal {

    public static double getDivValue(int a, int b) {
        return new BigDecimal((float) a / b)
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    public static float getDivValue(int a, float b) {
        return new BigDecimal((float) a / b)
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .floatValue();
    }
}
