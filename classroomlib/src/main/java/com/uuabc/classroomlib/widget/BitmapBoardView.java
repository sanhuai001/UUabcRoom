package com.uuabc.classroomlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;

/**
 * 使用bitmap作为基础的画板控件
 * Created by user on 2018/3/28.
 */

public class BitmapBoardView extends androidx.appcompat.widget.AppCompatImageView {
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint paint;

    private Path path = new Path();
    private String currentColor = RoomConstant.COLOR_RED;
    private int userId;

    private OnPathListener listener;
    private boolean canDraw, canAnimate;

    private JSONObject content;
    private JSONArray points;
    private long currentTime;

    public void setOnPathListener(OnPathListener listener) {
        this.listener = listener;
    }

    /**
     * 滑动时的监听
     */
    public interface OnPathListener {
        void onMoveTo(int x, int y, float width, String color, String action);

        void onLineTo(int x, int y, float width, String color, String action);

        void onScreen(JSONObject content);
    }

    public BitmapBoardView(Context context) {
        this(context, null);
    }

    public BitmapBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        paint.setStrokeCap(Paint.Cap.ROUND);
        // 抗锯齿
        paint.setAntiAlias(true);
        // 防抖动
        paint.setDither(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canAnimate || !canDraw) return false;

        int x = (int) event.getX();    //获取手指移动的x坐标
        int y = (int) event.getY();    //获取手指移动的y坐标

        if (x < 0 || y < 0) return true;

        LogUtils.i("坐标", "x:" + x + ",y:" + y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                content = content == null ? new JSONObject() : content;
                content.clear();
                points = points == null ? new JSONArray() : points;
                points.clear();
                JSONObject point = new JSONObject();
                content.put("width", paint.getStrokeWidth() * RoomApplication.getInstance().scale);
                content.put("color", currentColor);

                point.put("x", x * RoomApplication.getInstance().scale);
                point.put("y", y * RoomApplication.getInstance().scale);
                point.put("t", 0);
                currentTime = System.currentTimeMillis();
                points.add(point);

                if (listener != null) {
                    listener.onMoveTo(x, y, paint.getStrokeWidth(), currentColor, RoomConstant.START);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                drawLine(x, y);
                currentTime = System.currentTimeMillis() - currentTime;

                JSONObject movePoint = new JSONObject();
                movePoint.put("x", x * RoomApplication.getInstance().scale);
                movePoint.put("y", y * RoomApplication.getInstance().scale);
                movePoint.put("t", currentTime);

                points.add(movePoint);
                if (listener != null) {
                    listener.onLineTo(x, y, paint.getStrokeWidth(), currentColor, RoomConstant.MOVE);
                }
                break;
            case MotionEvent.ACTION_UP:
                content = content == null ? new JSONObject() : content;
                content.put("point", points);
                if (listener != null) {
                    listener.onScreen(content);
                    listener.onMoveTo(x, y, paint.getStrokeWidth(), currentColor, RoomConstant.END);
                }
                break;
        }
        return true;
    }

    /**
     * 画线
     */
    private void drawLine(float x, float y) {
        path.lineTo(x, y);
        bitmapCanvas.drawPath(path, paint);
        setImageBitmap(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null == bitmap) {
            bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(bitmap);
            bitmapCanvas.drawColor(Color.TRANSPARENT);
            bitmapCanvas.drawBitmap(bitmap, new Matrix(), paint);
            setImageBitmap(bitmap);
        }
        super.onDraw(canvas);
    }

    /**
     * 重置画布
     */
    public void reset() {
        path.reset();
        if (bitmapCanvas == null) return;
        bitmapCanvas.drawPath(path, paint);
        setImageBitmap(bitmap);
        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    /**
     * 改变画笔的大小，并且需要一个新的path
     */
    public void setSize(float size) {
        path = new Path();
        paint.setStrokeWidth(size);
    }

    /**
     * 被远程画线
     */
    public void setLine(float x, float y) {
        drawLine(x, y);
    }

    /**
     * 移动起始点
     */
    public void setMove(float x, float y) {
        path.moveTo(x, y);
    }

    /**
     * 设置画笔的颜色
     */
    public void setColor(String color) {
        currentColor = color;
        paint.setColor(Color.parseColor(color));
        path = new Path();
    }

    public void setColor(int color) {
        currentColor = changeColor(color);
        paint.setColor(color);
        path = new Path();
    }

    public String changeColor(int id) {
        StringBuilder stringBuffer = new StringBuilder();
        int color = RoomApplication.getInstance().getResources().getColor(id);

        stringBuffer.append("#");
        stringBuffer.append(Integer.toHexString(Color.alpha(color)));
        stringBuffer.append(Integer.toHexString(Color.red(color)));
        stringBuffer.append(Integer.toHexString(Color.green(color)));
        stringBuffer.append(Integer.toHexString(Color.blue(color)));
        return stringBuffer.toString();
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public void setCanAnimate(boolean canAnimate) {
        this.canAnimate = canAnimate;
    }
}
