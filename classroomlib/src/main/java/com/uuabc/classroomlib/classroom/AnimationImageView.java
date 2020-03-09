package com.uuabc.classroomlib.classroom;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.SocketModel.FlowerModel;
import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetTextI18n")
public class AnimationImageView extends pl.droidsonroids.gif.GifImageView {
    private Context mContext;

    public AnimationImageView(Context context) {
        this(context, null);
    }

    public AnimationImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void showDiamonds(RelativeLayout flowerLayout, RecyclerView recyclerView, TextView topDiamondView, List<FlowerModel.UsersBean> users) {
        List<ImageView> imageViews = new ArrayList<>();
        int rootWidth = RoomApplication.getInstance().getScreenWidth();
        int rootHeight = RoomApplication.getInstance().getScreenHeight();
        int imageWidth = rootWidth / 12;
        int imageHeight = rootHeight / 12;
        int centerX = (int) (rootWidth * 0.55);
        int centerY = rootHeight / 2;

        for (int i = 0; i < users.size(); i++) {
            int simpleUserCount = ObjectUtil.getIntValue(users.get(i).getValue());
            if (simpleUserCount == 0) {
                continue;
            }

            int randomMultiple = i < 2 ? 1 : 2;
            List<Integer> imgTags = new ArrayList<>();
            imgTags.add(ObjectUtil.getIntValue(users.get(i).getId()));
            imgTags.add(users.get(i).getTotal());
            for (int j = 0; j < simpleUserCount; j++) {
                ImageView view = new ImageView(mContext);
                view.setTag(imgTags);
                view.setImageResource(R.drawable.ic_room_sdk_diamond);
                flowerLayout.addView(view);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                layoutParams.width = imageWidth;
                layoutParams.height = imageHeight;

                layoutParams.leftMargin = (int) (centerX - (imageWidth / 2) + randomMultiple * imageWidth * Math.random());
                layoutParams.topMargin = (int) (centerY - (imageHeight / 2) - randomMultiple * imageHeight * Math.random());

                view.animate().scaleX(2f).scaleY(2f).setDuration(100);
                imageViews.add(view);
            }
        }

        flower(recyclerView, imageViews, topDiamondView);
    }

    /**
     * 1v4 教室钻石动画
     */
    public void showDiamonds(RelativeLayout flowerLayout, RecyclerView recyclerView, TextView topDiamondView, FlowerModel flowerModel) {
        if (flowerModel == null || flowerModel.getUsers() == null) return;
        showDiamonds(flowerLayout, recyclerView, topDiamondView, flowerModel.getUsers());
    }

    private void flower(RecyclerView recyclerView, List<ImageView> imageViews, TextView topDiamondView) {
        for (int i = 0; i < imageViews.size(); i++) {
            ImageView view = imageViews.get(i);
            view.clearAnimation();
            for (int j = 0; j < recyclerView.getChildCount(); j++) {
                TextView tvDiamondCount = recyclerView.getChildAt(j).findViewById(R.id.tv_diamond_count);
                List<Integer> viewTag = (List<Integer>) view.getTag();
                if (ObjectUtil.getIntValue(tvDiamondCount.getTag()) == viewTag.get(0)) {
                    RelativeLayout.LayoutParams startParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    int[] endLoc = new int[2];
                    tvDiamondCount.getLocationInWindow(endLoc);

                    view.animate().scaleX(0.5f).scaleY(0.5f)
                            .translationX(endLoc[0] - startParams.leftMargin)
                            .translationY(endLoc[1] - startParams.topMargin - startParams.height / 2)
                            .setDuration(1000);
                    view.animate().setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            tvDiamondCount.setText(String.valueOf(viewTag.get(1)));
                            Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.room_sdk_diamond_text_scale);
                            tvDiamondCount.startAnimation(scaleAnim);
                            if (SPUtils.getInstance().getInt(RoomConstant.USER_ID) != viewTag.get(0)
                                    && SPUtils.getInstance().getInt(RoomConstant.USER_CHILD_ID, -8) != viewTag.get(0)) {
                                view.clearAnimation();
                                ((RelativeLayout) view.getParent()).removeView(view);
                                return;
                            }

                            int[] startLoc = new int[2];
                            view.getLocationOnScreen(startLoc);
                            int[] endLoc = new int[2];
                            topDiamondView.getLocationOnScreen(endLoc);
                            view.animate().x(endLoc[0]).y(endLoc[1]).setDuration(1000).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    topDiamondView.setText("x" + viewTag.get(1));
                                    view.clearAnimation();
                                    ((RelativeLayout) view.getParent()).removeView(view);
                                    Animation scaleAnim = AnimationUtils.loadAnimation(mContext, R.anim.room_sdk_diamond_text_scale);
                                    topDiamondView.startAnimation(scaleAnim);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                }
                            });
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                }
            }
        }
    }
}
