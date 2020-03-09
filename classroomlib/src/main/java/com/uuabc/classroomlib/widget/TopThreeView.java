package com.uuabc.classroomlib.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.main.TopRankView;
import com.uuabc.classroomlib.model.TopThreeModel;

public class TopThreeView extends LinearLayout {
    private CustomCircleImageView ivAvatar, ivAvatar2, ivAvatar3;
    private ImageView ivMe, ivMe2, ivMe3;
    private TextView tvNameLeft, tvNameMid, tvNameRight;
    private ImageView ivDiamondLeftOne, ivDiamondLeftTwo, ivDiamondLeftThree;
    private ImageView ivDiamondMidOne, ivDiamondMidTwo;
    private ImageView ivDiamondRightOne;
    private TopRankView topRankLeft;
    private TopRankView topRankMid;
    private TopRankView topRankRight;
    private TopThreeModel model;
    private RelativeLayout rlLeft, rlMid, rlRight, rlMidNull, rlRightNull;
    private ObjectAnimator leftOneAnimator;
    private ObjectAnimator leftTwoAnimator;
    private ObjectAnimator leftThreeAnimator;
    private ObjectAnimator midOneAnimator;
    private ObjectAnimator midTwoAnimator;
    private ObjectAnimator rightOneAnimator;

    private AnimatorSet leftSet;
    private AnimatorSet midSet;

    public TopThreeView(Context context) {
        super(context);
    }

    public TopThreeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_room_sdk_top_three_live, this);
        ivDiamondLeftOne = rootView.findViewById(R.id.iv_diamond_left_one);
        ivDiamondLeftTwo = rootView.findViewById(R.id.iv_diamond_left_two);
        ivDiamondLeftThree = rootView.findViewById(R.id.iv_diamond_left_three);
        ivDiamondMidOne = rootView.findViewById(R.id.iv_diamond_mid_one);
        ivDiamondMidTwo = rootView.findViewById(R.id.iv_diamond_mid_two);
        ivDiamondRightOne = rootView.findViewById(R.id.iv_diamond_right_one);
        topRankLeft = rootView.findViewById(R.id.rl_avatar_bg);
        topRankMid = rootView.findViewById(R.id.rl_avatar_bg2);
        topRankRight = rootView.findViewById(R.id.rl_avatar_bg3);
        rlLeft = rootView.findViewById(R.id.rl_left);
        rlMid = rootView.findViewById(R.id.rl_mid);
        rlRight = rootView.findViewById(R.id.rl_right);
        rlMidNull = rootView.findViewById(R.id.rl_mid_null);
        rlRightNull = rootView.findViewById(R.id.rl_right_null);

        initAnimation();
        initListener();
    }

    private void initAnimation() {

        leftOneAnimator = ObjectAnimator.ofFloat(ivDiamondLeftOne, "translationX", 500, 0f).setDuration(500);
        leftTwoAnimator = ObjectAnimator.ofFloat(ivDiamondLeftTwo, "translationX", 500, 0f).setDuration(500);
        leftThreeAnimator = ObjectAnimator.ofFloat(ivDiamondLeftThree, "translationX", 500, 0f).setDuration(500);

        leftSet = new AnimatorSet();
        leftSet.play(leftOneAnimator).before(leftTwoAnimator);
        leftSet.play(leftTwoAnimator).before(leftThreeAnimator);

        midOneAnimator = ObjectAnimator.ofFloat(ivDiamondMidOne, "translationX", 500, 0f).setDuration(500);
        midTwoAnimator = ObjectAnimator.ofFloat(ivDiamondMidTwo, "translationX", 500, 0f).setDuration(500);

        midSet = new AnimatorSet();
        midSet.play(midOneAnimator).before(midTwoAnimator);

        rightOneAnimator = ObjectAnimator.ofFloat(ivDiamondRightOne, "translationX", 500, 0f).setDuration(500);

    }

    private void initListener() {
        leftOneAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivDiamondLeftOne.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        leftTwoAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivDiamondLeftTwo.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        leftThreeAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivDiamondLeftThree.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        midOneAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivDiamondMidOne.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        midTwoAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivDiamondMidTwo.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        rightOneAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                ivDiamondRightOne.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setData(TopThreeModel model) {
        this.model = model;
        if (model != null && model.getRank() != null) {
            switch (model.getRank().size()) {
                case 3:
                    rlRight.setVisibility(View.VISIBLE);
                    rlRightNull.setVisibility(GONE);
                    topRankRight.setData(model, 2);
                case 2:
                    rlMid.setVisibility(View.VISIBLE);
                    rlMidNull.setVisibility(GONE);
                    topRankMid.setData(model, 1);
                case 1:
                    rlLeft.setVisibility(View.VISIBLE);
                    topRankLeft.setData(model, 0);
                    break;
            }
        }
    }

    /**
     * 执行动画
     */
    public void startDiaAnimation() {
        switch (model.getRank().size()) {
            case 3:
                rightOneAnimator.start();
            case 2:
                midSet.start();
            case 1:
                leftSet.start();
                break;
        }
    }

    /**
     * 重置状态
     */
    public void setDefault() {
        rlRightNull.setVisibility(VISIBLE);
        rlMidNull.setVisibility(VISIBLE);
        ivDiamondLeftOne.setVisibility(INVISIBLE);
        ivDiamondLeftTwo.setVisibility(INVISIBLE);
        ivDiamondLeftThree.setVisibility(INVISIBLE);
        ivDiamondMidOne.setVisibility(INVISIBLE);
        ivDiamondMidTwo.setVisibility(INVISIBLE);
        ivDiamondRightOne.setVisibility(INVISIBLE);
    }
}
