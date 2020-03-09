package com.uuabc.classroomlib.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Spanned;

import com.uuabc.classroomlib.widget.dialog.ConfirmDialog;


/**
 * Created by bobi on 2018/4/26.
 */

public class ConfirmDialogUtils {

    public static ConfirmDialog show(Context context, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setMsg(msg).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static ConfirmDialog show(Context context, Spanned msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setMsg(msg).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static ConfirmDialog show(Context context, String msg, String cancelText, DialogInterface.OnClickListener rightListener, String confirmText, DialogInterface.OnClickListener leftListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setMsg(msg).setCancelButton(cancelText, rightListener).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static ConfirmDialog show(Context context, String title, String msg, String cancelText, DialogInterface.OnClickListener rightListener, String confirmText, DialogInterface.OnClickListener leftListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setTitle(title);
        dialog.setMsg(msg).setCancelButton(cancelText, rightListener).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static ConfirmDialog show(Context context, int drawable, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setIcon(drawable);
        dialog.setMsg(msg).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static ConfirmDialog otherShow(Context context, int drawable, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setIcon(drawable);
        dialog.setMsg(msg).setCancelButton(confirmText, leftListener).show();
        return dialog;
    }

    public static ConfirmDialog show(Context context, int drawable, String msg, String cancelText, DialogInterface.OnClickListener rightListener, String confirmText, DialogInterface.OnClickListener leftListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setIcon(drawable);
        dialog.setMsg(msg).setCancelButton(cancelText, rightListener).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }

    public static ConfirmDialog show(Context context, int drawable, int count, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setIcon(drawable).setDiaCount(count).setMsg(msg).setCancelButton(confirmText, leftListener).show();
        return dialog;
    }

    public static ConfirmDialog show(Context context, int drawable, int dia, int ub, String msg, String confirmText, DialogInterface.OnClickListener leftListener) {
        ConfirmDialog dialog = new ConfirmDialog(context);
        dialog.setIcon(drawable).setLiveDiaCount(dia).setLiveUbCount(ub).setMsg(msg).setConfirmButton(confirmText, leftListener).show();
        return dialog;
    }
}
