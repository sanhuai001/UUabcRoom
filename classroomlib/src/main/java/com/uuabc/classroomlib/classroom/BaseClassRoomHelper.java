package com.uuabc.classroomlib.classroom;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.ViewDataBinding;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.JsonSyntaxException;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.common.BaseCommonActivity;
import com.uuabc.classroomlib.common.BaseIoSocketActivity;
import com.uuabc.classroomlib.common.RxTimer;
import com.uuabc.classroomlib.custom.TextureVideoViewOutlineProvider;
import com.uuabc.classroomlib.model.SocketModel.GifValueModel;
import com.uuabc.classroomlib.model.SocketModel.MoveValueModel;
import com.uuabc.classroomlib.model.SocketModel.PageModel;
import com.uuabc.classroomlib.model.SocketModel.SShareModel;
import com.uuabc.classroomlib.retrofit.ApiRetrofit;
import com.uuabc.classroomlib.retrofit.RequestBuilder;
import com.uuabc.classroomlib.utils.CompatUtil;
import com.uuabc.classroomlib.utils.ExceptionUtil;
import com.uuabc.classroomlib.utils.JsonUtils;
import com.uuabc.classroomlib.utils.MediaPlayerUtil;
import com.uuabc.classroomlib.utils.PointUtil;
import com.uuabc.classroomlib.utils.SConfirmDialogUtils;
import com.uuabc.classroomlib.utils.SocketIoUtils;
import com.uuabc.classroomlib.utils.TipUtils;
import com.uuabc.classroomlib.widget.BoardViewLayout;
import com.uuabc.classroomlib.widget.dialog.SConfirmDialog;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import pl.droidsonroids.gif.GifDrawable;

@SuppressLint({"CheckResult", "SimpleDateFormat", "SetTextI18n"})
public class BaseClassRoomHelper<T extends ViewDataBinding> {
    Handler mMainHandler = new Handler(Looper.getMainLooper());
    T mBinding;
    Context mContext;
    float mScale; //课件与画板的比例
    public int roomId;
    private GifDrawable gifFromAssets;
    int mCurrentCoursewarePage = 1;
    int totlePage;
    public int classType = 0;
    long skuId;
    boolean courseCaseLoaded;
    private RxTimer rxTimer;
    private RxTimer rxPageTimer;
    MediaPlayerUtil clickMediaPlayer;

    BaseClassRoomHelper(T t) {
        mBinding = t;
        mContext = mBinding.getRoot().getContext();
        clickMediaPlayer = new MediaPlayerUtil();
        RoomApplication.getInstance().PC_WIDTH = RoomConstant.PC_WIDTH;
        RoomApplication.getInstance().PC_HEIGHT = RoomConstant.PC_HEIGHT;
    }

    public void showAnimateLog() {

    }

    /**
     * 获取画笔颜色
     */
    String getPaintColorStr(int checkedId) {
        String mBoardPaintColor = null;
        clickMediaPlayer.resetToPlay(mContext, R.raw.click_sing);
        if (checkedId == R.id.rb_red) {
            mBoardPaintColor = RoomConstant.COLOR_RED;
        } else if (checkedId == R.id.rb_yellow) {
            mBoardPaintColor = RoomConstant.COLOR_YELLOW;
        } else if (checkedId == R.id.rb_blue) {
            mBoardPaintColor = RoomConstant.COLOR_BLUE;
        } else if (checkedId == R.id.rb_green) {
            mBoardPaintColor = RoomConstant.COLOR_GREEN;
        } else if (checkedId == R.id.rb_orange) {
            mBoardPaintColor = RoomConstant.COLOR_ORANGE;
        } else if (checkedId == R.id.rb_purple) {
            mBoardPaintColor = RoomConstant.COLOR_PURPLE;
        }
        return mBoardPaintColor;
    }

    /**
     * 获取画笔size
     */
    int getPaintSize(int checkedId) {
        int mBoardPaintSize = 2;
        clickMediaPlayer.resetToPlay(mContext, R.raw.click_sing);
        if (checkedId == R.id.rb_size3) {
            mBoardPaintSize = 5;
        } else if (checkedId == R.id.rb_size5) {
            mBoardPaintSize = 10;
        }
        return mBoardPaintSize;
    }

