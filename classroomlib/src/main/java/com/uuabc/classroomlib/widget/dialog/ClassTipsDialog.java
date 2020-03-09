package com.uuabc.classroomlib.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.CourseInfoModel;
import com.uuabc.classroomlib.model.OneToOneRoomInResult;
import com.uuabc.classroomlib.model.RoomInResult;
import com.uuabc.classroomlib.widget.CaptainDialog;
import com.uuabc.classroomlib.widget.GradeView;

public class ClassTipsDialog extends BaseDialog {
    private TextView tvTime;
    private TextView tvName;
    private TextView tvMsg;
    private GradeView gvDiamond;
    private ImageView ivTips;
    private CountDownTimer timer;

    public ClassTipsDialog(Context context) {
        super(context);
        builder = new CaptainDialog.Builder(context);
        dialog = builder.cancelTouchout(true)
                .view(R.layout.dialog_room_sdk_class_tips)
                .widthpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.72))
                .heightpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.64))
                .style(R.style.Dialog_No_Title)
                .build();
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        tvTime = (TextView) builder.getView(R.id.tv_time);
        tvName = (TextView) builder.getView(R.id.tv_name);
        tvMsg = (TextView) builder.getView(R.id.tv_msg);
        gvDiamond = (GradeView) builder.getView(R.id.gv_diamond);
        ivTips = (ImageView) builder.getView(R.id.iv_live_tips);
    }

    public ClassTipsDialog setData(RoomInResult result) {
        tvName.setText(context.getResources().getString(R.string.dialog_room_tips_name_str, SPUtils.getInstance().getString(RoomConstant.USER_NAME)));
        tvMsg.setText(context.getResources().getString(R.string.dialog_room_in_msg_str, ObjectUtils.isNotEmpty(result.getName()) ? result.getName() : "", result.getCourseware_name()));
        ivTips.setVisibility(View.VISIBLE);
        return this;
    }

    public ClassTipsDialog setData(OneToOneRoomInResult result) {
        tvName.setText(context.getResources().getString(R.string.dialog_room_tips_name_str, SPUtils.getInstance().getString(RoomConstant.USER_NAME)));
        tvMsg.setText(context.getResources().getString(R.string.dialog_room_in_msg_str, ObjectUtils.isNotEmpty(result.getTeaName()) ? result.getTeaName() : "", result.getCourseware_name()));
        gvDiamond.setVisibility(View.VISIBLE);
        return this;
    }

    public ClassTipsDialog setData(CourseInfoModel result) {
        tvName.setText(context.getResources().getString(R.string.dialog_room_tips_name_str, SPUtils.getInstance().getString(RoomConstant.USER_NAME)));
        tvMsg.setText(context.getResources().getString(R.string.dialog_room_in_msg_str, ObjectUtils.isNotEmpty(result.getTeacher_name()) ? result.getTeacher_name() : "", result.getCourseware_name()));
        ivTips.setVisibility(View.VISIBLE);
        return this;
    }

    public ClassTipsDialog setData(String teacherName, String courseCaseName, int classType) {
        int msgRes = R.string.dialog_room_in_ss_msg_str;
        switch (classType) {
            case RoomConstant.CLASS_TYPE_ONE_TO_FOUR:
                msgRes = R.string.dialog_room_in_ss_1v4_msg_str;
            case RoomConstant.CLASS_TYPE_ONE_TO_ONE:
                ivTips.setImageResource(R.drawable.ic_room_sdk_diamond_blue_small);
                ivTips.setVisibility(View.VISIBLE);
                break;
        }
        tvName.setText(context.getResources().getString(R.string.dialog_room_tips_name_str, SPUtils.getInstance().getString(RoomConstant.USER_NAME)));
        tvMsg.setText(context.getResources().getString(msgRes, ObjectUtils.isNotEmpty(teacherName) ? teacherName : "", courseCaseName));
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
