package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Chronometer;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.TimeUtils;
import com.jakewharton.rxbinding2.view.RxView;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.SocketModel.AnswerModel;
import com.uuabc.classroomlib.model.SocketModel.AnswerTimeModel;
import com.uuabc.classroomlib.model.SocketModel.KeyValueModel;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.utils.SocketIoUtils;
import com.uuabc.classroomlib.utils.TipUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressLint({"SimpleDateFormat", "CheckResult"})
public class AnswerView extends RelativeLayout {
    private Context context;
    private Chronometer chronometer;
    private AnswerChoiseView answerChoiseView;
    public AnswerModel answerModel;

    public AnswerView(Context context) {
        this(context, null);
    }

    public AnswerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_stub_room_sdk_answer, this);
        chronometer = rootView.findViewById(R.id.chronometer_answer);
        answerChoiseView = rootView.findViewById(R.id.answer_choise_view);

        RxView.clicks(rootView.findViewById(R.id.btn_submit)).throttleFirst(2, TimeUnit.SECONDS).subscribe(o -> {
            List<KeyValueModel> selectedList = getSelectedList();
            if (selectedList.size() == 0) {
                TipUtils.warningShow(context, context.getString(R.string.live_class_room_answer_choise_question_str));
                return;
            }

            StringBuilder sbSelected = new StringBuilder();
            for (int i = 0; i < selectedList.size(); i++) {
                sbSelected.append(selectedList.get(i).getKey());
                if (i != selectedList.size() - 1) {
                    sbSelected.append(",");
                }
            }

            HashMap<String, Object> shareMap = new HashMap<>();
            shareMap.put("value", sbSelected.toString());
            shareMap.put("action", RoomConstant.SUBMIT);
            RoomApplication.getInstance().sendMessage(RoomConstant.TOPIC, SocketIoUtils.getJsonObject(shareMap));
        });
    }

    public void setAnswerData(AnswerModel answerModel) {
        if (answerModel == null) return;
        this.answerModel = answerModel;
        if (answerModel.isFinished() || answerModel.isTasked()) {
            return;
        }

        stopPlayVideo();
        setVisibility(VISIBLE);
        answerChoiseView.setAnswerData(answerModel, false, null);
    }

    public List<KeyValueModel> getSelectedList() {
        return answerChoiseView.optionAdapter != null ?
                answerChoiseView.optionAdapter.getSelectedList() : new ArrayList<>();
    }

    public void stopPlayVideo() {
        answerChoiseView.stopPlayVideo();
    }

    public void setAnswerTime(AnswerTimeModel answerTimeModel) {
        if (answerModel == null || answerTimeModel == null) return;
        if (TextUtils.equals(answerModel.getId(), answerTimeModel.getTid())) {
            String dateTime = TimeUtils.millis2String(ObjectUtil.getLongValue(answerTimeModel.getTime() * 1000), new SimpleDateFormat("mm分ss秒"));
            chronometer.setText(dateTime);
        }
    }
}
