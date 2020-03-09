package com.uuabc.classroomlib.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.model.CourseDetailsResult.AnswerRankBean.StudentBean;

import java.util.ArrayList;
import java.util.List;

public class AnswerUsersView extends RelativeLayout {
    private List<AnswerUserView> viewUsers;

    public AnswerUsersView(Context context) {
        this(context, null);
    }

    public AnswerUsersView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnswerUsersView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewUsers = new ArrayList<>();
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return;
        View rootView = inflater.inflate(R.layout.view_room_sdk_answer_users, this);

        AnswerUserView viewUserOne = rootView.findViewById(R.id.view_user_one);
        AnswerUserView viewUserTwo = rootView.findViewById(R.id.view_user_two);
        AnswerUserView viewUserThree = rootView.findViewById(R.id.view_user_three);

        viewUsers.add(viewUserOne);
        viewUsers.add(viewUserTwo);
        viewUsers.add(viewUserThree);
    }

    public void setData(List<StudentBean> users, boolean scaleUp) {
        if (users == null) return;
        for (int i = 0; i < users.size(); i++) {
            if (i > 2) return;
            StudentBean student = users.get(i);
            if (student != null) {
                viewUsers.get(i).setData(student, scaleUp, i);
            }
        }
    }
}
