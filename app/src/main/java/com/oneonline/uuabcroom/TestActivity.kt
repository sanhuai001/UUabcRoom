package com.oneonline.uuabcroom

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ToastUtils
import com.jakewharton.rxbinding2.view.RxView
import com.uuabc.classroomlib.RoomApplication
import com.uuabc.classroomlib.model.RoomType
import kotlinx.android.synthetic.main.activity_test.*
import java.util.concurrent.TimeUnit

class TestActivity : AppCompatActivity() {
    private var currentId = R.id.rbOneToFour

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        rgType.setOnCheckedChangeListener { _, p1 ->
            currentId = p1
        }

        etToken.setText("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.IjIzYTA4MTQ0LTNlYzQtNDRkZC1hYzU5LTBhNDgyYWM0N2M1NzMwMTQi.dcHxPhSQZhD0KAYkcpJt8mUk7Id6dd-rgykHzOFAlJY")
        RxView.clicks(btnInter).throttleFirst(3, TimeUnit.SECONDS).subscribe {
            val roomToken = etToken.text.toString()
            if (TextUtils.isEmpty(roomToken)) {
                ToastUtils.showShort("roomToken不能为空")
                return@subscribe
            }

            RoomApplication.getInstance().setUserInfo("12020042206", "303878952827548723")
            when (currentId) {
                R.id.rbOneToFour -> {
                    RoomApplication.getInstance().jumpToClassRoom(roomToken, RoomType.ONETOFOUR)
                }
                R.id.rbOneToOne -> {
                    RoomApplication.getInstance().jumpToClassRoom(roomToken, RoomType.ONETOONE)
                }
                R.id.rbLive -> {
                    RoomApplication.getInstance().jumpToClassRoom(roomToken, RoomType.LIVE)
                }
            }

//            AutoSizeConfig.getInstance().unitsManager
//                    .setSupportDP(false)
//                    .setSupportSP(false).supportSubunits = Subunits.MM
//            LiveSDK.customEnvironmentPrefix = "b54152889"
//            LiveSDK.deployType = LPConstants.LPDeployType.Product
//            InteractiveClassUI.enterRoom(this, "9cjfzn", "Test666") { LogUtil.d("enterRoomTest", "进入课堂失败 : $it") }
//            LiveSDKWithUI.enterRoom(this, "9cjfzn", "Test666") {}
//            LiveSDKWithUI.enterRoomWithVerticalTemplate(this, "scm6rpk7", "张十实") { msg -> LogUtil.d("课表", "进入课堂失败 : $msg") }
        }
    }
}
