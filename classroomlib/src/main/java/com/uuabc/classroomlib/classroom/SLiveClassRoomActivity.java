package com.uuabc.classroomlib.classroom;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.jakewharton.rxbinding2.view.RxView;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.databinding.ActivityClassRoomSLiveBinding;
import com.uuabc.classroomlib.model.PubInfoModel;
import com.uuabc.classroomlib.model.SocketModel.GifValueModel;
import com.uuabc.classroomlib.model.SocketModel.OnlineUserModel;
import com.uuabc.classroomlib.model.SocketModel.SShareModel;
import com.uuabc.classroomlib.model.SocketModel.TopicModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.model.TopModel;
import com.uuabc.classroomlib.model.TopThreeModel;
import com.uuabc.classroomlib.utils.ExceptionUtil;
import com.uuabc.classroomlib.utils.JsonUtils;
import com.uuabc.classroomlib.utils.PointUtil;
import com.uuabc.classroomlib.utils.SConfirmDialogUtils;
import com.uuabc.classroomlib.utils.SocketIoUtils;
import com.uuabc.classroomlib.utils.UtilsBigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.socket.client.Socket;

@SuppressLint("CheckResult")
public class SLiveClassRoomActivity extends BaseClassRoomActivity {
    private ActivityClassRoomSLiveBinding mBinding;
    private SLiveClassRoomHelper roomHelper;
    private ObjectAnimator moveIn;
    private ObjectAnimator moveOut;
    private TopModel.RankBean myRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_class_room_s_live);
        mBinding = DataBindingUtil.bind(mChildView);
        mBinding.setHandler(roomHelper = new SLiveClassRoomHelper(mBinding));
        setListener();
        initAnimator();

        if (!RoomApplication.getInstance().isTable && RoomApplication.getInstance().isFirstInterLive) {
            RoomApplication.getInstance().isFirstInterLive = false;
            restartClassRoom(this);
        }
    }

    @Override
    public void onCustomResume() {
        if (!RoomApplication.getInstance().isTable) {
            restartClassRoom(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBinding.wvCourseware.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        roomHelper.record(RoomConstant.RECORD_EXIT, "");
        roomHelper.dismissBaseDialog();
        mBinding.clCoursewareContainer.removeAllViews();
        mBinding.wvCourseware.destoryWebView();
        mBinding.answerView.stopPlayVideo();
        mBinding.answerResultView.stopPlayVideo();
    }

    @Override
    public void permissionsGranted() {
        roomHelper.record(RoomConstant.RECORD_ENTER, "");
        roomHelper.refreshRoom();
    }

    @Override
    public void onBackPressed() {
        roomHelper.loginOutNewClassRoom();
    }

    private void initAnimator() {
        moveIn = ObjectAnimator.ofFloat(mBinding.topView, "translationY", -1000, 0f).setDuration(1000);
        moveIn.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mBinding.topView.startDiaAnimation();
                delayedClose();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        moveOut = ObjectAnimator.ofFloat(mBinding.topView, "translationY", 0f, 1000).setDuration(1000);
        moveOut.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mBinding.topView.setVisibility(View.GONE);
                mBinding.topView.setDefault();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void setListener() {
        roomHelper.setNewWebViewListener(mBinding.wvCourseware, mBinding.tvPageNum);

        mBinding.blBoard.getMyBoardView().setOnPathListener(null);

        /*
          画笔颜色选择事件
         */
        mBinding.rgColor.setOnCheckedChangeListener((group, checkedId) -> roomHelper.onColorCheckedChanged(checkedId));

        /*
          画笔大小的选择事件
         */
        mBinding.rgSize.setOnCheckedChangeListener((group, checkedId) -> roomHelper.onSizeCheckedChanged(checkedId));

        RxView.clicks(mBinding.llSendStar).throttleFirst(3, TimeUnit.SECONDS).subscribe(o -> roomHelper.sendStar());

        mBinding.ivClose.setOnClickListener(v -> roomHelper.loginOutNewClassRoom());
        mBinding.ivRefresh.setOnClickListener(v -> roomHelper.showNewRefreshTipDialog());
        mBinding.ibTechnicalSupport.setOnClickListener(v -> roomHelper.showNewSupportDialog());
        mBinding.ivPrePage.setOnClickListener(v -> roomHelper.turnPrePage());
        mBinding.ivNextPage.setOnClickListener(v -> roomHelper.turnNextPage());
        mBinding.tvClear.setOnClickListener(v -> roomHelper.clearPaint());
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
            case RoomConstant.ENTER_SUCCESS://登入成功
                List<OnlineUserModel> userModelList = JsonUtils.parseArray(data[0], OnlineUserModel.class);
                roomHelper.enterSuccess(userModelList);
                sendStudents(roomHelper.students, RoomConstant.CLASS_TYPE_LIVE);
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
                PointUtil.onEvent(this, RoomConstant.LIVE_RECONNECT);
                mBinding.viewNetworkTips.doSocketConnect();
                break;
            case Socket.EVENT_DISCONNECT:
                mBinding.viewNetworkTips.doSocketInConnect();
                break;
            case RoomConstant.TOPIC://开始答题
//                TopicModel topicModel = SocketIoUtils.parseData(TopicModel.class, data[0]);
//                doTopicEvent(topicModel);
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
            case RoomConstant.FLOWER://处理其他学生赠送星星
                roomHelper.doFlower(shareModel, userModel);
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
                SConfirmDialogUtils.show(this, R.drawable.ic_room_sdk_dialog_confirm_overclass,
                        getString(R.string.dialog_live_over_class_msg_ss_str),
                        getString(R.string.dialog_exit_title_str), (dialog1, which) -> finish());
                break;
        }
    }

    /**
     * 处理答题事件
     */
    private void doTopicEvent(TopicModel topicModel) {
        if (topicModel == null || topicModel.getValue() == null) return;
        switch (topicModel.getAction()) {
            case RoomConstant.START:
                mBinding.answerResultView.setVisibility(View.GONE);
                roomHelper.startAnswer(topicModel);
                break;
            case RoomConstant.RESULT:
                roomHelper.doTopicResult(topicModel);
                break;
            case RoomConstant.STAT:
                roomHelper.doStat(topicModel);
                break;
            case RoomConstant.FINISHED:
                TopThreeModel topThreeModel = SocketIoUtils.parseData(TopThreeModel.class, new Gson().toJson(topicModel.getValue()));
                if (topThreeModel.getRank().size() > 0) {
                    mBinding.topView.setVisibility(View.VISIBLE);
                    mBinding.topView.setData(topThreeModel);
                    moveIn.start();
                    for (TopThreeModel.RankBean rankBean : topThreeModel.getRank()) {
                        if (SPUtils.getInstance().getInt(RoomConstant.USER_ID) == rankBean.getId()) {
                            roomHelper.updateDiamondCount(rankBean.getDiamond());
                            break;
                        }
                    }
                }

                if (topThreeModel.getItem() != null && !topThreeModel.getItem().isTasked()) {
                    roomHelper.doTopicResult(topThreeModel.getItem());
                }
                mBinding.answerView.setVisibility(View.GONE);
                break;
            case RoomConstant.CLOSED:
                mBinding.answerView.setVisibility(View.GONE);
                mBinding.answerResultView.setVisibility(View.GONE);
                mBinding.answerView.stopPlayVideo();
                mBinding.answerResultView.stopPlayVideo();
                TopModel topModel = SocketIoUtils.parseData(TopModel.class, new Gson().toJson(topicModel.getValue()));
                roomHelper.topRank = topModel.getList();
                for (TopModel.RankBean rankBean : topModel.getList()) {
                    if (SPUtils.getInstance().getInt(RoomConstant.USER_ID) == rankBean.getUid()) {
                        roomHelper.updateGoldCount(rankBean.getUb());
                        myRank = rankBean;
                        break;
                    }
                }
                break;
            case RoomConstant.PUBINFO:
                PubInfoModel pubInfoModel = SocketIoUtils.parseData(PubInfoModel.class, new Gson().toJson(topicModel.getValue()));
                roomHelper.topRank = pubInfoModel.getRank();
                for (TopModel.RankBean rankBean : pubInfoModel.getRank()) {
                    if (SPUtils.getInstance().getInt(RoomConstant.USER_ID) == rankBean.getUid()) {
                        myRank = rankBean;
                        break;
                    }
                }
                break;
            case RoomConstant.CENTER:
                roomHelper.updateAnswerTime(topicModel);
                break;
        }
    }

    private void delayedClose() {
        new Handler().postDelayed(() -> moveOut.start(), 3000);
    }

    @Override
    public void onNetWorkMsgReceived(String event) {
        roomHelper.onNetWorkMsgReceived(event);
    }

    @Override
    public void onSetWifiLevel(int level) {
        super.onSetWifiLevel(level);
        if (mBinding == null) return;
        roomHelper.setWifiIcon(mBinding.ivWifi, level);
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