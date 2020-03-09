package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class DrawTextModel {
    private Object y;
    private Object x;
    private String color;
    private int fontSize;
    private String text;
    private Object height;
    private Object width;
    private String type;

    public int getY() {
        return ObjectUtil.getIntValue(y);
    }

    public void setY(Object y) {
        this.y = y;
    }

    public int getX() {
        return ObjectUtil.getIntValue(x);
    }

    public void setX(Object x) {
        this.x = x;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getHeight() {
        return ObjectUtil.getIntValue(height);
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return ObjectUtil.getIntValue(width);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
