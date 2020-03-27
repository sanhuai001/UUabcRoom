package com.uuabc.classroomlib.widget.dialog

/**
 * Created by ln on 2019/1/25.
 */
import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import com.uuabc.classroomlib.R
import kotlinx.android.synthetic.main.dialog_loading.*

fun createLoadingDialog(context: Context): Dialog {
    return Dialog(context, R.style.MyDialog_Loading).apply {
        setContentView(R.layout.dialog_loading)
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lavLoading.imageAssetsFolder = "images/"
        setCancelable(false)
    }
}