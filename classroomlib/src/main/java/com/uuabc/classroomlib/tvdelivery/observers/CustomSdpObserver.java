package com.uuabc.classroomlib.tvdelivery.observers;

import com.blankj.utilcode.util.LogUtils;
import com.uuabc.classroomlib.tvdelivery.ui.RemoteParticipant;

import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

public class CustomSdpObserver implements SdpObserver {
    private String tag = this.getClass().getCanonicalName();
    private RemoteParticipant remoteParticipant;

    public CustomSdpObserver(String logTag) {
        this.tag = this.tag + " " + logTag;
    }

    protected CustomSdpObserver(String logTag, RemoteParticipant remoteParticipant) {
        this.tag = this.tag + " " + logTag;
        this.remoteParticipant = remoteParticipant;
    }

    protected RemoteParticipant getRemoteParticipant() {
        return remoteParticipant;
    }

    @Override
    public void onCreateSuccess(SessionDescription sessionDescription) {
        LogUtils.d(tag, "onCreateSuccess() called with: sessionDescription = [" + sessionDescription + "]");
    }

    @Override
    public void onSetSuccess() {
        LogUtils.d(tag, "onSetSuccess() called");
    }

    @Override
    public void onCreateFailure(String s) {
        LogUtils.d(tag, "onCreateFailure() called with: s = [" + s + "]");
    }

    @Override
    public void onSetFailure(String s) {
        LogUtils.d(tag, "onSetFailure() called with: s = [" + s + "]");
    }

}