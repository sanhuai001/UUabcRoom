package com.uuabc.classroomlib.widget.dialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.TopModel;
import com.uuabc.classroomlib.widget.AnswerRankItemView;
import com.uuabc.classroomlib.widget.CaptainDialog;
import com.uuabc.classroomlib.widget.FilterImageView;

import java.util.ArrayList;
import java.util.List;

public class LiveAnswerRankDialog extends BaseDialog {
    private List<TopModel.RankBean> rankListModels;
    private AnswerRankItemView[] itemViews = new AnswerRankItemView[5];
    private ImageView ivNoRank;
    private TextView tvNoRankTips;

    public LiveAnswerRankDialog(Context context) {
        super(context);
        this.context = context;
        builder = new CaptainDialog.Builder(context);
        dialog = builder.cancelTouchout(true)
                .view(R.layout.dialog_room_sdk_answer_rank)
                .widthpx((int) (RoomApplication.getInstance().getScreenWidth() * 0.3))
                .heightpx((int) (RoomApplication.getInstance().getScreenHeight() * 0.75))
                .style(R.style.Dialog_No_Title)
                .build();
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent);
        FilterImageView btnExit = (FilterImageView) builder.getView(R.id.btn_exit);
        btnExit.setOnClickListener(v -> dismiss());

        itemViews[0] = (AnswerRankItemView) builder.getView(R.id.item1);
        itemViews[1] = (AnswerRankItemView) builder.getView(R.id.item2);
        itemViews[2] = (AnswerRankItemView) builder.getView(R.id.item3);
        itemViews[3] = (AnswerRankItemView) builder.getView(R.id.item4);
        itemViews[4] = (AnswerRankItemView) builder.getView(R.id.item5);
        dialog.setOnDismissListener(dialog -> {
            for (AnswerRankItemView itemView : itemViews) {
                itemView.setVisibility(View.INVISIBLE);
            }
        });
        ivNoRank = (ImageView) builder.getView(R.id.iv_no_rank);
        tvNoRankTips = (TextView) builder.getView(R.id.tv_no_rank_tips);
    }

    public void setData(List<TopModel.RankBean> rankListModels) {
        if (rankListModels == null || rankListModels.size() == 0) {
            return;
        }
        ivNoRank.setVisibility(View.GONE);
        tvNoRankTips.setVisibility(View.GONE);
        int position = -1;
        //找出自己的position
        for (int i = 0; i < rankListModels.size(); i++) {
            if (rankListModels.get(i).getUid() == SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                position = i;
                break;
            }
        }
        //集合初始化
        this.rankListModels = this.rankListModels == null ? new ArrayList<>() : this.rankListModels;
        this.rankListModels.clear();
        //如果自己的position大于4，则截取前4个数据，把自己添加进末尾凑成5个
        if (position > 4) {
            this.rankListModels.addAll(rankListModels.subList(0, 4));
            this.rankListModels.add(rankListModels.get(position));
        } else {//否则就直接截取前5个（视数据的长度而定）
            this.rankListModels.addAll(rankListModels.subList(0, rankListModels.size() > 4 ? 5 : (rankListModels.size())));
        }

        for (int i = 0; i < this.rankListModels.size(); i++) {
            itemViews[i].setVisibility(View.VISIBLE);
            itemViews[i].setData(this.rankListModels.get(i));
        }
    }
}
