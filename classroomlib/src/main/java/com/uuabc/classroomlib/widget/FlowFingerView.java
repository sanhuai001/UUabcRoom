package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewConfigurationCompat;

import com.uuabc.classroomlib.RoomApplication;

/**
 * Created by user on 2018/4/3.
 */

public class FlowFingerView extends ConstraintLayout {

    private ViewGroup.MarginLayoutParams marginLayoutParams;
    private int screenWidth;
    private int screenHeight;
//    private Scroller scroller;

//    private View rootView;

    //    private OnMoveListener listener;
    private OnClickListener listener;
    private int slop;

//    public void setOnMoveListener(OnMoveListener listener) {
//        this.listener = listener;
//    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 滑动时的监听
     */
    public interface OnMoveListener {
        void onMove(int offsetX, int offsetY);
    }

    public interface OnClickListener {
        void onClick();
    }

    public FlowFingerView(Context context) {
        this(context, null);
    }

    public FlowFingerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        slop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));

    }

    private void initView(Context context) {
        screenWidth = RoomApplication.getInstance().getScreenWidth();
        screenHeight = RoomApplication.getInstance().getScreenHeight();
    }

    private int mFistX;
    private int mFistY;
    private int mLastX;
    private int mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                mFistX = (int) event.getRawX();
                mFistY = (int) event.getRawY();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE://滑动
                int offsetX = x - mLastX;
                int offsetY = y - mLastY;
                if ((Math.abs(offsetX) > 2 && Math.abs(offsetY) > 2)) {
                    marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
                    //判断下次移动会不会超过屏幕
                    if (getLeft() + offsetX >= 0 && getRight() + offsetX < ((View) getParent()).getWidth() - offsetX) {
                        marginLayoutParams.leftMargin = getLeft() + offsetX;
                    }
                    if (getTop() + offsetY >= 0 && getBottom() + offsetY < ((View) getParent()).getHeight() - offsetY) {
                        marginLayoutParams.topMargin = getTop() + offsetY;
                    }
                    setLayoutParams(marginLayoutParams);
                }
//                if (null != listener) {
//                    listener.onMove(offsetX, offsetY);
//                }
                //重新设置初始坐标
//                mLastX = x;
//                mLastY = y;
                break;
            case MotionEvent.ACTION_UP://抬起
                mLastX = (int) event.getRawX();
                mLastY = (int) event.getRawY();
                int oX = mFistX - mLastX;
                int oY = mFistY - mLastY;
                if (Math.abs(oX) <= 5 && Math.abs(oY) <= 5) {
                    if (isFocusable() && isFocusableInTouchMode() && !isFocused()) {
                        requestFocus();
                    }
                    performClick();
//                    if (null!=listener){
//                        listener.onClick();
//                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

//    private int lastX = 0;
//    private int lastY = 0; //手指在屏幕上的坐标
//
//    int rX = 0;
//    int rY = 0;
//
//    private boolean isDraged = true;
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int dx = 0;
//        int dy = 0; //手指在屏幕上移动的距离
//        int parentRight = ((View) getParent()).getWidth();
//        int parentBottom = ((View) getParent()).getHeight();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                isDraged = true;
//                lastX = (int) event.getRawX();
//                lastY = (int) event.getRawY();
//                rX = lastX;
//                rY = lastY;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                dx = (int) event.getRawX() - lastX;
//                dy = (int) event.getRawY() - lastY;
//
//                if (Math.abs(dy) < 3 || Math.abs(dx) < 3) {
//                    isDraged = false; //如果移动的距离为零，则认为控件没有被拖动过
//                } else {
//                    isDraged = true;
//                }
//
//                int l = getLeft() + dx;
//                int b = getBottom() + dy;
//                int r = getRight() + dx;
//                int t = getTop() + dy;
//                if (l < 0) {//处理按钮被移动到父布局的上下左右四个边缘时的情况，防止控件被拖出父布局
//                    l = 0;
//                    r = l + getWidth();
//                }
//                if (t < 0) {
//                    t = 0;
//                    b = t + getHeight();
//                }
//                if (r > parentRight) {
//                    r = parentRight;
//                    l = r - getWidth();
//                }
//                if (b > parentBottom) {
//                    b = parentBottom;
//                    t = b - getHeight();
//                }
//                layout(l, t, r, b);
//
//                lastX = (int) event.getRawX();
//                lastY = (int) event.getRawY();
//
//                postInvalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                dx = (int) event.getRawX() - rX;
//                dy = (int) event.getRawY() - rY;
//                if (Math.abs(dy) < 5 || Math.abs(dx) < 5) {
//                    if (isFocusable() && isFocusableInTouchMode() && !isFocused()) {
//                        requestFocus();
//                    }
//                    performClick();
//                }
//                break;
//        }
//        return isDraged;
//    }

    public void setMove(int offsetX, int offsetY) {
        marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        //判断下次移动会不会超过屏幕
        if (getLeft() + offsetX >= 0 && getRight() + offsetX < ((View) getParent()).getWidth() - offsetX) {
            marginLayoutParams.leftMargin = getLeft() + offsetX;
        }
        if (getTop() + offsetY >= 0 && getBottom() + offsetY < ((View) getParent()).getHeight() - offsetY) {
            marginLayoutParams.topMargin = getTop() + offsetY;
        }
        setLayoutParams(marginLayoutParams);
    }
}
