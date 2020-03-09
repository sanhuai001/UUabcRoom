package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnSeekCompleteListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.utils.ObjectUtil;

public class SimpleVideoView extends RelativeLayout {
    private PLVideoView plVideo;
    private ProgressBar loadingView;
    private boolean needReStart;
    private boolean needLoadingView = true;
    private boolean userMediaCodecAuto;
    private long seekTo;
    private PlayListener listener;

    public SimpleVideoView(Context context) {
        this(context, null);
    }

    public SimpleVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SimpleVideoView(Context context, boolean needLoadingView, boolean userMediaCodecAuto) {
        super(context, null, 0);
        this.needLoadingView = needLoadingView;
        this.userMediaCodecAuto = userMediaCodecAuto;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_room_sdk_simple_video, this);
        plVideo = rootView.findViewById(R.id.video_view);
        loadingView = rootView.findViewById(R.id.progress_bar);
        initPlayer();
    }

    private void initPlayer() {
        if (needLoadingView) {
            plVideo.setBufferingIndicator(loadingView);
        } else {
            loadingView.setVisibility(GONE);
        }
        AVOptions avOptions = new AVOptions();
        if (userMediaCodecAuto) {
            avOptions.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_AUTO);
            avOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
            avOptions.setInteger(AVOptions.KEY_OPEN_RETRY_TIMES, 5);
        }
        avOptions.setInteger(AVOptions.KEY_FAST_OPEN, 1);
        avOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        avOptions.setInteger(AVOptions.KEY_SEEK_MODE, 1);

        plVideo.setAVOptions(avOptions);
        plVideo.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_FIT_PARENT);//全屏铺满
        plVideo.setOnPreparedListener(i -> {
            try {
                if (seekTo == 0L) {
                    plVideo.start();
                }

                if (listener != null) {
                    listener.onPrepared();
                }
            } catch (Exception ignored) {
            }
        });

        plVideo.setOnErrorListener(i -> {
            if (listener != null) {
                listener.onError();
            }
            return true;
        });
    }

    public void setOnSeekCompleteListener(PLOnSeekCompleteListener seekCompleteListener) {
        plVideo.setOnSeekCompleteListener(seekCompleteListener);
    }

    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        try {
            plVideo.setZOrderMediaOverlay(isMediaOverlay);
        } catch (Exception ignored) {
        }
    }

    public void seekTo(long currentTime) {
        try {
            plVideo.seekTo(1000 * currentTime);
        } catch (Exception ignored) {
        }
    }

    public int getCurrentPosition() {
        return ObjectUtil.getIntValue(plVideo.getCurrentPosition() / 1000);
    }

    public int getMediaDuration() {
        return ObjectUtil.getIntValue(plVideo.getDuration() / 1000);
    }

    public void play(String videoPath, PLOnCompletionListener listener) {
        if (needLoadingView && !loadingView.isShown())
            loadingView.setVisibility(VISIBLE);

        stopPlay();
        try {
            plVideo.setVideoPath(videoPath);
            plVideo.setOnCompletionListener(listener);
        } catch (Exception ignored) {
        }
    }

    public void playByPath(String videoPath) {
        if (needLoadingView && !loadingView.isShown())
            loadingView.setVisibility(VISIBLE);

        stopPlay();
        try {
            plVideo.setVideoPath(videoPath);
        } catch (Exception ignored) {
        }
    }

    public void stopPlay() {
        try {
            if (plVideo.canPause()) {
                plVideo.pause();
            }
        } catch (Exception ignored) {
        }
    }

    public void stopPlayback() {
        try {
            plVideo.stopPlayback();
        } catch (Exception ignored) {
        }
    }

    public void pause() {
        try {
            plVideo.pause();
        } catch (Exception ignored) {
        }
    }

    public void start() {
        try {
            plVideo.start();
        } catch (Exception ignored) {
        }
    }

    public void setNeedReStart(boolean needReStart) {
        this.needReStart = needReStart;
    }

    public boolean needReStart() {
        return needReStart;
    }

    public long getSeekTo() {
        return seekTo;
    }

    public void setSeekTo(long seekTo) {
        this.seekTo = seekTo;
    }

    public void setListener(PlayListener listener) {
        this.listener = listener;
    }

    public interface PlayListener {
        void onPrepared();

        void onError();
    }
}
