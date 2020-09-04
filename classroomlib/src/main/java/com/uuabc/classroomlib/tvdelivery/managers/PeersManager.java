package com.uuabc.classroomlib.tvdelivery.managers;

import androidx.appcompat.app.AppCompatActivity;

import com.neovisionaries.ws.client.WebSocket;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.classroom.MonitorRoomActivity;
import com.uuabc.classroomlib.tvdelivery.listeners.CustomWebSocketListener;
import com.uuabc.classroomlib.tvdelivery.observers.CustomPeerConnectionObserver;
import com.uuabc.classroomlib.tvdelivery.ui.RemoteParticipant;

import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SoftwareVideoDecoderFactory;
import org.webrtc.SoftwareVideoEncoderFactory;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class PeersManager {
    private PeerConnectionFactory peerConnectionFactory;
    private CustomWebSocketListener webSocketAdapter;
    private WebSocket webSocket;
    private AppCompatActivity activity;

    public PeersManager(AppCompatActivity activity) {
        this.activity = activity;
    }

    public PeerConnectionFactory getPeerConnectionFactory() {
        return peerConnectionFactory;
    }

    public CustomWebSocketListener getWebSocketAdapter() {
        return webSocketAdapter;
    }

    public void setWebSocketAdapter(CustomWebSocketListener webSocketAdapter) {
        this.webSocketAdapter = webSocketAdapter;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public void start() {
        PeerConnectionFactory.InitializationOptions.Builder optionsBuilder =
                PeerConnectionFactory.InitializationOptions.builder(activity.getApplicationContext());
        optionsBuilder.setEnableInternalTracer(true);
        PeerConnectionFactory.InitializationOptions opt = optionsBuilder.createInitializationOptions();
        PeerConnectionFactory.initialize(opt);
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();

        final VideoEncoderFactory encoderFactory;
        final VideoDecoderFactory decoderFactory;
        encoderFactory = new SoftwareVideoEncoderFactory();
        decoderFactory = new SoftwareVideoDecoderFactory();

        peerConnectionFactory = PeerConnectionFactory.builder()
                .setVideoEncoderFactory(encoderFactory)
                .setVideoDecoderFactory(decoderFactory)
                .setOptions(options)
                .createPeerConnectionFactory();
    }

    public void createRemotePeerConnection(RemoteParticipant remoteParticipant) {
        LinkedList<PeerConnection.IceServer> iceServers = new LinkedList<>();

        if (RoomApplication.getInstance().iceServers == null) {
            iceServers.add(PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer());
        } else {
            for (int i = 0; i < RoomApplication.getInstance().iceServers.size(); i++) {
                if (RoomApplication.getInstance().iceServers.get(i).startsWith("stun:")) {
                    iceServers.add(PeerConnection.IceServer.builder(RoomApplication.getInstance().iceServers.get(i)).createIceServer());
                    continue;
                }

                String[] iceTurn = RoomApplication.getInstance().iceServers.get(i).split(",");
                if (iceTurn.length == 3)
                    iceServers.add(new PeerConnection.IceServer(iceTurn[0], iceTurn[1], iceTurn[2]));
            }
        }

        MediaConstraints sdpConstraints = new MediaConstraints();
        sdpConstraints.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"));
        sdpConstraints.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"));
        sdpConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));

        PeerConnection remotePeer = peerConnectionFactory.createPeerConnection(iceServers, new CustomPeerConnectionObserver("remotePeerCreation", remoteParticipant) {
            @Override
            public void onIceCandidate(IceCandidate iceCandidate) {
                super.onIceCandidate(iceCandidate);
                Map<String, Object> iceCandidateParams = new HashMap<>();
                iceCandidateParams.put("sdpMid", iceCandidate.sdpMid);
                iceCandidateParams.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
                iceCandidateParams.put("candidate", iceCandidate.sdp);
                iceCandidateParams.put("endpointName", getRemoteParticipant().getId());
                webSocketAdapter.sendJson(webSocket, "onIceCandidate", iceCandidateParams);
            }

            @Override
            public void onAddTrack(RtpReceiver rtpReceiver, MediaStream[] mediaStreams) {
                super.onAddTrack(rtpReceiver, mediaStreams);
                if (activity instanceof MonitorRoomActivity && mediaStreams.length > 0) {
                    ((MonitorRoomActivity) activity).gotRemoteStream(mediaStreams[0], getRemoteParticipant());
                }
            }

            @Override
            public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
                super.onIceConnectionChange(iceConnectionState);
                if (activity instanceof MonitorRoomActivity) {
                    ((MonitorRoomActivity) activity).onIceConnectionChange(iceConnectionState);
                }
            }
        });

        remoteParticipant.setPeerConnection(remotePeer);
    }

    public void hangup() {
        if (webSocketAdapter != null) {
            webSocketAdapter.canclePingTimer();
            webSocketAdapter.sendJson(webSocket, "leaveRoom", new HashMap<>());
            webSocket.disconnect();
            Map<String, RemoteParticipant> participants = webSocketAdapter.getParticipants();
            for (RemoteParticipant remoteParticipant : participants.values()) {
                remoteParticipant.getPeerConnection().close();
            }
        }
    }
}
