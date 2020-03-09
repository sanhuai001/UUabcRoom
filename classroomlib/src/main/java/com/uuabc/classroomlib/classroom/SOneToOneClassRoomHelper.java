package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.JsonSyntaxException;
import com.othershe.baseadapter.ViewHolder;
import com.othershe.baseadapter.base.CommonBaseAdapter;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.common.BaseCommonActivity;
import com.uuabc.classroomlib.common.RxTimer;
import com.uuabc.classroomlib.databinding.ActivityClassRoomSOneToOneBinding;
import com.uuabc.classroomlib.model.OneToOneCourseResult;
import com.uuabc.classroomlib.model.SClassRoomResult;
import com.uuabc.classroomlib.model.SRChartResult;
import com.uuabc.classroomlib.model.SUserModel;
import com.uuabc.classroomlib.model.SocketModel.ChartModel;
import com.uuabc.classroomlib.model.SocketModel.DrawTextModel;
import com.uuabc.classroomlib.model.SocketModel.FlowerModel;
import com.uuabc.classroomlib.model.SocketModel.OnlineUserModel;
import com.uuabc.classroomlib.model.SocketModel.SShareModel;
import com.uuabc.classroomlib.model.SocketModel.SwitchModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.retrofit.ApiRetrofit;
import com.uuabc.classroomlib.utils.CompatUtil;
import com.uuabc.classroomlib.utils.ExceptionUtil;
import com.uuabc.classroomlib.utils.JsonUtils;
import com.uuabc.classroomlib.utils.MediaPlayerUtil;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.utils.PointUtil;
import com.uuabc.classroomlib.utils.RxTimerUtil;
import com.uuabc.classroomlib.utils.SocketIoUtils;
import com.uuabc.roomvideo.model.EnterRoomModel;
import com.uuabc.roomvideo.model.RoomVideoType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static io.agora.rtc.Constants.QUALITY_BAD;
import static io.agora.rtc.Constants.QUALITY_EXCELLENT;
import static io.agora.rtc.Constants.QUALITY_GOOD;
import static io.agora.rtc.Constants.QUALITY_POOR;
import static io.agora.rtc.Constants.QUALITY_VBAD;

@SuppressLint({"CheckResult", "SetTextI18n", "SimpleDateFormat"})
public class SOneToOneClassRoomHelper extends SBaseClassRoomHelper<ActivityClassRoomSOneToOneBinding> {
    private CommonBaseAdapter<ChartModel> mMsgAdapter;

    SOneToOneClassRoomHelper(ActivityClassRoomSOneToOneBinding binding) {
        super(binding);
        classType = RoomConstant.CLASS_TYPE_ONE_TO_ONE;
        setVolumeDrawables();
        initView();
    }

    private void initView() {
        mBinding.rbSize1.setChecked(true);
        mBinding.rbRed.setChecked(true);

        initMsgAdapter();

        setClassRoomLayout(mBinding.clRoomContent);

        ViewTreeObserver vto = mBinding.clRoomContent.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int containerWidth = mBinding.clCoursewareContainer.getWidth();
                int containerHeight = mBinding.clCoursewareContainer.getHeight();
                mBinding.clRoomContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                double coursewareProportion = new BigDecimal(RoomApplication.getInstance().PC_WIDTH / RoomApplication.getInstance().PC_HEIGHT)
                        .setScale(4, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                double containerProportion = new BigDecimal((double) containerWidth / containerHeight)
                        .setScale(4, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
                //课件
                ViewGroup.LayoutParams courseWareLp = mBinding.wvCourseware.getLayoutParams();
                if (coursewareProportion > containerProportion) {
                    courseWareLp.width = containerWidth;
                    courseWareLp.height = (int) (containerWidth / coursewareProportion);
                } else {
                    courseWareLp.height = containerHeight;
                    courseWareLp.width = (int) (containerHeight * coursewareProportion);
                    mBinding.clCoursewareContainer.setBackgroundColor(CompatUtil.getColor(mContext, R.color.white));
                }
                mBinding.wvCourseware.setLayoutParams(courseWareLp);
                mBinding.wvCourseware.requestLayout();
                //画板，画笔
                mBinding.boardView.setLayoutParams(courseWareLp.width, courseWareLp.height, containerWidth, containerHeight);

                mScale = RoomApplication.getInstance().getScale(courseWareLp.width);
                mBinding.boardView.setPaintSize(2 / mScale);

                onNetWorkMsgReceived(NetworkUtils.isConnected() ? RoomConstant.NET_WORK_CONNECTED : RoomConstant.NET_WORK_INCONNECTED);

                ((BaseWifiListenerActivity) mContext).refreshWifiState();

                mBinding.boardView.setCanDraw(true);
            }
        });

