package com.uuabc.classroomlib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 生命周期实现类
 * 用于管理所有Activity
 */

public final class LifecycleManager implements Application.ActivityLifecycleCallbacks {

    private static final String CREATED = "onActivityCreated";
    private static final String STARTED = "onActivityStarted";
    private static final String RESUMED = "onActivityResumed";
    private static final String PAUSED = "onActivityPaused";
    private static final String STOPPED = "onActivityStopped";
    private static final String DESTROYED = "onActivityDestroyed";

    private static final String TAG = "LifecycleManager";
    private static LifecycleManager sInstance;
    private final HashMap<String, String> mActivityStatusMap;
    private final CopyOnWriteArrayList<Listener> mListeners;
    private final Handler mHandler;
    private Runnable mRunnable;
    private boolean isBackground;
    private boolean isPaused;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated() called with: activity = [" + activity + "], savedInstanceState = [" + savedInstanceState + "]");
        mActivityStatusMap.put(activity.getClass().getName(), CREATED);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Log.i(TAG, "onActivityStarted() called with: activity = [" + activity + "]");
        mActivityStatusMap.put(activity.getClass().getName(), STARTED);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Log.i(TAG, "onActivityResumed() called with: activity = [" + activity + "]");
        mActivityStatusMap.put(activity.getClass().getName(), RESUMED);

        isPaused = false;
        boolean wasBackground = isBackground;
        isBackground = false;

        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        if (wasBackground) {
            Log.i(TAG, "后台切到了前台");
            for (Listener listener : mListeners) {
                listener.onForeground();
            }
        } else {
            Log.i(TAG, "仍然处于前台");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Log.e(TAG, "onActivityPaused() called with: activity = [" + activity + "]");
        mActivityStatusMap.put(activity.getClass().getName(), PAUSED);

        isPaused = true;

        if (mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        mHandler.postDelayed(mRunnable = () -> {
            if (!isBackground && isPaused) {
                Log.i(TAG, "已经切到后台");
                isBackground = true;
                for (Listener listener : mListeners) {
                    listener.onBackground();
                }
            } else {
                Log.i(TAG, "仍然处于前台");
            }
        }, 500);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Log.e(TAG, "onActivityStopped() called with: activity = [" + activity + "]");
        mActivityStatusMap.put(activity.getClass().getName(), STOPPED);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Log.e(TAG, "onActivitySaveInstanceState() called with: activity = [" + activity + "], outState = [" + outState + "]");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityStatusMap.remove(activity.getClass().getName());
    }

    /**
     * 是否处于后台
     *
     * @return true：是 false：不是
     */
    public boolean isOnBackground() {
        return isBackground;
    }

    public String getStatus(@NonNull Activity activity) {
        String name = activity.getClass().getName();
        if (mActivityStatusMap.containsKey(name)) {
            return mActivityStatusMap.get(name);
        }
        return DESTROYED;
    }

    static void init(@NonNull Application app) {
        if (sInstance != null) {
            throw new RuntimeException("Don't re-init !");
        }
        sInstance = new LifecycleManager();
        app.registerActivityLifecycleCallbacks(sInstance);
    }

    public static LifecycleManager get() {
        if (sInstance == null) {
            throw new RuntimeException("Please init first !");
        }
        return sInstance;
    }

    private LifecycleManager() {
        mActivityStatusMap = new HashMap<>();
        mListeners = new CopyOnWriteArrayList<>();
        mHandler = new Handler();
    }

    /**
     * 应用前后台切换监听
     */
    public interface Listener {
        /**
         * 前台切到后台执行的操作
         */
        void onBackground();

        /**
         * 后台切到前台执行的操作
         */
        void onForeground();
    }

    public void addListener(@NonNull Listener listener) {
        mListeners.add(listener);
    }

    public void removeListener(@NonNull Listener listener) {
        mListeners.remove(listener);
    }
}
