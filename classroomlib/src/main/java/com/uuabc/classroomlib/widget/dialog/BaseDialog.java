package com.uuabc.classroomlib.widget.dialog;

import android.app.Dialog;
import android.content.Context;

import com.uuabc.classroomlib.widget.CaptainDialog;

public class BaseDialog {
    protected CaptainDialog.Builder builder;
    protected Dialog dialog;
    protected Context context;

    public BaseDialog(Context context) {
        this.context = context;
    }

    public void dismiss() {
        try {
            if ((dialog != null) && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception ignored) {
        }
    }

    public void show() {
        try {
            if (dialog != null) {
                dialog.show();
            }
        } catch (Exception ignored) {
        }
    }
}
