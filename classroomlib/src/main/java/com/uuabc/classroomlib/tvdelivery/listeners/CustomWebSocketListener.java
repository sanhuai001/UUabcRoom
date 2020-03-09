package com.uuabc.classroomlib.tvdelivery.listeners;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.neovisionaries.ws.client.ThreadType;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketListener;
import com.neovisionaries.ws.client.WebSocketState;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.classroom.MonitorRoomActivity;
import com.uuabc.classroomlib.common.RxTimer;
import com.uuabc.classroomlib.tvdelivery.JSONConstants;
import com.uuabc.classroomlib.tvdelivery.managers.PeersManager;
import com.uuabc.classroomlib.tvdelivery.observers.CustomSdpObserver;
import com.uuabc.classroomlib.tvdelivery.ui.RemoteParticipant;
import com.uuabc.classroomlib.utils.ObjectUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.RendererCommon;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class CustomWebSocketListener implements WebSocketListener {
    private static final String TAG = "CustomWebSocketAdapter";
    private static final String JSON_RPCVERSION = "2.0";

    private AppCompatActivity roomActivity;
    private int id;
    public String userId;
    private LinearLayout views_container;
    private Map<String, RemoteParticipant> participants;
    private String remoteParticipantId;
    private PeersManager peersManager;
    private RxTimer rxTimer = new RxTimer();

    public CustomWebSocketListener(AppCompatActivity roomActivity, PeersManager peersManager, LinearLayout views_container) {
        this.roomActivity = roomActivity;
        this.peersManager = peersManager;
        this.id = 0;
        this.views_container = views_container;
        this.participants = new HashMap<>();
    }

    public Map<String, RemoteParticipant> getParticipants() {
        return participants;
    }

    public int getId() {
        return id;
    }

    private void updateId() {
        id++;
    }

    @Override
    public void onStateChanged(WebSocket websocket, WebSocketState newState) {
        LogUtils.i(TAG, "State changed: " + newState.name());
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
        LogUtils.i(TAG, "Connected");
        pingMessageHandler(websocket);

        Map<String, Object> joinRoomParams = new HashMap<>();
        joinRoomParams.put("platform", "Android PHONE");
        joinRoomParams.put(JSONConstants.METADATA, "");
        joinRoomParams.put("secret", (roomActivity instanceof MonitorRoomActivity) ? ObjectUtil.getString(RoomApplication.getInstance().secret) : "");
        joinRoomParams.put("recorder", false);
        joinRoomParams.put("session", RoomApplication.getInstance().channelId);
        joinRoomParams.put("token", RoomApplication.getInstance().token);
        sendJson(websocket, "joinRoom", joinRoomParams);
    }

    private void pingMessageHandler(final WebSocket webSocket) {
        sendPing(webSocket);
        rxTimer.interval(3000, number -> {
            sendPing(webSocket);
        });
    }

    private void sendPing(final WebSocket webSocket) {
        Map<String, Object> pingParams = new HashMap<>();
        if (id == 0) {
            pingParams.put("interval", "3000");
        }
        sendJson(webSocket, "ping", pingParams);
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException cause) {
        LogUtils.i(TAG, "Connect error: " + cause);
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
        LogUtils.i(TAG, "Disconnected " + serverCloseFrame.getCloseReason() + " " + clientCloseFrame.getCloseReason() + " " + closedByServer);
    }

    @Override
    public void onFrame(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Frame");
    }

    @Override
    public void onContinuationFrame(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Continuation Frame");
    }

    @Override
    public void onTextFrame(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Text Frame");
    }

    @Override
    public void onBinaryFrame(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Binary Frame");
    }

    @Override
    public void onCloseFrame(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Close Frame");
    }

    @Override
    public void onPingFrame(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Ping Frame");
    }

    @Override
    public void onPongFrame(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Pong Frame");
    }

    @Override
    public void onTextMessage(final WebSocket websocket, String text) throws Exception {
        LogUtils.i("reciveMessage", text);
        JSONObject json = new JSONObject(text);
        if (json.has(JSONConstants.RESULT)) {
            handleResult(websocket, json);
        } else {
            handleMethod(websocket, json);
        }
    }

    private void handleResult(final WebSocket webSocket, JSONObject json) throws JSONException {
        JSONObject result = new JSONObject(json.getString(JSONConstants.RESULT));
        if (result.has(JSONConstants.SDP_ANSWER)) {
            saveAnswer(result);
        } else if (result.has(JSONConstants.SESSION_ID)) {
            if (result.has(JSONConstants.VALUE)) {
                if (result.getJSONArray(JSONConstants.VALUE).length() > 0) {
                    addParticipantsAlreadyInRoom(result, webSocket);
                }
                this.userId = result.getString(JSONConstants.ID);
            }
        } else if (result.has(JSONConstants.VALUE)) {
            LogUtils.i(TAG, "pong");
        } else {
            LogUtils.e(TAG, "Unrecognized " + result);
        }
    }

    private void addParticipantsAlreadyInRoom(JSONObject result, final WebSocket webSocket) throws JSONException {
        LogUtils.i("openvide", "AlreadyInRoom:" + result.toString());
        for (int i = 0; i < result.getJSONArray(JSONConstants.VALUE).length(); i++) {
            JSONObject jsonObject = result.getJSONArray(JSONConstants.VALUE).getJSONObject(i);
            if (!jsonObject.has("streams")
                    || !jsonObject.has("metadata")
                    || jsonObject.get("metadata") == null) {
                continue;
            }

            JSONArray jsonArray = jsonObject.getJSONArray("streams");
            if (jsonArray.length() == 0) {
                continue;
            }

            JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);

            String senderID = jsonObject1.getString("id");
            remoteParticipantId = jsonObject.getString(JSONConstants.ID);
            final RemoteParticipant remoteParticipant = new RemoteParticipant();
            if (views_container != null)
                createVideoView(remoteParticipant);
            remoteParticipant.setId(remoteParticipantId);
            participants.put(remoteParticipantId, remoteParticipant);
            peersManager.createRemotePeerConnection(remoteParticipant);

            MediaConstraints pcConstraints = new MediaConstraints();
            pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
            pcConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));
            pcConstraints.optional.add(new MediaConstraints.KeyValuePair("DtlsSrtpKeyAgreement", "true"));
            remoteParticipant.getPeerConnection().createOffer(new CustomSdpObserver("remoteCreateOffer") {
                @Override
                public void onCreateSuccess(SessionDescription sessionDescription) {
                    super.onCreateSuccess(sessionDescription);
                    remoteParticipant.getPeerConnection().setLocalDescription(new CustomSdpObserver("remoteSetLocalDesc"), sessionDescription);
                    Map<String, Object> remoteOfferParams = new HashMap<>();
                    remoteOfferParams.put("sdpOffer", sessionDescription.description);
                    remoteOfferParams.put("sender", senderID);
                    sendJson(webSocket, "receiveVideoFrom", remoteOfferParams);
                }
            }, pcConstraints);
        }
    }

    private void createVideoView(final RemoteParticipant remoteParticipant) {
        Handler mainHandler = new Handler(roomActivity.getMainLooper());
        Runnable myRunnable = () -> {
            views_container.removeAllViews();

            @SuppressLint("InflateParams")
            View rowView = roomActivity.getLayoutInflater().inflate(R.layout.peer_video, null);
            int rowId = View.generateViewId();
            rowView.setId(rowId);
            views_container.addView(rowView);

            ViewGroup.LayoutParams params = rowView.getLayoutParams();
            params.width = views_container.getWidth();
            params.height = views_container.getHeight();
            rowView.requestLayout();

            SurfaceViewRenderer videoView = rowView.findViewById(R.id.local_gl_surface_view);

            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) videoView.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            remoteParticipant.setVideoView(videoView);
            videoView.setMirror(false);
            EglBase rootEglBase = EglBase.create();
            videoView.init(rootEglBase.getEglBaseContext(), new RendererCommon.RendererEvents() {

                @Override
                public void onFirstFrameRendered() {
                    if (roomActivity instanceof MonitorRoomActivity)
                        ((MonitorRoomActivity) roomActivity).onFirstFrameRendered();
                }

                @Override
                public void onFrameResolutionChanged(int i, int i1, int i2) {

                }
            });
            videoView.setZOrderMediaOverlay(true);
            remoteParticipant.setView(rowView);
        };

        mainHandler.post(myRunnable);
    }

    private void handleMethod(final WebSocket webSocket, JSONObject json) throws JSONException {
        if (!json.has(JSONConstants.PARAMS)) {
            LogUtils.e(TAG, "No params" + json);
        } else {
            final JSONObject params = new JSONObject(json.getString(JSONConstants.PARAMS));
            String method = json.getString(JSONConstants.METHOD);
            switch (method) {
                case JSONConstants.ICE_CANDIDATE:
                    iceCandidateMethod(params);
                    break;
                case JSONConstants.PARTICIPANT_JOINED:
                    break;
                case JSONConstants.PARTICIPANT_PUBLISHED:
                    break;
                case JSONConstants.PARTICIPANT_LEFT:
                    break;
                default:
                    throw new JSONException("Can't understand method: " + method);
            }
        }
    }

    private void iceCandidateMethod(JSONObject params) throws JSONException {
        if (params.getString("endpointName").equals(userId)) {
            saveIceCandidate(params, null);
        } else {
            saveIceCandidate(params, params.getString("endpointName"));
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) {
        LogUtils.i(TAG, "Binary Message");
    }

    @Override
    public void onSendingFrame(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Sending Frame");
    }

    @Override
    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Frame sent");
    }

    @Override
    public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) {
        LogUtils.i(TAG, "Frame unsent");
    }

    @Override
    public void onThreadCreated(WebSocket websocket, ThreadType threadType, Thread thread) {
        LogUtils.i(TAG, "Thread created");
    }

    @Override
    public void onThreadStarted(WebSocket websocket, ThreadType threadType, Thread thread) {
        LogUtils.i(TAG, "Thread started");
    }

    @Override
    public void onThreadStopping(WebSocket websocket, ThreadType threadType, Thread thread) {
        LogUtils.i(TAG, "Thread stopping");
    }

    @Override
    public void onError(WebSocket websocket, WebSocketException cause) {
        LogUtils.e(TAG, "Error! " + cause);
    }

    @Override
    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) {
        LogUtils.e(TAG, "Frame error");
    }

    @Override
    public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) {
        LogUtils.e(TAG, "Message error! " + cause);
    }

    @Override
    public void onMessageDecompressionError(WebSocket websocket, WebSocketException cause, byte[] compressed) {
        LogUtils.e(TAG, "Message Decompression Error");
    }

    @Override
    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) {
        LogUtils.e(TAG, "Text Message Error! " + cause);
    }

    @Override
    public void onSendError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) {
        LogUtils.e(TAG, "Send Error! " + cause);
    }

    @Override
    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) {
        LogUtils.e(TAG, "Unexpected error! " + cause);
    }

    @Override
    public void handleCallbackError(WebSocket websocket, Throwable cause) {
        LogUtils.e(TAG, "Handle callback error! " + cause);
    }

    @Override
    public void onSendingHandshake(WebSocket websocket, String requestLine, List<String[]> headers) {
        LogUtils.i(TAG, "Sending Handshake! Hello!");
    }

    private void saveIceCandidate(JSONObject json, String endPointName) throws JSONException {
        IceCandidate iceCandidate = new IceCandidate(json.getString("sdpMid"), Integer.parseInt(json.getString("sdpMLineIndex")), json.getString("candidate"));
        Objects.requireNonNull(participants.get(endPointName)).getPeerConnection().addIceCandidate(iceCandidate);
    }

    private void saveAnswer(JSONObject json) throws JSONException {
        SessionDescription sessionDescription = new SessionDescription(SessionDescription.Type.ANSWER, json.getString("sdpAnswer"));
        Objects.requireNonNull(participants.get(remoteParticipantId)).getPeerConnection().setRemoteDescription(new CustomSdpObserver("remoteSetRemoteDesc"), sessionDescription);
    }

    public void sendJson(WebSocket webSocket, String method, Map<String, Object> params) {
        try {
            JSONObject paramsJson = new JSONObject();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                paramsJson.put(param.getKey(), param.getValue());
            }
            JSONObject jsonObject = new JSONObject();
            if (method.equals(JSONConstants.JOIN_ROOM)) {
                jsonObject.put(JSONConstants.ID, 1).put(JSONConstants.PARAMS, paramsJson);
            } else if (paramsJson.length() > 0) {
                jsonObject.put(JSONConstants.ID, getId()).put(JSONConstants.PARAMS, paramsJson);
            } else {
                jsonObject.put(JSONConstants.ID, getId());
            }
            jsonObject.put("jsonrpc", JSON_RPCVERSION).put(JSONConstants.METHOD, method);
            String jsonString = jsonObject.toString();
            updateId();
            webSocket.sendText(jsonString);
            LogUtils.i("SendMessage", jsonString);
        } catch (JSONException e) {
            LogUtils.i(TAG, "JSONException raised on sendJson");
        }
    }

    public void canclePingTimer() {
        if (rxTimer != null)
            rxTimer.cancel();
    }
}