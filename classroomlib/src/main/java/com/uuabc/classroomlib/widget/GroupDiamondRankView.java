package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ObjectUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.main.DiamondCountInsideView;
import com.uuabc.classroomlib.model.OverClassModel;

import static com.blankj.utilcode.util.SizeUtils.dp2px;
import static com.blankj.utilcode.util.SizeUtils.sp2px;

public class GroupDiamondRankView extends ConstraintLayout {
    private CustomCircleImageView ivLeftAvatar1, ivLeftAvatar2, ivRightAvatar1, ivRightAvatar2;//每个人的头像
    private TextView tvLeftGroupName, tvRightGroupName;//分组名
    private TextView tvLeftName1, tvLeftName2, tvRightName1, tvRightName2;//每个人的名字
    private ImageView ivLeftMe1, ivLeftMe2, ivRightMe1, ivRightMe2;//“我”图标
    private DiamondCountInsideView dcLeftCount1, dcLeftCount2, dcRightCount1, dcRightCount2;//钻石条数控件
    private ImageView ivLeftWin, ivRightWin, ivDraw;

    private int diaCount;

    public GroupDiamondRankView(Context context) {
        this(context, null);
    }

    public GroupDiamondRankView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_room_sdk_goup_rank, this);
        ivLeftAvatar1 = rootView.findViewById(R.id.iv_left_avatar1);
        ivLeftAvatar2 = rootView.findViewById(R.id.iv_left_avatar2);
        ivRightAvatar1 = rootView.findViewById(R.id.iv_right_avatar1);
        ivRightAvatar2 = rootView.findViewById(R.id.iv_right_avatar2);

        tvLeftGroupName = rootView.findViewById(R.id.tv_left_group_name);
        tvRightGroupName = rootView.findViewById(R.id.tv_right_group_name);

        tvLeftName1 = rootView.findViewById(R.id.tv_left_name1);
        tvLeftName2 = rootView.findViewById(R.id.tv_left_name2);
        tvRightName1 = rootView.findViewById(R.id.tv_right_name1);
        tvRightName2 = rootView.findViewById(R.id.tv_right_name2);

        ivLeftMe1 = rootView.findViewById(R.id.iv_left_me1);
        ivLeftMe2 = rootView.findViewById(R.id.iv_left_me2);
        ivRightMe1 = rootView.findViewById(R.id.iv_right_me1);
        ivRightMe2 = rootView.findViewById(R.id.iv_right_me2);

        dcLeftCount1 = rootView.findViewById(R.id.dc_left_group_count_1);
        dcLeftCount2 = rootView.findViewById(R.id.dc_left_group_count_2);
        dcRightCount1 = rootView.findViewById(R.id.dc_right_group_count_1);
        dcRightCount2 = rootView.findViewById(R.id.dc_right_group_count_2);

        ivLeftWin = rootView.findViewById(R.id.iv_left_victory);
        ivRightWin = rootView.findViewById(R.id.iv_right_victory);
        ivDraw = rootView.findViewById(R.id.iv_draw);
    }

    public void setData(OverClassModel model) {

        diaCount = model.getValue().getTeamdia1() + model.getValue().getTeamdia2();
        //获取队伍名
        String teamOne = model.getValue().getTeam1();
        String teamTwo = model.getValue().getTeam2();
        //获取钻石数
        int teamdia1 = model.getValue().getTeamdia1();
        int teamdia2 = model.getValue().getTeamdia2();
        //设置胜利结果
        if (teamdia1 > teamdia2) {
            ivLeftWin.setVisibility(View.VISIBLE);
        } else if (teamdia1 < teamdia2) {
            ivRightWin.setVisibility(View.VISIBLE);
        } else {
            ivDraw.setVisibility(View.VISIBLE);
        }
        //设置队伍名和钻石数
        tvLeftGroupName.setText(getContext().getResources().getString(R.string.view_group_name_str, teamOne, teamdia1));
        tvRightGroupName.setText(getContext().getResources().getString(R.string.view_group_name_str, teamTwo, teamdia2));
        //设置头像和名字和钻石
        for (OverClassModel.ValueModel.User user : model.getValue().getUser()) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.ic_room_sdk_boy_head)
                    .error(R.drawable.ic_room_sdk_boy_head);
            if (ObjectUtils.equals(user.getTeamname(), teamOne)) {
                if (ObjectUtils.isEmpty(tvLeftName1.getText().toString().trim())) {
                    Glide.with(getContext()).load(user.getPhoto())
                            .apply(options)
                            .into(ivLeftAvatar1);
                    setView(tvLeftName1, dcLeftCount1, ivLeftMe1, user, R.color.color_group_green);
                } else {
                    Glide.with(getContext()).load(user.getPhoto())
                            .apply(options)
                            .into(ivLeftAvatar2);
                    setView(tvLeftName2, dcLeftCount2, ivLeftMe2, user, R.color.color_group_green);
                }
            } else if (ObjectUtils.equals(user.getTeamname(), teamTwo)) {
                if (ObjectUtils.isEmpty(tvRightName1.getText().toString().trim())) {
                    Glide.with(getContext()).load(user.getPhoto())
                            .apply(options)
                            .into(ivRightAvatar1);
                    setView(tvRightName1, dcRightCount1, ivRightMe1, user, R.color.color_group_orange);
                } else {
                    Glide.with(getContext()).load(user.getPhoto())
                            .apply(options)
                            .into(ivRightAvatar2);
                    setView(tvRightName2, dcRightCount2, ivRightMe2, user, R.color.color_group_orange);
                }
            }
        }
    }

    /**
     * 设置界面的方法
     */
    private void setView(TextView nameView, DiamondCountInsideView dcView, ImageView meView, OverClassModel.ValueModel.User user, int color) {

        nameView.setText(user.getName());
        dcView.setDiamondCount(user.getDia());
        ViewTreeObserver vto = dcView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> dcView.setViewHight((int) ((float) user.getDia() / diaCount * (dcView.getHeight() - dp2px(30) - sp2px(14)))));
        dcView.setViewBg(color);
        if (user.isMe()) {
            meView.setVisibility(View.VISIBLE);
        }
    }

}
