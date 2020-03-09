package com.uuabc.classroomlib.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
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
import com.uuabc.classroomlib.model.CourseDetailsResult.AnswerRankBean.StudentBean;

import de.hdodenhof.circleimageview.CircleImageView;

public class AnswerUserView extends RelativeLayout {
    private Context context;
    private CircleImageView ivUserHead;
    private ImageView ivUserNum;
    private TextView tvUserNum;
    private TextView tvUserName;
    private TextView tvAnswerNum;
    private TextView tvAnswerTime;

    public AnswerUserView(Context context) {
        this(context, null);
    }

    public AnswerUserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnswerUserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_room_sdk_answer_user, this);
        ivUserHead = rootView.findViewById(R.id.iv_user_head);
        tvUserName = rootView.findViewById(R.id.tv_user_name);
        tvAnswerNum = rootView.findViewById(R.id.tv_answer_num);
        ivUserNum = rootView.findViewById(R.id.iv_user_number);
        tvUserNum = rootView.findViewById(R.id.tv_user_number);
        tvAnswerTime = rootView.findViewById(R.id.tv_answer_time);
    }

    @SuppressLint("SetTextI18n")
    public void setData(StudentBean bean, boolean scaleUp, int position) {
        if (bean == null) return;
        tvUserName.setText(bean.getStudentName());
        tvAnswerNum.setText("答对" + bean.getTrueNum() + "题");
        if (SPUtils.getInstance().getInt(RoomConstant.USER_ID) == Integer.valueOf(bean.getStudentId())) {
            tvUserNum.setText("我");
        } else {
            tvUserNum.setText(bean.getRank());
        }
        if (scaleUp) {
            tvAnswerTime.setText(bean.getTime());
        } else {
            tvAnswerTime.setVisibility(GONE);
        }

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

        Glide.with(context).load(TextUtils.isEmpty(bean.getStudentAvatar()) ? R.drawable.ic_room_sdk_boy_head : bean.getStudentAvatar())
                .apply(new RequestOptions().error(R.drawable.ic_room_sdk_boy_head))
                .into(ivUserHead);

        if (scaleUp) {
            tvAnswerNum.setTextSize(12);
            tvUserName.setTextSize(12);
            tvUserNum.setTextSize(12);
            tvAnswerTime.setTextSize(12);
        }
    }
}
