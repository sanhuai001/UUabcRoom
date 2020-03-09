package com.uuabc.classroomlib.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uuabc.classroomlib.R;

public class DiamondCountInsideView extends RelativeLayout {
    private TextView tvDiamondCount;
    private View viewDiamondCount;

    public DiamondCountInsideView(Context context) {
        this(context, null);
    }

    public DiamondCountInsideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiamondCountInsideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_room_sdk_show_diamonds_inside_count, this);
        tvDiamondCount = rootView.findViewById(R.id.tv_diamond_count);
        viewDiamondCount = rootView.findViewById(R.id.view_diamonds_num);
    }

    public void setViewHight(int height) {
        LayoutParams params = (LayoutParams) viewDiamondCount.getLayoutParams();
        params.height = height;
        viewDiamondCount.requestLayout();
    }

    public void setViewBg(int drawableId) {
        viewDiamondCount.setBackgroundResource(drawableId);
    }

    public void setDiamondCount(int count) {
        tvDiamondCount.setText(String.valueOf(count));
    }
}
