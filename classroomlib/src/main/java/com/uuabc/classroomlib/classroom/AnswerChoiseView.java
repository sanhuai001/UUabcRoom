package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gcssloop.widget.RCImageView;
import com.jakewharton.rxbinding2.view.RxView;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.common.CommonAdapter;
import com.uuabc.classroomlib.common.ViewHolder;
import com.uuabc.classroomlib.custom.TextureVideoViewOutlineProvider;
import com.uuabc.classroomlib.model.SocketModel.AnswerModel;
import com.uuabc.classroomlib.model.SocketModel.KeyValueModel;
import com.uuabc.classroomlib.utils.MediaPlayerUtil;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.widget.SimpleVideoView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SuppressLint("CheckResult")
public class AnswerChoiseView extends RelativeLayout {
    private TextView tvAnswerContent;
    private List<GridView> gridViews;
    private GridView gvCurrent;
    private ConstraintLayout clMedia;
    private FrameLayout flMedia;
    private ImageView ivPlay;
    private SimpleVideoView plVideo;
    private FrameLayout flVideo;
    private LottieAnimationView ivMediaType;
    private RCImageView ivTopic;
    public CommonAdapter<KeyValueModel> optionAdapter;
    private List<KeyValueModel> optionList;
    private boolean isResult;
    private String options;
    private List<KeyValueModel> selectedList;
    private String correctAnswer;
    private String currentMediaUrl;
    private boolean isTopicVideo;
    private MediaPlayerUtil mediaPlayer;

    public AnswerChoiseView(Context context) {
        this(context, null);
    }

    public AnswerChoiseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnswerChoiseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        gridViews = new ArrayList<>();
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView;
        if (inflater == null) return;
        rootView = inflater.inflate(R.layout.view_room_sdk_choise_question, this);
        tvAnswerContent = rootView.findViewById(R.id.tv_answer_content);

        gridViews.add(rootView.findViewById(R.id.gv_answer_choise_one));
        gridViews.add(rootView.findViewById(R.id.gv_answer_choise_two));
        gridViews.add(rootView.findViewById(R.id.gv_answer_choise_three));

        clMedia = rootView.findViewById(R.id.cl_media);
        flMedia = rootView.findViewById(R.id.fl_media);
        flMedia.setOutlineProvider(new TextureVideoViewOutlineProvider(SizeUtils.sp2px(20)));
        flMedia.setClipToOutline(true);

        ivTopic = rootView.findViewById(R.id.iv_answer_img);
        ivMediaType = rootView.findViewById(R.id.iv_media_type);
        flVideo = rootView.findViewById(R.id.fl_video);

