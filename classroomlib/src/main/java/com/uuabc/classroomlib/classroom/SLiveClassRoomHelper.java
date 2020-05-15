package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.JsonSyntaxException;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.common.BaseCommonActivity;
import com.uuabc.classroomlib.common.BaseCommonFragment;
import com.uuabc.classroomlib.databinding.ActivityClassRoomSLiveBinding;
import com.uuabc.classroomlib.main.AnswerRankDialog;
import com.uuabc.classroomlib.model.FlowerCountModel;
import com.uuabc.classroomlib.model.SClassRoomResult;
import com.uuabc.classroomlib.model.SaveQuestionResult;
import com.uuabc.classroomlib.model.SocketModel.AnswerModel;
import com.uuabc.classroomlib.model.SocketModel.AnswerResultModel;
import com.uuabc.classroomlib.model.SocketModel.AnswerStatModel;
import com.uuabc.classroomlib.model.SocketModel.AnswerTimeModel;
import com.uuabc.classroomlib.model.SocketModel.ChartModel;
import com.uuabc.classroomlib.model.SocketModel.DrawTextModel;
import com.uuabc.classroomlib.model.SocketModel.OnlineUserModel;
import com.uuabc.classroomlib.model.SocketModel.SShareModel;
import com.uuabc.classroomlib.model.SocketModel.TopicModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.model.TopModel;
import com.uuabc.classroomlib.retrofit.ApiRetrofit;
import com.uuabc.classroomlib.retrofit.RequestBuilder;
import com.uuabc.classroomlib.utils.CompatUtil;
import com.uuabc.classroomlib.utils.ExceptionUtil;
import com.uuabc.classroomlib.utils.MediaPlayerUtil;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.utils.PointUtil;
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

@SuppressLint({"CheckResult", "SimpleDateFormat", "SetTextI18n"})
public class SLiveClassRoomHelper extends SBaseClassRoomHelper<ActivityClassRoomSLiveBinding> {
    //    private QBadgeView qbvChat;
    private List<UserModel> studentModels = new ArrayList<>();
    private List<BaseCommonFragment> listFragment = new ArrayList<>();
    //    private List<ChartModel> chartModelList = new ArrayList<>();
    private int myStarCount;
    //    private boolean isSeat = true;//是否处于座位列表
//    private int chatCount;
    List<TopModel.RankBean> topRank;

    SLiveClassRoomHelper(ActivityClassRoomSLiveBinding activityClassRoomLiveBinding) {
        super(activityClassRoomLiveBinding);
//        qbvChat = new QBadgeView(mContext);
//        qbvChat.setExactMode(true);
//        qbvChat.setBadgeTextSize(8, true);
//        qbvChat.setGravityOffset(30, 10, true);
        classType = RoomConstant.CLASS_TYPE_LIVE;

        initView();
    }

