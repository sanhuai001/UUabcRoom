package com.uuabc.classroomlib.model;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

public class PathModel {
    private Path path;
    private Paint paint;


    public PathModel() {
        path = new Path();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        // 抗锯齿
        paint.setAntiAlias(true);
        // 防抖动
        paint.setDither(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }


    public PathModel(int color, float size) {
        this();
        paint.setColor(color);
        paint.setStrokeWidth(size);
    }

    public PathModel(float size, int color) {
        this();
        paint.setColor(color);
        paint.setStrokeWidth(size);
    }

    public Path getPath() {
        return path;
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }
}
