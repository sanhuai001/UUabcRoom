package com.uuabc.classroomlib.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.CourseInfoModel;
import com.uuabc.classroomlib.widget.CaptainDialog;

public class LiveClassInDialog extends BaseDialog {
    private TextView tvTime;
    private TextView tvMyName;
    private TextView tvTips;
    private CountDownTimer timer;

    public LiveClassInDialog(Context context) {
        super(context);
        this.context = context;
        builder = new CaptainDialog.Builder(context);
        dialog = builder.cancelTouchout(true)
                .view(R.layout.dialog_room_sdk_live_room_in)
                .widthpx((int) (RoomApplication.getInstance().getScreenWidth() * 0.5))
                .heightpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.6))
                .style(R.style.Dialog_No_Title)
                .build();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        tvTime = (TextView) builder.getView(R.id.tv_time);
        tvMyName = (TextView) builder.getView(R.id.tv_my_name);
        tvTips = (TextView) builder.getView(R.id.tv_tips);
    }

    public LiveClassInDialog setData(CourseInfoModel result) {
        tvMyName.setText(context.getResources().getString(R.string.dialog_room_tips_name_str, SPUtils.getInstance().getString(RoomConstant.USER_NAME)));
        tvTips.setText(Html.fromHtml(context.getResources().getString(R.string.dialog_room_tips_str, ObjectUtils.isNotEmpty(result.getTeacher_name()) ? result.getTeacher_name() : "", result.getCourseware_name())));
        return this;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void show() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(1000 * 10, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTime.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                tvTime.setText("0" + "s");
                dismiss();
                timer.cancel();
            }
        };
        timer.start();
        super.show();
    }
}
