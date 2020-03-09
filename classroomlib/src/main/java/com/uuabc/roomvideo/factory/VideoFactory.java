package com.uuabc.roomvideo.factory;

import com.uuabc.roomvideo.model.RoomVideoType;
import com.uuabc.roomvideo.observer.VideoManagerObserver;

public interface VideoFactory {
    VideoManagerObserver getVideoManager(RoomVideoType videoType);
}
