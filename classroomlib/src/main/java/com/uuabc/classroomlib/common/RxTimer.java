package com.uuabc.classroomlib.common;

import androidx.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class RxTimer {
    private Disposable mDisposable;
    private long mCurrentTimeMillis;

    public void throttleFirst(long milliSeconds, final IRxNext next) {
        if (System.currentTimeMillis() - mCurrentTimeMillis > milliSeconds) {
            mCurrentTimeMillis = System.currentTimeMillis();
            if (next != null) {
                next.doNext(milliSeconds);
            }
        }
    }

    public void timer(long milliSeconds, final IRxNext next) {
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
    public void interval(long milliSeconds, final IRxNext next) {
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
                        cancel();
                    }
                });
    }

    public void countDownTime(Long time, final IRxNext next) {
        Observable.intervalRange(0, time, 0, 1, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long number) {
                        if (next != null && mDisposable != null && !mDisposable.isDisposed()) {
                            next.doNext(time - number);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        next.doNext(0);
                    }
                });
    }

    /**
     * 取消订阅
     */
    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    public interface IRxNext {
        void doNext(long number);
    }
}
