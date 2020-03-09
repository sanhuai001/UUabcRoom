package com.uuabc.classroomlib.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.common.BaseDialogFragment;
import com.uuabc.classroomlib.common.CommonAdapter;
import com.uuabc.classroomlib.common.ViewHolder;
import com.uuabc.classroomlib.model.CourseDetailsResult;
import com.uuabc.classroomlib.model.CourseDetailsResult.AnswerRankBean.StudentBean;
import com.uuabc.classroomlib.model.TopModel;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.widget.CustomCircleImageView;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"StaticFieldLeak", "SimpleDateFormat"})
public class AnswerRankDialog extends BaseDialogFragment {
    private static AnswerRankDialog dialog;
    private CourseDetailsResult courseDetails;
    private ListView rvAnswers;
    private List<TopModel.RankBean> topRank;

    public static AnswerRankDialog getInstance() {
        dismissDialog(dialog);
        dialog = null;
        synchronized (AnswerRankDialog.class) {
            if (dialog == null) {
                dialog = new AnswerRankDialog();
            }
        }

        return dialog;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setCompat();

        View rootView = inflater.inflate(R.layout.dialog_fragment_room_sdk_answer_rank, container, false);

        AnswerUsersView answerUsersView = rootView.findViewById(R.id.view_diamonds_rank);
        rvAnswers = rootView.findViewById(R.id.rv_answer_rank);
        TextView tvTips = rootView.findViewById(R.id.tv_tips);
        if (courseDetails != null && courseDetails.getAnswerRank() != null) {
            answerUsersView.setVisibility(View.VISIBLE);
            rvAnswers.setVisibility(View.VISIBLE);
            tvTips.setVisibility(View.GONE);
            answerUsersView.setData(courseDetails.getAnswerRank().getFirst(), true);
            setAdapter();
        } else {
            answerUsersView.setVisibility(View.GONE);
            rvAnswers.setVisibility(View.GONE);
            tvTips.setVisibility(View.VISIBLE);
        }

        rootView.findViewById(R.id.cl_content).setOnClickListener(v -> dismissDialogFrag());

        return rootView;
    }

    private void setAdapter() {
        String userId = String.valueOf(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
        List<StudentBean> students = courseDetails.getAnswerRank().getSecond();
        CommonAdapter<StudentBean> adapter = new CommonAdapter<StudentBean>(R.layout.item_room_sdk_student_answer) {
            @Override
            public void convert(ViewHolder vh, StudentBean student) {
                vh.setText(R.id.tv_rank_num, student.getRank());
                vh.setText(R.id.tv_student_name, student.getStudentName());
                vh.setText(R.id.tv_answer_num, "答对" + student.getTrueNum() + "题");
                vh.getView(R.id.iv_mine).setVisibility(TextUtils.equals(userId, student.getStudentId()) ? View.VISIBLE : View.GONE);

                if (ObjectUtils.isNotEmpty(student.getTime())) {
                    vh.setText(R.id.tv_time, student.getTime());
                }

                CustomCircleImageView userHead = vh.getView(R.id.iv_user_head);
                Glide.with(getContext()).load(TextUtils.isEmpty(student.getStudentAvatar()) ? R.drawable.ic_room_sdk_boy_head : student.getStudentAvatar())
                        .apply(new RequestOptions().error(R.drawable.ic_room_sdk_boy_head))
                        .into(userHead);
            }
        };

        rvAnswers.setAdapter(adapter);
        adapter.setDatas(students);
    }

    public void setCourseDetails(CourseDetailsResult courseDetails) {
        this.courseDetails = courseDetails;
    }

    public void setLiveRank(List<TopModel.RankBean> topRank) {
        if (topRank == null || topRank.size() == 0) {
            return;
        }
        int position = -1;
        //找出自己的position
        for (int i = 0; i < topRank.size(); i++) {
            if (topRank.get(i).getUid() == SPUtils.getInstance().getInt(RoomConstant.USER_ID)) {
                position = i;
                break;
            }
        }
        //集合初始化
        this.topRank = this.topRank == null ? new ArrayList<>() : this.topRank;
        this.topRank.clear();
        if (position > 6) {
            this.topRank.addAll(topRank.subList(0, 6));
            this.topRank.add(topRank.get(position));
        } else {
            this.topRank.addAll(topRank.subList(0, topRank.size() > 6 ? 7 : (topRank.size())));
        }

        List<StudentBean> firstStudents = new ArrayList<>();
        List<StudentBean> secondStudents = new ArrayList<>();

        for (int i = 0; i < this.topRank.size(); i++) {
            this.topRank.size();
            if (i < 3) {
                firstStudents.add(getStudents(this.topRank.get(i)));
            } else if (i < 7) {
                secondStudents.add(getStudents(this.topRank.get(i)));
            }
        }

        CourseDetailsResult.AnswerRankBean answerRank = new CourseDetailsResult.AnswerRankBean();
        answerRank.setFirst(firstStudents);
        answerRank.setSecond(secondStudents);

        courseDetails = new CourseDetailsResult();
        courseDetails.setAnswerRank(answerRank);
    }

    private StudentBean getStudents(TopModel.RankBean rankBean) {
        StudentBean bean = new StudentBean();
        bean.setRank(String.valueOf(rankBean.getRank()));
        bean.setStudentAvatar(rankBean.getPhoto());
        bean.setStudentId(String.valueOf(rankBean.getUid()));
        bean.setStudentName(rankBean.getUname());
        bean.setTime(ObjectUtil.getString(rankBean.getTimer()) + "秒");
        bean.setTrueNum(String.valueOf(rankBean.getCnum()));
        return bean;
    }

    private void dismissDialogFrag() {
        dismissDialogFrag(dialog);
    }
}
