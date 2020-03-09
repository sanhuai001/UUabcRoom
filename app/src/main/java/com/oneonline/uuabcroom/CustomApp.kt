package com.oneonline.uuabcroom

import com.blankj.utilcode.util.SPUtils
import com.uuabc.classroomlib.RoomApplication
import com.uuabc.classroomlib.RoomConstant

class CustomApp : RoomApplication() {

    override fun onCreate() {
        super.onCreate()

        SPUtils.getInstance()
            .put(RoomConstant.ONLINE_SS_COURSE_HOST, BuildConfig.ONLINE_SS_COURSE_HOST)
    }
}