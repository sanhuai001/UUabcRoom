package com.uuabc.classroomlib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.blankj.utilcode.util.ZipUtils;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4WithSpeed;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;
import com.uuabc.classroomlib.builder.SocketIoBuild;
import com.uuabc.classroomlib.classroom.SLiveClassRoomActivity;
import com.uuabc.classroomlib.classroom.SOneToFourClassRoomActivity;
import com.uuabc.classroomlib.classroom.SOneToOneClassRoomActivity;
import com.uuabc.classroomlib.model.Event.MessageEvent;
import com.uuabc.classroomlib.model.Event.NetWorkEvent;
import com.uuabc.classroomlib.model.Event.RtcMsgEvent;
import com.uuabc.classroomlib.model.RoomType;
import com.uuabc.classroomlib.model.SOverClassModel;
import com.uuabc.classroomlib.model.db.DaoMaster;
import com.uuabc.classroomlib.model.db.DaoSession;
import com.uuabc.classroomlib.utils.CameraUtil;
import com.uuabc.classroomlib.utils.TipUtils;
import com.uuabc.roomvideo.factory.RoomVideoFactory;
import com.uuabc.roomvideo.factory.VideoFactory;
import com.uuabc.roomvideo.model.RoomVideoType;
import com.uuabc.roomvideo.observer.RoomManagerObserver;
import com.uuabc.roomvideo.observer.VideoManagerObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.greendao.database.Database;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;

import static com.uuabc.classroomlib.classroom.WebActivityKt.launchUuabcRoomWeb;

/**
 * RoomApplication
 * Created by wb.cn on 2017/7/27.
 */
@SuppressLint({"Registered", "CheckResult"})
public class RoomApplication extends MultiDexApplication {
    public static final String IOSOCKET_TAG = "ioSocket_tag";
    private static RoomApplication sApplication;
    private Socket mSocket;
    public int screenWidth;//屏幕宽
    public int screenHeight;//屏幕高
    public String versionName;
    public float scale;
    public float uLiveScale;
    private VideoFactory factory = new RoomVideoFactory();
    private VideoManagerObserver videoManager;
    public String roomToken;
    public float PC_WIDTH = RoomConstant.PC_WIDTH;//PC课件宽
    public float PC_HEIGHT = RoomConstant.PC_HEIGHT;//PC课件高
    private DaoSession daoSession;
    public String saveLottiePath;
    public String emojiZipFileName = "emoji.zip";
    public boolean cameraEnable; //是否有摄像头

    public String token;
    public String channelId;
    public String openviduSocketUrl;
    public String secret;
    public String roomId;

    public String GRAPHQL_URL;
    public String ONLINE_SS_COURSE_HOST;

