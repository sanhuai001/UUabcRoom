package com.uuabc.classroomlib.classroom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.uuabc.classroomlib.R;

public class CourseLoadFailView extends RelativeLayout {
    private View rootView;

    public CourseLoadFailView(Context context) {
        this(context, null);
    }

    public CourseLoadFailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CourseLoadFailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setClickListener(OnClickListener listener) {
        if (rootView != null) {
            rootView.findViewById(R.id.btn_reload_course).setOnClickListener(listener);
        }
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        rootView = inflater.inflate(R.layout.view_room_sdk_course_load_fail, this);
    }
}
