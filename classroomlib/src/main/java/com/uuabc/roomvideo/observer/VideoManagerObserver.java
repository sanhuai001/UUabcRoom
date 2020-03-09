package com.uuabc.roomvideo.observer;

import android.content.Context;
import android.view.ViewGroup;

import com.uuabc.roomvideo.model.EnterRoomModel;
import com.uuabc.roomvideo.model.RoomVideoType;

public interface VideoManagerObserver {
    /**
     * SDK初始化
     *
     * @param context  上下文
     * @param appId    appId
     * @param observer sdk回调监听对象
     */
    void initSdk(Context context, String appId, RoomManagerObserver observer);

    /**
     * 加入声网通道
     *
     * @param model 具体需传的数，参考EnterRoomModel注释
     */
    void joinRoom(EnterRoomModel model);

    /**
     * 设置视频宽高
     *
     * @param width  宽度
     * @param height 高度
     */
    void setVideoProfile(int width, int height);

    /**
     * 设置视频宽高
     *
     * @param width  宽度
     * @param height 高度
     */
    void setBigVideoProfile(int width, int height);

    /**
     * 离开房间
     */
    void leaveRoom();

    /**
     * 声网离开房间，不销毁SDK
     */
    void leaveRoom2();

    /**
     * 对自己禁言|取消禁言
     *
     * @param muted true 禁言 false 检出禁言
     */
    void muteLocalAudioStream(boolean muted);

    /**
     * 对某个用户禁言|取消禁言
     *
     * @param muted true 禁言 false 检出禁言
     * @param uid   用户id
     */
    void muteRemoteAudioStream(boolean muted, String uid);

    /**
     * 禁止上传本地视频|取消禁上传本地视频
     *
     * @param muted true 禁言 false 检出禁言
     */
    void muteLocalVideoStream(boolean muted);


    /**
     * 声网，talkcloud切换
     *
     * @param type AGORA：声网  TALKCLOUD：talkCloud
     */
    void switchTo(RoomVideoType type);

    /**
     * 往容器viewGroup中添加视频流播放器，播放远程uid用户视频画面
     *
     * @param context   上下文
     * @param viewGroup 需要添加视频播放器的容器
     * @param uid       用户id
     * @param isFillet  是否为圆角
     */
    void setupRemoteVideo(Context context, ViewGroup viewGroup, String uid, boolean isFillet);

    /**
     * 往容器viewGroup中添加视频流播放器，根据uid播放本地用户视频画面
     *
     * @param context   上下文
     * @param viewGroup 需要添加视频播放器的容器
     * @param uid       用户id
     */
    void setupLocalVideo(Context context, ViewGroup viewGroup, String uid);

    /**
     * 移除容器中的view
     *
     * @param viewGroup 容器
     */
    void onRemoveView(ViewGroup viewGroup);

    /**
     * 设置用户角色  声网专用
     *
     * @param isAnchor true为主播 false为观众
     */
    void setClientRole(boolean isAnchor);

    /**
     * 设置成直播模式 声网专用
     */
    void setChannelProfileLive();

    /**
     * 启用视频 声网专用
     */
    void startPreview();

    /**
     * 设置日志路径
     *
     * @param logFilePath 日志存放路径
     */
    void setLogFile(String logFilePath);

    /**
     * 启用或禁用音量提示,定期向应用程序反馈当前谁在说话以及说话者的音量
     *
     * @param interval <=0: 禁用音量提示功能
     *                 >0: 提示间隔，单位为毫秒。建议设置到大于 200 毫秒
     */
    void enableAudioVolumeIndication(int interval);


    /**
     * 通知SDK现在开始使用外部视频源
     *
     * @param externalAudio 开启外部音频源
     * @param var2          采样率，可以有8k，16k，32k，44.1k和48kHz等模式
     * @param var3          外部音源的通道数，最多2个
     */
    void setExternalAudioSource(boolean externalAudio, int var2, int var3);

    /**
     * 持续的输出音频数据
     *
     * @param data      byte[] 类型的音频数据
     * @param timestamp 时间戳
     */
    void pushExternalAudioFrame(byte[] data, long timestamp);

    /**
     * 通过 JSON 配置 SDK 提供技术预览或特别定制功能
     */
    int setParameters(String parameters);

    /**
     * 更新token
     *
     * @param token token
     */
    void renewToken(String token);


    /**
     * 禁用或开启麦克风
     *
     * @param isEnable 是否enable
     */
    void setEnableSpeakerphone(boolean isEnable);

    /**
     * 调节录音音量
     *
     * @param volume 录音信号音量，可在 0~400 范围内进行调节
     */
    void adjustRecordingSignalVolume(int volume);

    /**
     * 调节播放音量
     *
     * @param volume 播放信号音量，可在 0~400 范围内进行调节
     */
    void adjustPlaybackSignalVolume(int volume);
}
