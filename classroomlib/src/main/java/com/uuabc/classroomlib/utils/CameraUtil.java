package com.uuabc.classroomlib.utils;


import android.hardware.Camera;

import java.util.List;

public class CameraUtil {
    public static boolean checkCameraEnable() {
        boolean result = false;
        Camera camera = null;
        try {
            camera = Camera.open();
            if (camera == null) {
                boolean connected = false;
                for (int camIdx = 0; camIdx < Camera.getNumberOfCameras(); ++camIdx) {
                    try {
                        camera = Camera.open(camIdx);
                        connected = true;
                    } catch (RuntimeException ignored) {
                    }
                    if (connected) {
                        break;
                    }
                }
            }
            List<Camera.Size> supportedPreviewSizes;
            if (camera != null) {
                supportedPreviewSizes = camera.getParameters().getSupportedPreviewSizes();
                result = supportedPreviewSizes != null;
                /* Finally we are ready to start the preview */
                camera.startPreview();
            }
        } catch (Exception e) {
            result = false;
        } finally {
            if (camera != null) {
                camera.release();
            }
        }
        return result;
    }
}
