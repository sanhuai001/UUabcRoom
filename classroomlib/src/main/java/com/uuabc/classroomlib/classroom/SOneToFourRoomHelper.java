package com.uuabc.classroomlib.classroom;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.SystemClock;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonSyntaxException;
import com.othershe.baseadapter.ViewHolder;
import com.othershe.baseadapter.base.CommonBaseAdapter;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.common.BaseCommonActivity;
import com.uuabc.classroomlib.common.RxTimer;
import com.uuabc.classroomlib.databinding.ActivityClassRoomSOneToFourBinding;
import com.uuabc.classroomlib.model.OneToOneCourseResult;
import com.uuabc.classroomlib.model.RoomType;
import com.uuabc.classroomlib.model.SClassRoomResult;
import com.uuabc.classroomlib.model.SRChartResult;
import com.uuabc.classroomlib.model.SUserModel;
import com.uuabc.classroomlib.model.SocketModel.ChartModel;
import com.uuabc.classroomlib.model.SocketModel.DrawTextModel;
import com.uuabc.classroomlib.model.SocketModel.FlowerModel;
import com.uuabc.classroomlib.model.SocketModel.MoveValueModel;
import com.uuabc.classroomlib.model.SocketModel.OnlineUserModel;
import com.uuabc.classroomlib.model.SocketModel.RostrumModel;
import com.uuabc.classroomlib.model.SocketModel.SShareModel;
import com.uuabc.classroomlib.model.SocketModel.SwitchModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.retrofit.ApiRetrofit;
import com.uuabc.classroomlib.utils.ExceptionUtil;
import com.uuabc.classroomlib.utils.JsonUtils;
import com.uuabc.classroomlib.utils.MediaPlayerUtil;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.utils.PointUtil;
import com.uuabc.classroomlib.utils.RxTimerUtil;
import com.uuabc.classroomlib.utils.SocketIoUtils;
import com.uuabc.classroomlib.widget.ClassRoomDiamondRankView;
import com.uuabc.classroomlib.widget.dialog.SettingDialog;
import com.uuabc.roomvideo.model.EnterRoomModel;
import com.uuabc.roomvideo.model.RoomVideoType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressLint({"CheckResult", "SetTextI18n", "SimpleDateFormat"})
public class SOneToFourRoomHelper extends SBaseClassRoomHelper<ActivityClassRoomSOneToFourBinding> {
    private CommonBaseAdapter<ChartModel> mMsgAdapter;
    private CommonBaseAdapter<UserModel> mStudentAdapter;
    private List<UserModel> temporaryStudents;
    private List<Integer> onlineStudentIds;
    private int studentRecycleViewWidth;
    private int studentItemHeight;
    private boolean isMuted;
    SettingDialog dialog;

    SOneToFourRoomHelper(ActivityClassRoomSOneToFourBinding binding) {
        super(binding);
        onlineStudentIds = new ArrayList<>();
        classType = RoomConstant.CLASS_TYPE_ONE_TO_FOUR;
        mBinding.rlRostrum.setNewRoom();
        setVolumeDrawables();
        setSingalDrawables();
        initView();
    }

    private void initView() {
        initMsgAdapter();
        initStudentAdapter();

        ViewTreeObserver vto = mBinding.wvCourseware.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int courseWidth = mBinding.wvCourseware.getWidth();
                mBinding.wvCourseware.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mScale = RoomApplication.getInstance().getScale(mBinding.wvCourseware.getWidth());
                mBinding.boardView.setPaintSize(2 / mScale);
                //上台
                mBinding.rlRostrum.setSize(courseWidth);
                //底部单个学生在列表中宽度
                studentItemHeight = mBinding.rvStudents.getHeight();
                studentRecycleViewWidth = mBinding.rvStudents.getWidth();
                mBinding.rvStudents.setItemWidth(studentRecycleViewWidth / 4);

                onNetWorkMsgReceived(NetworkUtils.isConnected() ? RoomConstant.NET_WORK_CONNECTED : RoomConstant.NET_WORK_INCONNECTED);
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
        mBinding.rvChatList.setLayoutManager(layoutManager);
        int userId = SPUtils.getInstance().getInt(RoomConstant.USER_ID);
        mBinding.rvChatList.setAdapter(mMsgAdapter = new CommonBaseAdapter<ChartModel>(mContext, null, false) {
            @Override
            protected void convert(ViewHolder viewHolder, ChartModel chartModel, int i) {
                TextView tvMsgLeft = viewHolder.getView(R.id.tv_msg_left);
                TextView tvMsgRight = viewHolder.getView(R.id.tv_msg_right);
                TextView tvNameLeft = viewHolder.getView(R.id.tv_name_left);
                TextView tvNameRight = viewHolder.getView(R.id.tv_name_right);

                boolean isMine = TextUtils.equals(chartModel.getSendId(), String.valueOf(userId));
                tvMsgLeft.setVisibility(isMine ? View.GONE : View.VISIBLE);
                tvMsgRight.setVisibility(isMine ? View.VISIBLE : View.GONE);
                tvNameLeft.setVisibility(isMine ? View.GONE : View.VISIBLE);
                tvNameRight.setVisibility(isMine ? View.VISIBLE : View.GONE);
                if (isMine) {
                    tvMsgRight.setBackgroundResource(R.drawable.room_sdk_shape_msg_right_blue);
                    tvMsgRight.setText(chartModel.getMsg());
                    tvNameRight.setText(chartModel.getUserName());
                } else {
                    tvMsgLeft.setBackgroundResource(TextUtils.equals(teacherId, chartModel.getSendId())
                            ? R.drawable.room_sdk_shape_msg_left_deep_blue : R.drawable.room_sdk_shape_msg_left_blue);
                    tvMsgLeft.setText(chartModel.getMsg());
                    tvNameLeft.setText(chartModel.getUserName());
                }
            }

            @Override
            protected int getItemLayoutId() {
                return R.layout.item_room_sdk_chart_msg;
            }
        });
    }

