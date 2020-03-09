package com.uuabc.classroomlib.widget.dialog;

import android.content.Context;
import android.widget.ImageButton;

import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.widget.CaptainDialog;
import com.uuabc.classroomlib.widget.GradeView;

public class ClassOverDialog extends BaseDialog {
    private GradeView gvDiamond;

    private OnClickBtnListener listener;

    public void setExitListener(OnClickBtnListener listener) {
        this.listener = listener;
    }

    public interface OnClickBtnListener {
        void onClick();
    }

    public ClassOverDialog(Context context) {
        super(context);
        this.context = context;
        builder = new CaptainDialog.Builder(context);
        dialog = builder.cancelTouchout(true)
                .view(R.layout.dialog_room_sdk_over_class)
                .widthpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.5))
                .heightpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.5))
                .style(R.style.Dialog_No_Title)
                .build();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        gvDiamond = (GradeView) builder.getView(R.id.gv_diamond);
        ImageButton btnClick = (ImageButton) builder.getView(R.id.btn_click);
        btnClick.setOnClickListener(v -> {
            if (null != listener) {
                listener.onClick();
            }
        });
    }

    public ClassOverDialog setData(int count) {
        gvDiamond.setStar(count);
        return this;
    }
}