    private void initView() {
        mBinding.flowView.setOnClickListener(v -> showAnswerRankDialog(topRank));

//        mBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        mBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
//        mBinding.tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
//        mBinding.tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
//        mBinding.tabLayout.setSelectedTabIndicatorHeight(5);
//        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("座位表"));
//        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("聊天室"));
//        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                if (tab.getPosition() == 0) {
//                    isSeat = true;
//                    if (chatCount != 0) {
//                        qbvChat.setVisibility(View.VISIBLE);
//                        qbvChat.bindTarget(mBinding.tabLayout).setBadgeNumber(chatCount);
//                    }
//
//                    mBinding.tabLayout.setBackgroundResource(R.drawable.room_sdk_shape_deep_blue_chart_up);
//                    mBinding.vpContainer.setBackgroundResource(R.drawable.room_sdk_shape_deep_blue_chart_down);
//                } else {
//                    qbvChat.setVisibility(View.GONE);
//                    chatCount = 0;
//                    isSeat = false;
//
//                    mBinding.tabLayout.setBackgroundResource(R.drawable.room_sdk_shape_blue_chart_up_s);
//                    mBinding.vpContainer.setBackgroundResource(R.drawable.room_sdk_shape_blue_chart_down_s);
//                }
//                mBinding.vpContainer.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        listFragment.add(SeatListFragment.newInstant());
//        SeatListFragment.newInstant().setSiShu();
        listFragment.add(ChartFragment.Companion.newInstant());
        mBinding.vpContainer.setAdapter(new LiveFragmentPagerAdapter(((BaseCommonActivity) mContext).getSupportFragmentManager(), listFragment));

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
                mBinding.blBoard.setLayoutParams(courseWareLp.width, courseWareLp.height, containerWidth, containerHeight);

                mScale = new BigDecimal(RoomApplication.getInstance().PC_WIDTH / courseWareLp.width)
                        .setScale(4, BigDecimal.ROUND_HALF_UP)
                        .floatValue();
                mBinding.blBoard.setPaintSize(2 / mScale);

                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) mBinding.flowView.getLayoutParams();
                marginLayoutParams.leftMargin = (int) (mBinding.clCoursewareContainer.getWidth() - mBinding.flowView.getWidth() / 0.8);
                marginLayoutParams.topMargin = mBinding.flowView.getHeight();
                mBinding.flowView.setLayoutParams(marginLayoutParams);

