package com.uuabc.classroomlib.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager

import androidx.appcompat.app.AppCompatActivity

import me.jessyan.autosize.internal.CustomAdapt

@SuppressLint("Registered")
open class BaseAdapterScreenActivity : AppCompatActivity(), CustomAdapt {
    var isFullScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAdapterScreen()
    }

    override fun onResume() {
        super.onResume()
        initAdapterScreen()
    }

    fun initAdapterScreen() {
        if (isFullScreen)
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 720f
    }
}
