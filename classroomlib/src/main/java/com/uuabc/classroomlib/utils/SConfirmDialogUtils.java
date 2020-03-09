package com.uuabc.classroomlib.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Spanned;

import com.uuabc.classroomlib.widget.dialog.SConfirmDialog;

public class SConfirmDialogUtils {
    public static SConfirmDialog show(Context context, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setMsg(msg).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static SConfirmDialog show(Context context, Spanned msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setMsg(msg).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static SConfirmDialog show(Context context, String msg, String cancelText, DialogInterface.OnClickListener rightListener, String confirmText, DialogInterface.OnClickListener leftListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setMsg(msg).setCancelButton(cancelText, rightListener).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static SConfirmDialog show(Context context, String title, String msg, String cancelText, DialogInterface.OnClickListener rightListener, String confirmText, DialogInterface.OnClickListener leftListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setTitle(title);
        dialog.setMsg(msg).setCancelButton(cancelText, rightListener).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static SConfirmDialog show(Context context, int drawable, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setIcon(drawable);
        dialog.setMsg(msg).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static SConfirmDialog otherShow(Context context, int drawable, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setIcon(drawable);
        dialog.setMsg(msg).setCancelButton(confirmText, leftListener).show();
        return dialog;
    }

    public static SConfirmDialog show(Context context, int drawable, String msg, String cancelText, DialogInterface.OnClickListener rightListener, String confirmText, DialogInterface.OnClickListener leftListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setIcon(drawable);
        dialog.setMsg(msg).setCancelButton(cancelText, rightListener).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static SConfirmDialog show(Context context, int drawable, int count, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setIcon(drawable).setDiaCount(count).setMsg(msg).setCancelButton(confirmText, leftListener).show();
        return dialog;
    }

    public static SConfirmDialog show(Context context, int drawable, int count, String msg, String leftText, DialogInterface.OnClickListener leftListener, String rightText, DialogInterface.OnClickListener rightListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setIcon(drawable).setDiaCount(count).setMsg(msg).setCancelButton(leftText, leftListener).setConfirmButton(rightText, rightListener).show();
        return dialog;
    }

    public static SConfirmDialog show(Context context, int drawable, int dia, int ub, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        SConfirmDialog dialog = new SConfirmDialog(context);
        dialog.setIcon(drawable).setLiveDiaCount(dia).setLiveUbCount(ub).setMsg(msg).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }
}
