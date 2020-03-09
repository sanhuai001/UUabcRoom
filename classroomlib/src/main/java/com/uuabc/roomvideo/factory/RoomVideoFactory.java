package com.uuabc.roomvideo.factory;

import com.uuabc.roomvideo.model.RoomVideoType;
import com.uuabc.roomvideo.observer.VideoManagerObserver;
import com.uuabc.roomvideo.product.AgoraVideoManager;

public class RoomVideoFactory implements VideoFactory {
    private static AgoraVideoManager agoraVideoManager;

    @Override
    public VideoManagerObserver getVideoManager(RoomVideoType videoType) {
        return getAgoraInstance();
    }

    private static AgoraVideoManager getAgoraInstance() {
        if (agoraVideoManager == null) {
            synchronized (RoomVideoFactory.class) {
                if (agoraVideoManager == null) {
                    agoraVideoManager = new AgoraVideoManager();
                }
            }
        }
        return agoraVideoManager;
    }
}
