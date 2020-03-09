package com.uuabc.classroomlib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by wb on 2017/10/27.
 * 兼容工具类
 */

public final class CompatUtil {

    /**
     * 获取颜色
     *
     * @param context 上下文
     * @param colorId 颜色资源id
     * @return 颜色资源id
     */
    public static int getColor(Context context, int colorId) {
        if (Build.VERSION.SDK_INT >= 23) {
            return getColor(context, colorId, null);
        }
        return context.getResources().getColor(colorId);
    }

    /**
     * 获取颜色
     *
     * @param context 上下文
     * @param colorId 颜色资源id
     * @param theme   主题
     * @return 颜色资源id
     */
    public static int getColor(Context context, int colorId, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getResources().getColor(colorId, theme);
        }
        return context.getResources().getColor(colorId);
    }

    /**
     * 获取图片
     *
     * @param context    上下文
     * @param drawableId 图片资源id
     * @return 图片
     */
    public static Drawable getDrawable(Context context, int drawableId) {
        return getDrawable(context, drawableId, null);
    }

    /**
     * 获取图片
     *
     * @param context    上下文
     * @param drawableId 图片资源id
     * @param theme      主题
     * @return 图片
     */
    public static Drawable getDrawable(Context context, int drawableId, Resources.Theme theme) {
        if (Build.VERSION.SDK_INT >= 21) {
            return context.getResources().getDrawable(drawableId, theme);
        }
        return context.getResources().getDrawable(drawableId);
    }

    /**
     * 设置背景图片
     *
     * @param view     被设置的view
     * @param drawable 背景图
     */
    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 获取drawableRight图片
     *
     * @param context    上下文
     * @param drawableId 图片资源id
     * @return 图片
     */
    public static Drawable getRightDrawable(Context context, int drawableId) {
        Drawable drawable = CompatUtil.getDrawable(context, drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }
}