                onNetWorkMsgReceived(NetworkUtils.isConnected() ? RoomConstant.NET_WORK_CONNECTED : RoomConstant.NET_WORK_INCONNECTED);
                mBinding.blBoard.setCanDraw(true);
            }
        });

        setViewRadius(mBinding.clCoursewareContainer, 25);
        setViewRadius(mBinding.ivGif, 25);
        setViewRadius(mBinding.flTeacher, 17);
        mBinding.lavLoading.setImageAssetsFolder("images/");
    }

    void onColorCheckedChanged(int checkedId) {
        mBinding.blBoard.setPaintColor(getPaintColorStr(checkedId));
        PointUtil.onEvent(mContext, RoomConstant.LIVE_CLICK_PEN_COLOR);
    }

    void onSizeCheckedChanged(int checkedId) {
        mBinding.blBoard.setPaintSize(getPaintSize(checkedId) / mScale);
        PointUtil.onEvent(mContext, RoomConstant.LIVE_CLICK_PEN_SIZE);
    }

    @Override
    public void checkRoomResultSuccess(SClassRoomResult result, int srvTime, boolean isLoadedReply) {
        mBinding.blBoard.clearAllBoard();
        if (isLoadedReply) {
            if (courseCaseLoaded) {
                mBinding.wvCourseware.reload();
            } else {
                mBinding.wvCourseware.setUrl(result.getCoursewareUrl());
                mBinding.wvCourseware.loadUrl(result.getCoursewareUrl());
                mBinding.wvCourseware.loadUrl("javascript:PageMgr.hasAuthority(true)");
            }
        } else {
            mBinding.blBoard.setCanDraw(false);
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
        mBinding.blBoard.setMyselfViewTag();
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
     * 提交答案
     */
    private void submitAnswer(AnswerResultModel resultModel) {
        if (!((BaseCommonActivity) mContext).checkNetWork()) {
            doNetWorkConnectFail(mBinding.viewNetworkTips);
            return;
        }

        ((BaseCommonActivity) mContext).showProgress();
        ApiRetrofit.getInstance().liveSaveQuestion(new RequestBuilder()
                .build("userId", SPUtils.getInstance().getInt(RoomConstant.USER_ID))
                .build("token", SPUtils.getInstance().getString(RoomConstant.TOKEN))
                .build("topicId", resultModel.getTid())
                .build("option", resultModel.getOption())
                .build("diamond", resultModel.getDiamond())
                .build("t", resultModel.getTime())
                .build("classId", roomId).create())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commonResult -> {
                    if (((BaseCommonActivity) mContext).isDestroyed()) return;
                    ((BaseCommonActivity) mContext).dismissProgress();
                    mBinding.answerView.setVisibility(View.GONE);
                    if (commonResult.isOut()) {
                        ((BaseCommonActivity) mContext).out();
                        return;
                    }
                    if (!commonResult.isResultSuccess()) {
                        responseErrorToast();
                        return;
                    }

                    SaveQuestionResult result = commonResult.getData();
                    showAnswerQuestResult(result, resultModel);
                }, this::responseErrorToast);
    }


    /**
     * 处理答题结果
     */
    private void showAnswerQuestResult(SaveQuestionResult result, AnswerResultModel answerResultModel) {
        if (result == null) return;

        if (result.isCorrect()) {
            //U币数
            setDrawLeftImg(result.getGold_number() == 0 ? R.drawable.ic_room_sdk_ub_gray : R.drawable.ic_room_sdk_dialog_ub, mBinding.tvUb);
            CustomLottieAnimationView animationView = new CustomLottieAnimationView(mContext);
            animationView.showUb(mBinding.rlLottie, mBinding.tvUb, mBinding.clCoursewareContainer, result.getGold_number());
            MediaPlayerUtil.getInstance().resetToPlay(mContext, R.raw.magic);
        }

        mBinding.answerView.setVisibility(View.GONE);
        mBinding.answerView.stopPlayVideo();
        mBinding.answerResultView.setAnswerResultData(result.isCorrect(), mBinding.answerView.answerModel, answerResultModel);
    }

    void updateGoldCount(int gold) {
        //U币数
        setDrawLeftImg(gold == 0 ? R.drawable.ic_room_sdk_ub_gray : R.drawable.ic_room_sdk_dialog_ub, mBinding.tvUb);
        mBinding.tvUb.setText(String.valueOf(gold));
    }

    void updateDiamondCount(int diamond) {
        //钻石数
        int diamondCount;
        if (TextUtils.isEmpty(mBinding.tvDiamond.getText())) {
            diamondCount = 0;
        } else {
            diamondCount = Integer.parseInt(mBinding.tvDiamond.getText().toString()) + diamond;
        }
        setDrawLeftImg(diamond == 0 ? R.drawable.ic_room_sdk_live_class_diamond_gray : R.drawable.ic_room_sdk_class_room_blue_diamond, mBinding.tvDiamond);
        mBinding.tvDiamond.setText(String.valueOf(diamondCount));
    }

    /**
     * 送星星
     */
    private void sendFlowers() {
        if (!((BaseCommonActivity) mContext).checkNetWork()) {
            doNetWorkConnectFail(mBinding.viewNetworkTips);
            return;
        }
        ((BaseCommonActivity) mContext).showProgress();
        ApiRetrofit.getInstance().liveSendFlowers(new RequestBuilder()
                .build("studentId", SPUtils.getInstance().getInt(RoomConstant.USER_ID))
                .build("token", SPUtils.getInstance().getString(RoomConstant.TOKEN))
                .build("classId", roomId).create())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(commonResult -> {
                    if (((BaseCommonActivity) mContext).isDestroyed()) return;
                    ((BaseCommonActivity) mContext).dismissProgress();
                    if (commonResult.isOut()) {
                        ((BaseCommonActivity) mContext).out();
                        return;
                    }
                    if (!commonResult.isResultSuccess()) {
                        responseErrorToast();
                        return;
                    }
                    PointUtil.onEvent(mContext, RoomConstant.LIVE_SEND_STARS);
                    /*送星星动画*/
                    CustomLottieAnimationView animationView = new CustomLottieAnimationView(mContext);
                    animationView.setListener(() -> {
                        if (((BaseCommonActivity) mContext).isDestroyed()) return;
                        HashMap shareMap = getFlowShareMap();
                        RoomApplication.getInstance().sendMessage(RoomConstant.SHARE, SocketIoUtils.getJsonObject(shareMap));
                    });
//                    animationView.showStar(mBinding.rlFlower, mBinding.tvTeacherStar, mBinding.clCoursewareContainer, mBinding.flTeacher.getWidth());
//                    MediaPlayerUtil.getInstance().resetToPlay(mContext, R.raw.magic);

                    FlowerCountModel model = commonResult.getData();
                    if (model != null) {
                        myStarCount = model.getFlowers();
                    } else {
                        myStarCount--;
                    }

                    updateSendStarView();
                }, this::responseErrorToast);
    }

    private void updateSendStarView() {
        mBinding.ivSendStar.setImageResource(myStarCount == 0 ? R.drawable.ic_room_sdk_send_star_over : R.drawable.ic_room_sdk_send_star);
        mBinding.tvSendStar.setText(myStarCount == 0 ? mContext.getString(R.string.live_class_room_send_star_over_str)
                : mContext.getString(R.string.live_class_room_send_star_str, myStarCount));
    }

    /**
     * 送星星
     */
    void sendStar() {
        if (myStarCount < 1) {
            return;
        }

        clickMediaPlayer.resetToPlay(mContext, R.raw.click_sing);
        sendFlowers();
        startRotation(mBinding.ivSendStar);
    }

    void clearPaint() {
        clickMediaPlayer.resetToPlay(mContext, R.raw.click_sing);
        mBinding.blBoard.getMyBoardView().reset();
        startShakeAnimation(mBinding.tvClear);
    }

    /**
     * 清空画板的方法
     */
    public void clearBoard() {
        mBinding.blBoard.clearAllBoard();
        PointUtil.onEvent(mContext, RoomConstant.LIVE_CLICK_CLEAN);
    }

    /**
     * 鼠标移动的处理方法
     */
    void doPosition(SShareModel shareModel, int userId) {
        if (shareModel.getData() == null) return;
        doPosition(shareModel, mBinding.blBoard, userId);
    }

    /**
     * 聊天
     */
    void loadChart(SShareModel shareModel, UserModel userModel) {
        if (shareModel.getData() == null || userModel == null) return;
        ChartModel chartModel = new ChartModel();
        chartModel.setType(userModel.getType());
        chartModel.setUserName(userModel.getName());
        chartModel.setUserPhoto(userModel.getPhoto());
        chartModel.setMsg(String.valueOf(shareModel.getData()));
        chartModel.setSendId(String.valueOf(userModel.getId()));
        ChartFragment.Companion.newInstant().addChartModel(chartModel, true);
        setTipNum();
    }

    private void setTipNum() {
//        if (isSeat) {
//            chatCount += 1;
//            qbvChat.setVisibility(View.VISIBLE);
//            qbvChat.bindTarget(mBinding.tabLayout).setBadgeText("");
//        }
    }

    /**
     * 添加文字
     */
    void doDrawText(SShareModel shareModel) {
        try {
            DrawTextModel value = SocketIoUtils.parseData(DrawTextModel.class, (Map) shareModel.getData());
            mBinding.blBoard.drawText(value, mScale);
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.TEXTSTART, classType, e.getMessage());
        }
    }

    /**
     * 处理其他学生赠送星星
     */
    void doFlower(SShareModel shareModel, UserModel userModel) {
        if (shareModel.getData() == null) return;

        int count = ObjectUtil.getIntValue(shareModel.getData());
        mBinding.tvTeacherStar.setText(String.valueOf(count));
        Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.room_sdk_diamond_text_scale);
        mBinding.tvTeacherStar.startAnimation(scaleAnim);

        ChartModel chartModel = new ChartModel();
        chartModel.setUserName(userModel != null ? userModel.getName() : "");
        ChartFragment.Companion.newInstant().addChartModel(chartModel, false);
        setTipNum();
    }

    /**
     * 用户进入教室 分配画笔，画笔,上台view
     */
    void interRoom(OnlineUserModel userEnterModel) {
        if (userEnterModel == null) return;

        switch (userEnterModel.getRole()) {
            case RoomConstant.TEACHER_TYPE:
                mBinding.blBoard.teacherInterRoom(userEnterModel);
                mBinding.ivNextPage.setVisibility(View.GONE);
                mBinding.ivPrePage.setVisibility(View.GONE);
                UserModel teacherModel = userEnterModel.getInfo();
                if (teacherModel != null) {
                    mBinding.tvTeacherName.setVisibility(View.VISIBLE);
                    mBinding.tvTeacherName.setText(TextUtils.isEmpty(teacherModel.getName()) ? "" : teacherModel.getName());
                }
                break;
            case RoomConstant.STUDENT_TYPE:
//                boolean isRepeat = false;
//                if (studentModels.size() > 0) {
//                    for (UserModel userModel : studentModels) {
//                        if (ObjectUtils.equals(userModel.getId(), userEnterModel.getUser_id())) {
//                            isRepeat = true;
//                            break;
//                        }
//                    }
//                }
//
//                if (!isRepeat) {
//                    UserModel userModel = userEnterModel.getInfo();
//                    userModel.setId(userEnterModel.getUser_id());
//                    userModel.setType(RoomConstant.STUDENT_TYPE);
//                    userModel.setPhoto(userModel.getPhoto());
//                    studentModels.add(userModel);
//                    SeatListFragment.newInstant().setSeatList(studentModels);
//                    ChartFragment.newInstant().newUserRoomIn(userModel.getName());
//                }
                break;
        }
    }

    /**
     * 获取答题信息，开始答题
     */
    void startAnswer(TopicModel topicModel) {
        try {
            AnswerModel answerModel = SocketIoUtils.parseData(AnswerModel.class,
                    (Map) topicModel.getValue());
            mBinding.answerResultView.stopPlayVideo();
            mBinding.answerView.setAnswerData(answerModel);
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.START, classType, e.getMessage());
        }
    }

    /**
     * 更新答题时间
     */
    void updateAnswerTime(TopicModel topicModel) {
        AnswerTimeModel answerModel = SocketIoUtils.parseData(AnswerTimeModel.class,
                (Map) topicModel.getValue());
        mBinding.answerView.setAnswerTime(answerModel);
    }

    /**
     * IO接口答案提交结果处理
     */
    void doTopicResult(TopicModel topicModel) {
        AnswerResultModel resultModel = SocketIoUtils.parseData(AnswerResultModel.class,
                (Map) topicModel.getValue());
        doTopicResult(resultModel);
    }

    void doTopicResult(AnswerResultModel resultModel) {
        if (resultModel == null) return;
        if (resultModel.isTasked() || resultModel.isFinished()) {
            mBinding.answerView.stopPlayVideo();
            mBinding.answerView.setVisibility(View.GONE);
            mBinding.answerResultView.setAnswerResultData(resultModel.isResult(), mBinding.answerView.answerModel, resultModel);
        } else {
            submitAnswer(resultModel);
        }
    }

    //答题过程中，动态更新统计选项进度
    void doStat(TopicModel topicModel) {
        AnswerStatModel resultModel = SocketIoUtils.parseData(AnswerStatModel.class,
                (Map) topicModel.getValue());
        mBinding.answerResultView.updateAnswerStat(resultModel, mBinding.answerView.answerModel);
    }

    @Override
    public void turnPage(int currentPage) {
        mBinding.wvCourseware.loadUrl("javascript:PageMgr.turnPage(" + mCurrentCoursewarePage + ")");
        mBinding.tvPageNum.setText(mCurrentCoursewarePage + "/" + totlePage);
        clearBoard();
    }

    /**
     * 进入教室成功
     */
    void enterSuccess(List<OnlineUserModel> seatListModel) {
        studentModels.clear();
        if (sMyselfModel != null) {
            UserModel userModel = new UserModel();
            userModel.setId(sMyselfModel.getUser_id());
            userModel.setName(sMyselfModel.getNickname());
            userModel.setPhoto(sMyselfModel.getAvatar());
            studentModels.add(userModel);
        }

        if (seatListModel != null) {
            for (OnlineUserModel seatModel : seatListModel) {
                switch (seatModel.getRole()) {
                    case RoomConstant.STUDENT_TYPE:
                        UserModel userModel = seatModel.getInfo();
                        if (userModel == null) continue;
                        userModel.setId(seatModel.getUser_id());
                        studentModels.add(userModel);
                        break;
                    case RoomConstant.TEACHER_TYPE:
                        mBinding.ivNextPage.setVisibility(View.GONE);
                        mBinding.ivPrePage.setVisibility(View.GONE);
                        break;
                }
            }
        }
        SeatListFragment.newInstant().setSeatList(studentModels);

        Observable.just("isAgora")
                .subscribeOn(Schedulers.io())
                .doOnNext(s -> RoomApplication.getInstance().getVideoManager().leaveRoom())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((response) -> {
                    String uid = 1 + String.valueOf(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
                    RoomApplication.getInstance().initRoomVideoSDK(RoomVideoType.AGORA, agoraKey);
                    String filePath = RoomApplication.getInstance().getExternalCacheDir() + RoomConstant.AGORA_LOG_S_LIVE;
                    if (FileUtils.createOrExistsFile(filePath)) {
                        RoomApplication.getInstance().getVideoManager().setLogFile(filePath);
                    }
                    EnterRoomModel model = new EnterRoomModel();
                    model.setUid(uid);
                    model.setAppId(agoraKey);
                    model.setRoomId("td" + roomId);
                    RoomApplication.getInstance().getVideoManager().joinRoom(model);
                }, throwable -> {
                });
    }

    void userQuit(OnlineUserModel userQuitModel) {
        if (userQuitModel == null) return;
        for (UserModel studentModel : studentModels) {
            if (studentModel.getId() == userQuitModel.getUser_id()) {
                studentModels.remove(studentModel);
                break;
            }
        }
        SeatListFragment.newInstant().setSeatList(studentModels);
    }

    /**
     * 显示排行对话框
     */
    private void showAnswerRankDialog(List<TopModel.RankBean> rankListModels) {
        AnswerRankDialog dialogFragment = AnswerRankDialog.getInstance();
        dialogFragment.setLiveRank(rankListModels);
        dialogFragment.showDialogFragment((BaseCommonActivity) mContext);
    }

    void onNetWorkMsgReceived(String event) {
        onNetWorkMsgReceived(mBinding.viewNetworkTips, event, RoomConstant.LIVE_ERROR);
    }

    void onRtcMsgReceived(String event, String uidStr, String errorMsg) {
        switch (event) {
            case RoomConstant.ROOM_JOINED:
                RoomApplication.getInstance().getVideoManager().muteLocalVideoStream(true);
                break;
            case RoomConstant.ROOM_USER_JOINED:
                //自己
                if (TextUtils.equals(uidStr, "1" + SPUtils.getInstance().getInt(RoomConstant.USER_ID))) {
                    return;
                }

                //老师
                if (!TextUtils.isEmpty(uidStr) && uidStr.startsWith("2")) {
                    RoomApplication.getInstance().getVideoManager().setupRemoteVideo(mContext, mBinding.flTeacher, uidStr, true);
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
                    return;
                }
                break;
            case RoomConstant.ROOM_ERROR:
                RoomApplication.getInstance().getVideoManager().onRemoveView(mBinding.flTeacher);
                ExceptionUtil.sendException(RoomConstant.JOIN_AGORA_ERROR, classType, errorMsg);
                break;
        }
    }

    @Override
    public void setTeacherInfo(int teacherId, String teacherName) {
        mBinding.blBoard.setTeacherInfo(teacherId, teacherName);
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
        mBinding.wvCourseware.setCanAnimate(false);
        mBinding.blBoard.setCanDraw(true);
        mBinding.wvCourseware.loadUrl("javascript:PageMgr.hasAuthority(true)");
    }
}

