package com.uuabc.classroomlib.classroom;

import android.Manifest;
import android.annotation.SuppressLint;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.common.BaseIoSocketActivity;
import com.uuabc.classroomlib.model.Event.NetWorkEvent;
import com.uuabc.classroomlib.model.Event.OutRoomEvent;
import com.uuabc.classroomlib.model.Event.RtcMsgEvent;
import com.uuabc.classroomlib.observer.VolumeChangeObserver;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.utils.SConfirmDialogUtils;
import com.uuabc.classroomlib.widget.dialog.ConfirmDialog;
import com.uuabc.classroomlib.widget.dialog.SConfirmDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

@SuppressLint("Registered")
public class BaseClassRoomActivity extends BaseIoSocketActivity implements VolumeChangeObserver.VolumeChangeListener {
    private boolean isFirstResume = true;
    private ConfirmDialog dialog;
    private VolumeChangeObserver mVolumeChangeObserver;
    public int musicVoiceMax = 1;
    public int musicVoiceCurrent = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_SECURE);

        mVolumeChangeObserver = new VolumeChangeObserver(this);
        mVolumeChangeObserver.setVolumeChangeListener(this);
        musicVoiceMax = ObjectUtil.getIntValue(mVolumeChangeObserver.getMaxMusicVolume());
        musicVoiceCurrent = musicVoiceMax / 2;
        mVolumeChangeObserver.getAudioManager().setStreamVolume(AudioManager.STREAM_MUSIC, musicVoiceCurrent, 0);
        onVolumeChanged(musicVoiceCurrent);
    }

    @Override
    public void initUI(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            SConfirmDialog confirmDialog = SConfirmDialogUtils.show(this, R.drawable.ic_room_sdk_dialog_confirm_exit, getString(R.string.dialog_msg_exit_str), getString(R.string.fragment_check_exit_str), (dialog, which) -> dialog.dismiss(), getString(R.string.dialog_cancel_str), (dialog, which) -> dialog.dismiss());
            confirmDialog.dismiss();
            return;
        }

        musicVoiceCurrent = ObjectUtil.getIntValue(mVolumeChangeObserver.getCurrentMusicVolume());
        onNetWorkMsgReceived(NetworkUtils.isConnected() ? RoomConstant.NET_WORK_CONNECTED : RoomConstant.NET_WORK_INCONNECTED);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mVolumeChangeObserver.registerReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVolumeChangeObserver.unregisterReceiver();
        RoomApplication.getInstance().getVideoManager().leaveRoom();
    }

    @Override
    protected void onDestroy() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
        }
        loginOut();
        doDestory();
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                mVolumeChangeObserver.getAudioManager().adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FX_FOCUS_NAVIGATION_UP);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                mVolumeChangeObserver.getAudioManager().adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FX_FOCUS_NAVIGATION_UP);
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void doDestory() {
        RoomApplication.getInstance().getVideoManager().leaveRoom();
        RoomApplication.getInstance().ioSocketDisconnect();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverRtcMsg(RtcMsgEvent eventModel) {
        if (ObjectUtils.equals(eventModel.getEvent(), RoomConstant.ROOM_VOLUME)) {
            onVolumeCallBack(eventModel.getMap());
        } else if (ObjectUtils.equals(eventModel.getEvent(), RoomConstant.ROOM_NET)) {
            onNetWorkCallBack(eventModel.getMap());
        } else {
            mMainHandler.post(() -> {
                LogUtils.i("receiverRtcMsg", eventModel.getEvent() + ":" + eventModel.getUid());
                onRtcMsgReceived(eventModel.getEvent(), eventModel.getUid(), eventModel.getErrMsg());
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverNetWorkMsg(NetWorkEvent eventModel) {
        mMainHandler.post(() -> {
            LogUtils.i("receiverNetWorkMsg", eventModel.getEvent());
            onNetWorkMsgReceived(eventModel.getEvent());
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveOutRoomMsg(OutRoomEvent eventModel) {
        mMainHandler.post(() -> {
            LogUtils.i("receiveOutRoomMsg", eventModel.getEvent());
            onOutRoomMsg(eventModel);
        });
    }

    @SuppressLint("CheckResult")
    public void requestPermissions() {
        if (isDestroyed()) return;
        mRxPermissions = mRxPermissions == null ? new RxPermissions(this) : mRxPermissions;

        mRxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS)
                .subscribe(granted -> {
                    if (granted) {
                        permissionsGranted();
                    } else {
                        dialog = dialog == null ? new ConfirmDialog(this) : dialog;
                        if (dialog.isShow()) return;
                        dialog.setMsg("无法获取相机或麦克风权限，请在系统设置中开启").setCancelButton("暂不", (dialog1, which) -> {
                            dialog.dismiss();
                            finish();
                        }).setConfirmButton("去设置", (dialog1, which) -> {
                            dialog.dismiss();
                            AppUtils.launchAppDetailsSettings();
                            finish();
                        }).show();
                    }
                });
    }

    public void permissionsGranted() {

    }

    public void onRtcMsgReceived(String event, String uid, String errorMsg) {

    }

    public void onNetWorkMsgReceived(String event) {

    }

    public void onVolumeCallBack(HashMap<Integer, Integer> map) {

    }

    public void onNetWorkCallBack(HashMap<Integer, Integer> map) {

    }

    public void onOutRoomMsg(OutRoomEvent eventModel) {

    }

    @Override
    public void onVolumeChanged(int volume) {
        musicVoiceCurrent = volume;
    }
}
