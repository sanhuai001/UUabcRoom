package com.uuabc.classroomlib.classroom;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.widget.GradeView;

public class CustomLottieAnimationView extends LottieAnimationView {
    private AnimationFinishListener listener;

    public CustomLottieAnimationView(Context context) {
        super(context);
    }

    public CustomLottieAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLottieAnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(AnimationFinishListener listener) {
        this.listener = listener;
    }

    public interface AnimationFinishListener {
        void onFinish();
    }

    /**
     * 1v1教室钻石动画
     */
    public void showDiamonds(RelativeLayout rlFlower, View topDiamondView, int count) {
        setImageAssetsFolder("images/");
        addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                moveDiamond(rlFlower, topDiamondView, count);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        rlFlower.addView(this);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        requestLayout();
        setAnimation("diamond.json");
        playAnimation();
    }

    /**
     * 1v4钻石动画
     */
    public void showDiamonds(LinearLayout rlFlower) {
        setImageAssetsFolder("images/");
        rlFlower.setGravity(Gravity.CENTER);
        rlFlower.addView(this);
        setAnimation("diamond.json");
        playAnimation();
    }

    @SuppressLint("SetTextI18n")
    private void moveDiamond(RelativeLayout rlFlower, View topDiamondView, int count) {
        int[] endLoc = new int[2];
        topDiamondView.getLocationOnScreen(endLoc);
        animate().scaleX(0.2f).scaleY(0.2f)
                .translationX(endLoc[0] / 2 - getWidth() / 2)
                .translationY(endLoc[1] - getHeight() / 2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (topDiamondView instanceof TextView) {
                            ((TextView) topDiamondView).setText("x" + count);
                        } else {
                            ((GradeView) topDiamondView).setStar(count);
                        }
                        rlFlower.removeView(CustomLottieAnimationView.this);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).setDuration(1000);
    }

    /**
     * 直播课星星动画
     */
    public void showStar(RelativeLayout rlFlower, View targetView, ConstraintLayout clCoursewareContainer, int teacherViewWidth) {
        setImageAssetsFolder("images/");
        addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                moveStar(rlFlower, targetView, clCoursewareContainer, teacherViewWidth);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        rlFlower.addView(this);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.topMargin = (int) (RoomApplication.getInstance().getScreenHeight() * 0.09);
        params.width = clCoursewareContainer.getWidth();
        params.height = clCoursewareContainer.getHeight();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        requestLayout();
        setAnimation("star.json");
        playAnimation();
    }

    /**
     * 直播课Ub动画
     */
    public void showUb(RelativeLayout rlFlower, View targetView, ConstraintLayout clCoursewareContainer, int count) {
        setImageAssetsFolder("images/");
        addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                moveUb(rlFlower, targetView, clCoursewareContainer, count);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        rlFlower.addView(this);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.width = clCoursewareContainer.getWidth();
        params.height = clCoursewareContainer.getHeight();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        requestLayout();
        setAnimation("ub.json");
        playAnimation();
    }

    private void moveStar(RelativeLayout rlFlower, View targetView, ConstraintLayout clCoursewareContainer, int teacherViewWidth) {
        int[] endLoc = new int[2];
        targetView.getLocationOnScreen(endLoc);
        animate().scaleX(0.1f).scaleY(0.1f)
                .translationX(endLoc[0] / 2 - clCoursewareContainer.getWidth() / 2 - teacherViewWidth)
                .translationY(endLoc[1] / 2 - getHeight() / 2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (targetView instanceof TextView) {
                            TextView textView = (TextView) targetView;
                            String teacherCountStr = textView.getText().toString();
                            textView.setText(String.valueOf(ObjectUtil.getIntValue(teacherCountStr) + 1));
                            if (listener != null)
                                listener.onFinish();
                        }
                        rlFlower.removeView(CustomLottieAnimationView.this);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).setDuration(1000);
    }

    private void moveUb(RelativeLayout rlFlower, View targetView, ConstraintLayout clCoursewareContainer, int count) {
        int[] endLoc = new int[2];
        targetView.getLocationOnScreen(endLoc);
        animate().scaleX(0.1f).scaleY(0.1f)
                .translationX(endLoc[0] / 2 - clCoursewareContainer.getWidth() / 2)
                .translationY(endLoc[1] / 2 - getHeight() / 2)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (targetView instanceof TextView) {
                            ((TextView) targetView).setText(String.valueOf(count));
                            Animation scaleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.room_sdk_diamond_text_scale);
                            targetView.startAnimation(scaleAnim);
                        }
                        rlFlower.removeView(CustomLottieAnimationView.this);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).setDuration(1000);
    }
}