    /**
     * 禁言|取消禁言
     */
    void muteLocalAudioStream(boolean muted, String userId) {
        RoomApplication.getInstance().getVideoManager().muteRemoteAudioStream(muted, userId);
    }

    /**
     * talkcloud 禁言|取消禁言
     */
    void muteSLocalAudioStream(boolean muted, String userId) {
        RoomApplication.getInstance().getVideoManager().muteLocalAudioStream(muted);
        RoomApplication.getInstance().getVideoManager().muteRemoteAudioStream(muted, userId);
    }

    /**
     * 禁言|取消禁言
     */
    void muteRemoteAudioStream(boolean muted, String userId) {
        RoomApplication.getInstance().getVideoManager().muteRemoteAudioStream(muted, userId);
    }

    /**
     * 刷新容器中的LocalVideo
     */
    void setupLocalVideo(ViewGroup viewGroup, String uid) {
        RoomApplication.getInstance().getVideoManager().setupLocalVideo(mContext, viewGroup, uid);
    }

    /**
     * 刷新容器中的RemoteVideo
     */
    void setupRemoteVideo(ViewGroup viewGroup, String uid) {
        RoomApplication.getInstance().getVideoManager().setupRemoteVideo(mContext, viewGroup, uid, true);
    }

    /**
     * 移除容器中的View
     */
    void onRemoveView(ViewGroup viewGroup) {
        RoomApplication.getInstance().getVideoManager().onRemoveView(viewGroup);
    }

    /**
     * 提示刷新教室
     */
    void showNewRefreshTipDialog() {
        clickMediaPlayer.resetToPlay(mContext, R.raw.click_sing);
        SConfirmDialogUtils.show(mContext, R.drawable.ic_room_sdk_dialog_confirm_refresh, mContext.getString(R.string.dialog_msg_refresh_str), mContext.getString(R.string.dialog_refresh_str), (dialog, which) -> {
            dialog.dismiss();
            PointUtil.onEvent(mContext, classType == RoomConstant.CLASS_TYPE_ONE_TO_ONE ? RoomConstant.ONE_TO_ONE_CLICK_REFRESH : classType == RoomConstant.CLASS_TYPE_ONE_TO_FOUR ? RoomConstant.ONE_TO_FOUR_CLICK_REFRESH : RoomConstant.LIVE_CLICK_REFRESH);
            ((BaseClassRoomActivity) mContext).showProgress();
            ((BaseClassRoomActivity) mContext).onNetWorkMsgReceived(NetworkUtils.isConnected() ? RoomConstant.NET_WORK_CONNECTED : RoomConstant.NET_WORK_INCONNECTED);
        }, mContext.getString(R.string.dialog_cancel_str), (dialog, which) -> dialog.dismiss());
    }

    /**
     * 技术支持
     */
    void showNewSupportDialog() {
        clickMediaPlayer.resetToPlay(mContext, R.raw.click_sing);
        SConfirmDialogUtils.showPhone(mContext, R.drawable.ic_room_sdk_dialog_confirm_support, mContext.getString(R.string.dialog_msg_support_str, String.valueOf(skuId)), mContext.getString(R.string.dialog_know_str),
                (dialog, which) -> dialog.dismiss());
    }

    /**
     * 退出教室
     */
    void loginOutNewClassRoom() {
        clickMediaPlayer.resetToPlay(mContext, R.raw.click_sing);
        PointUtil.onEvent(mContext, classType == RoomConstant.CLASS_TYPE_ONE_TO_ONE ? RoomConstant.ONE_TO_ONE_QUIT_CLASS : classType == RoomConstant.CLASS_TYPE_ONE_TO_FOUR ? RoomConstant.ONE_TO_FOUR_QUIT_CLASS : RoomConstant.LIVE_QUIT_CLASS);
        SConfirmDialogUtils.show(mContext, R.drawable.ic_room_sdk_dialog_confirm_exit, mContext.getString(R.string.dialog_msg_exit_str), mContext.getString(R.string.fragment_check_exit_str), (dialog, which) -> {
            dialog.dismiss();
            requestOutClassRoom();
            doOutClassRoom();
        }, mContext.getString(R.string.dialog_cancel_str), (dialog, which) -> dialog.dismiss());
    }

