package com.uuabc.classroomlib.classroom;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.JsonSyntaxException;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.databinding.ActivityClassRoomSOneToFourBinding;
import com.uuabc.classroomlib.model.SocketModel.GifValueModel;
import com.uuabc.classroomlib.model.SocketModel.OnlineUserModel;
import com.uuabc.classroomlib.model.SocketModel.SShareModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.utils.ExceptionUtil;
import com.uuabc.classroomlib.utils.JsonUtils;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.utils.PointUtil;
import com.uuabc.classroomlib.utils.SocketIoUtils;
import com.uuabc.classroomlib.utils.TipUtils;
import com.uuabc.classroomlib.utils.UtilsBigDecimal;
import com.uuabc.classroomlib.widget.ClassicsBoardView;
import com.uuabc.classroomlib.widget.dialog.SettingDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.Socket;

public class SOneToFourClassRoomActivity extends BaseClassRoomActivity {
    private ActivityClassRoomSOneToFourBinding mBinding;
    private SOneToFourRoomHelper roomHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_class_room_s_one_to_four);
        getWindow().setBackgroundDrawable(null);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        mBinding = DataBindingUtil.bind(mChildView);
        mBinding.setHandler(roomHelper = new SOneToFourRoomHelper(mBinding));
        roomHelper.dialog = new SettingDialog(this);

        mBinding.ivClose.setOnClickListener(v -> roomHelper.loginOutNewClassRoom());
        mBinding.ivRefresh.setOnClickListener(v -> roomHelper.showNewRefreshTipDialog());
        mBinding.ibTechnicalSupport.setOnClickListener(v -> roomHelper.showNewSupportDialog());

        mBinding.ibSetting.setOnClickListener(view -> {
            if (roomHelper.students == null || roomHelper.students.size() == 0) return;
            roomHelper.onSetVoice(true);
            if (roomHelper.dialog.getStudentCount() == 0) {
                TipUtils.warningShow(this, "暂时不能设置音量~");
            }
        });

        if (!RoomApplication.getInstance().isTable && RoomApplication.getInstance().isFirstInterOneToFour) {
            RoomApplication.getInstance().isFirstInterOneToFour = false;
            finish();
            ActivityUtils.startActivity(SOneToFourClassRoomActivity.class);
            overridePendingTransition(0, 0);
            return;
        }

        PointUtil.onEvent(this, RoomConstant.ONE_TO_FOUR_TO_CLASS);
    }

    @Override
    protected void onDestroy() {
        mBinding.rlRostrum.removeAllView();
        roomHelper.record(RoomConstant.RECORD_VIDEO_STOP, "");
        roomHelper.record(RoomConstant.RECORD_EXIT, "");
        roomHelper.dismissAllDialog();
        mBinding.clCoursewareContainer.removeAllViews();
        mBinding.wvCourseware.destoryWebView();
        super.onDestroy();
    }

    @Override
    public void permissionsGranted() {
        setListener();
        mBinding.rlRostrum.removeAllView();
        roomHelper.record(RoomConstant.RECORD_ENTER, "");
        roomHelper.refreshRoom();
    }

    @Override
    public void onBackPressed() {
        roomHelper.loginOutNewClassRoom();
    }

    private void setListener() {
        roomHelper.setNewWebViewListener(mBinding.wvCourseware, mBinding.tvPageNum);
        mBinding.boardView.getMyBoardView().setOnPathListener(new ClassicsBoardView.OnPathListener() {
            @Override
            public void onMoveTo(int x, int y, float width, String color, String action) {
                HashMap valueMap = roomHelper.getPathValueMap(x, y, width, color, roomHelper.mScale, action);
                HashMap shareMap = roomHelper.getNewPathShareMap(valueMap);
                RoomApplication.getInstance().sendMessage(RoomConstant.SHARE, SocketIoUtils.getJsonObject(shareMap));
            }

            @Override
            public void onLineTo(int x, int y, float width, String color, String action) {
                HashMap valueMap = roomHelper.getPathValueMap(x, y, width, color, roomHelper.mScale, action);
                HashMap shareMap = roomHelper.getNewPathShareMap(valueMap);
                RoomApplication.getInstance().sendMessage(RoomConstant.SHARE, SocketIoUtils.getJsonObject(shareMap));
            }

            @Override
            public void onScreen(JSONObject content) {
                if (content != null) {
                    content.put("page", roomHelper.mCurrentCoursewarePage);
                    roomHelper.record(RoomConstant.RECORD_POINT, content);
                }
            }
        });

         /*
          画笔颜色选择事件
         */
        mBinding.rgColor.setOnCheckedChangeListener((group, checkedId) -> roomHelper.onColorCheckedChanged(checkedId));

        /*
          画笔大小的选择事件
         */
        mBinding.rgSize.setOnCheckedChangeListener((group, checkedId) -> roomHelper.onSizeCheckedChanged(checkedId));

        mBinding.ivPrePage.setOnClickListener(v -> roomHelper.turnPrePage());
        mBinding.ivNextPage.setOnClickListener(v -> roomHelper.turnNextPage());
    }

    @Override
    public void onRtcMsgReceived(String event, String uid, String errorMsg) {
        roomHelper.onRtcMsgReceived(event, uid, errorMsg);
    }

    @Override
    public void onVolumeCallBack(HashMap<Integer, Integer> map) {
        roomHelper.doVolumeEvent(map);
    }

    @Override
    public void onNetWorkCallBack(HashMap<Integer, Integer> map) {
        roomHelper.doNetEvent(map);
    }

    @Override
    public void onIoSocketMsgReceived(String event, String... data) {
        switch (event) {
            case Socket.EVENT_CONNECT:
                mBinding.viewNetworkTips.doSocketConnect();
                authenticate(RoomConstant.STUDENT_TYPE);
                break;
            case RoomConstant.AUTHENTICATED:
                join(roomHelper.roomId, roomHelper.sMyselfModel, RoomConstant.STUDENT_TYPE, RoomApplication.getInstance().isCameraEnable());
                break;
            case Socket.EVENT_DISCONNECT:
                mBinding.viewNetworkTips.doSocketInConnect();
                break;
            case RoomConstant.ENTER_SUCCESS://登入成功
                List<OnlineUserModel> userModelList = JsonUtils.parseArray(data[0], OnlineUserModel.class);
                roomHelper.enterSuccess(userModelList);
                sendStudents(roomHelper.students, RoomConstant.CLASS_TYPE_ONE_TO_FOUR);
                roomHelper.onSetVoice(false);
                break;
            case RoomConstant.USER_ENTER://新的用户加入
                try {
                    OnlineUserModel userEnterModel = SocketIoUtils.parseData(OnlineUserModel.class, data[0]);
                    roomHelper.interRoom(userEnterModel);
                } catch (JsonSyntaxException e) {
                    ExceptionUtil.sendException(RoomConstant.USER_ENTER, roomHelper.classType, e.getMessage());
                }
                break;
            case RoomConstant.USER_QUIT://用户退出
                try {
                    OnlineUserModel userQuitModel = SocketIoUtils.parseData(OnlineUserModel.class, data[0]);
                    roomHelper.userQuit(userQuitModel);
                } catch (JsonSyntaxException e) {
                    ExceptionUtil.sendException(RoomConstant.USER_QUIT, roomHelper.classType, e.getMessage());
                }
                break;
            case RoomConstant.SHARE://共享事件
                try {
                    SShareModel shareModel = SocketIoUtils.parseData(SShareModel.class, data[0]);
                    doShareEvent(shareModel, data[1]);
                } catch (JsonSyntaxException e) {
                    ExceptionUtil.sendException(RoomConstant.SHARE, roomHelper.classType, e.getMessage());
                }
                break;
            case RoomConstant.OFF_LINE://服务器端通知断开
                roomHelper.doNewOffLineClassRoom();
                doDestory();
                break;
            case Socket.EVENT_RECONNECT:
                PointUtil.onEvent(this, RoomConstant.ONE_TO_FOUR_RECONNECT);
                mBinding.viewNetworkTips.doSocketConnect();
                break;
        }
    }

    /**
     * 处理共享事件
     *
     * @param shareModel 共享事件的model
     */
    private void doShareEvent(SShareModel shareModel, String data2) {
        if (shareModel == null) return;
        UserModel userModel = JsonUtils.parseObject(data2, UserModel.class);
        switch (shareModel.getEvent()) {
            case RoomConstant.FLOWER://奖励钻石后更新的钻石列表
                roomHelper.doFlower(shareModel);
                break;
            case RoomConstant.MUTED://对学生禁言
                roomHelper.controlMuted(shareModel);
                break;
            case RoomConstant.DRAW://涂鸦开关
                roomHelper.controlDraw(shareModel);
                break;
            case RoomConstant.ANIMATE://互动权限
                roomHelper.controlAnimate(shareModel);
                break;
            case RoomConstant.TEXTSTART://课件打字| 老师端操作
            case RoomConstant.DRAW_TEXT:
                roomHelper.doDrawText(shareModel);
                break;
            case RoomConstant.EXCIT://激励动画
                try {
                    GifValueModel gifValueModel = SocketIoUtils.parseData(GifValueModel.class, (Map) shareModel.getData());
                    roomHelper.loadGif(gifValueModel, mBinding.ivGif);
                } catch (JsonSyntaxException e) {
                    ExceptionUtil.sendException(RoomConstant.EXCIT, roomHelper.classType, e.getMessage());
                }
                break;
            case RoomConstant.ROSTRUM://控制学生上下台 | 老师端操作
                roomHelper.doRostrum(shareModel);
                break;
            case RoomConstant.POSITION://鼠标移动
                roomHelper.doPosition(shareModel, userModel != null ? userModel.getId() : 0);
                break;
            case RoomConstant.LINE://画线
                roomHelper.doLine(shareModel, userModel != null ? userModel.getId() : 0);
                break;
            case RoomConstant.CHAT://聊天
                roomHelper.loadChart(shareModel, userModel);
                break;
            case RoomConstant.PAGE://翻页
                roomHelper.doPage(shareModel);
                break;
            case RoomConstant.CLEAR://清除课件区域屏幕
                roomHelper.clearBoard();
                break;
            case RoomConstant.ACTION://交互课件中的交互指令
                mBinding.wvCourseware.loadUrl("javascript:PageMgr.receiveMsg('" + shareModel.getData() + "')");
                break;
            case RoomConstant.COMPLETE://下课通知
                Map completeMap = (Map) shareModel.getData();
                roomHelper.overClassEvent(completeMap);
                break;
            case RoomConstant.STUDENTS://学生列表
                Map studentMap = (Map) shareModel.getData();
                roomHelper.doStudents(studentMap);
                break;
            case RoomConstant.COURSEWARE://切换课件
                roomHelper.changeCourseWare(shareModel);
                break;
            case RoomConstant.REFRESH_CLASSROOM:
                if (userModel == null || userModel.getType() != 4) return;
                if (shareModel.getData() == null) return;
                Map targetMap = (Map) shareModel.getData();
                if (targetMap.containsKey("token") && TextUtils.equals(ObjectUtil.getString(targetMap.get("token")), "1" + SPUtils.getInstance().getInt(RoomConstant.USER_ID))) {
                    roomHelper.refreshRoom();
                }
                break;
        }
    }

    @Override
    public void onNetWorkMsgReceived(String event) {
        roomHelper.onNetWorkMsgReceived(event);
        if (roomHelper.dialog == null || roomHelper.students == null) return;
        roomHelper.onSetVoice(false);
    }

    @Override
    public void onSetWifiLevel(int level) {
        super.onSetWifiLevel(level);
        if (mBinding == null) return;
        roomHelper.setWifiIcon(mBinding.ivWifi, level);
    }

    @Override
    public void onVolumeChanged(int volume) {
        super.onVolumeChanged(volume);
        double realVolume = UtilsBigDecimal.getDivValue(musicVoiceCurrent, musicVoiceMax);
        RoomApplication.getInstance().getVideoManager().adjustPlaybackSignalVolume((int) (100 * realVolume));
    }
}

