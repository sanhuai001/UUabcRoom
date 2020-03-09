package com.uuabc.classroomlib.utils;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Rxjava2.x实现定时器
 * Created by wb on 2017/9/21.
 */

public class RxTimerUtil {
    private static Disposable mDisposable;
    private static long mCurrentTimeMillis;

    public static void throttleFirst(long milliSeconds, final IRxNext next) {
        if (System.currentTimeMillis() - mCurrentTimeMillis > milliSeconds) {
            mCurrentTimeMillis = System.currentTimeMillis();
            if (next != null) {
                next.doNext(milliSeconds);
            }
        }
    }

    public static void timer(long milliSeconds, final IRxNext next) {
        cancel();
        Observable.timer(milliSeconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null && mDisposable != null && !mDisposable.isDisposed()) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        cancel();
                    }
                });
    }

    /**
     * 每隔milliSecond毫秒后执行next操作
     */
    public static void interval(long milliSeconds, final IRxNext next) {
        cancel();
        Observable.interval(milliSeconds, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (next != null && mDisposable != null && !mDisposable.isDisposed()) {
                            next.doNext(number);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 取消订阅
     */
    public static void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public interface IRxNext {
        void doNext(long number);
    }
}
