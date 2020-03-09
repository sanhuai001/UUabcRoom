package com.uuabc.classroomlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.utils.MediaPlayerUtil;

/**
 * 音效按钮
 */
public class VoiceButton extends androidx.appcompat.widget.AppCompatButton {

    public VoiceButton(Context context) {
        this(context, null);
    }

    public VoiceButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSound();
    }

    private void initSound() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                MediaPlayerUtil.getInstance().resetToPlay(getContext(), R.raw.click_sing);
                break;
        }
        return true;
    }

}
