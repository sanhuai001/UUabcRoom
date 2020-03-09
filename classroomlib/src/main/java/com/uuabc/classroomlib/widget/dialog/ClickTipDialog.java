package com.uuabc.classroomlib.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.widget.CaptainDialog;

public class ClickTipDialog extends BaseDialog {

    private ImageButton ibClose;
    private ImageButton btnClick;
    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvContent;

    private OnClickBtnListener listener;
    private String deviceName;

    public void setExitListener(OnClickBtnListener listener) {
        this.listener = listener;
    }

    public interface OnClickBtnListener {
        void onExit();
    }

    public ClickTipDialog(Context context, boolean canCancel) {
        super(context);
        this.context = context;
        builder = new CaptainDialog.Builder(context);
        dialog = builder.cancelTouchout(canCancel)
                .view(R.layout.dialog_room_sdk_exit_class)
                .widthpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.5))
                .heightpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.5))
                .style(R.style.Dialog_No_Title)
                .build();
        dialog.setCancelable(canCancel);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        ibClose = (ImageButton) builder.getView(R.id.ib_close);
        btnClick = (ImageButton) builder.getView(R.id.btn_click);
        ivIcon = (ImageView) builder.getView(R.id.iv_icon);
        tvTitle = (TextView) builder.getView(R.id.tv_title);
        tvContent = (TextView) builder.getView(R.id.tv_content);

        ibClose.setOnClickListener(v -> dismiss());
        btnClick.setOnClickListener(v -> {
            if (null != listener) {
                listener.onExit();
            }
        });
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isShow() {
        return dialog != null && dialog.isShowing();
    }

    public Dialog setType(int type) {
        switch (type) {
            case RoomConstant.EXIT:
                ivIcon.setBackgroundResource(R.drawable.ic_room_sdk_exit_class);
                tvTitle.setText(context.getResources().getString(R.string.dialog_exit_title_str));
                btnClick.setBackgroundResource(R.drawable.room_sdk_selector_exit_dialog_button);
                tvContent.setText(context.getResources().getString(R.string.dialog_exit_content_str));
                break;
            case RoomConstant.REFRESH:
                ivIcon.setBackgroundResource(R.drawable.ic_room_sdk_class_refresh);
                tvTitle.setText(context.getResources().getString(R.string.dialog_refresh_title_str));
                btnClick.setBackgroundResource(R.drawable.room_sdk_selector_refresh_dialog_button);
                tvContent.setText(context.getResources().getString(R.string.dialog_refresh_content_str));
                break;
            case RoomConstant.OFFLINE:
                builder.cancelTouchout(false);
                ivIcon.setBackgroundResource(R.drawable.ic_room_sdk_exit_class);
                tvTitle.setText(context.getResources().getString(R.string.dialog_exit_title_str));
                btnClick.setBackgroundResource(R.drawable.room_sdk_selector_exit_dialog_button);
                tvContent.setText(context.getResources().getString(R.string.dialog_room_tips_offline_str, TextUtils.isEmpty(deviceName) ? "" : deviceName));
                ibClose.setVisibility(View.GONE);
                break;
        }
        return dialog;
    }
}
