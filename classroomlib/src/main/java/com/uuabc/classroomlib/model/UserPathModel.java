package com.uuabc.classroomlib.model;


import java.util.ArrayList;

public class UserPathModel {
    private int userId;
    private ArrayList<PathModel> pathList = new ArrayList<>();

    public UserPathModel() {
        pathList.add(new PathModel());
    }

    public UserPathModel(int userId) {
        this();
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ArrayList<PathModel> getPathList() {
        return pathList;
    }

    public void setPathList(ArrayList<PathModel> pathList) {
        this.pathList = pathList;
    }

    public PathModel getPathModel() {
        return pathList.get(pathList.size() - 1);
    }

    public void setNewPath() {
        pathList.add(new PathModel());
    }

    public void setNewPathColor(int color, float size) {
        pathList.add(new PathModel(color, size));
    }

    public void setNewPathSize(float size, int color) {
        pathList.add(new PathModel(size, color));
//        pathList.get(pathList.size() - 1).getPaint().setStrokeWidth(size);
    }

    public void clear(int currentColor, float currentSize) {
        pathList.clear();
        pathList.add(new PathModel(currentColor, currentSize));
    }
}
