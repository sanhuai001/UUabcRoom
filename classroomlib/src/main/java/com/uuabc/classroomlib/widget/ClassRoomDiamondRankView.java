package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.main.DiamondCountInsideView;
import com.uuabc.classroomlib.model.OverClassModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;

import java.util.List;

import static com.blankj.utilcode.util.SizeUtils.dp2px;
import static com.blankj.utilcode.util.SizeUtils.sp2px;

public class ClassRoomDiamondRankView extends ConstraintLayout {
    private CustomCircleImageView ivLeftAvatar, ivMiddelAvatar, ivRightAvatar, ivRightAvatar2;//每个人的头像
    private TextView tvLeftName, tvMiddleName, tvRightName, tvRightName2;//每个人的名字
    private ImageView ivLeftMe, ivMiddleMe, ivRightMe, ivRightMe2;//“我”图标
    private DiamondCountInsideView dcLeftCount, dcMiddleCount, dcRightCount, dcRightCount2;//钻石条数控件
    private ConstraintLayout clCountMiddle, clCountRight, clCountRight2;
    private ConstraintLayout clMiddle, clRight, clRight2;

    private int maxDiaCount;

    public ClassRoomDiamondRankView(Context context) {
        this(context, null);
    }

    public ClassRoomDiamondRankView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_room_sdk_rank, this);
        ivLeftAvatar = rootView.findViewById(R.id.iv_left_avatar);
        ivMiddelAvatar = rootView.findViewById(R.id.iv_middle_avatar);
        ivRightAvatar = rootView.findViewById(R.id.iv_right_avatar);
        ivRightAvatar2 = rootView.findViewById(R.id.iv_right2_avatar);

        tvLeftName = rootView.findViewById(R.id.tv_left_name);
        tvMiddleName = rootView.findViewById(R.id.tv_middle_name);
        tvRightName = rootView.findViewById(R.id.tv_right_name);
        tvRightName2 = rootView.findViewById(R.id.tv_right2_name);

        ivLeftMe = rootView.findViewById(R.id.iv_left_me);
        ivMiddleMe = rootView.findViewById(R.id.iv_middle_me);
        ivRightMe = rootView.findViewById(R.id.iv_right_me);
        ivRightMe2 = rootView.findViewById(R.id.iv_right_me2);

        dcLeftCount = rootView.findViewById(R.id.dc_left_count);
        dcMiddleCount = rootView.findViewById(R.id.dc_middle_count);
        dcRightCount = rootView.findViewById(R.id.dc_right_count);
        dcRightCount2 = rootView.findViewById(R.id.dc_right2_count);

        clCountMiddle = rootView.findViewById(R.id.cl_count_middle);
        clCountRight = rootView.findViewById(R.id.cl_count_right);
        clCountRight2 = rootView.findViewById(R.id.cl_count_right2);

        clMiddle = rootView.findViewById(R.id.cl_middle);
        clRight = rootView.findViewById(R.id.cl_right);
        clRight2 = rootView.findViewById(R.id.cl_right2);
    }

    public void setData(OverClassModel model) {
        getMaxDiaCount(model.getValue().getUser());
        //设置头像和名字和钻石
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_room_sdk_boy_head)
                .error(R.drawable.ic_room_sdk_boy_head);
        switch (model.getValue().getUser().size()) {
            case 4:
                clCountRight2.setVisibility(VISIBLE);
                clRight2.setVisibility(VISIBLE);
                setView(tvRightName2, dcRightCount2, ivRightMe2, model.getValue().getUser().get(3));
                Glide.with(getContext()).load(model.getValue().getUser().get(3).getPhoto())
                        .apply(options)
                        .into(ivRightAvatar2);
            case 3:
                clCountRight.setVisibility(VISIBLE);
                clRight.setVisibility(VISIBLE);
                setView(tvRightName, dcRightCount, ivRightMe, model.getValue().getUser().get(2));
                Glide.with(getContext()).load(model.getValue().getUser().get(2).getPhoto())
                        .apply(options)
                        .into(ivRightAvatar);
            case 2:
                clCountMiddle.setVisibility(VISIBLE);
                clMiddle.setVisibility(VISIBLE);
                setView(tvMiddleName, dcMiddleCount, ivMiddleMe, model.getValue().getUser().get(1));
                Glide.with(getContext()).load(model.getValue().getUser().get(1).getPhoto())
                        .apply(options)
                        .into(ivMiddelAvatar);
            case 1:
                setView(tvLeftName, dcLeftCount, ivLeftMe, model.getValue().getUser().get(0));
                Glide.with(getContext()).load(model.getValue().getUser().get(0).getPhoto())
                        .apply(options)
                        .into(ivLeftAvatar);
                break;
        }
    }

    public void setNewData(List<UserModel> userList) {
        if (userList == null) return;
        getNewMaxDiaCount(userList);
        //设置头像和名字和钻石
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_room_sdk_boy_head)
                .error(R.drawable.ic_room_sdk_boy_head);

        switch (userList.size()) {
            case 4:
                clCountRight2.setVisibility(VISIBLE);
                clRight2.setVisibility(VISIBLE);
                setNewView(tvRightName2, dcRightCount2, ivRightMe2, userList.get(3));
                Glide.with(getContext()).load(userList.get(3).getPhoto())
                        .apply(options)
                        .into(ivRightAvatar2);
            case 3:
                clCountRight.setVisibility(VISIBLE);
                clRight.setVisibility(VISIBLE);
                setNewView(tvRightName, dcRightCount, ivRightMe, userList.get(2));
                Glide.with(getContext()).load(userList.get(2).getPhoto())
                        .apply(options)
                        .into(ivRightAvatar);
            case 2:
                clCountMiddle.setVisibility(VISIBLE);
                clMiddle.setVisibility(VISIBLE);
                setNewView(tvMiddleName, dcMiddleCount, ivMiddleMe, userList.get(1));
                Glide.with(getContext()).load(userList.get(1).getPhoto())
                        .apply(options)
                        .into(ivMiddelAvatar);
            case 1:
                setNewView(tvLeftName, dcLeftCount, ivLeftMe, userList.get(0));
                Glide.with(getContext()).load(userList.get(0).getPhoto())
                        .apply(options)
                        .into(ivLeftAvatar);
                break;
        }
    }

    private void getMaxDiaCount(List<OverClassModel.ValueModel.User> userList) {
        for (OverClassModel.ValueModel.User user : userList) {
            if (user == null) continue;
            if (maxDiaCount == 0) {
                maxDiaCount = user.getDia();
            } else if (user.getDia() > maxDiaCount) {
                maxDiaCount = user.getDia();
            }
        }
    }

    private void getNewMaxDiaCount(List<UserModel> userList) {
        for (UserModel user : userList) {
            if (user == null) continue;
            if (maxDiaCount == 0) {
                maxDiaCount = user.getDia();
            } else if (user.getDia() > maxDiaCount) {
                maxDiaCount = user.getDia();
            }
        }
    }

    /**
     * 设置界面的方法
     */
    private void setView(TextView nameView, DiamondCountInsideView dcView, ImageView meView, OverClassModel.ValueModel.User user) {
        nameView.setText(user.getName());
        dcView.setDiamondCount(user.getDia());
        ViewTreeObserver vto = dcView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dcView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dcView.setViewHight((int) ((float) user.getDia() / maxDiaCount * (dcView.getHeight() - dp2px(30) - sp2px(14))));
            }
        });
        if (user.isMe()) {
            meView.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 设置界面的方法
     */
    private void setNewView(TextView nameView, DiamondCountInsideView dcView, ImageView meView, UserModel user) {
        nameView.setText(user.getName());
        dcView.setDiamondCount(user.getDia());
        ViewTreeObserver vto = dcView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                dcView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                dcView.setViewHight((int) ((float) user.getDia() / maxDiaCount * (dcView.getHeight() - dp2px(30) - sp2px(14))));
            }
        });

        if (SPUtils.getInstance().getInt(RoomConstant.USER_ID) == user.getId()) {
            meView.setVisibility(VISIBLE);
        }
    }
}
