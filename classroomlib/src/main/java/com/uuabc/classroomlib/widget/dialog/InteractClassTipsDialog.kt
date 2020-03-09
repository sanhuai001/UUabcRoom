package com.uuabc.classroomlib.widget.dialog

import android.content.Context
import com.blankj.utilcode.util.ScreenUtils
import com.uuabc.classroomlib.R
import com.uuabc.classroomlib.widget.CaptainDialog
import kotlinx.android.synthetic.main.dialog_class_tips.view.*
import kotlinx.coroutines.*

class InteractClassTipsDialog(context: Context) : BaseDialog(context) {

    lateinit var listener: () -> Unit
    private var timeCount = 10

    init {
        this.context = context
        builder = CaptainDialog.Builder(context)
        dialog = builder.cancelTouchout(false)
                .view(R.layout.dialog_class_tips)
                .widthpx((ScreenUtils.getScreenWidth() * 0.65f).toInt())
                .heightpx((ScreenUtils.getScreenHeight() * 0.7f).toInt())
                .style(R.style.Dialog_No_Title)
                .build()
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        builder.rootView.ivClose.setOnClickListener {
            dismiss()
        }
    }

    fun setOnBindingListener(listener: () -> Unit) {
        this.listener = listener
    }

    override fun show() {
        super.show()
        countDown()
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }

    private fun countDown() {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                builder.rootView.tvTime.text = "${--timeCount}S"
                if (timeCount > 0) {
                    countDown()
                } else {
                    dismiss()
                }
            }
        }
    }
}
