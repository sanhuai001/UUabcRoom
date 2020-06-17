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

        etToken.setText("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.ImRlMzU0ODM2LTYxN2EtNGVjNy04Nzg2LTQ3NzdkMWExYWI4MiI.poBSW2KTyhrt1XhijhbjDQzabhMbkJGPEVc2YKalfa8")
        RxView.clicks(btnInter).throttleFirst(3, TimeUnit.SECONDS).subscribe {
            val roomToken = etToken.text.toString()
            if (TextUtils.isEmpty(roomToken)) {
                ToastUtils.showShort("roomToken不能为空")
                return@subscribe
            }

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
        }
    }
}
