package com.uuabc.classroomlib.widget.dialog

/**
 * Created by ln on 2019/1/25.
 */
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.uuabc.classroomlib.R
import kotlinx.android.synthetic.main.dialog_loading.*

fun createLoadingDialog(context: Context): Dialog {
    return Dialog(context, R.style.MyDialog_Loading).apply {
        setContentView(R.layout.dialog_loading)
        window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val animator = ObjectAnimator.ofFloat(rotation, "rotation", -360f)
        animator.duration = 1500
        animator.interpolator = LinearInterpolator()
        animator.repeatCount = ValueAnimator.INFINITE
        animator.start()
        setCancelable(false)
        setOnDismissListener { animator.end() }
    }
}