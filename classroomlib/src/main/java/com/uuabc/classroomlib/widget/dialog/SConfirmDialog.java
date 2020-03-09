package com.uuabc.classroomlib.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.widget.CaptainDialog;
import com.uuabc.classroomlib.widget.GradeView;

public class SConfirmDialog extends BaseDialog {
    private TextView tvTitle;
    private TextView tvMsg;
    private TextView btnConfirm;
    private TextView btnCancel;
    private ImageView ivIcon;
    private GradeView gvDiamond;
    private TextView tvDia;
    private TextView tvUb;

    public SConfirmDialog(Context context) {
        super(context);
        builder = new CaptainDialog.Builder(context);
        dialog = builder.cancelTouchout(false)
                .view(R.layout.dialog_room_sdk_s_confirm)
                .widthpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.68))
                .heightpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.517))
                .style(R.style.Dialog_No_Title)
                .build();
        dialog.setCancelable(false);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        tvMsg = (TextView) builder.getView(R.id.tv_msg);
        btnConfirm = (TextView) builder.getView(R.id.btn_confirm);
        btnCancel = (TextView) builder.getView(R.id.btn_cancel);
        tvTitle = (TextView) builder.getView(R.id.tv_title);
        ivIcon = (ImageView) builder.getView(R.id.iv_icon);
        gvDiamond = (GradeView) builder.getView(R.id.gv_diamond);
        tvDia = (TextView) builder.getView(R.id.tv_dia);
        tvUb = (TextView) builder.getView(R.id.tv_ub);
    }

    public SConfirmDialog setIcon(int drawable) {
        if (ObjectUtils.isNotEmpty(drawable)) {
            ivIcon.setVisibility(View.VISIBLE);
            ivIcon.setBackgroundResource(drawable);
        }
        return this;
    }

    public SConfirmDialog setTitle(String title) {
        if (ObjectUtils.isNotEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        return this;
    }

    public SConfirmDialog setMsg(String text) {
        tvMsg.setText(text);
        return this;
    }

    public SConfirmDialog setMsg(Spanned text) {
        tvMsg.setText(text);
        return this;
    }

    public SConfirmDialog setDiaCount(int count) {
//        gvDiamond.setVisibility(View.VISIBLE);
//        gvDiamond.setStar(count);
        return this;
    }

    public SConfirmDialog setLiveDiaCount(int count) {
        tvDia.setVisibility(View.VISIBLE);
        tvDia.setText(String.valueOf(count));
        return this;
    }

    public SConfirmDialog setLiveUbCount(int count) {
//        tvUb.setVisibility(View.VISIBLE);
//        tvUb.setText(String.valueOf(count));
        return this;
    }

    public SConfirmDialog setCancelButton(String text, DialogInterface.OnClickListener listener) {
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setText(text);
        btnConfirm.setOnClickListener(v -> listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE));
        return this;
    }

    public SConfirmDialog setConfirmButton(String text, DialogInterface.OnClickListener listener) {
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setText(text);
        btnCancel.setOnClickListener(v -> listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE));
        return this;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        dialog = null;
    }
}
