package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.ObjectUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.common.CommonAdapter;
import com.uuabc.classroomlib.common.ViewHolder;
import com.uuabc.classroomlib.model.SocketModel.AnswerModel;
import com.uuabc.classroomlib.model.SocketModel.AnswerResultModel;
import com.uuabc.classroomlib.model.SocketModel.AnswerStatModel;
import com.uuabc.classroomlib.model.SocketModel.KeyValueModel;
import com.uuabc.classroomlib.utils.CompatUtil;
import com.uuabc.classroomlib.utils.RxTimerUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class AnswerResultView extends RelativeLayout {
    private Context context;
    private ProgressBar progressBar;
    private TextView tvProgress;
    private LottieAnimationView ivAnswerResult;
    private TextView tvAnswerResult;
    private TextView tvAnswerResultUb;
    private AnswerChoiseView answerChoiseView;
    private GridView gvOptionsProgress;
    public CommonAdapter<KeyValueModel> optionAdapter;
    private List<KeyValueModel> optionList;
    private int peopleCount;
    private String correctAnswer;

    public AnswerResultView(Context context) {
        this(context, null);
    }

    public AnswerResultView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnswerResultView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_stub_room_sdk_answer_result, this);

        progressBar = rootView.findViewById(R.id.progress_bar);
        tvProgress = rootView.findViewById(R.id.tv_progress);
        ivAnswerResult = rootView.findViewById(R.id.iv_answer_result);
        tvAnswerResult = rootView.findViewById(R.id.tv_answer_result);
        answerChoiseView = rootView.findViewById(R.id.answer_choise_view);
        gvOptionsProgress = rootView.findViewById(R.id.gv_options_progress);
        tvAnswerResultUb = rootView.findViewById(R.id.tv_answer_result_ub);
        ivAnswerResult.setImageAssetsFolder("images/");

        gvOptionsProgress.setAdapter(optionAdapter = new CommonAdapter<KeyValueModel>(R.layout.view_room_sdk_answer_progress_item) {
            @Override
            public void convert(ViewHolder vh, KeyValueModel model) {
                TextView tvCount = vh.getView(R.id.tv_person_count);
                tvCount.setText(model.getKey() + ":  " + model.getValue() + "人");
                ProgressBar progressBar = vh.getView(R.id.progress_bar);
                progressBar.setMax(peopleCount);
                progressBar.setProgress(Integer.parseInt(model.getValue()));

                if (!TextUtils.isEmpty(correctAnswer) && correctAnswer.contains(model.getKey())) {
                    progressBar.setProgressDrawable(CompatUtil.getDrawable(context, R.drawable.room_sdk_progress_green));
                    tvCount.setTextColor(CompatUtil.getColor(context, R.color.green));
                } else {
                    progressBar.setProgressDrawable(CompatUtil.getDrawable(context, R.drawable.room_sdk_progress_red));
                    tvCount.setTextColor(CompatUtil.getColor(context, R.color.gray_activity_color));
                }
            }
        });
    }

    public void setAnswerResultData(boolean isCorrect, AnswerModel answerModel, AnswerResultModel answerResult) {
        if (answerModel == null) return;
        setVisibility(VISIBLE);
        stopPlayVideo();
        correctAnswer = answerResult.getCorrect();
        if (isCorrect) {
            tvAnswerResult.setTextColor(CompatUtil.getColor(context, R.color.color_class_room_answer_green));
            int percent = ((answerResult.getPeople() - answerResult.getIndex()) * 100) / answerResult.getPeople();
            tvAnswerResult.setText(Html.fromHtml(context.getString(R.string.live_class_room_answer_correct_str, answerResult.getTime(), percent + "%")));
            tvAnswerResultUb.setVisibility(VISIBLE);
            tvAnswerResultUb.setText(context.getString(R.string.live_class_room_answer_result_ub_str, answerResult.getUb()));
            ivAnswerResult.setAnimation("answer_correct.json");
        } else {
            tvAnswerResult.setTextColor(CompatUtil.getColor(context, R.color.color_class_room_answer_red));
            tvAnswerResult.setText(Html.fromHtml(context.getString((ObjectUtils.isNotEmpty(answerResult.getOption())) ? R.string.live_class_room_answer_error_str : R.string.live_class_room_answer_notasked_str)));
            tvAnswerResultUb.setVisibility(GONE);
            ivAnswerResult.setAnimation("answer_err.json");
        }

        try {
            ivAnswerResult.cancelAnimation();
            ivAnswerResult.playAnimation();
        } catch (Exception ignored) {
        }

        answerChoiseView.setOptions(answerResult.getOption());
        answerChoiseView.setAnswerData(answerModel, true, correctAnswer);
    }

    public void updateAnswerStat(AnswerStatModel statModel, AnswerModel answerModel) {
        if (answerModel == null) return;
        RxTimerUtil.timer(200, number -> {
            progressBar.setMax(statModel.getPeople());
            progressBar.setProgress(statModel.getCount());
            tvProgress.setText(Html.fromHtml(context.getString(R.string.live_class_room_answer_progress, statModel.getCount(), statModel.getPeople())));

            optionList = optionList == null ? new ArrayList<>() : optionList;
            optionList.clear();

            if (!TextUtils.isEmpty(answerModel.getA()))
                addOption("A", String.valueOf(statModel.getOpt_a()));
            if (!TextUtils.isEmpty(answerModel.getB()))
                addOption("B", String.valueOf(statModel.getOpt_b()));
            if (!TextUtils.isEmpty(answerModel.getC()))
                addOption("C", String.valueOf(statModel.getOpt_c()));
            if (!TextUtils.isEmpty(answerModel.getD()))
                addOption("D", String.valueOf(statModel.getOpt_d()));

            peopleCount = statModel.getPeople();
            gvOptionsProgress.setNumColumns(optionList.size() < 3 ? 3 : optionList.size());
            optionAdapter.setDatas(optionList);
        });
    }

    private void addOption(String key, String value) {
        if (!TextUtils.isEmpty(value) && !TextUtils.equals("-1", value)) {
            KeyValueModel model = new KeyValueModel(key, value);
            optionList.add(model);
        }
    }

    public void stopPlayVideo() {
        answerChoiseView.stopPlayVideo();
    }
}
