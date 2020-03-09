package com.uuabc.classroomlib.common;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.utils.CompatUtil;

public class BaseDialogFragment extends DialogFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
        if (getContext() != null && getContext() instanceof BaseCommonActivity) {
            ((BaseCommonActivity) getContext()).initAdapterScreen();
        }
    }

    protected void setCompat() {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(CompatUtil.getColor(getContext(), R.color.translucent)));
        }

        try {
            int dividerID = getResources().getIdentifier("android:id/titleDivider", null, null);
            View divider = getDialog().findViewById(dividerID);
            divider.setBackgroundColor(Color.TRANSPARENT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showDialogFragment(AppCompatActivity context) {
        try {
            FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            show(ft, "df");
        } catch (Exception ignored) {

        }
    }

    protected void dismissDialogFrag(BaseDialogFragment dialog) {
        try {
            if (dialog != null && dialog.isAdded()) {
                dialog.dismiss();
            }
        } catch (Exception ignored) {

        }
    }

    protected static void dismissDialog(BaseDialogFragment dialog) {
        try {
            if (dialog != null && dialog.isAdded()) {
                dialog.dismiss();
            }
        } catch (Exception ignored) {

        }
    }
}
