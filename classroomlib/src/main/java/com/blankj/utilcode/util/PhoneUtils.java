package com.blankj.utilcode.util;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/08/02
 *     desc  : utils about phone
 * </pre>
 */
public final class PhoneUtils {

    private PhoneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return whether the device is phone.
     *
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isPhone() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    public static boolean isSimCardReady() {
        TelephonyManager tm = getTelephonyManager();
        return tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    private static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) Utils.getApp().getSystemService(Context.TELEPHONY_SERVICE);
    }
}
