package com.uuabc.classroomlib.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

public class MediaPlayerUtil {
    private static MediaPlayerUtil mediaPlayerUtil;
    private MediaPlayer mMediaPlayer;
    private boolean needReStart;

    public static MediaPlayerUtil getInstance() {
        if (mediaPlayerUtil == null) {
            synchronized (MediaPlayerUtil.class) {
                if (mediaPlayerUtil == null) {
                    mediaPlayerUtil = new MediaPlayerUtil();
                }
            }
        }
        return mediaPlayerUtil;
    }

    public void resetToPlay(Context context, int resid) {
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resid);
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
                release();
            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(mediaPlayer -> release());
        } catch (Exception ignored) {
        }
    }

    public void play(String url, MediaPlayer.OnCompletionListener listener) {
        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.reset();
                release();
            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(listener);
        } catch (Exception ignored) {
        }
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    public void release() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void setNeedReStart(boolean needReStart) {
        this.needReStart = needReStart;
    }

    public boolean needReStart() {
        return needReStart;
    }
}
