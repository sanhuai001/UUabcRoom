package com.oneonline.uuabcroom

import com.blankj.utilcode.util.SPUtils
import com.uuabc.classroomlib.ApolloApp
import com.uuabc.classroomlib.RoomConstant

class CustomApp : ApolloApp() {

    override fun onCreate() {
        super.onCreate()

        try {
            SPUtils.getInstance()
                    .put(RoomConstant.ONLINE_SS_COURSE_HOST, BuildConfig.ONLINE_SS_COURSE_HOST)
            SPUtils.getInstance()
                    .put(RoomConstant.GRAPHQL_URL, BuildConfig.GRAPHQL_URL)
        } catch (e: Exception) {
        }

        initApolloClient()
        getEmoticons()
    }
}