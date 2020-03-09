package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.widget.VideoView;

import com.uuabc.classroomlib.utils.ObjectUtil;

public class SimpleVideoView2 extends VideoView {
    public long seekTo;
    private PlayListener listener;

    public SimpleVideoView2(Context context) {
        this(context, null);
    }

    public SimpleVideoView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleVideoView2(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SimpleVideoView2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupVideo();

    }

    private void setupVideo() {
        setOnPreparedListener(mp -> {
            if (listener != null) {
                listener.onPrepared(mp);
            }
        });

        setOnCompletionListener(mp -> {
            stopPlaybackVideo();
            if (listener != null) {
                listener.onComplete();
            }
        });

        setOnErrorListener((mp, what, extra) -> {
            if (listener != null) {
                listener.onError();
            }
            return true;
        });
    }

    public void stopPlaybackVideo() {
        try {
            stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListener(PlayListener listener) {
        this.listener = listener;
    }

    public void setZOrderOverlay(boolean isMediaOverlay) {
        try {
            setZOrderMediaOverlay(isMediaOverlay);
        } catch (Exception ignored) {
        }
    }

    public void setSeekTo(long currentTime) {
        try {
            seekTo((int) (1000 * currentTime));
        } catch (Exception ignored) {
        }
    }

    public int getCurrentPos() {
        return ObjectUtil.getIntValue(getCurrentPosition() / 1000);
    }

    public int getMediaDuration() {
        return ObjectUtil.getIntValue(getDuration() / 1000);
    }

    public interface PlayListener {
        void onPrepared(MediaPlayer mp);

        void onError();

        void onComplete();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(0, widthMeasureSpec);
        int height = getDefaultSize(0, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