        ivPlay = rootView.findViewById(R.id.fiv_play);
        ivMediaType.setImageAssetsFolder("images/");
        RxView.clicks(ivPlay).throttleFirst(500, TimeUnit.MILLISECONDS).subscribe(o -> {
            MediaPlayerUtil.getInstance().resetToPlay(getContext(), R.raw.click_sing);
            if (isTopicVideo && (plVideo == null || plVideo.needReStart())) {
                doMediaPlay();
            } else if (!isTopicVideo && (mediaPlayer == null || mediaPlayer.needReStart())) {
                doMediaPlay();
            } else {
                doPauseResume();
            }
        });
    }

    public void setAnswerData(AnswerModel answerModel, boolean isResult, String correctAnswer) {
        if (answerModel == null) return;
        this.isResult = isResult;
        this.correctAnswer = correctAnswer;
        tvAnswerContent.setText(Html.fromHtml(answerModel.getTopic_content()));

        boolean topicFileTypeNull = doAnswerTopic(answerModel);
        doAnswerOptions(answerModel, topicFileTypeNull);
    }

    private boolean doAnswerTopic(AnswerModel answerModel) {
        stopPlayVideo();
        flVideo.removeAllViews();
        String topicFileType = TextUtils.isEmpty(answerModel.getTopic_file_type()) ? "" : answerModel.getTopic_file_type();

        switch (topicFileType) {
            case "image/png"://图片
            case "image/jpeg"://图片
                controlViewShown(true);
                RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_room_sdk_live_class_load_fail_default).error(R.drawable.ic_room_sdk_live_class_load_fail_default);
                Glide.with(getContext()).load(answerModel.getTopic_img()).apply(options).into(ivTopic);
                controlGridViewShow(2);
                return false;
            case "audio/mp3"://音频
            case "video/mp4"://视频
                isTopicVideo = TextUtils.equals("video/mp4", topicFileType);
                controlViewShown(false);
                if (isTopicVideo) {
                    flVideo.setVisibility(VISIBLE);
                    ivMediaType.setImageResource(R.drawable.ic_room_sdk_live_class_answer_video);
                    ivMediaType.setBackgroundResource(R.color.transparent);
                } else {
                    ivMediaType.setAnimation("audio_play.json");
                    ivMediaType.setBackgroundResource(R.drawable.ic_room_sdk_live_class_answer_audio);
                }
                currentMediaUrl = answerModel.getTopic_img();
                controlGridViewShow(2);
                if (!isResult)
                    doMediaPlay();
                return false;
            default://文字
                clMedia.setVisibility(GONE);
                controlGridViewShow(TextUtils.equals(answerModel.getTopic_content_type(), "3") ? 0 : 1);
                return true;
        }
    }

    private void controlViewShown(boolean isImg) {
        clMedia.setVisibility(VISIBLE);
        ivTopic.setVisibility(isImg ? VISIBLE : GONE);
        flMedia.setVisibility(isImg ? GONE : VISIBLE);
        ivPlay.setVisibility(isImg ? GONE : VISIBLE);
        flVideo.removeAllViews();
        ivMediaType.setVisibility(isImg ? GONE : VISIBLE);
        ivPlay.setImageResource(R.drawable.ic_room_sdk_play_off);
    }

    private void controlGridViewShow(int position) {
        for (int i = 0; i < gridViews.size(); i++) {
            if (i == position) {
                gvCurrent = gridViews.get(i);
                gridViews.get(i).setVisibility(VISIBLE);
            } else {
                gridViews.get(i).setVisibility(GONE);
            }
        }
    }

    private void doAnswerOptions(AnswerModel answerModel, boolean topicFileTypeNull) {
        String optionType = TextUtils.isEmpty(answerModel.getTopic_content_type()) ? "" : answerModel.getTopic_content_type();
        optionList = optionList == null ? new ArrayList<>() : optionList;
        optionList.clear();
        selectedList = selectedList == null ? new ArrayList<>() : selectedList;
        selectedList.clear();
        addOption("A", answerModel.getA());
        addOption("B", answerModel.getB());
        addOption("C", answerModel.getC());
        addOption("D", answerModel.getD());
        addOption("E", answerModel.getE());

        switch (optionType) {
            case "1"://图片
            case "2"://音视频
                gvCurrent.setNumColumns(2);
                gvCurrent.setAdapter(optionAdapter = new CommonAdapter<KeyValueModel>(R.layout.view_room_sdk_answer_choice_item) {
                    @Override
                    public void convert(ViewHolder vh, KeyValueModel model) {
                        ImageView ivOption = vh.getView(R.id.iv_option);
                        ivOption.setImageResource(getOptionResId(model.getKey()));
                        CheckBox checkBox = vh.getView(R.id.cb_item);
                        if (TextUtils.equals(optionType, "1")) {
                            RCImageView imageView = vh.getView(R.id.iv_answer);
                            RequestOptions options = new RequestOptions().placeholder(R.drawable.ic_room_sdk_live_class_load_fail_default).error(R.drawable.ic_room_sdk_live_class_load_fail_default);
                            Glide.with(getContext()).load(model.getValue()).apply(options).into(imageView);
                        }

                        vh.getView(R.id.cl_item).setBackgroundResource(checkBox.isChecked() ? R.drawable.ic_room_sdk_answer_img_option_checked : R.drawable.ic_room_sdk_answer_img_option_unchecked);

                        if (isResult) {
                            doCorrectOption(vh.getView(R.id.iv_correct_tag), model);
                        }
                    }

                    @Override
                    public void checkedUpdate() {
                        optionAdapter.notifyDataSetChanged();
                    }
                });
                break;
            case "3"://文字
                gvCurrent.setNumColumns(1);
                gvCurrent.setAdapter(optionAdapter = new CommonAdapter<KeyValueModel>(topicFileTypeNull ?
                        R.layout.view_room_sdk_answer_choice_simple_item : R.layout.view_room_sdk_answer_choise_simple_item_two) {
                    @Override
                    public void convert(ViewHolder vh, KeyValueModel model) {
                        ImageView ivOption = vh.getView(R.id.iv_option);
                        ImageView ivOptionBg = vh.getView(R.id.iv_option_bg);
                        CheckBox checkBox = vh.getView(R.id.cb_item);
                        ivOption.setImageResource(getOptionResId(model.getKey()));
                        ivOptionBg.setBackgroundResource(checkBox.isChecked() ? R.drawable.ic_room_sdk_live_class_answer_checked : R.drawable.ic_room_sdk_live_class_answer_unchecked);
                        vh.setText(R.id.tv_option_desc, model.getValue());

                        if (isResult) {
                            doCorrectOption(vh.getView(R.id.iv_correct_tag), model);
                        }
                    }

                    @Override
                    public void checkedUpdate() {
                        optionAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }

        optionAdapter.setSelectedList(selectedList);
        optionAdapter.setDatas(optionList)
                .setCheckBoxId(R.id.cb_item)
                .setSingleElection(TextUtils.equals(answerModel.getTopic_type(), "1"))
                .setCanEnable(!isResult)
                .setClickableViewId(R.id.cl_item);
    }

    private int getOptionResId(String option) {
        int optionResId = 0;
        switch (option) {
            case "A":
                optionResId = R.drawable.ic_room_sdk_answer_option_a;
                break;
            case "B":
                optionResId = R.drawable.ic_room_sdk_answer_option_b;
                break;
            case "C":
                optionResId = R.drawable.ic_room_sdk_answer_option_c;
                break;
            case "D":
                optionResId = R.drawable.ic_room_sdk_answer_option_d;
                break;
        }
        return optionResId;
    }

    private void doCorrectOption(ImageView ivCorrect, KeyValueModel model) {
        if (model == null) return;

        if (!TextUtils.isEmpty(correctAnswer) && correctAnswer.contains(model.getKey())) {
            ivCorrect.setVisibility(VISIBLE);
        } else {
            ivCorrect.setVisibility(GONE);
        }
    }

    private void addOption(String key, String value) {
        if (!TextUtils.isEmpty(value)) {
            KeyValueModel model = new KeyValueModel(key, value);
            optionList.add(model);
            if (isResult && !TextUtils.isEmpty(options) && options.contains(model.getKey())) {
                selectedList.add(model);
            }
        }
    }

    public void setOptions(String options) {
        this.options = options;
    }

    private void doMediaPlay() {
        if (isTopicVideo) {
            if (plVideo == null) {
                plVideo = new SimpleVideoView(getContext());
                plVideo.setNeedReStart(true);
                plVideo.setOutlineProvider(new TextureVideoViewOutlineProvider(SizeUtils.sp2px(20)));
                plVideo.setClipToOutline(true);
                plVideo.setZOrderMediaOverlay(true);
            }
            flVideo.removeAllViews();
            flVideo.addView(plVideo);

            plVideo.play(currentMediaUrl, videoCompletelistener);
            ivMediaType.setVisibility(GONE);
            plVideo.setVisibility(VISIBLE);
            plVideo.setNeedReStart(false);
        } else {
            mediaPlayer = mediaPlayer == null ? new MediaPlayerUtil() : mediaPlayer;
            mediaPlayer.play(currentMediaUrl, audioCompletelistener);
            mediaPlayer.setNeedReStart(false);
            playAudioAnimation();
        }

        ivPlay.setImageResource(R.drawable.ic_room_sdk_play_on);
        ivPlay.setTag(true);
    }

    private void doPauseResume() {
        if (ObjectUtil.getBoolean(ivPlay.getTag())) {
            doMediaPause();
            ivPlay.setImageResource(R.drawable.ic_room_sdk_play_off);
            ivPlay.setTag(false);
        } else {
            doMediaReStart();
            ivPlay.setImageResource(R.drawable.ic_room_sdk_play_on);
            ivPlay.setTag(true);
        }
    }

    private void doMediaPause() {
        if (isTopicVideo)
            plVideo.pause();
        else {
            cancleAduioAnimation();
            mediaPlayer.pause();
        }
    }

    private void doMediaReStart() {
        if (isTopicVideo)
            plVideo.start();
        else {
            playAudioAnimation();
            mediaPlayer.start();
        }
    }

    private void playAudioAnimation() {
        try {
            ivMediaType.cancelAnimation();
            ivMediaType.playAnimation();
        } catch (Exception ignored) {
        }
    }

    private void cancleAduioAnimation() {
        try {
            ivMediaType.cancelAnimation();
        } catch (Exception ignored) {
        }
    }

    public void stopPlayVideo() {
        if (plVideo != null) {
            plVideo.stopPlay();
            plVideo.setNeedReStart(true);
            flVideo.removeAllViews();
        }

        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer.setNeedReStart(true);
            cancleAduioAnimation();
        }
    }

    MediaPlayer.OnCompletionListener audioCompletelistener = mp -> {
        cancleAduioAnimation();
        ivPlay.setImageResource(R.drawable.ic_room_sdk_play_off);
        mediaPlayer.setNeedReStart(true);
        mediaPlayer.release();
    };

    PLOnCompletionListener videoCompletelistener = () -> {
        ivPlay.setImageResource(R.drawable.ic_room_sdk_play_off);
        plVideo.setNeedReStart(true);
    };
}
