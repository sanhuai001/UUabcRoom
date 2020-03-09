package com.uuabc.classroomlib.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.common.BaseCommonActivity;
import com.uuabc.classroomlib.common.RxTimer;
import com.uuabc.classroomlib.utils.CompatUtil;

@SuppressLint("StaticFieldLeak")
public class TipsDialogFragment extends DialogFragment {
    private static TipsDialogFragment tipsDialog;
    private Drawable leftDrawable;
    private String text;

    public static TipsDialogFragment getInstance() {
        try {
            if (tipsDialog != null && tipsDialog.isAdded()) {
                tipsDialog.dismiss();
            }
        } catch (Exception ignored) {
        }
        tipsDialog = null;

        synchronized (TipsDialogFragment.class) {
            if (tipsDialog == null) {
                tipsDialog = new TipsDialogFragment();
            }
        }

        return tipsDialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            try {
                int dividerID = getResources().getIdentifier("android:id/titleDivider", null, null);
                View divider = getDialog().findViewById(dividerID);
                divider.setBackgroundColor(Color.TRANSPARENT);
            } catch (Exception e) {
                //上面的代码，是用来去除Holo主题的蓝色线条
                e.printStackTrace();
            }
        }
        View view = inflater.inflate(R.layout.dialog_fragment_room_sdk_tips, container, false);
        if (getContext() != null && getContext() instanceof BaseCommonActivity) {
            ((BaseCommonActivity) getContext()).initAdapterScreen();
        }

        TextView tvWarning = view.findViewById(R.id.tv_warning);
        tvWarning.setText(TextUtils.isEmpty(text) ? "" : text);
        tvWarning.setCompoundDrawables(leftDrawable, null, null, null);

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //不一定是要触发返回栈，可以做一些其他的事情，我只是举个栗子。
                if (isAdded()) {
                    dismissDialogFrag();
                    getActivity().onBackPressed();
                }
                return true;
            }
            return false;
        });
        return view;
    }

    public TipsDialogFragment setText(String text) {
        this.text = text;
        return this;
    }

    private TipsDialogFragment setDrawable(Drawable leftDrawable) {
        this.leftDrawable = leftDrawable;
        return this;
    }

    public TipsDialogFragment setIcon(Context context) {
        leftDrawable = CompatUtil.getDrawable(context, R.drawable.ic_room_sdk_warning);
        leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
        setDrawable(leftDrawable);
        return this;
    }

    public void show(AppCompatActivity context) {
        setDrawable(null);
        showDialogFragment(context);
    }

    public void warningShow(Context context) {
        setIcon(context);
        showDialogFragment(context);
    }

    private void dismissDialogFrag() {
        try {
            if (tipsDialog != null && tipsDialog.isAdded()) {
                tipsDialog.dismiss();
            }
        } catch (Exception ignored) {
        }
    }

    private void showDialogFragment(Context context) {
        if (context instanceof AppCompatActivity) {
            if (((AppCompatActivity) context).isDestroyed()) return;
            FragmentTransaction ft = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            try {
                show(ft, "df");
            } catch (Exception e) {
                dismissDialogFrag();
            }
            new RxTimer().timer(2000, number -> dismissDialogFrag());
        }
    }
}
