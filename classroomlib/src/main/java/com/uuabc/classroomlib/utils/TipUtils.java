package com.uuabc.classroomlib.utils;

import android.content.Context;
import android.text.TextUtils;

import com.uuabc.classroomlib.widget.dialog.TipsDialogFragment;

/**
 * 提示dialog的工具类
 * Created by bobi on 2018/4/24.
 */

public class TipUtils {
    /**
     * 显示警告
     *
     * @param text
     */
    public static void warningShow(Context context, String text) {
        if (TextUtils.isEmpty(text)) return;
        TipsDialogFragment.getInstance().setText(text).warningShow(context);
    }

    /**
     * 显示普通提示
     *
     * @param text
     */
    public static void show(Context context, String text) {
        if (TextUtils.isEmpty(text)) return;
        TipsDialogFragment.getInstance().setText(text).warningShow(context);
    }
}