    /**
     * 处理退出教室
     */
    void doOutClassRoom() {
        ((BaseIoSocketActivity) mContext).loginOut();
        RoomApplication.getInstance().getVideoManager().leaveRoom();
        ((BaseIoSocketActivity) mContext).finish();
    }

    void requestOutClassRoom() {
    }

    /**
     * 预习，上一页
     */
    void turnPrePage() {
        clickMediaPlayer.resetToPlay(mContext, R.raw.click_sing);
        if (mCurrentCoursewarePage < 2) {
            TipUtils.warningShow(mContext, mContext.getString(R.string.common_is_first_page));
            return;
        }
        mCurrentCoursewarePage--;
        turnPage(mCurrentCoursewarePage);
    }

    /**
     * 预习，下一页
     */
    void turnNextPage() {
        clickMediaPlayer.resetToPlay(mContext, R.raw.click_sing);
        if (mCurrentCoursewarePage >= totlePage) {
            TipUtils.warningShow(mContext, mContext.getString(R.string.common_is_last_page));
            return;
        }
        mCurrentCoursewarePage++;
        turnPage(mCurrentCoursewarePage);
    }

    /**
     * 翻页
     */
    void doPage(SShareModel shareModel) {
        rxPageTimer = rxPageTimer == null ? new RxTimer() : rxPageTimer;
        rxPageTimer.timer(400, number -> {
            try {
                PageModel pageModel = SocketIoUtils.parseData(PageModel.class, (Map) shareModel.getData());
                turnPage(mCurrentCoursewarePage = pageModel.getPage());
            } catch (JsonSyntaxException e) {
                ExceptionUtil.sendException(RoomConstant.PAGE, classType, e.getMessage());
            }
        });
    }

    public void turnPage(int currentPage) {

    }

