package com.uuabc.classroomlib.widget.dialog

/**
 * Created by ln on 2019/1/25.
 */
import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import com.uuabc.classroomlib.R
import kotlinx.android.synthetic.main.dialog_sdk_room_loading.*

fun createLoadingDialog(context: Context, isPortrait: Boolean? = true): Dialog {
    return Dialog(context, R.style.MyDialog_Loading).apply {
        if (isPortrait == null || isPortrait) {
            setContentView(R.layout.dialog_sdk_room_loading)
        } else {
            setContentView(R.layout.dialog_sdk_room_portrait_loading)
        }
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lavLoading.imageAssetsFolder = "images/"
        setCancelable(false)
    }
}