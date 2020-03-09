package com.uuabc.classroomlib.model.SocketModel;

import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.List;

/**
 * Created by user on 2018/4/19.
 */

public class MoveValueModel {
    /**
     * cameraItem : {"deviceId":"4c391198ab011411ce2043d009792f1bd04dadc41ed799192d1debd07a9494a4","label":"A4tech USB2.0 Camera (0ac8:3420)"}
     * microphoneItem : {"deviceId":"default","label":"默认 - 麦克风 (A4tech USB2.0 Camera (Audio))"}
     */
    private String type;
    private float x;
    private float y;
    private Object mouseDown;
    private float width;
    private String color;
    private Object isText;
    private List<List<Object>> points;

    private CameraItemModel cameraItem;
    private MicrophoneItemModel microphoneItem;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isMouseDown() {
        return ObjectUtil.getBoolean(mouseDown);
    }

    public void setMouseDown(Object mouseDown) {
        this.mouseDown = mouseDown;
    }


    public CameraItemModel getCameraItem() {
        return cameraItem;
    }

    public void setCameraItem(CameraItemModel cameraItem) {
        this.cameraItem = cameraItem;
    }

    public MicrophoneItemModel getMicrophoneItem() {
        return microphoneItem;
    }

    public void setMicrophoneItem(MicrophoneItemModel microphoneItem) {
        this.microphoneItem = microphoneItem;
    }

    public boolean isText() {
        return ObjectUtil.getBoolean(isText);
    }

    public void setIsText(Object isText) {
        this.isText = isText;
    }

    public List<List<Object>> getPoints() {
        return points;
    }

    public void setPoints(List<List<Object>> points) {
        this.points = points;
    }

    public static class CameraItemModel {
        /**
         * deviceId : 4c391198ab011411ce2043d009792f1bd04dadc41ed799192d1debd07a9494a4
         * label : A4tech USB2.0 Camera (0ac8:3420)
         */

        private String deviceId;
        private String label;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }

    public static class MicrophoneItemModel {
        /**
         * deviceId : default
         * label : 默认 - 麦克风 (A4tech USB2.0 Camera (Audio))
         */

        private String deviceId;
        private String label;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }
    }
}
