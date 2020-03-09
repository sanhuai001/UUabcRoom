package com.uuabc.classroomlib.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import java.io.File;

public class MemoryUtil {
    final static int m = 1024 * 1024;

    /**
     * 获得系统可用内存信息
     */
    public static long getSystemAvaialbeMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得MemoryInfo对象
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        //获得系统可用内存，保存在MemoryInfo对象上
        if (am != null) {
            am.getMemoryInfo(memoryInfo);
            return memoryInfo.availMem / m;
        }
        return 0;
    }

    /**
     * 获取应用可用内存
     */
    public static long getAppAvaialbeMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() / m;
    }

    /**
     * 调用系统函数，字符串转换 long -String KB/MB
     */
    public static String formateFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 获取手机内部存储空间
     *
     * @return 以M单位的容量
     */
    public static long getInternalMemorySize() {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        long size = blockCountLong * blockSizeLong;
        return toMB(size);
    }

    public static long toMB(long size) {
        return size / 1024 / 1024;
    }
}