    /**
     * 鼓励动画
     */
    void loadGif(GifValueModel gifValueModel, AnimationImageView ivGif) {
        if (((BaseCommonActivity) mContext).isDestroyed()) return;
        String srcStr;
        if (gifValueModel == null || (srcStr = gifValueModel.getSrc()) == null
                || gifValueModel.getDelay() == null) {
            return;
        }

        int gifDrawableResId;
        int map3ResId;
        if (srcStr.endsWith("goodjob.html")) {
            gifDrawableResId = R.drawable.room_sdk_gif_1;
            map3ResId = R.raw.good_job;
        } else if (srcStr.endsWith("bingo.html")) {
            gifDrawableResId = R.drawable.room_sdk_gif_2;
            map3ResId = R.raw.bingo;
        } else if (srcStr.endsWith("youcandoit.html")) {
            gifDrawableResId = R.drawable.room_sdk_gif_3;
            map3ResId = R.raw.you_can_do_it;
        } else if (srcStr.endsWith("congratulations.html")) {
            gifDrawableResId = R.drawable.room_sdk_gif_4;
            map3ResId = R.raw.congratulation;
        } else if (srcStr.endsWith("keepitup.html")) {
            gifDrawableResId = R.drawable.room_sdk_gif_5;
            map3ResId = R.raw.keep_it_up;
        } else if (srcStr.endsWith("amazing.html")) {
            gifDrawableResId = R.drawable.room_sdk_gif_6;
            map3ResId = R.raw.amazing;
        } else {
            return;
        }

        ivGif.setVisibility(View.VISIBLE);
        MediaPlayerUtil.getInstance().resetToPlay(mContext, map3ResId);
        try {
            if (gifFromAssets != null) {
                gifFromAssets.stop();
                gifFromAssets.recycle();
            }
            gifFromAssets = new GifDrawable(mContext.getResources(), gifDrawableResId);
            ivGif.setImageDrawable(gifFromAssets);
            gifFromAssets.start();
            gifFromAssets.addAnimationListener(loopNumber -> {
                if (!((BaseCommonActivity) mContext).isDestroyed()) {
                    ivGif.setVisibility(View.GONE);
                    gifFromAssets.recycle();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 鼠标移动的处理方法
     */
    void doPosition(SShareModel shareModel, BitmapBoardLayout boardLayout, int userId) {
        if (shareModel == null || boardLayout == null) return;
        try {
            MoveValueModel value = SocketIoUtils.parseData(MoveValueModel.class, (Map) shareModel.getData());
            boolean isMouseDown = value.isMouseDown();
            switch (value.getType()) {//根据type来判断是哪种移动方式
                case RoomConstant.START://开始
                    boardLayout.setLeftImage(value.isText());
                    boardLayout.doPositionStart(userId, isMouseDown, value, mScale);
                    break;
                case RoomConstant.MOVE://移动
                    boardLayout.setLeftImage(value.isText());
                    boardLayout.doPositionMove(userId, isMouseDown, value, mScale);
                    break;
                case RoomConstant.END://结束
                    boardLayout.doPositionEnd(userId);
                    break;
            }
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.POSITION, classType, e.getMessage());
        }
    }

    /**
     * 鼠标移动的处理方法
     */
    void doPosition(SShareModel shareModel, BoardViewLayout boardLayout, int userId) {
        if (shareModel == null || boardLayout == null) return;
        try {
            MoveValueModel value = SocketIoUtils.parseData(MoveValueModel.class, (Map) shareModel.getData());
            boolean isMouseDown = value.isMouseDown();
            switch (value.getType()) {//根据type来判断是哪种移动方式
                case RoomConstant.START://开始
                    boardLayout.setLeftImage(value.isText());
                    boardLayout.doPositionStart(userId, isMouseDown, value, mScale);
                    break;
                case RoomConstant.MOVE://移动
                    boardLayout.setLeftImage(value.isText());
                    boardLayout.doPositionMove(userId, isMouseDown, value, mScale);
                    break;
                case RoomConstant.END://结束
                    boardLayout.doPositionEnd(userId);
                    break;
            }
        } catch (JsonSyntaxException e) {
            ExceptionUtil.sendException(RoomConstant.POSITION, classType, e.getMessage());
        }
    }

    /**
     * 网络断开|连接处理
     */
    void onNetWorkMsgReceived(NetworkTipsView viewNetworkTips, String event, String classErrorType) {
        if (viewNetworkTips == null) return;
        rxTimer = rxTimer == null ? new RxTimer() : rxTimer;
        rxTimer.timer(500, number -> {
            switch (event) {
                case RoomConstant.NET_WORK_CONNECTED:
                    viewNetworkTips.doNetWorkConnect();
                    ((BaseClassRoomActivity) mContext).requestPermissions();
                    break;
                case RoomConstant.NET_WORK_INCONNECTED:
                    PointUtil.onEvent(mContext, classErrorType);
                    viewNetworkTips.doNetWorkInconnect();
                    ((BaseClassRoomActivity) mContext).requestPermissions();
                    break;
            }
        });
    }

    /**
     * 开始晃动动画
     */
    void startShakeAnimation(View view) {
        if (view.getAnimation() == null) {
            view.setAnimation(shakeAnimation());
        }
        view.startAnimation(view.getAnimation());
    }

    /**
     * 晃动动画
     */
    private Animation shakeAnimation() {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(4));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    /**
     * 开始旋转动画
     */
    void startRotation(View view) {
        view.animate().scaleX(0.6f).scaleY(0.6f).setDuration(1500);
        new RxTimer().timer(1500, number -> {
            if (((BaseClassRoomActivity) mContext).isDestroyed()) return;
            ObjectAnimator scaleXAnim = ObjectAnimator.ofFloat(view, "scaleX", 0.6f, 1.0f);
            ObjectAnimator scaleYAnim = ObjectAnimator.ofFloat(view, "scaleY", 0.6f, 1.0f);
            ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(view, "rotation", 0, 360);
            AnimatorSet set = new AnimatorSet();
            set.playTogether(scaleXAnim, scaleYAnim, rotateAnim);
            set.setDuration(1500);
            set.start();
        });
    }

    void sendNewAction(String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", msg);
        map.put("event", RoomConstant.ACTION);
        RoomApplication.getInstance().sendMessage(RoomConstant.SHARE, SocketIoUtils.getJsonObject(map));
    }

    HashMap getPathValueMap(int x, int y, float width, String color, float mScale, String actionType) {
        HashMap<String, Object> valueMap = new HashMap<>();
        valueMap.put("type", actionType);
        valueMap.put("x", new BigDecimal(x * mScale).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());
        valueMap.put("y", new BigDecimal(y * mScale).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());
        valueMap.put("mouseDown", true);
        valueMap.put("width", new BigDecimal(width * mScale).setScale(3, BigDecimal.ROUND_HALF_UP).floatValue());
        valueMap.put("color", color);
        return valueMap;
    }

    HashMap getNewPathShareMap(HashMap valueMap) {
        HashMap<String, Object> shareMap = new HashMap<>();
        shareMap.put("event", RoomConstant.POSITION);
        shareMap.put("data", valueMap);
        return shareMap;
    }

    HashMap getFlowShareMap() {
        HashMap<String, Object> shareMap = new HashMap<>();
        shareMap.put("action", RoomConstant.FLOWER);
        shareMap.put("value", 1);
        shareMap.put("toId", 0);
        shareMap.put("toType", 0);
        shareMap.put("back", true);
        return shareMap;
    }

    void responseErrorToast() {
        if (((BaseCommonActivity) mContext).isDestroyed()) return;
        ((BaseClassRoomActivity) mContext).dismissProgress();
        TipUtils.warningShow(mContext, mContext.getString(R.string.common_server_error));
    }

    void responseErrorToast(Throwable throwable) {
        if (throwable != null) LogUtils.i(throwable.getCause() != null ? throwable.getCause() : "");
        if (((BaseCommonActivity) mContext).isDestroyed()) return;
        ((BaseClassRoomActivity) mContext).dismissProgress();
        TipUtils.warningShow(mContext, mContext.getString(R.string.common_server_error));
    }

    void responseErrorToast(String code) {
        if (((BaseCommonActivity) mContext).isDestroyed()) return;
        ((BaseClassRoomActivity) mContext).dismissProgress();
        TipUtils.warningShow(mContext, !TextUtils.isEmpty(code) ? mContext.getString(R.string.common_ss_server_error, code) : mContext.getString(R.string.common_server_error));
    }

    SConfirmDialog loadFailDialog;

    void requestLoadFail(boolean isNetError) {
        ((BaseCommonActivity) mContext).dismissProgress();
        if (loadFailDialog != null) {
            loadFailDialog.dismiss();
        }
        loadFailDialog = SConfirmDialogUtils.show(mContext,
                R.drawable.ic_room_sdk_dialog_confirm_refresh,
                mContext.getString(isNetError ? R.string.common_network_bad_error : R.string.common_server_error),
                mContext.getString(R.string.fragment_check_exit_str),
                (dialog, which) -> {
                    dialog.dismiss();
                    ((BaseIoSocketActivity) mContext).finish();
                },
                mContext.getString(R.string.dialog_refresh_str)
                , (dialog, which) -> {
                    dialog.dismiss();
                    ((BaseClassRoomActivity) mContext).showProgress();
                    ((BaseClassRoomActivity) mContext).onNetWorkMsgReceived(NetworkUtils.isConnected() ? RoomConstant.NET_WORK_CONNECTED : RoomConstant.NET_WORK_INCONNECTED);
                });
    }

    void doNewOffLineClassRoom() {
        SConfirmDialogUtils.otherShow(mContext, R.drawable.ic_room_sdk_dialog_confirm_offline, mContext.getString(R.string.dialog_msg_offline_ss_str), mContext.getString(R.string.fragment_check_exit_str), (dialog, which) -> {
            dialog.dismiss();
            ((BaseIoSocketActivity) mContext).finish();
        });
    }

    /**
     * 课件加载失败
     */
    void doSCourseLoadFail() {
        SConfirmDialogUtils.show(mContext, R.drawable.ic_room_sdk_dialog_confirm_support, mContext.getString(R.string.dialog_msg_no_courseware_str), mContext.getString(R.string.dialog_exit_title_str), (dialog, which) -> {
            dialog.dismiss();
            PointUtil.onEvent(mContext, classType == RoomConstant.CLASS_TYPE_ONE_TO_ONE ? RoomConstant.ONE_TO_ONE_MAKE_SURE_QUIT_CLASS : classType == RoomConstant.CLASS_TYPE_ONE_TO_FOUR ? RoomConstant.ONE_TO_FOUR_MAKE_SURE_QUIT_CLASS : RoomConstant.LIVE_MAKE_SURE_QUIT_CLASS);
            doOutClassRoom();
        });
    }

    /**
     * 网络断开显示NetworkTipsView
     *
     * @param view NetworkTipsView
     */
    void doNetWorkConnectFail(NetworkTipsView view) {
        ((BaseCommonActivity) mContext).dismissProgress();
        view.doNetWorkInconnect();
    }

    void setWifiIcon(ImageView ivWifi, int level) {
        if (ivWifi == null) return;
        switch (level) {
            case 1:
                ivWifi.setImageResource(R.drawable.ic_room_sdk_wifi_three);
                break;
            case 2:
                ivWifi.setImageResource(R.drawable.ic_room_sdk_wifi_two);
                break;
            case 3:
            case 4:
                ivWifi.setImageResource(R.drawable.ic_room_sdk_wifi_one);
                break;
            default:
                ivWifi.setImageResource(R.drawable.ic_room_sdk_wifi_zero);
                break;
        }
    }

    void setDrawLeftImg(int drawableId, TextView textView) {
        Drawable drawable = CompatUtil.getDrawable(mContext, drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    void setDrawTopImg(int drawableId, TextView textView) {
        Drawable drawable = CompatUtil.getDrawable(mContext, drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, drawable, null, null);
    }

    void setClassRoomLayout(ConstraintLayout clRoomContent) {
        int roomContentWidth = (int) (RoomApplication.getInstance().getScreenHeight() * new BigDecimal(RoomApplication.getInstance().PC_WIDTH / RoomApplication.getInstance().PC_HEIGHT)
                .setScale(4, BigDecimal.ROUND_HALF_UP)
                .doubleValue());
        ViewGroup.LayoutParams params = clRoomContent.getLayoutParams();
        params.width = roomContentWidth;
        clRoomContent.requestLayout();
    }

    void doSChronometer(Chronometer chronometer) {
        String timeStr = chronometer.getText().toString();
        if (TextUtils.isEmpty(timeStr)) return;
        boolean isCountDown = timeStr.contains("-") || timeStr.contains("−");
        if (!isCountDown || !timeStr.contains(":")) {
            return;
        }
        String[] times = timeStr.split(":");
        if (TextUtils.isEmpty(times[times.length - 1])) return;
        int time = Integer.parseInt(times[times.length - 1]);
        if (time < 0) {
            time = -1000 * time;
            String dateTime = TimeUtils.millis2String(time, new SimpleDateFormat(time > 60 * 60 * 1000 ? "HH:mm:ss" : "mm:ss"));
            chronometer.setText("- " + dateTime);
        }
    }

    void setViewRadius(View view, int dpValue) {
        if (view == null) return;
        view.setOutlineProvider(new TextureVideoViewOutlineProvider(dpValue));
        view.setClipToOutline(true);
    }

    /**
     * 数据录制，保存上课过程中各项数据
     */
    void record(int recordType, Object jsonObj) {
        if (!((BaseCommonActivity) mContext).checkNetWork()) {
            return;
        }

        JSONObject jsonObject;
        if (jsonObj instanceof JSONObject) {
            jsonObject = (JSONObject) jsonObj;
        } else {
            jsonObject = JsonUtils.parseObject(jsonObj);
        }

        if (recordType == RoomConstant.RECORD_ANIMATE_INFO && jsonObject.containsKey("pageIndex")) {
            jsonObject.put("page", jsonObject.getIntValue("pageIndex"));
        }

        ApiRetrofit.getInstance().recordCourseInfo(new RequestBuilder()
                .build("receiver_id", -1)
                .build("receiver_type", "")
                .build("type_id", recordType)
                .build("content", jsonObject == null ? new JSONObject() : jsonObject).sCreate())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultModel -> {
                }, throwable -> {
                });
    }
}
