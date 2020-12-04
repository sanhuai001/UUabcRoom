package com.uuabc.roomvideo.product;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.roomvideo.model.EnterRoomModel;
import com.uuabc.roomvideo.model.RoomVideoType;
import com.uuabc.roomvideo.observer.RoomManagerObserver;
import com.uuabc.roomvideo.observer.VideoManagerObserver;
import com.uuabc.roomvideo.video.AgoraManager;

import java.util.HashMap;

import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;

public class AgoraVideoManager implements VideoManagerObserver {
    private RoomManagerObserver observer;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, Integer> speakerMap = new HashMap<>();

    @Override
    public void initSdk(Context context, String appId, RoomManagerObserver observer) {
        this.observer = observer;
        AgoraManager.getInstance().initializeAgoraEngine(context, appId, rtcHandler);
    }

    @Override
    public void joinRoom(EnterRoomModel model) {
        if (model == null) return;
        AgoraManager.getInstance().joinChannel(model.getAppId(), model.getRoomId(), ObjectUtil.getIntValue(model.getUid()));
    }

    @Override
    public void setVideoProfile(int width, int height) {
        AgoraManager.getInstance().setupVideoProfile(width, height);
    }

    @Override
    public void setBigVideoProfile(int width, int height) {
        AgoraManager.getInstance().setupBigVideoProfile(width, height);
    }

    @Override
    public void leaveRoom() {
        AgoraManager.getInstance().leaveChannel();
    }

    @Override
    public void leaveRoom2() {
        AgoraManager.getInstance().leaveChannel2();
    }

    @Override
    public void muteLocalAudioStream(boolean muted) {
        AgoraManager.getInstance().muteLocalAudioStream(muted);
    }

    @Override
    public void muteRemoteAudioStream(boolean muted, String uid) {
        AgoraManager.getInstance().muteRemoteAudioStream(muted, ObjectUtil.getIntValue(uid));
    }

    @Override
    public void muteLocalVideoStream(boolean muted) {
        AgoraManager.getInstance().muteLocalVideoStream(muted);
    }

    @Override
    public void switchTo(RoomVideoType type) {
        if (type != RoomVideoType.AGORA) {
            AgoraManager.getInstance().leaveChannel();
        }
    }

    @Override
    public void setupRemoteVideo(Context context, ViewGroup viewGroup, String uid, boolean isFillet) {
        AgoraManager.getInstance().setupRemoteVideo(context, viewGroup, uid, isFillet);
    }

    @Override
    public void setupLocalVideo(Context context, ViewGroup viewGroup, String uid) {
        AgoraManager.getInstance().setupLocalVideo(context, viewGroup, uid);
    }

    @Override
    public void onRemoveView(ViewGroup viewGroup) {
        AgoraManager.getInstance().onRemoveView(viewGroup);
    }

    @Override
    public void setClientRole(boolean isAnchor) {
        AgoraManager.getInstance().setClientRole(isAnchor);
    }

    @Override
    public void setChannelProfileLive() {
        AgoraManager.getInstance().setChannelProfileLive();
    }

    @Override
    public void startPreview() {
        AgoraManager.getInstance().startPreview();
    }

    @Override
    public void setLogFile(String logFilePath) {
        AgoraManager.getInstance().setLogFile(logFilePath);
    }

    @Override
    public void enableAudioVolumeIndication(int interval) {
        AgoraManager.getInstance().enableAudioVolumeIndication(interval);
    }

    @Override
    public void setExternalAudioSource(boolean externalAudio, int var2, int var3) {
        AgoraManager.getInstance().setExternalAudioSource(externalAudio, var2, var3);
    }

    @Override
    public void pushExternalAudioFrame(byte[] data, long timestamp) {
        AgoraManager.getInstance().pushExternalAudioFrame(data, timestamp);
    }

    @Override
    public int setParameters(String parameters) {
        return AgoraManager.getInstance().setParameters(parameters);
    }

    @Override
    public void renewToken(String token) {
        AgoraManager.getInstance().renewToken(token);
    }

    @Override
    public void setEnableSpeakerphone(boolean isEnable) {
        AgoraManager.getInstance().setEnableSpeakerphone(isEnable);
    }

    @Override
    public void adjustRecordingSignalVolume(int volume) {
        AgoraManager.getInstance().adjustRecordingSignalVolume(volume);
    }

    @Override
    public void adjustPlaybackSignalVolume(int volume) {
        AgoraManager.getInstance().adjustPlaybackSignalVolume(volume);
    }

    /**
     * 声网的回调
     */
    private final IRtcEngineEventHandler rtcHandler = new IRtcEngineEventHandler() { // Tutorial Step 1

        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            if (observer != null) {
                observer.onRoomJoined();
            }
        }

        @Override
        public void onRemoteVideoStateChanged(final int uid, int width, int height, int elapsed) { // Tutorial Step 5
            if (observer != null) {
                observer.onUserJoined(String.valueOf(uid));
            }
        }

        @Override
        public void onUserOffline(int uid, int reason) { // Tutorial Step 7
            if (observer != null) {
                observer.onUserLeaved(String.valueOf(uid));
            }
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            LogUtils.i("IRtcEngine", "leaveChannel success");
            if (observer != null) {
                observer.onLeaveChannelSuccess();
            }
        }

        @Override
        public void onError(int err) {
            super.onError(err);
            LogUtils.i("IRtcEngine", "errorcode = " + err);
            if (err == ErrorCode.ERR_LEAVE_CHANNEL_REJECTED) return;
            if (observer != null) {
                observer.onError("errorcode = " + err);
            }
        }

        @Override
        public void onStreamMessageError(int uid, int streamId, int error, int missed, int cached) {
            super.onStreamMessageError(uid, streamId, error, missed, cached);
            LogUtils.i("IRtcEngine", "errorcode = " + error);
        }

        @Override
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
            if (observer != null && speakers != null) {
                speakerMap.clear();
                for (AudioVolumeInfo speaker : speakers) {
                    speakerMap.put(speaker.uid, speaker.volume);
                }
                observer.onAudioVolumeIndication(speakerMap, totalVolume);
            }
        }

        @Override
        public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
            if (observer != null) {
                observer.onNetworkQuality(uid, txQuality, rxQuality);
            }
        }

        @Override
        public void onTokenPrivilegeWillExpire(String token) {
            if (observer != null) {
                observer.onRenewToken();
            }
        }

        @Override
        public void onConnectionStateChanged(int state, int reason) {
            if (observer != null && state == Constants.CONNECTION_CHANGED_TOKEN_EXPIRED) {
                observer.onRenewToken();
            }
        }
    };
}
