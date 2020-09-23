package com.sdk;

import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.sdk.core.callback.CompletedCallback;
import com.sdk.request.PostCachedLogRequest;
import com.sdk.result.PostCachedLogResult;
import com.uuabc.classroomlib.model.db.LogEntity;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CacheManager {

    private final static String TAG = "CacheManager";

    private Timer mTimer;
    private LOGClient mClient;

    public CacheManager(LOGClient mClient) {
        this.mClient = mClient;
    }

    public void setupTimer() {
        // 初始化定时器
        mTimer = new Timer();
        TimerTask timerTask = new CacheTimerTask(this);
        mTimer.schedule(timerTask, 30000, 30000);
    }

    private static class CacheTimerTask extends TimerTask {

        private WeakReference<CacheManager> mWeakCacheManager;

        public CacheTimerTask(CacheManager manager) {
            mWeakCacheManager = new WeakReference<>(manager);
        }

        @Override
        public void run() {
            CacheManager cacheManager = mWeakCacheManager.get();
            if (cacheManager == null) {
                return;
            }

            Log.i("aliyunLog", "---定时任务，开始处理本地缓存日志---");
            SLSDatabaseManager.getInstance().deleteTwoDaysRecords();
            SLSDatabaseManager.getInstance().deleteSurpassRecords();

            if (!NetworkUtils.isConnected()) {
                Log.i("aliyunLog", "Network connection failed, timed task processing cache ended");
                return;
            }

            cacheManager.fetchDataFromDBAndPost();
        }
    }

    // 停止定时器
    public void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            // 一定设置为null，否则定时器不会被回收
            mTimer = null;
        }
    }

    private void fetchDataFromDBAndPost() {
        List<LogEntity> list = SLSDatabaseManager.getInstance().queryRecordFromDB();
        if (list == null) return;
        Log.i("aliyunLog", "上传本地缓存日志条数:" + list.size());
        for (final LogEntity item : list) {
            try {
                PostCachedLogRequest request = new PostCachedLogRequest(item.getProject(), item.getStore(), item.getJsonString());
                this.mClient.asyncPostCachedLog(request, new CompletedCallback<PostCachedLogRequest, PostCachedLogResult>() {
                    @Override
                    public void onSuccess(PostCachedLogRequest request, PostCachedLogResult result) {
                        Log.i("aliyunLog", "send cached log success");
                        SLSDatabaseManager.getInstance().deleteRecordFromDB(item);
                    }

                    @Override
                    public void onFailure(PostCachedLogRequest request, LogException exception) {
                        Log.i("aliyunLog", "send cached log failed");
                    }
                });
            } catch (LogException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        stopTimer();
        Log.d(TAG, "CacheManager finalize");
    }
}
