package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.PathModel;
import com.uuabc.classroomlib.model.UserPathModel;

import java.util.ArrayList;

/**
 * 使用bitmap作为基础的画板控件
 * Created by user on 2018/3/28.
 */

public class ClassicsBoardView extends androidx.appcompat.widget.AppCompatImageView {
    private boolean canDraw;
    private boolean canAnimate;

    private String currentColor = RoomConstant.COLOR_RED;
    private float currentSize;

    private UserPathModel pathModelMe;
    private UserPathModel pathModelTea;
    private UserPathModel pathModel2;
    private UserPathModel pathModel3;
    private UserPathModel pathModel4;

    private ArrayList<UserPathModel> userPathList = new ArrayList<>();

    private int userId;

    private OnPathListener listener;

    private JSONObject content;
    private JSONArray points;
    private long currentTime;


    public void setOnPathListener(OnPathListener listener) {
        this.listener = listener;
    }

    public void setDrawPath(ArrayList<UserPathModel> pathList) {
        this.userPathList = pathList;
        invalidate();
    }

    public void drawLine() {
        invalidate();
    }


    /**
     * 滑动时的监听
     */
    public interface OnPathListener {
        void onMoveTo(int x, int y, float width, String color, String action);

        void onLineTo(int x, int y, float width, String color, String action);

        void onScreen(JSONObject content);
    }

    public ClassicsBoardView(Context context) {
        this(context, null);
    }

    public ClassicsBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        pathModelMe = new UserPathModel(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        userPathList.add(pathModelMe);

        pathModelTea = new UserPathModel();
        userPathList.add(pathModelTea);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canAnimate || !canDraw) return false;


        int x = (int) event.getX();
        int y = (int) event.getY();

        if (x < 0 || y < 0) return true;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pathModelMe.getPathModel().getPath().moveTo(x, y);
                content = content == null ? new JSONObject() : content;
                content.clear();
                points = points == null ? new JSONArray() : points;
                points.clear();
                JSONObject point = new JSONObject();
                content.put("width", pathModelMe.getPathModel().getPaint().getStrokeWidth() * RoomApplication.getInstance().scale);
                content.put("color", currentColor);

                point.put("x", x * RoomApplication.getInstance().scale);
                point.put("y", y * RoomApplication.getInstance().scale);
                point.put("t", 0);
                currentTime = System.currentTimeMillis();
                points.add(point);

                if (listener != null) {
                    listener.onMoveTo(x, y, pathModelMe.getPathModel().getPaint().getStrokeWidth(), currentColor, RoomConstant.START);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                pathModelMe.getPathModel().getPath().lineTo(x, y);
                invalidate();
//                topBoard.setDrawMePath(pathModelMe);
                currentTime = System.currentTimeMillis() - currentTime;

                JSONObject movePoint = new JSONObject();
                movePoint.put("x", x * RoomApplication.getInstance().scale);
                movePoint.put("y", y * RoomApplication.getInstance().scale);
                movePoint.put("t", currentTime);

                points.add(movePoint);
                if (listener != null) {
                    listener.onLineTo(x, y, pathModelMe.getPathModel().getPaint().getStrokeWidth(), currentColor, RoomConstant.MOVE);
                }
                break;
            case MotionEvent.ACTION_UP:

                content = content == null ? new JSONObject() : content;
                content.put("point", points);
                if (listener != null) {
                    listener.onScreen(content);
                    listener.onMoveTo(x, y, pathModelMe.getPathModel().getPaint().getStrokeWidth(), currentColor, RoomConstant.END);
                }
                break;
        }
        return true;
    }


    /**
     * 画线
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (pathModel4 != null) {
            for (PathModel pathModel : pathModel4.getPathList()) {
                canvas.drawPath(pathModel.getPath(), pathModel.getPaint());
            }
        }
        if (pathModel3 != null) {
            for (PathModel pathModel : pathModel3.getPathList()) {
                canvas.drawPath(pathModel.getPath(), pathModel.getPaint());
            }
        }
        if (pathModel2 != null) {
            for (PathModel pathModel : pathModel2.getPathList()) {
                canvas.drawPath(pathModel.getPath(), pathModel.getPaint());
            }
        }
        if (pathModelTea != null) {
            for (PathModel pathModel : pathModelTea.getPathList()) {
                canvas.drawPath(pathModel.getPath(), pathModel.getPaint());
            }
        }
        if (pathModelMe != null) {
            for (PathModel pathModel : pathModelMe.getPathList()) {
                if (pathModelMe.getPathList().size() == 1 && pathModel.getPaint().getStrokeWidth() != currentSize) {
                    pathModel.getPaint().setStrokeWidth(currentSize);
                }
                canvas.drawPath(pathModel.getPath(), pathModel.getPaint());
            }
        }

    }

    public void setCanAnimate(boolean canAnimate) {
        this.canAnimate = canAnimate;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    /**
     * 重置画布
     */
    public void reset() {
        invalidate();
    }

//    /**
//     * 改变画笔的大小，并且需要一个新的path
//     */
//    public void setSize(float size) {
//        path = new Path();
//        paint.setStrokeWidth(size);
//    }


    public int createPathModel(int userId) {
        int position = 0;
        if (pathModel2 == null) {
            pathModel2 = new UserPathModel(userId);
            userPathList.add(pathModel2);
            position = 2;
        } else if (pathModel3 == null) {
            pathModel3 = new UserPathModel(userId);
            userPathList.add(pathModel3);
            position = 3;
        } else if (pathModel4 == null) {
            pathModel4 = new UserPathModel(userId);
            userPathList.add(pathModel4);
            position = 4;
        }
        return position;
    }

    public void clearBoard() {
        for (UserPathModel userPathModel : userPathList) {
            userPathModel.clear(Color.parseColor(currentColor), currentSize);
        }
        invalidate();
    }

    public ArrayList<UserPathModel> getList() {
        return userPathList;
    }

    /**
     * 设置画笔的颜色
     */
    public void setColor(String color, int userId) {
        for (UserPathModel model : userPathList) {
            if (userId == model.getUserId()) {
                model.setNewPathColor(Color.parseColor(color), currentSize);
                break;
            }
        }
    }

    public void setColor(int color) {
        currentColor = changeColor(color);
        pathModelMe.setNewPathColor(color, currentSize);
    }

    public void setMyColor(String color) {
        currentColor = color;
        pathModelMe.setNewPathColor(Color.parseColor(color), currentSize);
//        pathModelMe.getPathModel().getPaint().setStrokeWidth(currentSize);
    }

    public void setMySize(float size) {
        currentSize = size;
        pathModelMe.setNewPathSize(size, Color.parseColor(currentColor));
//        pathModelMe.getPathModel().getPaint().setColor(Color.parseColor(currentColor));
    }

    public void initSize(float size) {
        currentSize = size;
        pathModelMe.getPathModel().getPaint().setStrokeWidth(size);
    }

    public String changeColor(int id) {
        StringBuilder stringBuffer = new StringBuilder();
        int color = getContext().getResources().getColor(id);

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

}