    public List<String> iceServers = new ArrayList<>();
    public boolean isTable;
    public boolean isFirstInterOneToOne = true;
    public boolean isFirstInterOneToFour = true;
    public boolean isFirstInterLive = true;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
        saveLottiePath = getExternalCacheDir() + RoomConstant.LOTTIE_FACIAL_PATH;
        Utils.init(this);
        registerEventBus(this);
        initLifecycleManager();
        netWorkChangeListener();
        initDb();
        isTable = DeviceUtils.isTablet();
    }

    public String getAppVersionName() {
        if (TextUtils.isEmpty(versionName)) {
            versionName = AppUtils.getAppVersionName();
            return versionName;
        }
        return versionName;
    }

    public int getScreenWidth() {
        return screenWidth == 0 ? ScreenUtils.getScreenWidth() : screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight == 0 ? ScreenUtils.getScreenHeight() : screenHeight;
    }

    public float setScale(int width) {
        return scale = new BigDecimal(PC_WIDTH / width).setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public float getScale(int width) {
        return scale == 0 ? setScale(width) : scale;
    }

    public float setULiveScale(int width) {
        return uLiveScale = new BigDecimal(RoomConstant.PC_ULIVE_WIDTH / width).setScale(4, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public float getULiveScale(int width) {
        return uLiveScale == 0 ? setULiveScale(width) : uLiveScale;
    }

    public static RoomApplication getInstance() {
        return sApplication;
    }

    /**
     * 注册EventBus
     *
     * @param object 订阅者
     */
    @Subscribe
    public void registerEventBus(Context object) {
        try {
            if (!EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().register(object);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 取消EventBus
     */
    public void unregisterEventBus(Context object) {
        try {
            if (EventBus.getDefault().isRegistered(object)) {
                EventBus.getDefault().unregister(object);
            }
        } catch (Exception ignored) {
        }
    }

    public void ioSocketNewListener(String url, String path) {
        if (mSocket == null) {
            initNewIoSocket(url, path);

            mSocket
                    .on(Socket.EVENT_CONNECT, args -> {
                        LogUtils.i(IOSOCKET_TAG, "已连接");
                        EventBus.getDefault().post(new MessageEvent(Socket.EVENT_CONNECT, ""));
                    })
                    .on(Socket.EVENT_CONNECTING, args -> {
                        LogUtils.i(IOSOCKET_TAG, "连接中");
                        EventBus.getDefault().post(new MessageEvent(Socket.EVENT_CONNECTING));
                    })
                    .on(Socket.EVENT_DISCONNECT, args -> {
                        LogUtils.i(IOSOCKET_TAG, "断开连接");
                        EventBus.getDefault().post(new MessageEvent(Socket.EVENT_DISCONNECT));
                    })
                    .on(Socket.EVENT_RECONNECTING, args -> {
                        LogUtils.i(IOSOCKET_TAG, "重连中");
                        EventBus.getDefault().post(new MessageEvent(Socket.EVENT_RECONNECTING));
                    })
                    .on(Socket.EVENT_RECONNECT_ERROR, args -> {
                        LogUtils.i(IOSOCKET_TAG, "重连ERROR");
                        EventBus.getDefault().post(new MessageEvent(Socket.EVENT_RECONNECT_ERROR));
                    })
                    .on(Socket.EVENT_RECONNECT_FAILED, args -> {
                        LogUtils.i(IOSOCKET_TAG, "重连失败");
                        EventBus.getDefault().post(new MessageEvent(Socket.EVENT_RECONNECT_FAILED));
                    })
                    .on(Socket.EVENT_RECONNECT, args -> {
                        LogUtils.i(IOSOCKET_TAG, "重连成功");
                        EventBus.getDefault().post(new MessageEvent(Socket.EVENT_RECONNECT));
                    })
                    .on(Socket.EVENT_ERROR, args -> LogUtils.i(IOSOCKET_TAG, "失败"))
                    .on(Socket.EVENT_MESSAGE, args -> LogUtils.i(IOSOCKET_TAG, "mess"))
                    .on(RoomConstant.AUTHENTICATED, args -> EventBus.getDefault().post(new MessageEvent(RoomConstant.AUTHENTICATED)))
                    .on(RoomConstant.ENTER_SUCCESS, args -> postMessageEvent(RoomConstant.ENTER_SUCCESS, args))
                    .on(RoomConstant.USER_ENTER, args -> postMessageEvent(RoomConstant.USER_ENTER, args))
                    .on(RoomConstant.ENTER_REJECT, args -> EventBus.getDefault().post(new MessageEvent(RoomConstant.ENTER_REJECT)))
                    .on(RoomConstant.USER_QUIT, args -> postMessageEvent(RoomConstant.USER_QUIT, args))
                    .on(RoomConstant.OFF_LINE, args -> postMessageEvent(RoomConstant.OFF_LINE, args))
                    .on(RoomConstant.SPEAKING, args -> postMessageEvent(RoomConstant.SPEAKING, args))
                    .on(RoomConstant.SHARE, args -> postMessageEvent(RoomConstant.SHARE, args));
        }

        mSocket.connect();
    }

    private void postMessageEvent(String event, Object... args) {
        if (args == null || args.length == 0) {
            EventBus.getDefault().post(new MessageEvent(event));
        } else if (args.length == 1) {
            EventBus.getDefault().post(new MessageEvent(event, args[0] != null ? String.valueOf(args[0]) : ""));
        } else {
            EventBus.getDefault().post(new MessageEvent(event, args[0] != null ? String.valueOf(args[0]) : "", args[1] != null ? String.valueOf(args[1]) : ""));
        }
    }

    /**
     * 断开链接
     */
    public void ioSocketDisconnect() {
        if (mSocket != null) {
            mSocket.disconnect();
            mSocket = null;
        }
    }

    public boolean socketConnected() {
        return mSocket != null && mSocket.connected();
    }

    public void sendMessage(String event, Object msg) {
        LogUtils.i(IOSOCKET_TAG, msg != null ? msg.toString() : "");
        if (mSocket != null && mSocket.connected()) {
            mSocket.emit(event, msg, (Ack) args -> {
            });
        }
    }

    private void initNewIoSocket(String url, String path) {
        mSocket = new SocketIoBuild.Builder()
                .url(url)
                .timeout(-1)
                .reconnection(true)
                .reconnectionAttempts(10000)
                .reconnectionDelay(2000)
                .reconnectionDelayMax(2000)
                .forceNew(true)
                .transports(new String[]{WebSocket.NAME})
                .path(path)
                .build();
    }

    private void initLifecycleManager() {
        LifecycleManager.init(this);
        LifecycleManager.get().addListener(new LifecycleManager.Listener() {
            @Override
            public void onBackground() {
                LogUtils.d("LifecycleManager", "onBackground");
                SPUtils.getInstance().put(RoomConstant.IS_FOREGROUND, false);
                onBackGround();
            }

            @Override
            public void onForeground() {
                LogUtils.d("LifecycleManager", "onForeground");
                SPUtils.getInstance().put(RoomConstant.IS_FOREGROUND, true);
                onForeGround();
                onRoomForeGround();
            }
        });
    }

    public void onBackGround() {
    }

    public void onForeGround() {
    }

    private void onRoomForeGround() {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity == null) return;
        if (topActivity instanceof SOneToFourClassRoomActivity) {
            topActivity.finish();
            ActivityUtils.startActivity(SOneToFourClassRoomActivity.class);
            topActivity.overridePendingTransition(0, 0);
        } else if (topActivity instanceof SOneToOneClassRoomActivity) {
            topActivity.finish();
            ActivityUtils.startActivity(SOneToOneClassRoomActivity.class);
            topActivity.overridePendingTransition(0, 0);
        } else if (topActivity instanceof SLiveClassRoomActivity) {
            topActivity.finish();
            ActivityUtils.startActivity(SLiveClassRoomActivity.class);
            topActivity.overridePendingTransition(0, 0);
        }
    }

    /**
     * 网络监听
     */
    public void netWorkChangeListener() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                    /**
                     * 网络可用的回调
                     */
                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
                        if (ActivityUtils.getTopActivity() != null) {
                            EventBus.getDefault().post(new NetWorkEvent(RoomConstant.NET_WORK_CONNECTED));
                            if (!isInClassRoom()) return;
                            TipUtils.warningShow(ActivityUtils.getTopActivity(), getString(NetworkUtils.isWifiConnected() ?
                                    R.string.common_current_network_wifi : R.string.common_current_network_not_wifi));
                        }
                    }

                    /**
                     * 网络丢失的回调
                     */
                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        if (ActivityUtils.getTopActivity() != null) {
                            EventBus.getDefault().post(new NetWorkEvent(RoomConstant.NET_WORK_INCONNECTED));
                            if (!isInClassRoom()) return;
                            TipUtils.warningShow(ActivityUtils.getTopActivity(), getString(R.string.common_current_network_connect_fail));
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化教室视频SDK
     *
     * @param videoType   视频类型 声网 talkCloud
     * @param appId       appId
     * @param videoWidth  videoWidth
     * @param videoHeight videoHeight
     */
    public void initRoomVideoSDK(RoomVideoType videoType, String appId, int videoWidth, int videoHeight) {
        switchVideo(videoType);
        videoManager.initSdk(this, appId, observer);
        videoManager.setBigVideoProfile(videoWidth, videoHeight);
        videoManager.startPreview();
    }

    /**
     * 初始化教室视频SDK
     *
     * @param videoType 视频类型 声网 talkCloud
     * @param appId     appId
     */
    public void initRoomVideoSDK(RoomVideoType videoType, String appId) {
        initRoomVideoSDK(videoType, appId, 0, 0);
    }

    /**
     * 初始化直播课教室视频SDK
     *
     * @param videoType 视频类型 声网 talkCloud
     * @param appId     appId
     */
    public void initLiveRoomVideoSDK(RoomVideoType videoType, String appId, boolean isAnchor, int videoWidth, int videoHeight) {
        if (TextUtils.isEmpty(appId)) return;
        switchVideo(videoType);
        videoManager.initSdk(this, appId, observer);
        videoManager.setBigVideoProfile(videoWidth, videoHeight);
        videoManager.setChannelProfileLive();
        videoManager.startPreview();
        videoManager.setClientRole(isAnchor);
    }

    /**
     * 设置视频宽高
     */
    public void setVideoProfile(int videoWidth, int videoHeight) {
        if (videoManager == null) return;
        videoManager.setBigVideoProfile(videoWidth, videoHeight);
    }

    /**
     * 启用或禁用音量提示,定期向应用程序反馈当前谁在说话以及说话者的音量
     *
     * @param interval <=0: 禁用音量提示功能
     *                 >0: 提示间隔，单位为毫秒。建议设置到大于 200 毫秒
     */
    public void enableAudioVolumeIndication(int interval) {
        if (videoManager == null) return;
        videoManager.enableAudioVolumeIndication(interval);
    }

    /**
     * 教室内视频切换
     *
     * @param videoType 切换到的videoType
     */
    public void switchVideo(RoomVideoType videoType) {
        videoManager = factory.getVideoManager(videoType);
    }

    /**
     * 获取教室内视频管理对象
     *
     * @return 视频管理对象
     */
    public VideoManagerObserver getVideoManager() {
        videoManager = videoManager == null ? factory.getVideoManager(RoomVideoType.AGORA) : videoManager;
        return videoManager;
    }

    RoomManagerObserver observer = new RoomManagerObserver() {
        @Override
        public void onRoomJoined() {
            EventBus.getDefault().post(new RtcMsgEvent(RoomConstant.ROOM_JOINED));
        }

        @Override
        public void onUserJoined(String uid) {
            EventBus.getDefault().post(new RtcMsgEvent(RoomConstant.ROOM_USER_JOINED, uid));
        }

        @Override
        public void onUserLeaved(String uid) {
            EventBus.getDefault().post(new RtcMsgEvent(RoomConstant.ROOM_USER_LEAVED, uid));
        }

        @Override
        public void onKickedout() {
            //自己被踢出房间
            EventBus.getDefault().post(new RtcMsgEvent(RoomConstant.ROOM_KICKED_OUT));
        }

        @Override
        public void onError(String errMsg) {
            RtcMsgEvent rtcMsgEvent = new RtcMsgEvent(RoomConstant.ROOM_ERROR);
            rtcMsgEvent.setErrMsg(errMsg);
            EventBus.getDefault().post(rtcMsgEvent);
        }

        @Override
        public void onLeaveChannelSuccess() {
            RtcMsgEvent rtcMsgEvent = new RtcMsgEvent(RoomConstant.ROOM_LEAVE_SUCCESS);
            EventBus.getDefault().post(rtcMsgEvent);
        }

        @Override
        public void onNetworkQuality(int uid, int txQuality, int rxQuality) {
            @SuppressLint("UseSparseArrays")
            HashMap<Integer, Integer> map = new HashMap<>();
            map.put(uid, Math.max(txQuality, rxQuality));
            RtcMsgEvent rtcMsgEvent = new RtcMsgEvent(RoomConstant.ROOM_NET, map);
            EventBus.getDefault().post(rtcMsgEvent);
        }

        @Override
        public void onAudioVolumeIndication(HashMap<Integer, Integer> speakers, int totalVolume) {
            RtcMsgEvent rtcMsgEvent = new RtcMsgEvent(RoomConstant.ROOM_VOLUME, speakers);
            EventBus.getDefault().post(rtcMsgEvent);
        }

        @Override
        public void onRenewToken() {

        }
    };

    private void initDb() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "roomDb");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        if (daoSession == null) {
            initDb();
        }
        return daoSession;
    }

    public void downLoadTask(String url, int updateTime) {
        if (TextUtils.isEmpty(url)) return;
        if (FileUtils.isFileExists(saveLottiePath + updateTime + emojiZipFileName)) return;
        try {
            deleteLottieFiles();
            DownloadTask task = new DownloadTask.Builder(url, saveLottiePath, updateTime + emojiZipFileName)
                    .setFilename(updateTime + emojiZipFileName)
                    .setMinIntervalMillisCallbackProcess(50)
                    .setPassIfAlreadyCompleted(false)
                    .build();
            task.setTag(updateTime);
            task.enqueue(downloadListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    DownloadListener4WithSpeed downloadListener = new DownloadListener4WithSpeed() {
        @Override
        public void taskStart(@NonNull DownloadTask task) {

        }

        @Override
        public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {

        }

        @Override
        public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

        }

        @Override
        public void infoReady(@NonNull DownloadTask task, @NonNull BreakpointInfo info, boolean fromBreakpoint, @NonNull Listener4SpeedAssistExtend.Listener4SpeedModel model) {

        }

        @Override
        public void progressBlock(@NonNull DownloadTask task, int blockIndex, long currentBlockOffset, @NonNull SpeedCalculator blockSpeed) {

        }

        @Override
        public void progress(@NonNull DownloadTask task, long currentOffset, @NonNull SpeedCalculator taskSpeed) {

        }

        @Override
        public void blockEnd(@NonNull DownloadTask task, int blockIndex, BlockInfo info, @NonNull SpeedCalculator blockSpeed) {

        }

        @Override
        public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull SpeedCalculator taskSpeed) {
            LogUtils.i("fileDownloadListener", "completed" + task.getTag());
            Observable.just("unzipFile")
                    .subscribeOn(Schedulers.io())
                    .doOnNext(s -> ZipUtils.unzipFile(saveLottiePath + task.getTag() + emojiZipFileName, saveLottiePath))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((response) -> {
                    }, throwable -> {
                    });
        }
    };

    public void deleteLottieFiles() {
        FileUtils.deleteAllInDir(saveLottiePath);
    }

    /**
     * 跳转到教室
     */
    public void jumpToClassRoom(String roomToken, RoomType roomType) {
        if (TextUtils.isEmpty(roomToken) || roomType == null) return;
        this.roomToken = roomToken;

        switch (roomType) {
            case ONETOFOUR:
                ActivityUtils.startActivity(SOneToFourClassRoomActivity.class);
                break;
            case ONETOONE:
                ActivityUtils.startActivity(SOneToOneClassRoomActivity.class);
                break;
            case LIVE:
                ActivityUtils.startActivity(SLiveClassRoomActivity.class);
                break;
        }
    }

    public boolean isInClassRoom() {
        Activity topActivity = ActivityUtils.getTopActivity();
        if (topActivity == null) return false;
        return topActivity instanceof SOneToFourClassRoomActivity
                || topActivity instanceof SOneToOneClassRoomActivity
                || topActivity instanceof SLiveClassRoomActivity;
    }

    public void doComment(Context context, RoomType roomType, SOverClassModel model) {
        if (roomType == null || model == null) return;
        if (roomType == RoomType.ONETOFOUR || roomType == RoomType.ONETOONE) {
            launchUuabcRoomWeb(context, model.getRedirect_url(), "");
        }
    }

    public boolean isCameraEnable() {
        if (cameraEnable) return true;
        return cameraEnable = CameraUtil.checkCameraEnable();
    }
}
