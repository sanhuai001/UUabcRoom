package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.TopModel;

public class AnswerRankItemView extends ConstraintLayout {
    private TextView tvIndex;
    private ConstraintLayout rlAvatarBg;
    private CustomCircleImageView ivAvatar;
    private ImageView ivMe;
    private TextView tvName;
    private TextView tvNum;
    private ImageView ivCrown;
    private TopModel.RankBean rankListModel;

    public AnswerRankItemView(Context context) {
        this(context, null);
    }

    public AnswerRankItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.item_room_sdk_answer_rank, this);
        tvIndex = rootView.findViewById(R.id.tv_index);
        rlAvatarBg = rootView.findViewById(R.id.rl_avatar_bg);
        ivAvatar = rootView.findViewById(R.id.iv_avatar);
        ivCrown = rootView.findViewById(R.id.iv_crown);
        tvName = rootView.findViewById(R.id.tv_name);
        tvNum = rootView.findViewById(R.id.tv_num);
        ivMe = rootView.findViewById(R.id.iv_me);
    }

    public void setData(TopModel.RankBean rankListModel) {
        this.rankListModel = rankListModel;
        switch (rankListModel.getRank()) {
            case 1:
                setItemView(rankListModel.getRank());
                break;
            case 2:
                setItemView(rankListModel.getRank());
                break;
            case 3:
                setItemView(rankListModel.getRank());
                break;
            default:
                ivCrown.setVisibility(View.GONE);
                tvIndex.setBackgroundResource(0);
                tvIndex.setText(String.valueOf(rankListModel.getRank()));
                rlAvatarBg.setBackgroundResource(rankListModel.getUid() == SPUtils.getInstance().getInt(RoomConstant.USER_ID) ? R.drawable.room_sdk_shape_circle_pink : R.drawable.room_sdk_shape_circle_blue);
                break;
        }
        ivMe.setVisibility(rankListModel.getUid() == SPUtils.getInstance().getInt(RoomConstant.USER_ID) ? View.VISIBLE : View.GONE);
        tvName.setText(rankListModel.getUname());
        tvNum.setText(getContext().getResources().getString(R.string.dialog_answer_num_str, String.valueOf(rankListModel.getCnum())));

        Glide.with(getContext()).load(rankListModel.getPhoto())
                .apply(new RequestOptions().error(R.drawable.ic_room_sdk_boy_head))
                .into(ivAvatar);
    }

    private void setItemView(int rank) {
        switch (rank) {
            case 1:
                tvIndex.setBackgroundResource(R.drawable.ic_room_sdk_medal_gold);
                break;
            case 2:
                tvIndex.setBackgroundResource(R.drawable.ic_room_sdk_medal_silver);
                break;
            case 3:
                tvIndex.setBackgroundResource(R.drawable.ic_room_sdk_medal_copper);
                break;

        }
        tvIndex.setText("");
        ivCrown.setVisibility(View.VISIBLE);
        rlAvatarBg.setBackgroundResource(rankListModel.getUid() == SPUtils.getInstance().getInt(RoomConstant.USER_ID) ? R.drawable.room_sdk_shape_circle_pink : R.drawable.room_sdk_shape_circle_orange);
    }
}
