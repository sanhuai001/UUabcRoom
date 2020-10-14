package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.ViewDataBinding;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.common.BaseCommonActivity;
import com.uuabc.classroomlib.common.RxTimer;
import com.uuabc.classroomlib.model.AnimateLogModel;
import com.uuabc.classroomlib.model.Event.MessageEvent;
import com.uuabc.classroomlib.model.RSocketModel;
import com.uuabc.classroomlib.model.RoomType;
import com.uuabc.classroomlib.model.SClassRoomResult;
import com.uuabc.classroomlib.model.SOverClassModel;
import com.uuabc.classroomlib.model.SRCommonResult;
import com.uuabc.classroomlib.model.SRSwitchModel;
import com.uuabc.classroomlib.model.SUserModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.model.StateToolResult;
import com.uuabc.classroomlib.retrofit.ApiRetrofit;
import com.uuabc.classroomlib.utils.ExceptionUtil;
import com.uuabc.classroomlib.utils.JsonUtils;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.widget.ProgressWebView;
import com.uuabc.classroomlib.widget.dialog.ClassTipsDialog;
import com.uuabc.classroomlib.widget.dialog.InteractClassTipsDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.agora.rtc.Constants;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressLint({"CheckResult", "SetTextI18n"})
class SBaseClassRoomHelper<T extends ViewDataBinding> extends BaseClassRoomHelper<T> {
    List<UserModel> students;
    List<AnimateLogModel> animationLogList;
    SUserModel sMyselfModel;
    String agoraKey;
    RxTimer activeRxTimer;
    int diamondCount;
    String teacherId;
    String mCurrentUrl;
    boolean showClassTipsDialog = true;
    private ClassTipsDialog dialog;
    private SClassRoomResult.SettingBean settingBean;
    private Drawable[] volumesOneList;
    private Drawable[] volumesTwoList;
    private Drawable[] mSingalList;
    private InteractClassTipsDialog classTipsDialog;
    private SClassRoomResult infoResult;
    private int nowTime;

    SBaseClassRoomHelper(T ViewDataBinding) {
        super(ViewDataBinding);

        RoomApplication.getInstance().PC_WIDTH = RoomConstant.PC_WIDTH;
        RoomApplication.getInstance().PC_HEIGHT = RoomConstant.PC_HEIGHT;
        EventBus.getDefault().post(new MessageEvent(RoomConstant.ENTER_DESTORY_AROTE_VIEW));
    }