        setViewRadius(mBinding.clCoursewareContainer, 25);
        setViewRadius(mBinding.ivGif, 25);
        setViewRadius(mBinding.flTeacher, 17);
        setViewRadius(mBinding.flStudent, 17);
        mBinding.ivActive.setImageAssetsFolder("images/");
    }

    private void initMsgAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        int userId = SPUtils.getInstance().getInt(RoomConstant.USER_ID);
        mBinding.rvChatList.setLayoutManager(layoutManager);
        mBinding.rvChatList.setAdapter(mMsgAdapter = new CommonBaseAdapter<ChartModel>(mContext, null, false) {
            @Override
            protected void convert(ViewHolder viewHolder, ChartModel chartModel, int i) {
                TextView tvMsgLeft = viewHolder.getView(R.id.tv_msg_left);
                TextView tvMsgRight = viewHolder.getView(R.id.tv_msg_right);

                boolean isMine = TextUtils.equals(chartModel.getSendId(), String.valueOf(userId));
                tvMsgLeft.setVisibility(isMine ? View.GONE : View.VISIBLE);
                tvMsgRight.setVisibility(isMine ? View.VISIBLE : View.GONE);
                if (isMine) {
                    tvMsgRight.setBackgroundResource(R.drawable.room_sdk_shape_msg_right_blue);
                    tvMsgRight.setText(chartModel.getMsg());
                } else {
                    tvMsgLeft.setBackgroundResource(R.drawable.room_sdk_shape_msg_left_deep_blue);
                    tvMsgLeft.setText(chartModel.getMsg());
                }
            }

            @Override
            protected int getItemLayoutId() {
                return R.layout.item_room_sdk_chart_msg;
            }
        });
    }

    void onColorCheckedChanged(int checkedId) {
        PointUtil.onEvent(mContext, RoomConstant.ONE_TO_ONE_CLICK_PEN_COLOR);
        mBinding.boardView.setPaintColor(getPaintColorStr(checkedId));
    }

    void onSizeCheckedChanged(int checkedId) {
        PointUtil.onEvent(mContext, RoomConstant.ONE_TO_ONE_CLICK_PEN_SIZE);
        mBinding.boardView.setPaintSize(getPaintSize(checkedId) / mScale);
    }

    @Override
    public void turnPage(int currentPage) {
        mBinding.wvCourseware.loadUrl("javascript:PageMgr.turnPage(" + mCurrentCoursewarePage + ")");
        mBinding.tvPageNum.setText(mCurrentCoursewarePage + "/" + totlePage);
        clearBoard();
    }

    /**
     * 清空画板的方法
     */
    void clearBoard() {
        mBinding.boardView.clearAllBoard();
    }

    /**
     * 鼠标移动的处理方法
     */
    void doPosition(SShareModel shareModel, int userId) {
        if (shareModel.getData() == null) return;
        doPosition(shareModel, mBinding.boardView, userId);
    }

    /**
     * 聊天
     */
    void loadChart(SShareModel shareModel, UserModel userModel) {
        if (shareModel.getData() == null || userModel == null) return;
        List<ChartModel> chartModelList = new ArrayList<>();
        ChartModel chartModel = new ChartModel();
        chartModel.setUserName(userModel.getName());
        chartModel.setUserPhoto(userModel.getPhoto());
        chartModel.setMsg(String.valueOf(shareModel.getData()));
        chartModel.setSendId(userModel.getIdStr());
        chartModelList.add(chartModel);
        mBinding.ivChatNodata.setVisibility(View.GONE);
        mMsgAdapter.setLoadMoreData(chartModelList);
        RxTimerUtil.timer(500, number -> mBinding.rvChatList.scrollToPosition(mMsgAdapter.getItemCount() - 1));
    }

    /**
     * 添加文字
     */
    void doDrawText(SShareModel shareModel) {
        try {
            DrawTextModel value = SocketIoUtils.parseData(DrawTextModel.class, (Map) shareModel.getData());
            mBinding.boardView.drawText(value, mScale);
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.TEXTSTART, classType, e.getMessage());
        }
    }

    /**
     * 涂鸦开关
     */
    void controlDraw(SShareModel shareModel) {
        if (shareModel.getData() == null) return;
        try {
            SwitchModel switchModel = SocketIoUtils.parseData(SwitchModel.class, (Map) shareModel.getData());
            boolean isOpen = ObjectUtil.getBoolean(switchModel.getValue());
            mBinding.boardView.setCanDraw(isOpen);
            doCanDraw(isOpen);
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.DRAWING, classType, e.getMessage());
        }
    }

    /**
     * 涂鸦切换
     */
    private void doCanDraw(boolean canDraw) {
        mBinding.clPaint.setVisibility(canDraw ? View.VISIBLE : View.GONE);
    }

    /**
     * 互动开关
     */
    void controlAnimate(SShareModel shareModel) {
        if (shareModel.getData() == null) return;
        try {
            SwitchModel value = SocketIoUtils.parseData(SwitchModel.class, (Map) shareModel.getData());
            for (int i = 0; i < mBinding.boardView.getBoardSize(); i++) {
                if (value.getId() == SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                    mBinding.boardView.setCanAnimate(value.isOpen());
                    mBinding.wvCourseware.setCanAnimate(value.isOpen());
                    controlShowActive(value.isOpen());
                    break;
                }
            }
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.ANIMATE, classType, e.getMessage());
        }
    }

    private void controlShowActive(boolean isActive) {
        try {
            if (isActive) {
                if (mBinding.ivActive.isShown()) mBinding.ivActive.cancelAnimation();
                mBinding.ivActive.setAnimation("active.json");
                showActive();
            } else {
                if (mBinding.ivActive.isShown()) mBinding.ivActive.cancelAnimation();
                mBinding.ivActive.setAnimation("active_closed.json");
                showActive();
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * 显示互动动画
     */
    private void showActive() {
        mBinding.ivActive.setVisibility(View.VISIBLE);
        mBinding.ivActive.cancelAnimation();
        mBinding.ivActive.playAnimation();

        activeRxTimer = activeRxTimer == null ? new RxTimer() : activeRxTimer;
        activeRxTimer.timer(3000, number -> {
            mBinding.ivActive.cancelAnimation();
            mBinding.ivActive.setVisibility(View.GONE);
        });
    }

    /**
     * 奖励钻石后更新的钻石列表
     */
    void doFlower(SShareModel shareModel) {
        if (shareModel.getData() == null) return;
        RxTimerUtil.timer(200, number -> {
            String jsonStr = JsonUtils.entityToJsonString(shareModel.getData());
            List<FlowerModel.UsersBean> users = JsonUtils.parseArray(jsonStr, FlowerModel.UsersBean.class);
            if (users == null || users.size() == 0) return;
            CustomLottieAnimationView animationView = new CustomLottieAnimationView(mContext);
            diamondCount = ObjectUtil.getIntValue(users.get(0).getTotal());
            setDrawLeftImg(diamondCount == 0 ? R.drawable.ic_room_sdk_live_class_diamond_gray : R.drawable.ic_room_sdk_class_room_blue_diamond, mBinding.tvDiamond);
            animationView.showDiamonds(mBinding.rlFlower, mBinding.tvDiamond, diamondCount);
            MediaPlayerUtil.getInstance().resetToPlay(mContext, R.raw.magic);
        });
    }

    /**
     * 用户进入教室 分配画笔，画笔,上台view
     */
    void interRoom(OnlineUserModel userEnterModel) {
        if (userEnterModel == null || userEnterModel.getInfo() == null) return;

        UserModel userModel = userEnterModel.getInfo();
        userModel.setId(userEnterModel.getUser_id());
        userModel.setType(userEnterModel.getRole());
        if (userEnterModel.getRole() == RoomConstant.TEACHER_TYPE) {
            mBinding.boardView.teacherInterRoom(userModel);
            mBinding.ivNextPage.setVisibility(View.GONE);
            mBinding.ivPrePage.setVisibility(View.GONE);
            teacherId = String.valueOf(userModel.getId());
            mBinding.tvTeacherName.setVisibility(View.VISIBLE);
            mBinding.tvTeacherName.setText(TextUtils.isEmpty(userModel.getName()) ? "" : userModel.getName());
        }
    }

    /**
     * 切换课件
     */
    void changeCourseWare(SShareModel shareModel) {
        if (shareModel == null || shareModel.getData() == null) return;

        try {
            OneToOneCourseResult.CourselistBean courseWareModel = SocketIoUtils.parseData(OneToOneCourseResult.CourselistBean.class,
                    (Map) shareModel.getData());

            mCurrentCoursewarePage = courseWareModel.getPage();
            mBinding.wvCourseware.setUrl(courseWareModel.getUrl());
            mBinding.wvCourseware.loadUrl(courseWareModel.getUrl());
            mBinding.wvCourseware.loadUrl("javascript:PageMgr.hasAuthority(true)");
            mBinding.tvPageNum.setText(mCurrentCoursewarePage + "/" + totlePage);
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.COURSEWARE, classType, e.getMessage());
        }
    }

    void enterSuccess() {
        String uid = 1 + String.valueOf(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        EnterRoomModel roomModel = new EnterRoomModel();
        if (students != null && students.size() > 0) {
            doCanDraw(students.get(0).isDrawing());
            mBinding.boardView.setCanDraw(students.get(0).isDrawing());
            mBinding.boardView.setCanAnimate(students.get(0).isAnimate());
            mBinding.wvCourseware.setCanAnimate(students.get(0).isAnimate());
            controlShowActive(students.get(0).isAnimate());
            mBinding.ivNoCamera.setVisibility(students.get(0).isCamera() ? View.GONE : View.VISIBLE);
        }

        Observable.just("isAgora")
                .subscribeOn(Schedulers.io())
                .doOnNext(s -> RoomApplication.getInstance().getVideoManager().leaveRoom())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((response) -> {
                    RoomApplication.getInstance().initRoomVideoSDK(RoomVideoType.AGORA, agoraKey);
                    RoomApplication.getInstance().enableAudioVolumeIndication(300);
                    String filePath = RoomApplication.getInstance().getExternalCacheDir() + RoomConstant.AGORA_LOG_S_ONE_TO_ONE;
                    if (FileUtils.createOrExistsFile(filePath)) {
                        RoomApplication.getInstance().getVideoManager().setLogFile(filePath);
                    }
                    roomModel.setUid(uid);
                    roomModel.setAppId(agoraKey);
                    roomModel.setRoomId("td" + roomId);
                    RoomApplication.getInstance().getVideoManager().joinRoom(roomModel);
                }, throwable -> {
                });
    }

    /**
     * 声网 talkCloud 切换,刷新教室
     */
    void doSwithVideo() {
        refreshRoom();
    }

    @Override
    public void checkRoomResultSuccess(SClassRoomResult result, int srvTime, boolean isLoadedReply) {
        mBinding.boardView.clearAllBoard();
        if (isLoadedReply) {
            if (courseCaseLoaded) {
                mBinding.wvCourseware.reload();
            } else {
                mBinding.wvCourseware.setUrl(result.getCourseware());
                mBinding.wvCourseware.loadUrl(result.getCourseware());
                mBinding.wvCourseware.loadUrl("javascript:PageMgr.hasAuthority(true)");
            }
        } else {
            mBinding.wvCourseware.setCanAnimate(true);
            mBinding.wvCourseware.loadUrl(RoomConstant.LOAD_REPLY_URL);
        }
        mBinding.tvPageNum.setText(mCurrentCoursewarePage + "/" + totlePage);

        int totleSeconds = srvTime - result.getStart_time();
        totleSeconds = totleSeconds % (60 * 60 * 24);
        mBinding.tvDuration.setBase(SystemClock.elapsedRealtime() - totleSeconds * 1000);
        mBinding.tvDuration.setOnChronometerTickListener(this::doSChronometer);
        mBinding.tvDuration.start();

        mBinding.tvCourseNumber.setVisibility(View.VISIBLE);
        mBinding.tvCourseNumber.setText(mContext.getString(R.string.common_course_id, skuId = ObjectUtil.getLongValue(result.getCourse_num())));
        mBinding.tvDiamond.setText("x" + diamondCount);
        setDrawLeftImg(diamondCount == 0 ? R.drawable.ic_room_sdk_live_class_diamond_gray : R.drawable.ic_room_sdk_class_room_blue_diamond, mBinding.tvDiamond);

        if (sMyselfModel != null) {
            mBinding.tvMineName.setVisibility(View.VISIBLE);
            mBinding.tvMineName.setText(TextUtils.isEmpty(sMyselfModel.getNickname()) ? "" : sMyselfModel.getNickname());
        }
        mBinding.boardView.setMyselfViewTag();

        readSpeak();
    }

    /**
     * 恢复课件互动动画
     */
    @Override
    public void showAnimateLog() {
        if (animationLogList == null) return;
        for (int i = 0; i < animationLogList.size(); i++) {
            if (animationLogList.get(i) != null && animationLogList.get(i).getPageIndex() == mCurrentCoursewarePage)
                mBinding.wvCourseware.loadUrl("javascript:PageMgr.receiveMsg('" + animationLogList.get(i).getAnimateLog() + "')");
        }
    }

    /**
     * 获取聊天内容
     */
    private void readSpeak() {
        if (!((BaseCommonActivity) mContext).checkNetWork()) {
            doNetWorkConnectFail(mBinding.viewNetworkTips);
            return;
        }

        ApiRetrofit.getInstance().readChart()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultModel -> {
                    if (((BaseCommonActivity) mContext).isDestroyed()) return;

                    if (!resultModel.isSuccess()) {
                        return;
                    }

                    SRChartResult chartResult = resultModel.getResult();
                    List<SUserModel> result = chartResult.getList();
                    List<ChartModel> chartList = new ArrayList<>();
                    if (result == null || result.size() == 0) {
                        mBinding.ivChatNodata.setVisibility(View.VISIBLE);
                        mMsgAdapter.setNewData(chartList);
                        return;
                    } else {
                        mBinding.ivChatNodata.setVisibility(View.GONE);
                    }
                    ChartModel model;
                    for (int i = 0; i < result.size(); i++) {
                        model = new ChartModel();
                        model.setUserName(result.get(i).getNickname());
                        model.setMsg(result.get(i).getMessage());
                        model.setUserPhoto(result.get(i).getAvatar());
                        model.setSendId(result.get(i).getUser_id());
                        chartList.add(model);
                    }
                    mMsgAdapter.setNewData(chartList);
                    new RxTimer().timer(500, number -> mBinding.rvChatList.scrollToPosition(mMsgAdapter.getItemCount() - 1));
                }, throwable -> {
                });
    }

    void onRtcMsgReceived(String event, String uid, String errorMsg) {
        String uidStr = String.valueOf(uid);
        switch (event) {
            case RoomConstant.ROOM_JOINED:
                record(RoomConstant.RECORD_VIDEO_PUB, "");
                setupLocalVideo(mBinding.flStudent, 1 + String.valueOf(SPUtils.getInstance().getInt(RoomConstant.USER_ID)));
                break;
            case RoomConstant.ROOM_USER_JOINED:
                if (TextUtils.isEmpty(uidStr)) return;
                //自己
                if (TextUtils.equals("1" + SPUtils.getInstance().getInt(RoomConstant.USER_ID), uidStr)) {
                    return;
                }

                //老师
                if (uidStr.startsWith("2")) {
                    RoomApplication.getInstance().getVideoManager().setupRemoteVideo(mContext, mBinding.flTeacher, String.valueOf(uid), true);

                    if (!mBinding.ivVolumeTea.isShown())
                        mBinding.ivVolumeTea.setVisibility(View.VISIBLE);
                    if (!mBinding.clNet.isShown()) mBinding.clNet.setVisibility(View.VISIBLE);

                    mBinding.tvTeacherName.setVisibility(View.VISIBLE);
                    mBinding.ivNextPage.setVisibility(View.GONE);
                    mBinding.ivPrePage.setVisibility(View.GONE);
                    return;
                }
                break;
            case RoomConstant.ROOM_USER_LEAVED:
                //老师
                if (!TextUtils.isEmpty(uidStr) && uidStr.startsWith("2")) {
                    RoomApplication.getInstance().getVideoManager().onRemoveView(mBinding.flTeacher);
                    mBinding.ivVolumeTea.setVisibility(View.GONE);
                    mBinding.clNet.setVisibility(View.GONE);
                    mBinding.tvTeacherName.setVisibility(View.GONE);
                    return;
                }
                break;
            case RoomConstant.ROOM_ERROR:
                ExceptionUtil.sendException(RoomConstant.JOIN_AGORA_ERROR, classType, errorMsg);
                break;
        }
    }

    void onNetWorkMsgReceived(String event) {
        onNetWorkMsgReceived(mBinding.viewNetworkTips, event, RoomConstant.ONE_TO_ONE_ERROR);
    }

    @Override
    public void setTeacherInfo(int teacherId, String teacherName) {
        mBinding.boardView.setTeacherInfo(teacherId, teacherName);
        mBinding.tvTeacherName.setText(teacherName);
    }

    @Override
    public void doNetWorkConnectFail() {
        doNetWorkConnectFail(mBinding.viewNetworkTips);
    }

    @Override
    public void loadUrl() {
        if (TextUtils.isEmpty(mCurrentUrl)) return;
        mBinding.wvCourseware.setUrl(mCurrentUrl);
        mBinding.wvCourseware.loadUrl(mCurrentUrl);
        mBinding.wvCourseware.loadUrl("javascript:PageMgr.hasAuthority(true)");
    }

    void doNetEvent(HashMap<Integer, Integer> map) {
        if (!ObjectUtil.isEmpty(teacherId)) {
            int teacher = Float.valueOf("2" + teacherId).intValue();
            if (map.containsKey(teacher)) {
                switch (map.get(teacher)) {
                    case QUALITY_EXCELLENT:
                        mBinding.ivSignal.setImageResource(R.drawable.icon_signal_3);
                        break;
                    case QUALITY_GOOD:
                    case QUALITY_POOR:
                        mBinding.ivSignal.setImageResource(R.drawable.icon_signal_2);
                        break;
                    case QUALITY_BAD:
                    case QUALITY_VBAD:
                        mBinding.ivSignal.setImageResource(R.drawable.icon_signal_1);
                        break;
                }
            }
        }
    }

    void doVolumeEvent(HashMap<Integer, Integer> map) {
        if (map.containsKey(0)) {
            setVolumeLevel(mBinding.ivVolume, map.get(0), false);
        }

        if (!ObjectUtil.isEmpty(teacherId)) {
            int teacher = Float.valueOf("2" + teacherId).intValue();
            if (map.containsKey(teacher)) {
                setVolumeLevel(mBinding.ivVolumeTea, map.get(teacher), false);
            }
        }
    }
}

