package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uuabc.classroomlib.R;

public class DrawTextImageView extends LinearLayout {
    private ImageView ivMouse;
    private TextView tvUserName;

    public DrawTextImageView(Context context) {
        this(context, null);
    }

    public DrawTextImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawTextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View root = LayoutInflater.from(context).inflate(R.layout.view_room_sdk_drawtext_imageview_layout, this, true);
        ivMouse = root.findViewById(R.id.iv_mouse);
        tvUserName = root.findViewById(R.id.tv_user_name);
    }

    public void setImage(int imageRes) {
        ivMouse.setImageResource(imageRes);
    }

    public void setUserName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            tvUserName.setVisibility(GONE);
        } else {
            tvUserName.setText(userName);
        }
    }
}