    private void initStudentAdapter() {
        mBinding.rvStudents.setAdapter(mStudentAdapter = new CommonBaseAdapter<UserModel>(mContext, null, false) {

            @Override
            protected void convert(ViewHolder viewHolder, UserModel studentModel, int i) {
                ViewGroup.LayoutParams layoutParams = viewHolder.getConvertView().getLayoutParams();
                layoutParams.height = studentItemHeight;
                layoutParams.width = mBinding.rvStudents.getItemWidth();
                viewHolder.setVisibility(R.id.cl_group, View.GONE);
                viewHolder.setVisibility(R.id.cl_user, View.VISIBLE);
                TextView tvName = viewHolder.getView(R.id.tv_name);
                TextView tvMic = viewHolder.getView(R.id.tv_mic);
                setDrawLeftImg(studentModel.isMuted() ? R.drawable.ic_room_sdk_speak_close_s : R.drawable.ic_room_sdk_speak_open_s, tvMic);
                tvName.setText(studentModel.getName());
                TextView tvDiamond = viewHolder.getView(R.id.tv_diamond_count);
                tvDiamond.setTag(studentModel.getId());
                tvDiamond.setText(String.valueOf(studentModel.getDia()));
                ImageView ivVolume = viewHolder.getView(R.id.ivVolume);

                CircleImageView userHead = viewHolder.getView(R.id.iv_user_head);
                CircleImageView userHeadOfflineCover = viewHolder.getView(R.id.iv_user_head_cover);
                if (studentModel.isOnline()) {
                    userHeadOfflineCover.setVisibility(View.GONE);
                    Glide.with(mContext).load(TextUtils.isEmpty(studentModel.getPhoto()) ? R.drawable.ic_room_sdk_boy_head : studentModel.getPhoto())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_room_sdk_boy_head).error(R.drawable.ic_room_sdk_boy_head))
                            .into(userHead);
                } else {
                    userHeadOfflineCover.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(R.drawable.ic_room_sdk_student_offline_cover).into(userHeadOfflineCover);
                }

                boolean isUpState = mBinding.rlRostrum.isUpStateById(studentModel.getId());
                if (studentModel.getId() == SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                    mBinding.ivMyMic.setVisibility(View.VISIBLE);
                    mBinding.ivMyMic.setImageResource(studentModel.isMuted() ? R.drawable.ic_room_sdk_my_mic_close_s : R.drawable.ic_room_sdk_my_mic_open_s);
                    if (isUpState) {
                        mBinding.ivVolume.setVisibility(View.GONE);
                        ivVolume.setVisibility(studentModel.isMuted() ? View.GONE : View.VISIBLE);
                        mBinding.rlRostrum.setVolumeVisible(studentModel.getId(), studentModel.isMuted());
                    } else {
                        mBinding.ivVolume.setVisibility(studentModel.isMuted() ? View.GONE : View.VISIBLE);
                        ivVolume.setVisibility(studentModel.isMuted() ? View.GONE : View.VISIBLE);
                    }
                    mBinding.clPaint.setVisibility(studentModel.isDrawing() ? View.VISIBLE : View.GONE);
                } else {
                    if (isUpState) {
                        ivVolume.setVisibility(View.GONE);
                        mBinding.rlRostrum.setVolumeVisible(studentModel.getId(), studentModel.isMuted());
                    } else {
                        ivVolume.setVisibility(studentModel.isMuted() ? View.GONE : View.VISIBLE);
                    }
                }

                mBinding.rlRostrum.setStudents(mStudentAdapter.getAllData(), false);
                setVolumeLevel(ivVolume, studentModel.getVolume(), true);
                viewHolder.getConvertView().requestLayout();
            }

            @Override
            protected int getItemLayoutId() {
                return R.layout.item_room_sdk_s_online_class_student;
            }
        });
    }

    void onColorCheckedChanged(int checkedId) {
        PointUtil.onEvent(mContext, RoomConstant.ONE_TO_FOUR_CLICK_PEN_COLOR);
        mBinding.boardView.setPaintColor(getPaintColorStr(checkedId));
    }

    void onSizeCheckedChanged(int checkedId) {
        PointUtil.onEvent(mContext, RoomConstant.ONE_TO_FOUR_CLICK_PEN_SIZE);
        mBinding.boardView.setPaintSize(getPaintSize(checkedId) / mScale);
    }

    @Override
    public void turnPage(int currentPage) {
        mBinding.wvCourseware.loadUrl("javascript:PageMgr.turnPage(" + mCurrentCoursewarePage + ")");
        mBinding.tvPageNum.setText(mCurrentCoursewarePage + "/" + totlePage);
        clearBoard();
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
     * 清空画板的方法
     */
    public void clearBoard() {
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
     * 其他学生划线
     */
    void doLine(SShareModel shareModel, int userId) {
        if (shareModel.getData() == null) return;
        try {
            MoveValueModel value = SocketIoUtils.parseData(MoveValueModel.class, (Map) shareModel.getData());
            mBinding.boardView.doDrawLine(userId, value, mScale);
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.LINE, classType, e.getMessage());
        }
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
        mMsgAdapter.setLoadMoreData(chartModelList);
        mBinding.ivChatNodata.setVisibility(View.GONE);
        RxTimerUtil.timer(500, number -> mBinding.rvChatList.scrollToPosition(mMsgAdapter.getItemCount() - 1));
    }

    /**
     * 添加文字
     */
    void doDrawText(SShareModel shareModel) {
        if (shareModel.getData() == null) return;
        try {
            DrawTextModel value = SocketIoUtils.parseData(DrawTextModel.class, (Map) shareModel.getData());
            mBinding.boardView.drawText(value, mScale);
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.TEXTSTART, classType, e.getMessage());
        }
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

            for (int i = 0; i < mStudentAdapter.getDataCount(); i++) {
                if (mStudentAdapter.getData(i).getId() == value.getId()) {
                    mStudentAdapter.getData(i).setAnimate(value.isOpen());
                    mStudentAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.ANIMATE, classType, e.getMessage());
        }
    }

    /**
     * 涂鸦开关
     */
    void controlDraw(SShareModel shareModel) {
        if (shareModel.getData() == null) return;
        try {
            SwitchModel value = SocketIoUtils.parseData(SwitchModel.class, (Map) shareModel.getData());
            for (int i = 0; i < mBinding.boardView.getBoardSize(); i++) {
                if (value.getId() == SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                    mBinding.boardView.setCanDraw(value.isOpen());
                    doCanDraw(value.isOpen());
                    break;
                }
            }

            for (int i = 0; i < mStudentAdapter.getDataCount(); i++) {
                if (mStudentAdapter.getData(i).getId() == value.getId()) {
                    mStudentAdapter.getData(i).setDrawing(value.isOpen());
                    mStudentAdapter.notifyDataSetChanged();
                    break;
                }
            }
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.DRAW, classType, e.getMessage());
        }
    }

    /**
     * 禁言开关
     */
    void controlMuted(SShareModel shareModel) {
        if (shareModel.getData() == null) return;
        try {
            SwitchModel switchModel = SocketIoUtils.parseData(SwitchModel.class, (Map) shareModel.getData());
            if (switchModel.getId() == SPUtils.getInstance().getInt(RoomConstant.USER_ID) || switchModel.getId() == 0) {
                muteSLocalAudioStream(ObjectUtil.getBoolean(switchModel.getValue()), "1" + switchModel.getId());
            } else {
                muteRemoteAudioStream(ObjectUtil.getBoolean(switchModel.getValue()), "1" + switchModel.getId());
            }

            if (switchModel.getId() == 0) {
                for (int i = 0; i < mStudentAdapter.getDataCount(); i++) {
                    mStudentAdapter.getData(i).setMuted(switchModel.isOpen());
                }
                mStudentAdapter.notifyDataSetChanged();
                return;
            }

            for (int i = 0; i < mStudentAdapter.getDataCount(); i++) {
                if (mStudentAdapter.getData(i).getId() == switchModel.getId()) {
                    mStudentAdapter.getData(i).setMuted(switchModel.isOpen());
                    mStudentAdapter.notifyItemChanged(i);
                    break;
                }
            }
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.MUTED, classType, e.getMessage());
        }
    }

    /**
     * 奖励钻石后更新的钻石列表
     */
    void doFlower(SShareModel shareModel) {
        if (shareModel.getData() == null) return;
        String jsonStr = JsonUtils.entityToJsonString(shareModel.getData());
        List<FlowerModel.UsersBean> users = JsonUtils.parseArray(jsonStr, FlowerModel.UsersBean.class);
        if (users == null) return;
        FlowerModel.UsersBean usersBean;
        float studentId;
        for (int i = 0; i < users.size(); i++) {
            usersBean = users.get(i);
            if (usersBean == null) continue;
            studentId = Float.parseFloat(usersBean.getId());

            int boardPosition = mBinding.boardView.getPosition((int) studentId);
            boolean isMySelf = studentId == SPUtils.getInstance().getInt(RoomConstant.USER_ID);
            if (isMySelf) {
                setDrawLeftImg(usersBean.getTotal() == 0 ? R.drawable.ic_room_sdk_live_class_diamond_gray : R.drawable.ic_room_sdk_class_room_blue_diamond, mBinding.tvDiamond);
            }
            if (boardPosition == -1 && !isMySelf) continue;

            if (students == null) continue;
            for (int j = 0; j < students.size(); j++) {
                if (students.get(j).getId() == studentId) {
                    students.get(j).setDia(usersBean.getTotal());
                    mBinding.rlRostrum.setRostrumText(students.get(j));
                    break;
                }
            }
        }

        RxTimerUtil.timer(200, number -> {
            MediaPlayerUtil.getInstance().resetToPlay(mContext, R.raw.magic);
            CustomLottieAnimationView animationView = new CustomLottieAnimationView(mContext);
            animationView.addAnimatorListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mBinding.llLottie.removeAllViews();
                    String jsonStr = JsonUtils.entityToJsonString(shareModel.getData());
                    List<FlowerModel.UsersBean> users = JsonUtils.parseArray(jsonStr, FlowerModel.UsersBean.class);
                    if (users == null || users.size() == 0) return;
                    mBinding.ivGif.showDiamonds(mBinding.rlFlower, mBinding.rvStudents, mBinding.tvDiamond, users);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            animationView.showDiamonds(mBinding.llLottie);
        });
    }

    /**
     * 学生上下台
     */
    void doRostrum(SShareModel shareModel) {
        try {
            RostrumModel value = SocketIoUtils.parseData(RostrumModel.class, (Map) shareModel.getData());
            int userId = ObjectUtil.getIntValue(value.getId());
            if (value.isStage()) {
                switch (value.getType()) {
                    case RoomConstant.START:
                    case RoomConstant.MOVE:
                        mBinding.rlRostrum.doNewUpState(userId, value, mScale, (rostrumLayout, userId1) -> {
                            String uid = 1 + String.valueOf(userId1);
                            if (rostrumLayout.getChildCount() != 0 && rostrumLayout.getChildAt(0).isShown())
                                return;
                            if (userId1 == SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                                onRemoveView(mBinding.flStudent);
                                mBinding.flStudent.setBackgroundResource(R.drawable.ic_room_sdk_class_room_stage_s);
                                setupLocalVideo(rostrumLayout, uid);
                            } else {
                                setupRemoteVideo(rostrumLayout, uid);
                            }
                            mStudentAdapter.notifyDataSetChanged();
                        });
                        break;
                    case RoomConstant.END:
                        LogUtils.i("doRostrumEnd", "end");
                        break;
                }
            } else {
                mBinding.rlRostrum.doDownState(userId, () -> {
                    if (userId == SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                        String uid = 1 + String.valueOf(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
                        setupLocalVideo(mBinding.flStudent, uid);
                        mBinding.flStudent.setBackgroundResource(0);
                    }
                    mStudentAdapter.notifyDataSetChanged();
                });
            }
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.ROSTRUM, classType, e.getMessage());
        }
    }

    /**
     * 进入教室成功
     */
    void enterSuccess(List<OnlineUserModel> onlineStudents) {
        onlineStudentIds.clear();
        onlineStudentIds.add(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        if (onlineStudents != null) {
            for (int i = 0; i < onlineStudents.size(); i++) {
                if (onlineStudents.get(i) == null || onlineStudents.get(i).getUser_id() == 0) {
                    continue;
                }

                if (onlineStudents.get(i).getRole() == 2) {
                    mBinding.ivNextPage.setVisibility(View.GONE);
                    mBinding.ivPrePage.setVisibility(View.GONE);
                    continue;
                }

                onlineStudentIds.add(onlineStudents.get(i).getUser_id());
            }
        }

        Observable.just("isAgora")
                .subscribeOn(Schedulers.io())
                .doOnNext(s -> RoomApplication.getInstance().getVideoManager().leaveRoom())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((response) -> {
                    String uid = 1 + String.valueOf(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
                    EnterRoomModel roomModel = new EnterRoomModel();
                    RoomApplication.getInstance().initRoomVideoSDK(RoomVideoType.AGORA, agoraKey);
                    RoomApplication.getInstance().enableAudioVolumeIndication(300);
                    String filePath = RoomApplication.getInstance().getExternalCacheDir() + RoomConstant.AGORA_LOG_S_ONE_TO_FOUR;
                    if (FileUtils.createOrExistsFile(filePath)) {
                        RoomApplication.getInstance().getVideoManager().setLogFile(filePath);
                    }
                    roomModel.setUid(uid);
                    roomModel.setAppId(agoraKey);
                    roomModel.setRoomId("td" + roomId);
                    RoomApplication.getInstance().getVideoManager().joinRoom(roomModel);
                }, throwable -> {
                });

        mBinding.rvStudents.setItemWidth(studentRecycleViewWidth / 4);
        if (students == null) return;
        for (UserModel studentModel : students) {
            studentModel.setOnline(onlineStudentIds.contains(studentModel.getId()));
            mBinding.rvStudents.addStudent(mStudentAdapter, studentModel);
            if (studentModel.getId() == SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                controlShowActive(studentModel.isAnimate());
                mBinding.ivNoCamera.setVisibility(studentModel.isCamera() ? View.GONE : View.VISIBLE);
            }
        }

        mBinding.rvStudents.setStudentRecycleViewParams(students.size());
        mStudentAdapter.setNewData(students);
    }

    /**
     * 用户进入教室 分配画笔，画板,上台view
     */
    void interRoom(OnlineUserModel userEnterModel) {
        if (userEnterModel == null || userEnterModel.getInfo() == null) return;

        UserModel userModel = userEnterModel.getInfo();
        userModel.setId(userEnterModel.getUser_id());
        userModel.setType(userEnterModel.getRole());
        switch (userModel.getType()) {
            case RoomConstant.STUDENT_TYPE:
                userModel.setOnline(true);
                if (!onlineStudentIds.contains(userModel.getId()))
                    onlineStudentIds.add(userModel.getId());
                break;
            case RoomConstant.TEACHER_TYPE:
                mBinding.boardView.teacherInterRoom(userEnterModel);
                mBinding.ivNextPage.setVisibility(View.GONE);
                mBinding.ivPrePage.setVisibility(View.GONE);
                teacherId = String.valueOf(userModel.getId());

                mBinding.tvTeacherName.setVisibility(View.VISIBLE);
                mBinding.tvTeacherName.setText(TextUtils.isEmpty(userModel.getName()) ? "" : userModel.getName());
                break;
        }
    }

    /**
     * 其他用户退出教室
     */
    void userQuit(OnlineUserModel userQuitModel) {
        if (userQuitModel == null) return;
        int boardPosition = mBinding.boardView.getPosition(userQuitModel.getUser_id());
        if (boardPosition == -1) return;

        mBinding.rlRostrum.doQuitRostrum(userQuitModel.getUser_id());
        if (onlineStudentIds.contains(userQuitModel.getUser_id())) {
            onlineStudentIds.remove((Object) userQuitModel.getUser_id());
        }

        mBinding.rvStudents.updateStudent(mStudentAdapter, userQuitModel.getUser_id(), false);
    }

    /**
     * students指令处理
     */
    void doStudents(Map studentMap) {
        if (studentMap == null) return;
        temporaryStudents = temporaryStudents == null ? new ArrayList<>() : temporaryStudents;
        temporaryStudents.clear();
        for (Object value : studentMap.values()) {
            String studentJson = JsonUtils.entityToJsonString(value);
            UserModel userModel = JsonUtils.parseObject(studentJson, UserModel.class);
            if (userModel == null) continue;
            userModel.setOnline(onlineStudentIds.contains(userModel.getId()));
            temporaryStudents.add(userModel);
        }
        students = temporaryStudents;
        doStudents(true);
        onSetVoice(false);
    }

    /**
     * 切换课件interRoom
     */
    void changeCourseWare(SShareModel shareModel) {
        if (shareModel.getData() == null) return;

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

    @Override
    public void checkRoomResultSuccess(SClassRoomResult result, int srvTime, boolean isLoadedReply) {
        mBinding.boardView.clearAllBoard();
        if (isLoadedReply) {
            if (courseCaseLoaded) {
                mBinding.wvCourseware.reload();
            } else {
                mBinding.wvCourseware.setUrl(result.getCoursewareUrl());
                mBinding.wvCourseware.loadUrl(result.getCoursewareUrl());
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

        mBinding.boardView.setMyselfViewTag();
        readSpeak();
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

    private void doStudents(boolean rtcJoinSuccess) {
        if (students == null) return;
        mBinding.rlRostrum.setStudents(students, true);
        for (int i = 0; i < students.size(); i++) {
            UserModel studentModel = students.get(i);
            if (studentModel.getId() == 0) continue;
            int studentId = studentModel.getId();
            boolean isStage = studentModel.isStage();
            isMuted = studentModel.isMuted();
            boolean isOnline = onlineStudentIds.contains(studentId);
            float x = 0, y = 0;
            String point = studentModel.getPoint();
            if (isStage) {
                String[] position = point.split(",");
                x = Float.parseFloat(position[0]) / mScale;
                y = Float.parseFloat(position[1]) / mScale;
            }
            String uid = "1" + studentId;
            //学生为自己时处理
            if (SPUtils.getInstance().getInt(RoomConstant.USER_ID) == studentId) {
                studentModel.setOnline(true);
                if (!onlineStudentIds.contains(studentId))
                    onlineStudentIds.add(studentId);
                doCanDraw(studentModel.isDrawing());
                setDrawLeftImg(studentModel.getDia() == 0 ? R.drawable.ic_room_sdk_live_class_diamond_gray : R.drawable.ic_room_sdk_class_room_blue_diamond, mBinding.tvDiamond);
                mBinding.tvDiamond.setText("x" + studentModel.getDia());
                mBinding.boardView.setCanDraw(studentModel.isDrawing());
                mBinding.boardView.setCanAnimate(studentModel.isAnimate());
                mBinding.clPaint.setVisibility(studentModel.isDrawing() ? View.VISIBLE : View.GONE);
                mBinding.wvCourseware.setCanAnimate(studentModel.isAnimate());
                mBinding.tvMineName.setText(studentModel.getName());
                if (isStage) {
                    onRemoveView(mBinding.flStudent);
                    mBinding.rlRostrum.doNewUpState(studentId, x, y, true, (frameLayout, userId) ->
                            setupLocalVideo(frameLayout, uid));
                    mBinding.ivVolume.setVisibility(View.GONE);
                    mBinding.flStudent.setBackgroundResource(R.drawable.ic_room_sdk_class_room_stage_s);
                } else {
                    if (rtcJoinSuccess) {
                        setupLocalVideo(mBinding.flStudent, uid);
                        mBinding.ivVolume.setVisibility(View.VISIBLE);
                        mBinding.flStudent.setBackgroundResource(0);
                    } else {
                        onRemoveView(mBinding.flStudent);
                        mBinding.flStudent.setBackgroundResource(R.drawable.ic_room_sdk_class_room_video_load_fail_s);
                    }
                }
                muteLocalAudioStream(isMuted, uid);
                mBinding.tvMineName.setVisibility(View.VISIBLE);
                continue;
            }

            studentModel.setOnline(isOnline);
            int boardPosition = mBinding.boardView.getPosition(studentId);
            muteRemoteAudioStream(isMuted, uid);
            if (boardPosition == -1) {
                mBinding.boardView.studentInterRoom(studentModel);
            }

            doRostumStudent(studentId, x, y, isStage && isOnline);
        }

        mBinding.rvStudents.setStudentRecycleViewParams(students.size());
        mStudentAdapter.setNewData(students);
    }

    private void doRostumStudent(int studentId, float x, float y, boolean isStage) {
        if (!isStage) return;
        mBinding.rlRostrum.doNewUpState(studentId, x, y, true, (frameLayout, userId) -> {
            setupRemoteVideo(frameLayout, "1" + studentId);
        });
    }

    /**
     * 涂鸦切换
     */
    private void doCanDraw(boolean canDraw) {
        mBinding.clPaint.setVisibility(canDraw ? View.VISIBLE : View.GONE);
    }

    private UserModel getStudent(int studentId) {
        if (students == null) return null;
        for (int i = 0; i < students.size(); i++) {
            if (studentId == students.get(i).getId()) {
                return students.get(i);
            }
        }
        return null;
    }

    void onSetVoice(boolean needRefreshUI) {
        if (dialog == null) return;

        String voiceJsonStr = SPUtils.getInstance().getString(RoomConstant.SP_CLASSROOM_VOICE_SETTING, "");
        if (!TextUtils.isEmpty(voiceJsonStr)) {
            JSONObject voiceJsonObj = JsonUtils.parseObject(voiceJsonStr);

            if (voiceJsonObj != null && voiceJsonObj.containsKey(RoomConstant.ONE_TO_FOUR_ROOM_ID)
                    && roomId == voiceJsonObj.getIntValue(RoomConstant.ONE_TO_FOUR_ROOM_ID))
                dialog.setVoiceJson(voiceJsonObj);
        }

        dialog.setRoomID(roomId);

        if (students == null)
            return;
        if (needRefreshUI || dialog.isShowing()) {
            dialog.setUserModel(students);
        } else {
            dialog.setList(students);
        }
    }

    /**
     * roomVideo回调
     */
    @SuppressLint("DefaultLocale")
    void onRtcMsgReceived(String event, String uid, String errorMsg) {
        switch (event) {
            case RoomConstant.ROOM_JOINED:
                doStudents(true);
                muteLocalAudioStream(isMuted, "1" + SPUtils.getInstance().getInt(RoomConstant.USER_ID));
                record(RoomConstant.RECORD_VIDEO_PUB, "");
                break;
            case RoomConstant.ROOM_USER_JOINED:
                if (TextUtils.isEmpty(uid)) return;

                //自己
                if (TextUtils.equals("1" + SPUtils.getInstance().getInt(RoomConstant.USER_ID), uid)) {
                    return;
                }

                //老师
                if (uid.startsWith("2")) {
                    mBinding.flTeacher.setBackgroundResource(0);
                    RoomApplication.getInstance().getVideoManager().setupRemoteVideo(mContext, mBinding.flTeacher, uid, true);
                    if (!mBinding.ivVolumeTea.isShown())
                        mBinding.ivVolumeTea.setVisibility(View.VISIBLE);
                    if (!mBinding.ivSignal.isShown()) mBinding.ivSignal.setVisibility(View.VISIBLE);

                    mBinding.tvTeacherName.setVisibility(View.VISIBLE);
                    mBinding.ivNextPage.setVisibility(View.GONE);
                    mBinding.ivPrePage.setVisibility(View.GONE);
                    return;
                }

                if (uid.startsWith("1")) {
                    doStudentJoin(uid.substring(1));
                    if (dialog == null) return;

                    Map<String, Integer> map = dialog.getVolumeMap();
                    if (!map.containsKey(uid)) {
                        onSetVoice(false);
                        map = dialog.getVolumeMap();
                    }

                    if (!map.containsKey(uid)) return;
                    RoomApplication.getInstance()
                            .getVideoManager().setParameters(String.format("{\"che.audio.playout.uid.volume\": {\"uid\":%d,\"volume\":%d}}", ObjectUtil.getIntValue(uid), map.get(uid)));
                }
                break;
            case RoomConstant.ROOM_USER_LEAVED:
                if (TextUtils.isEmpty(uid)) return;

                //自己
                if (TextUtils.equals("1" + SPUtils.getInstance().getInt(RoomConstant.USER_ID), uid)) {
                    mBinding.rlRostrum.doQuitRostrum(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
                    return;
                }

                //老师
                if (uid.startsWith("2")) {
                    onRemoveView(mBinding.flTeacher);
                    mBinding.ivVolumeTea.setVisibility(View.GONE);
                    mBinding.ivSignal.setVisibility(View.GONE);
                    mBinding.tvTeacherName.setVisibility(View.GONE);
                    mBinding.flTeacher.setBackgroundResource(R.drawable.ic_room_sdk_class_room_teacher_default_s);
                    return;
                }

                //其他学生
                mBinding.rlRostrum.doQuitRostrum(ObjectUtil.getIntValue(uid.substring(1)));
                break;
            case RoomConstant.ROOM_ERROR:
                doStudents(false);
                ExceptionUtil.sendException(RoomConstant.JOIN_AGORA_ERROR, classType, errorMsg);
                break;
        }
    }

    private void doStudentJoin(String userId) {
        if (TextUtils.isEmpty(userId)) return;
        UserModel studentModel = getStudent(ObjectUtil.getIntValue(userId));
        if (studentModel == null) return;
        int studentId = studentModel.getId();
        if (TextUtils.equals(userId, String.valueOf(studentId))) {
            boolean isStage = studentModel.isStage();
            boolean isOnline = onlineStudentIds.contains(studentId);
            studentModel.setOnline(isOnline);
            int boardPosition = mBinding.boardView.getPosition(studentId);
            if (boardPosition == -1) { //未添加
                mBinding.boardView.studentInterRoom(studentModel);
            }

            if (isStage && isOnline) {
                String point = studentModel.getPoint();
                String[] position = point.split(",");
                float x = Float.parseFloat(position[0]) / mScale;
                float y = Float.parseFloat(position[1]) / mScale;
                doRostumStudent(studentId, x, y, true);
            }

            if (!onlineStudentIds.contains(studentModel.getId())) {
                onlineStudentIds.add(studentModel.getId());
            }
            mBinding.rvStudents.updateStudent(mStudentAdapter, studentId, true);
        }
    }

    /**
     * 处理下课事件
     */
    private boolean inflated = false;

    void overClassEvent(Map completeMap) {
        if (completeMap == null) return;
        Map usersMap = (Map) completeMap.get("list");
        if (usersMap == null) return;
        PointUtil.onEvent(mContext, RoomConstant.ONE_TO_FOUR_CLASS_OVER);

        if (!inflated && !mBinding.vsGroup.isInflated())
            Objects.requireNonNull(mBinding.vsGroup.getViewStub()).inflate();
        inflated = true;
        @SuppressLint("CutPasteId")
        Button btnDoHomework = ((Activity) mContext).findViewById(R.id.btn_do_homework);
        Button btnNoDo = ((Activity) mContext).findViewById(R.id.btn_no_do);
        TextView tvDiamond = ((Activity) mContext).findViewById(R.id.tv_diamond);
        ImageView ivNoDiamond = ((Activity) mContext).findViewById(R.id.iv_no_diamond);
        LottieAnimationView lavBell = ((Activity) mContext).findViewById(R.id.lav_bell);
        ClassRoomDiamondRankView rvNormal = ((Activity) mContext).findViewById(R.id.rv_normal);
        btnDoHomework.setText(mContext.getString(R.string.classroom_evaluate_course_str));
        btnNoDo.setText(mContext.getString(R.string.dialog_exit_title_str));
        btnDoHomework.setBackgroundResource(R.drawable.room_sdk_selector_dialog_button_red_s);
        btnNoDo.setBackgroundResource(R.drawable.room_sdk_selector_dialog_button_blue_s);
        int diamondCount = 0;
        List<UserModel> userList = new ArrayList<>();
        for (Object value : usersMap.values()) {
            String studentJson = JsonUtils.entityToJsonString(value);
            UserModel user = JsonUtils.parseObject(studentJson, UserModel.class);
            if (user == null) continue;
            userList.add(user);
            diamondCount = diamondCount + user.getDia();
            if (ObjectUtils.equals(SPUtils.getInstance().getInt(RoomConstant.USER_ID), user.getId())) {
                tvDiamond.setText(Html.fromHtml(mContext.getResources().getString(R.string.activity_over_class_diamond_str, user.getDia())));//设置钻石数
            }
        }

        if (diamondCount == 0) {
            ivNoDiamond.setVisibility(View.VISIBLE);
        } else {
            lavBell.setImageAssetsFolder("images/");
            lavBell.playAnimation();
            rvNormal.setVisibility(View.VISIBLE);
            rvNormal.setNewData(userList);
        }

        btnDoHomework.setOnClickListener(v -> doClassOver(RoomType.ONETOFOUR));
        btnNoDo.setOnClickListener(v -> noDoHomework());
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
     * 稍后再做作业
     */
    private void noDoHomework() {
        ((BaseCommonActivity) mContext).finish();
    }

    void onNetWorkMsgReceived(String event) {
        onNetWorkMsgReceived(mBinding.viewNetworkTips, event, RoomConstant.ONE_TO_FOUR_ERROR);
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
        if (map == null) return;
        int mUid = SPUtils.getInstance().getInt(RoomConstant.USER_ID);
        if (!ObjectUtil.isEmpty(teacherId)) {
            int teacher = Float.valueOf("2" + teacherId).intValue();
            if (map.containsKey(teacher)) {
                setSignal(mBinding.ivSignal, map.get(teacher));
            }
        }

        for (Integer uId : onlineStudentIds) {
            if (map.containsKey(0) && uId == mUid) {
                int networkQuality = map.get(0);
                LogUtils.i("networkQuality", "networkQuality:" + networkQuality);
                mBinding.viewNetworkTips.setNetQuality(networkQuality < 3);
                setWifiIcon(mBinding.ivWifi, networkQuality);
                continue;
            }

            int otherStudent = Integer.parseInt("1" + uId);
            if (map.containsKey(otherStudent)) {
                mBinding.rlRostrum.setSignal(uId, map.get(otherStudent));
            }
        }
    }

    void doVolumeEvent(HashMap<Integer, Integer> map) {
        if (map == null) return;
        int mUid = SPUtils.getInstance().getInt(RoomConstant.USER_ID);
        for (Integer uId : onlineStudentIds) {
            if (map.containsKey(0) && uId == mUid) {
                setVolumeLevel(mBinding.ivVolume, map.get(0), false);
                updateStudent(mUid, map.get(0));
                mBinding.rlRostrum.setVolumeLevel(mUid, map.get(0));
            } else {
                int otherStudent = Integer.parseInt("1" + uId);
                if (map.containsKey(otherStudent)) {
                    updateStudent(uId, map.get(otherStudent));
                    mBinding.rlRostrum.setVolumeLevel(uId, map.get(otherStudent));
                }
            }
        }

        if (!ObjectUtil.isEmpty(teacherId)) {
            int teacher = Float.valueOf("2" + teacherId).intValue();
            if (map.containsKey(teacher)) {
                setVolumeLevel(mBinding.ivVolumeTea, map.get(teacher), false);
            }
        }
    }

    private void updateStudent(int uId, Integer volume) {
        for (UserModel user : students) {
            if (user.getId() == uId) {
                user.setVolume(volume);
            } else {
                user.setVolume(0);
            }
        }
        mStudentAdapter.notifyDataSetChanged();
    }

    public void dismissAllDialog() {
        dismissBaseDialog();
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
