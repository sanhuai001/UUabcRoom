package com.uuabc.classroomlib.common;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.widget.ImageView;

import com.uuabc.classroomlib.RoomApplication;

@SuppressLint("Registered")
public class BaseCloudAnimationActivity extends BaseCommonActivity {
    private AnimatorSet lefToRightSet;
    private AnimatorSet rightToLeftSet;

    /**
     * 初始化背景动画
     */
    public void initAnimation(ImageView ivCloudLeft, ImageView ivCloudRight) {
        ObjectAnimator cloudLeftAnimator1 = ObjectAnimator.ofFloat(ivCloudLeft, "translationX", 0, RoomApplication.getInstance().getScreenWidth()).setDuration(50000);
        ObjectAnimator cloudLeftAnimator2 = ObjectAnimator.ofFloat(ivCloudLeft, "translationX", RoomApplication.getInstance().getScreenWidth(), 0).setDuration(50000);
        cloudLeftAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                lefToRightSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        cloudLeftAnimator2.setStartDelay(2000);
        lefToRightSet = new AnimatorSet();
        lefToRightSet.play(cloudLeftAnimator1).before(cloudLeftAnimator2);
        lefToRightSet.start();

        ObjectAnimator cloudRightAnimator1 = ObjectAnimator.ofFloat(ivCloudRight, "translationX", 0, -RoomApplication.getInstance().getScreenWidth()).setDuration(50000);
        ObjectAnimator cloudRightAnimator2 = ObjectAnimator.ofFloat(ivCloudRight, "translationX", -RoomApplication.getInstance().getScreenWidth(), 0).setDuration(50000);
        cloudRightAnimator2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                rightToLeftSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        cloudRightAnimator2.setStartDelay(2000);
        rightToLeftSet = new AnimatorSet();
        rightToLeftSet.play(cloudRightAnimator1).before(cloudRightAnimator2);
        rightToLeftSet.start();

    }

}
