package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.common.BaseCommonActivity;
import com.uuabc.classroomlib.common.RxTimer;

@SuppressLint("Registered")
public class BaseWifiListenerActivity extends BaseCommonActivity {
    private IntentFilter wifiIntentFilter;
    private WifiManager wifiManager;
    private RxTimer rxTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        wifiIntentFilter = new IntentFilter();
        wifiIntentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        wifiIntentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        initWifiStrength();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initWifiStrength();
        registerReceiver(wifiIntentReceiver, wifiIntentFilter);
        refreshWifiState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiIntentReceiver);
    }

    private void initWifiStrength() {
        try {
            wifiManager = wifiManager == null ? (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE) : wifiManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 声明wifi消息处理过程
     */
    private BroadcastReceiver wifiIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (TextUtils.equals(action, WifiManager.RSSI_CHANGED_ACTION)
                    || TextUtils.equals(action, WifiManager.WIFI_STATE_CHANGED_ACTION)
                    || TextUtils.equals(action, WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                refreshWifiState();
            }
        }
    };

    public void refreshWifiState() {
        rxTimer = rxTimer == null ? new RxTimer() : rxTimer;
        rxTimer.timer(1000, number -> {
            if (wifiManager == null) return;
            try {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo == null) return;
                //获得信号强度值
                int level = wifiInfo.getRssi();
                //根据获得的信号强度发送信息
                if (level <= 0 && level >= -50) {
                    onSetWifiLevel(RoomConstant.WIFI_LEVEL_STRONG);
                } else if (level < -50 && level >= -70) {
                    onSetWifiLevel(RoomConstant.WIFI_LEVEL_MID);
                } else if (level < -70 && level >= -100) {
                    onSetWifiLevel(RoomConstant.WIFI_LEVEL_WEEK);
                } else {
                    onSetWifiLevel(RoomConstant.WIFI_LEVEL_NULL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void onSetWifiLevel(int level) {
        LogUtils.i("onSetWifiLevel", "onSetWifiLevel:" + level);
    }
}
