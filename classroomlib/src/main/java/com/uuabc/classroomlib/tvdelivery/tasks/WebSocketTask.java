package com.uuabc.classroomlib.tvdelivery.tasks;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.uuabc.classroomlib.classroom.MonitorRoomActivity;
import com.uuabc.classroomlib.tvdelivery.listeners.CustomWebSocketListener;
import com.uuabc.classroomlib.tvdelivery.managers.PeersManager;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class WebSocketTask extends AsyncTask<AppCompatActivity, Void, Void> {
    private static final String TAG = "WebSocketTask";
    private final TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
            Log.i(TAG, ": authType: " + authType);
        }

        @Override
        public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
            Log.i(TAG, ": authType: " + authType);
        }
    }};
    @SuppressLint("StaticFieldLeak")
    private AppCompatActivity activity;
    private String socketAddress;
    private PeersManager peersManager;
    private boolean isCancelled = false;

    public WebSocketTask(AppCompatActivity activity, PeersManager peersManager, String socketAddress) {
        this.activity = activity;
        this.peersManager = peersManager;
        this.socketAddress = socketAddress;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    @Override
    protected Void doInBackground(AppCompatActivity... parameters) {
        try {
            WebSocketFactory factory = new WebSocketFactory();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            factory.setSSLContext(sslContext);
            peersManager.setWebSocket(new WebSocketFactory().createSocket(socketAddress));
            if (activity instanceof MonitorRoomActivity) {
                peersManager.setWebSocketAdapter(new CustomWebSocketListener(activity, peersManager, ((MonitorRoomActivity)activity).getViewsContainer()));
            }else {
                peersManager.setWebSocketAdapter(new CustomWebSocketListener(activity, peersManager, null));
            }
            peersManager.getWebSocket().addListener(peersManager.getWebSocketAdapter());
            if (!isCancelled) {
                peersManager.getWebSocket().connect();
            }
        } catch (IOException | KeyManagementException | WebSocketException | NoSuchAlgorithmException | IllegalArgumentException e) {
            Handler mainHandler = new Handler(activity.getMainLooper());
            Runnable myRunnable = () -> {
                if (activity instanceof MonitorRoomActivity) {
                    ((MonitorRoomActivity) activity).hangup();
                }
            };
            mainHandler.post(myRunnable);
            isCancelled = true;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... progress) {
        Log.i(TAG, "PROGRESS " + Arrays.toString(progress));
    }

    @Override
    protected void onPostExecute(Void results) {
        isCancelled = false;
    }
}