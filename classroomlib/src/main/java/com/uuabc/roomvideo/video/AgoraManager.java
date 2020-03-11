package com.uuabc.roomvideo.video;

import android.content.Context;
import android.graphics.PixelFormat;
import android.text.TextUtils;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.roomvideo.common.TextureVideoViewOutlineProvider;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;

public class AgoraManager {

    private static AgoraManager manger;
    private RtcEngine rtcEngine;

    public static AgoraManager getInstance() {
        if (manger == null) {
            synchronized (AgoraManager.class) {
                if (manger == null) {
                    manger = new AgoraManager();
                }
            }
        }
        return manger;
    }

    /**
     * 初始化
     */
    public void initializeAgoraEngine(Context context, String agoraAppId, IRtcEngineEventHandler rtcHandler) {
        try {
            rtcEngine = RtcEngine.create(context, agoraAppId, rtcHandler);
            LogUtils.i("getSdkVersion", RtcEngine.getSdkVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通知SDK现在开始使用外部视频源
     *
     * @param externalAudio 开启外部音频源
     * @param var2          采样率，可以有8k，16k，32k，44.1k和48kHz等模式
     * @param var3          外部音源的通道数，最多2个
     */
    public void setExternalAudioSource(boolean externalAudio, int var2, int var3) {
        if (rtcEngine == null) return;
        rtcEngine.setExternalAudioSource(externalAudio, var2, var3);
    }

    /**
     * 更新token
     *
     * @param token token
     */
    public void renewToken(String token) {
        if (rtcEngine == null) return;
        rtcEngine.renewToken(token);
    }


    /**
     * 禁用或开启麦克风
     */
    public void setEnableSpeakerphone(boolean isEnable) {
        if (rtcEngine == null) return;
        rtcEngine.setEnableSpeakerphone(isEnable);
    }

    /**
     * 持续的输出音频数据
     *
     * @param data      byte[] 类型的音频数据
     * @param timestamp 时间戳
     */
    public void pushExternalAudioFrame(byte[] data, long timestamp) {
        if (rtcEngine == null) return;
        rtcEngine.pushExternalAudioFrame(data, timestamp);
    }

    /**
     * 允许 SDK 定期向应用程序反馈当前谁在说话以及说话者的音量
     */
    public void enableAudioVolumeIndication(int interval) {
        if (rtcEngine == null) return;
        rtcEngine.enableAudioVolumeIndication(interval, 3);
    }

    /**
     * 设置日志路径
     */
    public void setLogFile(String logFilePath) {
        if (rtcEngine == null || TextUtils.isEmpty(logFilePath)) return;
        rtcEngine.setLogFile(logFilePath);
        rtcEngine.setLogFilter(0x0f);
    }

    /**
     * 通过 JSON 配置 SDK 提供技术预览或特别定制功能
     */
    public int setParameters(String parameters) {
        if (rtcEngine == null) return -1;
        return rtcEngine.setParameters(parameters);
    }

    /**
     * 设置成直播模式
     */
    public void setChannelProfileLive() {
        if (rtcEngine == null) return;
        rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
        rtcEngine.enableWebSdkInteroperability(true);
        rtcEngine.enableDualStreamMode(false);
    }

    /**
     * 设置用户角色
     *
     * @param isAnchor true为主播 false为观众
     */
    public void setClientRole(boolean isAnchor) {
        if (rtcEngine == null) return;
        rtcEngine.setClientRole(isAnchor ? Constants.CLIENT_ROLE_BROADCASTER : Constants.CLIENT_ROLE_AUDIENCE);
    }

    /**
     * 设置本地视频属性
     *
     * @param width  视频宽
     * @param height 视频高
     */
    public void setupVideoProfile(int width, int height) {
        if (rtcEngine == null) return;
        VideoEncoderConfiguration videoEncoderConfiguration;
        if (width == 0 || height == 0) {
            videoEncoderConfiguration = new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_240x240, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
        } else {
            videoEncoderConfiguration = new VideoEncoderConfiguration(width, height, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
        }
        rtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);
    }


    /**
     * 设置本地视频属性
     *
     * @param width  视频宽
     * @param height 视频高
     */
    public void setupBigVideoProfile(int width, int height) {
        if (rtcEngine == null) return;
        VideoEncoderConfiguration videoEncoderConfiguration;
        if (width == 0 || height == 0) {
            videoEncoderConfiguration = new VideoEncoderConfiguration(VideoEncoderConfiguration.VD_360x360, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
        } else {
            videoEncoderConfiguration = new VideoEncoderConfiguration(width, height, VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                    VideoEncoderConfiguration.STANDARD_BITRATE,
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT);
        }
        rtcEngine.setVideoEncoderConfiguration(videoEncoderConfiguration);
    }

    /**
     * 调节录音音量
     *
     * @param volume 录音信号音量，可在 0~400 范围内进行调节
     */
    public void adjustRecordingSignalVolume(int volume) {
        if (rtcEngine == null) return;
        rtcEngine.adjustRecordingSignalVolume(volume);
    }

    public void adjustPlaybackSignalVolume(int volume) {
        if (rtcEngine == null) return;
        rtcEngine.adjustPlaybackSignalVolume(volume);
    }

    public void setAudioProfile() {
        rtcEngine.setAudioProfile(Constants.AUDIO_PROFILE_SPEECH_STANDARD, Constants.AUDIO_SCENARIO_CHATROOM_GAMING);
    }

    /**
     * 启用视频
     */
    public void startPreview() {
        if (rtcEngine == null) return;
        rtcEngine.enableVideo();
        rtcEngine.enableLocalVideo(true);
        rtcEngine.startPreview();
    }

    /**
     * 加入频道
     */
    public void joinChannel(String agoraAppId, String channelName, int uid) {
        if (rtcEngine == null) return;
        rtcEngine.joinChannel(agoraAppId, channelName, null, uid);
    }

    /**
     * 离开频道并销毁声网sdk
     */
    public void leaveChannel() {
        if (rtcEngine != null) {
            rtcEngine.leaveChannel();
            RtcEngine.destroy();
        }
    }

    /**
     * 离开频道
     */
    public void leaveChannel2() {
        if (rtcEngine != null) {
            rtcEngine.leaveChannel();
        }
    }

    /**
     * 本地禁言
     */
    public void muteLocalAudioStream(boolean muted) {
        if (rtcEngine == null) return;
        rtcEngine.muteLocalAudioStream(muted);
    }

    /**
     * 远程禁言，不播放其他用户的音频
     */
    public void muteRemoteAudioStream(boolean muted, int userId) {
        if (rtcEngine == null) return;
        rtcEngine.muteRemoteAudioStream(userId, muted);
    }

    public void muteLocalVideoStream(boolean muted) {
        if (rtcEngine == null) return;
        rtcEngine.enableLocalVideo(!muted);
        rtcEngine.muteLocalVideoStream(muted);
        rtcEngine.muteLocalAudioStream(muted);
    }

    public void setupRemoteVideo(Context context, ViewGroup viewGroup, String uid, boolean isFillet) {
        if (rtcEngine == null || viewGroup == null || TextUtils.isEmpty(uid)) return;
        SurfaceView surfaceView = getSurfaceView(viewGroup);
        if (surfaceView == null) {
            surfaceView = RtcEngine.CreateRendererView(context);
            if (isFillet) {
                surfaceView.setOutlineProvider(new TextureVideoViewOutlineProvider(sp2px(context)));
                surfaceView.setClipToOutline(true);
            }
            surfaceView.setZOrderMediaOverlay(true);
            surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            viewGroup.addView(surfaceView);
        }
        surfaceView.setTag(uid);
        //设置远端视频显示属性
        rtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, ObjectUtil.getIntValue(uid)));
    }

    public void setupLocalVideo(Context context, ViewGroup viewGroup, String uid) {
        if (rtcEngine == null || viewGroup == null || TextUtils.isEmpty(uid)) return;
        SurfaceView surfaceView = getSurfaceView(viewGroup);
        if (surfaceView == null) {
            surfaceView = RtcEngine.CreateRendererView(context);
            surfaceView.setOutlineProvider(new TextureVideoViewOutlineProvider(sp2px(context)));
            surfaceView.setClipToOutline(true);
            surfaceView.setZOrderMediaOverlay(true);
            surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
            viewGroup.addView(surfaceView);
        }
        surfaceView.setTag(uid);
        rtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, ObjectUtil.getIntValue(uid)));
    }

    public void onRemoveView(ViewGroup viewGroup) {
        if (viewGroup == null) return;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof SurfaceView) {
                viewGroup.removeViewAt(i);
            }
        }
    }

    private SurfaceView getSurfaceView(ViewGroup viewGroup) {
        if (viewGroup == null) return null;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i) instanceof SurfaceView) {
                return (SurfaceView) viewGroup.getChildAt(i);
            }
        }
        return null;
    }

    private static int sp2px(Context context) {
        final float fontScale = context.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) ((float) 15 * fontScale + 0.5f);
    }
}
