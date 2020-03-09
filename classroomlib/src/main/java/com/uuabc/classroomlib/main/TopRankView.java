package com.uuabc.classroomlib.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.TopThreeModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopRankView extends RelativeLayout {
    private Context context;
    private CircleImageView ivUserHead;
    private ImageView ivUserNum;
    private TextView tvUserNum;
    private TextView tvUserName;
    private TextView tvAnswerNum;
    private TextView tvAnswerTime;

    public TopRankView(Context context) {
        this(context, null);
    }

    public TopRankView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopRankView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_room_sdk_answer_user_live, this);
        ivUserHead = rootView.findViewById(R.id.iv_user_head);
        tvUserName = rootView.findViewById(R.id.tv_user_name);
        tvAnswerNum = rootView.findViewById(R.id.tv_answer_num);
        ivUserNum = rootView.findViewById(R.id.iv_user_number);
        tvUserNum = rootView.findViewById(R.id.tv_user_number);
        tvAnswerTime = rootView.findViewById(R.id.tv_answer_time);
    }

    @SuppressLint("SetTextI18n")
    public void setData(TopThreeModel model, int position) {
        if (model == null) return;
        tvUserName.setText(model.getRank().get(position).getName());
        tvUserName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tvAnswerNum.setVisibility(GONE);
        if (SPUtils.getInstance().getInt(RoomConstant.USER_ID) == model.getRank().get(position).getId()) {
            tvUserNum.setText("我");
        } else {
            tvUserNum.setText(String.valueOf(position + 1));
        }
        tvAnswerTime.setText(model.getRank().get(position).getTime() + "秒");
        tvAnswerTime.setTextColor(context.getResources().getColor(R.color.green));
        tvAnswerTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        switch (position) {
            case 0:
                Glide.with(context).load(R.drawable.ic_room_sdk_number_one_bg).into(ivUserNum);
                break;
            case 1:
                Glide.with(context).load(R.drawable.ic_room_sdk_number_two_bg).into(ivUserNum);
                break;
            case 2:
                Glide.with(context).load(R.drawable.ic_room_sdk_number_three_bg).into(ivUserNum);
                break;
        }
        Glide.with(context).load(TextUtils.isEmpty(model.getRank().get(position).getPhoto()) ? R.drawable.ic_room_sdk_boy_head : model.getRank().get(position).getPhoto())
                .apply(new RequestOptions().error(R.drawable.ic_room_sdk_boy_head))
                .into(ivUserHead);
    }
}
