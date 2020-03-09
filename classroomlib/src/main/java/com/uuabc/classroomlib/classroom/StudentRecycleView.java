package com.uuabc.classroomlib.classroom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.othershe.baseadapter.base.CommonBaseAdapter;
import com.uuabc.classroomlib.model.SocketModel.UserModel;

import java.util.ArrayList;
import java.util.List;

public class StudentRecycleView extends RecyclerView {
    private Context mContext;
    private int mItemWidth;

    public StudentRecycleView(Context context) {
        this(context, null);
    }

    public StudentRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StudentRecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setItemWidth(int mItemWidth) {
        this.mItemWidth = mItemWidth;
    }

    public int getItemWidth() {
        return mItemWidth;
    }

    public void updateStudent(CommonBaseAdapter<UserModel> studentAdapter, int studentId, boolean onLine) {
        if (studentAdapter == null || studentId == 0) return;

        int position = getStudentPosition(studentAdapter, studentId);
        if (position == -1) return;
        try {
            List<UserModel> studentModels = studentAdapter.getAllData();
            studentModels.get(position).setOnline(onLine);
            setStudentRecycleViewParams(studentModels.size());
            studentAdapter.notifyItemChanged(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudent(CommonBaseAdapter<UserModel> studentAdapter, UserModel student) {
        if (studentAdapter == null || student == null) return;

        int position = getStudentPosition(studentAdapter, student.getId());
        if (position == -1) {
            List<UserModel> addStudentList = new ArrayList<>();
            addStudentList.add(student);

            setStudentRecycleViewParams(studentAdapter.getDataCount() + addStudentList.size());
            studentAdapter.setLoadMoreData(addStudentList);
        } else {
            updateStudent(studentAdapter, student.getId(), true);
        }
    }

    public int getStudentPosition(CommonBaseAdapter<UserModel> studentAdapter, int studentId) {
        if (studentAdapter == null || studentId == 0) return -1;

        for (int i = 0; i < studentAdapter.getDataCount(); i++) {
            if (studentAdapter.getData(i).getId() == studentId) {
                return i;
            }
        }
        return -1;
    }

    public void setStudentRecycleViewParams(int studentNum) {
        if (studentNum == 0) return;
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, studentNum);
        setLayoutManager(layoutManager);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) getLayoutParams();
        params.width = mItemWidth * studentNum;
        requestLayout();
    }
}
