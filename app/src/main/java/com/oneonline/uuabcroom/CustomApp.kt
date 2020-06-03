package com.oneonline.uuabcroom

import com.uuabc.classroomlib.RoomApplication

class CustomApp : RoomApplication() {

    override fun onCreate() {
        super.onCreate()
        ONLINE_SS_COURSE_HOST = BuildConfig.ONLINE_SS_COURSE_HOST
        GRAPHQL_URL = BuildConfig.GRAPHQL_URL
    }
}