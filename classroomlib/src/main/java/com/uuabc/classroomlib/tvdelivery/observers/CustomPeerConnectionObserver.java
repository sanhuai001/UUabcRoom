package com.uuabc.classroomlib.tvdelivery.observers;

import com.blankj.utilcode.util.LogUtils;
import com.uuabc.classroomlib.tvdelivery.ui.RemoteParticipant;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.RtpReceiver;

import java.util.Arrays;

public class CustomPeerConnectionObserver implements PeerConnection.Observer {
    private String logTag = "SteamTag";
    private RemoteParticipant remoteParticipant;

    protected CustomPeerConnectionObserver(String logTag) {
        this.logTag = this.logTag + " " + logTag;
    }

    protected CustomPeerConnectionObserver(String logTag, RemoteParticipant remoteParticipant) {
        this.logTag = this.logTag + " " + logTag;
        this.remoteParticipant = remoteParticipant;
    }

    protected RemoteParticipant getRemoteParticipant() {
        return remoteParticipant;
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
        LogUtils.d(logTag, "onSignalingChange() called with: signalingState = [" + signalingState + "]");
    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        LogUtils.d(logTag, "onIceConnectionChange() called with: iceConnectionState = [" + iceConnectionState + "]");
    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {
        LogUtils.d(logTag, "onIceConnectionReceivingChange() called with: b = [" + b + "]");
    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
        LogUtils.d(logTag, "onIceGatheringChange() called with: iceGatheringState = [" + iceGatheringState + "]");
    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        LogUtils.d(logTag, "onIceCandidate() called with: iceCandidate = [" + iceCandidate + "]");
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
        LogUtils.d(logTag, "onIceCandidatesRemoved() called with: iceCandidates = [" + Arrays.toString(iceCandidates) + "]");
    }

    @Override
    public void onAddStream(MediaStream mediaStream) {
        LogUtils.d(logTag, "onAddStream() called with: mediaStream = [" + mediaStream + "]");
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {
        LogUtils.d(logTag, "onRemoveStream() called with: mediaStream = [" + mediaStream + "]");
    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        LogUtils.d(logTag, "onDataChannel() called with: dataChannel = [" + dataChannel + "]");
    }

    @Override
    public void onRenegotiationNeeded() {
        LogUtils.d(logTag, "onRenegotiationNeeded() called");
    }

    @Override
    public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
        LogUtils.d(logTag, "onAddTrack() called with: mediaStreams = [" + Arrays.toString(mediaStreams) + "]");
    }
}