    /**
     * 刷新教室
     */
    public void refreshRoom() {
        ((BaseCommonActivity) mContext).showProgress();
        Observable.just("refreshRoom")
                .subscribeOn(Schedulers.io())
                .doOnNext(s -> {
                    RoomApplication.getInstance().ioSocketDisconnect();
                    RoomApplication.getInstance().getVideoManager().leaveRoom();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((response) -> getRoomInfo(), throwable -> ((BaseCommonActivity) mContext).dismissProgress());
    }

    public void getRoomInfo() {
        if (!((BaseCommonActivity) mContext).checkInitNetWork()) {
            ((BaseCommonActivity) mContext).dismissProgress();
            doNetWorkConnectFail();
            return;
        }

        Observable<SRCommonResult<SUserModel>> userObservable = ApiRetrofit.getInstance().getUserInfo().subscribeOn(Schedulers.io());
        Observable<SRCommonResult<SClassRoomResult>> roomInfoObservable = ApiRetrofit.getInstance().getClassRoomInfo().subscribeOn(Schedulers.io());
        Observable<SRCommonResult<StateToolResult>> switchObservable = ApiRetrofit.getInstance().getStudentSwitchState().subscribeOn(Schedulers.io());

        Observable.zip(userObservable, roomInfoObservable, switchObservable, (userResult, classRoomReult, stateToolResult) -> {
            //请求完成对3个结果进行组装
            List<SRCommonResult> results = new ArrayList<>();
            results.add(userResult);
            results.add(classRoomReult);
            results.add(stateToolResult);
            return results;
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<SRCommonResult>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<SRCommonResult> results) {
                if (((BaseCommonActivity) mContext).isDestroyed()) return;

                for (int i = 0; i < results.size(); i++) {
                    SRCommonResult<Object> commonResult = results.get(i);
                    if (commonResult == null) {
                        requestLoadFail(true);
                        return;
                    }

                    if (!commonResult.isSuccess()) {
                        requestLoadFail(false);
                        return;
                    }

                    Object resultObj = commonResult.getResult();
                    if (resultObj instanceof SUserModel) {
                        sMyselfModel = (SUserModel) commonResult.getResult();
                        SPUtils.getInstance().put(RoomConstant.USER_ID, ObjectUtil.getIntValue(sMyselfModel.getUser_id()));
                        SPUtils.getInstance().put(RoomConstant.USER_CHILD_ID, sMyselfModel.getChild_id());
                    } else if (resultObj instanceof SClassRoomResult) {
                        infoResult = (SClassRoomResult) resultObj;
                        doRoomInfo();
                    } else if (resultObj instanceof StateToolResult) {
                        StateToolResult result = (StateToolResult) resultObj;
                        doStateToolResult(result);
                        nowTime = commonResult.getNow();
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (((BaseCommonActivity) mContext).isDestroyed()) return;
                ExceptionUtil.sendException("getRoomUserInfoError", classType, e.getMessage());
                requestLoadFail(true);
            }

            @Override
            public void onComplete() {
                if (((BaseCommonActivity) mContext).isDestroyed()) return;
                ((BaseCommonActivity) mContext).dismissProgress();

                if (infoResult == null || TextUtils.isEmpty(infoResult.getCoursewareUrl())) {
                    ExceptionUtil.sendException("getRoomCoursewareEmptyError", classType, "");
                    doSCourseLoadFail();
                    return;
                }

                settingBean = infoResult.getSetting();
                mCurrentUrl = infoResult.getCoursewareUrl();
                boolean isLoadedReply = true;//SPUtils.getInstance().getBoolean(RoomConstant.SP_LOADED_REPLY, false);
                if (isLoadedReply) {
                    ioSocketListener();
                }

                if (nowTime == 0) {
                    ExceptionUtil.sendException("getRoomSrvtimeError", classType, "");
                }

                if (infoResult.getSetting() == null || TextUtils.isEmpty(infoResult.getSetting().getAgora_key())) {
                    ExceptionUtil.sendException("getRoomAgorakeyError", classType, "");
                }

                checkRoomResultSuccess(infoResult, nowTime, isLoadedReply);
            }
        });

        /*Observable.meger(userObservable, roomInfoObservable, switchObservable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SRCommonResult>() {
                    @Override
                    public void onComplete() {
                        if (((BaseCommonActivity) mContext).isDestroyed()) return;
                        ((BaseCommonActivity) mContext).dismissProgress();

                        if (infoResult == null || TextUtils.isEmpty(infoResult.getCoursewareUrl())) {
                            ExceptionUtil.sendException("getRoomCoursewareEmptyError", classType, "");
                            doSCourseLoadFail();
                            return;
                        }

                        settingBean = infoResult.getSetting();
                        mCurrentUrl = infoResult.getCoursewareUrl();
                        boolean isLoadedReply = true;*//*SPUtils.getInstance().getBoolean(RoomConstant.SP_LOADED_REPLY, false);*//*
                        if (isLoadedReply) {
                            ioSocketListener();
                        }

                        if (nowTime == 0) {
                            ExceptionUtil.sendException("getRoomSrvtimeError", classType, "");
                        }

                        if (infoResult.getSetting() == null || TextUtils.isEmpty(infoResult.getSetting().getAgora_key())) {
                            ExceptionUtil.sendException("getRoomAgorakeyError", classType, "");
                        }

                        checkRoomResultSuccess(infoResult, nowTime, isLoadedReply);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (((BaseCommonActivity) mContext).isDestroyed()) return;
                        ExceptionUtil.sendException("getRoomUserInfoError", classType, e.getMessage());
                        requestLoadFail(true);
                    }

                    @Override
                    public void onNext(SRCommonResult commonResult) {
                        if (((BaseCommonActivity) mContext).isDestroyed()) return;

                        Object resultObj = commonResult.getResult();
                        if (resultObj == null) {
                            requestLoadFail(true);
                            return;
                        }

                        if (!commonResult.isSuccess()) {
                            requestLoadFail(false);
                            return;
                        }

                        if (resultObj instanceof SUserModel) {
                            sMyselfModel = (SUserModel) resultObj;
                            SPUtils.getInstance().put(RoomConstant.USER_ID, ObjectUtil.getIntValue(sMyselfModel.getUser_id()));
                            SPUtils.getInstance().put(RoomConstant.USER_CHILD_ID, sMyselfModel.getChild_id());
                        } else if (resultObj instanceof SClassRoomResult) {
                            infoResult = (SClassRoomResult) resultObj;
                            doRoomInfo();
                        } else if (resultObj instanceof StateToolResult) {
                            StateToolResult result = (StateToolResult) resultObj;
                            doStateToolResult(result);
                            nowTime = commonResult.getNow();
                        }
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }
                });*/
    }

    private void doRoomInfo() {
        roomId = infoResult.getRoom_id();
        String courseWare = infoResult.getCourseware();
        if (!TextUtils.isEmpty(courseWare)) {
            infoResult.setCoursewareUrl(courseWare + (courseWare.contains("?") ? "&app_id=" : "?app_id=") + infoResult.getApp_id() +
                    "&user_type=" + RoomConstant.STUDENT_TYPE +
                    "&user_id=" + SPUtils.getInstance().getInt(RoomConstant.USER_ID) +
                    "&room_id=" + roomId +
                    "&avatar=" + (sMyselfModel == null ? "" : sMyselfModel.getAvatar()) +
                    "&name=" + (sMyselfModel == null ? "" : sMyselfModel.getNickname()));
        }

        List<SUserModel> users = infoResult.getUsers();
        if (users == null) return;
        skuId = ObjectUtil.getLongValue(infoResult.getCourse_num());
        SUserModel userModel;
        students = students == null ? new ArrayList<>() : students;
        students.clear();
        String myselfId = String.valueOf(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        for (int i = 0; i < users.size(); i++) {
            userModel = users.get(i);
            switch (userModel.getUser_type()) {
                case RoomConstant.STUDENT_TYPE:
                    if (TextUtils.equals(userModel.getUser_id(), myselfId)) {
                        diamondCount = userModel.getDiamond();
                    }

                    UserModel student = new UserModel();
                    student.setId(userModel.getUser_id());
                    student.setType(userModel.getUser_type());
                    student.setName(userModel.getNickname());
                    student.setPhoto(userModel.getAvatar());
                    student.setDia(userModel.getDiamond());
                    student.setDiamond(userModel.getDiamond());
                    student.setEntryTime(userModel.getEntry_time());
                    student.setLeaveTime(userModel.getLeave_time());
                    student.setUuid(userModel.getExternal_id());
                    students.add(student);
                    break;
                case RoomConstant.TEACHER_TYPE:
                    String teacherName = userModel.getNickname();
                    if (showClassTipsDialog && dialog == null) {
                        dialog = new ClassTipsDialog(mContext);
                        dialog.setData(teacherName, infoResult.getCourseware_name(), classType).show();
                    } else if (!showClassTipsDialog) {
                        if (classTipsDialog == null) {
                            classTipsDialog = new InteractClassTipsDialog(mContext);
                            classTipsDialog.show();
                        }
                    }
                    setTeacherInfo(ObjectUtil.getIntValue(teacherId = userModel.getUser_id()), teacherName);
                    break;
            }
        }
    }

    private void doStateToolResult(StateToolResult result) {
        mCurrentCoursewarePage = result.getPageSetting();
        Map studentMap = (Map) result.getToolSetting();
        List<SRSwitchModel> switchModels;
        SRSwitchModel switchModel;
        for (int i = 0; i < students.size(); i++) {
            String modelStr = JsonUtils.entityToJsonString(studentMap.get(students.get(i).getIdStr()));
            switchModels = JsonUtils.parseArray(modelStr, SRSwitchModel.class);
            if (switchModels == null) continue;
            for (int j = 0; j < switchModels.size(); j++) {
                switchModel = switchModels.get(j);
                if (switchModel == null) continue;
                switch (switchModel.getType()) {
                    case 4:
                        students.get(i).setMuted(switchModel.getState() == 2);
                        break;
                    case 5:
                        students.get(i).setDrawing(switchModel.getState() == 1);
                        break;
                    case 6:
                        students.get(i).setAnimate(switchModel.getState() == 1);
                        break;
                    case 15:
                        students.get(i).setStage(switchModel.getState() == 1);
                        students.get(i).setPoint(TextUtils.isEmpty(switchModel.getExtend()) ? "0,0" : switchModel.getExtend());
                        break;
                }
            }
        }

        animationLogList = animationLogList == null ? new ArrayList<>() : animationLogList;
        animationLogList.clear();
        List<Object> animationLogResult = result.getAnimationSetting();
        if (animationLogResult != null) {
            AnimateLogModel animateLogModel;
            for (int i = 0; i < animationLogResult.size(); i++) {
                String animationLogStr = JsonUtils.entityToJsonString(animationLogResult.get(i));
                animateLogModel = JsonUtils.parseObject(animationLogStr, AnimateLogModel.class);
                if (animateLogModel == null) continue;
                animateLogModel.setAnimateLog(animationLogStr);
                animationLogList.add(animateLogModel);
            }
        }
    }

    private void ioSocketListener() {
        if (settingBean != null) {
            RSocketModel rSocketModel = settingBean.getSocket();
            RoomApplication.getInstance().ioSocketNewListener(rSocketModel != null ? rSocketModel.getUrl() : null, rSocketModel != null ? rSocketModel.getPath() : null);
            agoraKey = settingBean.getAgora_key();
        }
    }

    @Override
    public void requestOutClassRoom() {
        if (!((BaseCommonActivity) mContext).checkNetWork()) {
            return;
        }

        ApiRetrofit.getInstance().leaveClassRoom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultModel -> {
                }, this::responseErrorToast);
    }

    /**
     * 下课了，退出教室，评价课程
     */
    void doClassOver(RoomType roomType) {
        if (!((BaseCommonActivity) mContext).checkNetWork()) {
            doNetWorkConnectFail();
            return;
        }

        ApiRetrofit.getInstance().leaveClassRoom()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultModel -> {
                    if (((BaseCommonActivity) mContext).isDestroyed()) return;
                    ((BaseCommonActivity) mContext).dismissProgress();
                    if (!resultModel.isSuccess()) {
                        responseErrorToast(resultModel.getCode());
                        return;
                    }

                    SOverClassModel result = resultModel.getResult();
                    if (result == null) {
                        responseErrorToast();
                        return;
                    }

                    RoomApplication.getInstance().doComment(mContext, roomType, result);

                    doOutClassRoom();
                }, this::responseErrorToast);
    }

    /**
     * webview与JS交互监听，webView加载课件失败监听
     *
     * @param wvCourseware ProgressWebView
     * @param tvPageNum    显示当前页码view
     */
    private boolean isNewCourse;

    void setNewWebViewListener(ProgressWebView wvCourseware, TextView tvPageNum) {
        wvCourseware.addJavascriptInterface(
                new Object() {
                    @JavascriptInterface
                    public void actionReply() {
                        LogUtils.i("AndroidtoJs", "actionReply");
                        mMainHandler.post(() -> {
                            SPUtils.getInstance().put(RoomConstant.SP_LOADED_REPLY, true);
                            loadUrl();
                            ioSocketListener();
                        });
                    }

                    @JavascriptInterface
                    public void autoMode(boolean autoMode) {
                        isNewCourse = autoMode;
                    }

                    @JavascriptInterface
                    public void callbackResLoaded(String allPageCount) {
                        LogUtils.i("AndroidtoJs", "callbackResLoaded:" + allPageCount);
                        mMainHandler.post(() -> {
                            wvCourseware.removeLoadErrorListener();
                            courseCaseLoaded = true;
                            wvCourseware.loadUrl("javascript:GlobalData.userType = 1");
                            wvCourseware.loadUrl("javascript:GlobalData.hasAuthority(true)");

                            totlePage = Integer.parseInt(allPageCount);

                            wvCourseware.evaluateJavascript("javascript:ucPlugin.autoMode", autoMode -> {
                                        isNewCourse = TextUtils.equals(autoMode, "true");
                                        if (isNewCourse) return;

                                        wvCourseware.loadUrl("javascript:PageMgr.turnPage (" + mCurrentCoursewarePage + ")");
                                        if (tvPageNum != null)
                                            tvPageNum.setText(mCurrentCoursewarePage + "/" + allPageCount);
                                        showAnimateLog();
                                    }
                            );
                        });
                    }

                    @JavascriptInterface
                    public void callbackPageLoaded(boolean isSingle, boolean isInteraction) {
                        LogUtils.i("AndroidtoJs", "callbackPageLoaded:" + (isSingle ? "isSingle:true," : "isSingle:false,")
                                + (isInteraction ? "isInteraction true" : "isInteraction false"));

                        mMainHandler.post(() -> {
                            if (isNewCourse) {
                                wvCourseware.evaluateJavascript("javascript:PageMgr.cur_page", value -> {
                                    mCurrentCoursewarePage = ObjectUtil.getIntValue(value);
                                    if (tvPageNum != null)
                                        tvPageNum.setText(mCurrentCoursewarePage + "/" + totlePage);
                                });
                                clearBoard();
                            }
                        });
                    }

                    @JavascriptInterface
                    public void callbackSendMsg(String msg) {
                        LogUtils.i("AndroidtoJs", "callbackSendMsg:" + msg);
                        mMainHandler.post(() -> {
                            if (isNewCourse) return;
                            wvCourseware.loadUrl("javascript:PageMgr.receiveMsg('" + msg + "')");
                            sendNewAction(msg);
                            record(RoomConstant.RECORD_ANIMATE_INFO, msg);
                        });
                    }

                    @JavascriptInterface
                    public void progressNum(int tProgressNum) {
                        LogUtils.i("AndroidtoJs", "progressNum:" + tProgressNum);
                    }

                    @JavascriptInterface
                    public void mouseClick(float tStageX, float tStageY) {
                        LogUtils.i("AndroidtoJs", "mouseClick: tStageX:" + tStageX + ",tStageY:" + tStageY);
                    }

                    @JavascriptInterface
                    public void mouseDown(float tStageX, float tStageY) {
                        LogUtils.i("AndroidtoJs", "mouseDown: tStageX:" + tStageX + ",tStageY:" + tStageY);
                    }

                    @JavascriptInterface
                    public void mouseMove(float tStageX, float tStageY) {
                        LogUtils.i("AndroidtoJs", "mouseMove: tStageX:" + tStageX + ",tStageY:" + tStageY);
                    }

                    @JavascriptInterface
                    public void mouseUp(float tStageX, float tStageY) {
                        LogUtils.i("AndroidtoJs", "mouseUp: tStageX:" + tStageX + ",tStageY:" + tStageY);
                    }
                }, "uuabc");

        wvCourseware.setLoadErrorListener(() -> {
            if (NetworkUtils.isConnected()) {
                doSCourseLoadFail();
            }
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void setVolumeDrawables() {
        volumesOneList = new Drawable[]{
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_0_l),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_1_l),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_2_l),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_3_l),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_4_l),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_5_l),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_6_l),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_7_l)
        };
        volumesTwoList = new Drawable[]{
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_0),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_1),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_2),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_3),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_4),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_5),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_6),
                mContext.getResources().getDrawable(R.drawable.icon_volume_level_7)
        };
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void setSingalDrawables() {
        mSingalList = new Drawable[]{
                mContext.getResources().getDrawable(R.drawable.icon_signal_3),
                mContext.getResources().getDrawable(R.drawable.icon_signal_2),
                mContext.getResources().getDrawable(R.drawable.icon_signal_1),
        };
    }

    void setVolumeLevel(ImageView tvVolume, Integer volume, boolean isList) {
        if (volume == 0) {
            tvVolume.setImageDrawable(isList ? volumesOneList[0] : volumesTwoList[0]);
        } else if (volume <= 36) {
            tvVolume.setImageDrawable(isList ? volumesOneList[1] : volumesTwoList[1]);
        } else if (volume <= 72) {
            tvVolume.setImageDrawable(isList ? volumesOneList[2] : volumesTwoList[2]);
        } else if (volume <= 108) {
            tvVolume.setImageDrawable(isList ? volumesOneList[3] : volumesTwoList[3]);
        } else if (volume <= 144) {
            tvVolume.setImageDrawable(isList ? volumesOneList[4] : volumesTwoList[4]);
        } else if (volume <= 180) {
            tvVolume.setImageDrawable(isList ? volumesOneList[5] : volumesTwoList[5]);
        } else if (volume <= 216) {
            tvVolume.setImageDrawable(isList ? volumesOneList[6] : volumesTwoList[6]);
        } else {
            tvVolume.setImageDrawable(isList ? volumesOneList[7] : volumesTwoList[7]);
        }
    }

    void setSignal(ImageView ivSignal, Integer singal) {
        switch (singal) {
            case Constants.QUALITY_EXCELLENT:
                ivSignal.setImageDrawable(mSingalList[0]);
                break;
            case Constants.QUALITY_GOOD:
            case Constants.QUALITY_POOR:
                ivSignal.setImageDrawable(mSingalList[1]);
                break;
            case Constants.QUALITY_BAD:
            case Constants.QUALITY_VBAD:
            case Constants.QUALITY_DOWN:
                ivSignal.setImageDrawable(mSingalList[2]);
                break;
        }
    }

    void dismissBaseDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }

        if (classTipsDialog != null) {
            classTipsDialog.dismiss();
        }
    }

    public void showAnimateLog() {
    }

    public void loadUrl() {
    }

    public void doNetWorkConnectFail() {
    }

    public void setTeacherInfo(int teacherId, String teacherName) {
    }

    public void checkRoomResultSuccess(SClassRoomResult result, int srvTime, boolean isLoadedReply) {
    }

    public void clearBoard() {
    }
}
