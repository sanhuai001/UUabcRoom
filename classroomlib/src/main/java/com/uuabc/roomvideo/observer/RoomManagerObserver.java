package com.uuabc.roomvideo.observer;

import java.util.HashMap;

public interface RoomManagerObserver {
    /**
     * 成功进入房间
     */
    void onRoomJoined();

    /**
     * 用户加入房间
     *
     * @param uid 加入房间的用户Id
     */
    void onUserJoined(String uid);

    /**
     * 用户离开房间
     *
     * @param uid 离开房间的用户Id
     */
    void onUserLeaved(String uid);

    /**
     * 自己被踢出房间
     */
    void onKickedout();

    /**
     * 错误回调
     *
     * @param errMsg 错误信息
     */
    void onError(String errMsg);

    /**
     * 离开房间成功
     */
    void onLeaveChannelSuccess();

    /**
     * 通话中每个用户的网络上下行 last mile 质量报告回调
     *
     * @param uid       用户 ID
     * @param txQuality 该用户的上行网络质量
     * @param rxQuality 该用户的下行网络质量
     */
    void onNetworkQuality(int uid, int txQuality, int rxQuality);

    /**
     * 频道内谁正在说话以及说话者音量的回调
     *
     * @param speakers    每个说话者的用户 ID 和音量信息的数组：AudioVolumeInfo
     * @param totalVolume （混音后的）总音量（0~255）
     */
    void onAudioVolumeIndication(HashMap<Integer, Integer> speakers, int totalVolume);


    /**
     * 更新token
     */
    void onRenewToken();
}
