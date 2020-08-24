package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.ActivityUtils;
import com.google.gson.JsonSyntaxException;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.databinding.ActivityClassRoomSOneToOneBinding;
import com.uuabc.classroomlib.model.RoomType;
import com.uuabc.classroomlib.model.SocketModel.GifValueModel;
import com.uuabc.classroomlib.model.SocketModel.OnlineUserModel;
import com.uuabc.classroomlib.model.SocketModel.SShareModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.utils.ExceptionUtil;
import com.uuabc.classroomlib.utils.JsonUtils;
import com.uuabc.classroomlib.utils.PointUtil;
import com.uuabc.classroomlib.utils.SConfirmDialogUtils;
import com.uuabc.classroomlib.utils.SocketIoUtils;
import com.uuabc.classroomlib.utils.UtilsBigDecimal;
import com.uuabc.classroomlib.widget.ClassicsBoardView;

import java.util.HashMap;
import java.util.Map;

import io.socket.client.Socket;

@SuppressLint({"CheckResult", "SetTextI18n", "SimpleDateFormat"})
public class SOneToOneClassRoomActivity extends BaseClassRoomActivity {
    private ActivityClassRoomSOneToOneBinding mBinding;
    private SOneToOneClassRoomHelper roomHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_class_room_s_one_to_one);
        mBinding = DataBindingUtil.bind(mChildView);
        mBinding.setHandler(roomHelper = new SOneToOneClassRoomHelper(mBinding));

        mBinding.ivClose.setOnClickListener(v -> roomHelper.loginOutNewClassRoom());
        mBinding.ivRefresh.setOnClickListener(v -> roomHelper.showNewRefreshTipDialog());
        mBinding.ibTechnicalSupport.setOnClickListener(v -> roomHelper.showNewSupportDialog());
        if (!RoomApplication.getInstance().isTable && RoomApplication.getInstance().isFirstInterOneToOne) {
            RoomApplication.getInstance().isFirstInterOneToOne = false;
            finish();
            ActivityUtils.startActivity(SOneToOneClassRoomActivity.class);
            overridePendingTransition(0, 0);
            return;
        }
        PointUtil.onEvent(this, RoomConstant.ONE_TO_ONE_TO_CLASS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.wvCourseware.reload();
    }

    @Override
    protected void onDestroy() {
        roomHelper.record(RoomConstant.RECORD_VIDEO_STOP, "");
        roomHelper.record(RoomConstant.RECORD_EXIT, "");
        mBinding.clCoursewareContainer.removeAllViews();
        mBinding.wvCourseware.destoryWebView();
        super.onDestroy();
    }

    @Override
    public void permissionsGranted() {
        setListener();
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
                roomHelper.enterSuccess();
                sendStudents(roomHelper.students, RoomConstant.CLASS_TYPE_ONE_TO_ONE);
                break;
            case RoomConstant.USER_ENTER://新的用户加入
                try {
                    OnlineUserModel userEnterModel = SocketIoUtils.parseData(OnlineUserModel.class, data[0]);
                    roomHelper.interRoom(userEnterModel);
                } catch (JsonSyntaxException e) {
                    ExceptionUtil.sendException(RoomConstant.USER_ENTER, roomHelper.classType, e.getMessage());
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
                PointUtil.onEvent(this, RoomConstant.ONE_TO_ONE_RECONNECT);
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
            case RoomConstant.DRAWING://涂鸦开关
            case RoomConstant.DRAW:
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
            case RoomConstant.POSITION://鼠标移动
                roomHelper.doPosition(shareModel, userModel != null ? userModel.getId() : 0);
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
                PointUtil.onEvent(this, RoomConstant.ONE_TO_ONE_CLASS_OVER);
                SConfirmDialogUtils.show(this, R.drawable.ic_room_sdk_dialog_confirm_overclass, roomHelper.diamondCount,
                        getString(R.string.dialog_msg_overclass_str),
                        getString(R.string.dialog_exit_title_str), (dialog1, which) -> finish(),
                        getString(R.string.classroom_evaluate_course_str), (dialog, which) -> roomHelper.doClassOver(RoomType.ONETOONE));
                break;
            case RoomConstant.COURSEWARE://切换课件
                roomHelper.changeCourseWare(shareModel);
                break;
            case RoomConstant.REFRESH_CLASSROOM:
                roomHelper.refreshRoom();
                break;
        }
    }

    @Override
    public void onNetWorkMsgReceived(String event) {
        roomHelper.onNetWorkMsgReceived(event);
    }

    @Override
    public void onSetWifiLevel(int level) {
        super.onSetWifiLevel(level);
        if (mBinding.ivWifi == null) return;
        roomHelper.setWifiIcon(mBinding.ivWifi, level);
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
    public void onVolumeChanged(int volume) {
        super.onVolumeChanged(volume);
        double realVolume = UtilsBigDecimal.getDivValue(musicVoiceCurrent, musicVoiceMax);
        RoomApplication.getInstance().getVideoManager().adjustPlaybackSignalVolume((int) (100 * realVolume));
    }
}