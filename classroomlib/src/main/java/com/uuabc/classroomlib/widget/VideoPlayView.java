package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnAudioFrameListener;
import com.pili.pldroid.player.PLOnBufferingUpdateListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.PLOnPreparedListener;
import com.pili.pldroid.player.PLOnVideoFrameListener;
import com.pili.pldroid.player.PLOnVideoSizeChangedListener;
import com.pili.pldroid.player.widget.PLVideoView;
import com.uuabc.classroomlib.R;

import java.util.Arrays;

public class VideoPlayView extends RelativeLayout {
    private static final String TAG = VideoPlayView.class.getSimpleName();
    private PLVideoView videoView;
    private LinearLayout loadingView;
    private MediaController mediaController;
    private VideoPlayListener listener;

    public VideoPlayView(Context context) {
        this(context, null);
    }

    public VideoPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_room_sdk_video_play, this);
        videoView = rootView.findViewById(R.id.video_view);
        loadingView = rootView.findViewById(R.id.loading_view);
        initPlayer();
    }

    public void setVideoPath(String videoPath) {
        if (videoView == null || TextUtils.isEmpty(videoPath)) return;
        try {
            videoView.setVideoPath(videoPath);
        } catch (Exception ignored) {

        }
    }

    public void setListener(VideoPlayListener listener) {
        this.listener = listener;
    }

    public void pause() {
        if (videoView == null) return;
        try {
            videoView.pause();
        } catch (Exception ignored) {

        }
    }

    public void stopPlayback() {
        if (videoView == null) return;
        try {
            videoView.stopPlayback();
        } catch (Exception ignored) {

        }
    }

    public void setDisplayAspectRatio(int previewMode) {
        if (videoView == null) return;
        try {
            videoView.setDisplayAspectRatio(previewMode);
        } catch (Exception ignored) {

        }
    }

    public void hideMediaController() {
        try {
            mediaController.setVisibility(GONE);
        } catch (Exception ignored) {

        }
    }

    public void initPlayer() {
        if (videoView == null) return;
        try {
            videoView.setBufferingIndicator(loadingView);
            AVOptions options = new AVOptions();
            options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
            options.setInteger(AVOptions.KEY_SEEK_MODE, 1);
            videoView.setAVOptions(options);
            videoView.setOnPreparedListener(plOnPreparedListener);
            videoView.setOnInfoListener(mOnInfoListener);
            videoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
            videoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
            videoView.setOnErrorListener(mOnErrorListener);
            videoView.setOnVideoFrameListener(mOnVideoFrameListener);
            videoView.setOnAudioFrameListener(mOnAudioFrameListener);

            mediaController = new MediaController(getContext());
            mediaController.setOnClickSpeedAdjustListener(mOnClickSpeedAdjustListener);
            videoView.setMediaController(mediaController);
        } catch (Exception ignored) {

        }
    }

    private PLOnInfoListener mOnInfoListener = new PLOnInfoListener() {
        @Override
        public void onInfo(int what, int extra) {
            LogUtils.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START:
                    LogUtils.i("first video render time: " + extra + "ms");
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_RENDERING_START:
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    LogUtils.i(TAG, "video frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                    LogUtils.i(TAG, "audio frame rendering, ts = " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_GOP_TIME:
                    LogUtils.i(TAG, "Gop Time: " + extra);
                    break;
                case PLOnInfoListener.MEDIA_INFO_SWITCHING_SW_DECODE:
                    LogUtils.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLOnInfoListener.MEDIA_INFO_METADATA:
                    LogUtils.i(TAG, videoView.getMetadata().toString());
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_BITRATE:
                case PLOnInfoListener.MEDIA_INFO_VIDEO_FPS:
                    break;
                case PLOnInfoListener.MEDIA_INFO_CONNECTED:
                    LogUtils.i(TAG, "Connected !");
                    break;
                case PLOnInfoListener.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                    LogUtils.i(TAG, "Rotation changed: " + extra);
                default:
                    break;
            }
        }
    };

    private PLOnPreparedListener plOnPreparedListener = new PLOnPreparedListener() {
        @Override
        public void onPrepared(int i) {
            if (videoView == null) return;
            try {
                videoView.start();
                if (listener != null) {
                    listener.onPreparedListener();
                }
            } catch (Exception ignored) {

            }
        }
    };

    private PLOnErrorListener mOnErrorListener = errorCode -> {
        LogUtils.e(TAG, "Error happened, errorCode = " + errorCode);
        if (listener != null) {
            listener.onErrorListener();
        }

        switch (errorCode) {
            case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                LogUtils.e(TAG, "IO Error!");
                return false;
            case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                LogUtils.e("failed to open player !");
                break;
            case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                LogUtils.e("failed to seek !");
                break;
            default:
                LogUtils.e("unknown error !");
                break;
        }
        return true;
    };

    private PLOnBufferingUpdateListener mOnBufferingUpdateListener = precent -> LogUtils.i(TAG, "onBufferingUpdate: " + precent);

    private PLOnVideoSizeChangedListener mOnVideoSizeChangedListener = (width, height) -> LogUtils.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);

    private PLOnVideoFrameListener mOnVideoFrameListener = (data, size, width, height, format, ts) -> {
        LogUtils.i(TAG, "onVideoFrameAvailable: " + size + ", " + width + " x " + height + ", " + format + ", " + ts);
        if (format == PLOnVideoFrameListener.VIDEO_FORMAT_SEI && bytesToHex(Arrays.copyOfRange(data, 19, 23)).equals("ts64")) {
            LogUtils.i(TAG, " timestamp: " + Long.valueOf(bytesToHex(Arrays.copyOfRange(data, 23, 31)), 16));
        }
    };

    private PLOnAudioFrameListener mOnAudioFrameListener = (data, size, samplerate, channels, datawidth, ts) -> LogUtils.i(TAG, "onAudioFrameAvailable: " + size + ", " + samplerate + ", " + channels + ", " + datawidth + ", " + ts);

    private MediaController.OnClickSpeedAdjustListener mOnClickSpeedAdjustListener = new MediaController.OnClickSpeedAdjustListener() {
        @Override
        public void onClickNormal() {
            // 0x0001/0x0001 = 2
            if (videoView == null) return;
            try {
                videoView.setPlaySpeed(0X00010001);
            } catch (Exception ignored) {

            }
        }

        @Override
        public void onClickFaster() {
            // 0x0002/0x0001 = 2
            if (videoView == null) return;
            try {
                videoView.setPlaySpeed(0X00020001);
            } catch (Exception ignored) {

            }
        }

        @Override
        public void onClickSlower() {
            // 0x0001/0x0002 = 0.5
            if (videoView == null) return;
            try {
                videoView.setPlaySpeed(0X00010002);
            } catch (Exception ignored) {

            }
        }
    };

    public interface VideoPlayListener {
        void onPreparedListener();

        void onErrorListener();
    }

    private String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
