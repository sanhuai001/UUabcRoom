package com.uuabc.classroomlib.widget.dialog;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.model.TopModel;
import com.uuabc.classroomlib.widget.CaptainDialog;

public class LiveClassOverDialog extends BaseDialog {
    private TextView tvMsg;
    private TextView tvDia, tvUb;
    private OnClickBtnListener listener;

    public void setExitListener(OnClickBtnListener listener) {
        this.listener = listener;
    }

    public interface OnClickBtnListener {
        void onClick();

        void onExit();
    }

    public LiveClassOverDialog(Context context) {
        super(context);
        this.context = context;
        builder = new CaptainDialog.Builder(context);
        dialog = builder.cancelTouchout(false)
                .view(R.layout.dialog_room_sdk_live_over_class)
                .widthpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.8))
                .heightpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.7))
                .style(R.style.Dialog_No_Title)
                .build();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        tvMsg = (TextView) builder.getView(R.id.tv_msg);
        tvDia = (TextView) builder.getView(R.id.tv_dia);
        tvUb = (TextView) builder.getView(R.id.tv_ub);
        Button btnDoHomeWork = (Button) builder.getView(R.id.btn_do_homework);
        Button btnCancel = (Button) builder.getView(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onExit();
            }
        });
        btnDoHomeWork.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick();
            }
        });
    }

    public void setData(TopModel.RankBean rankBean) {
        tvMsg.setText(context.getResources().getString(R.string.dialog_live_class_over_str, rankBean != null ? rankBean.getCnum() : 0));
        tvDia.setText(context.getResources().getString(R.string.dialog_live_over_class_get_dia_str, rankBean != null ? rankBean.getDia() : 0));
        tvUb.setText(context.getResources().getString(R.string.dialog_live_over_class_get_ub_str, rankBean != null ? rankBean.getUb() : 0));
    }


}
