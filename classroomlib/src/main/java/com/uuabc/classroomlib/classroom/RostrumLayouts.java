package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.SocketModel.RostrumModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.utils.ObjectUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class RostrumLayouts extends FrameLayout {
    private int width;
    private int height;
    private Map<Integer, RostrumLayout> rostrumLayoutMap = new HashMap<>();
    private List<UserModel> students;
    private boolean isNewRoom;

    public RostrumLayouts(Context context) {
        this(context, null);
    }

    public RostrumLayouts(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RostrumLayouts(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNewRoom() {
        isNewRoom = true;
    }

    /**
     * 设置宽高
     */
    public void setSize(int size) {
        int rosturmSize = (int) (size / 5.3);
        this.width = rosturmSize;
        this.height = rosturmSize;
    }

    public void setStudents(List<UserModel> students, boolean updateView) {
        if (students == null) return;
        this.students = students;
        if (updateView) {
            UserModel userModel;
            for (int i = 0; i < students.size(); i++) {
                userModel = students.get(i);
                if (rostrumLayoutMap.containsKey(userModel.getId())) {
                    RostrumLayout rostrumLayout = rostrumLayoutMap.get(userModel.getId());
                    if (rostrumLayout == null) return;
                    rostrumLayout.setTvStuName(userModel.getName());
                    rostrumLayout.isTv(userModel.getChannel() == 1);
                    rostrumLayout.setTvDiamondCount(userModel.getDia());
                }
            }
        }
    }

    public boolean isUpStateById(int userId) {
        return rostrumLayoutMap.containsKey(userId);
    }

    public void removeAllView() {
        rostrumLayoutMap.clear();
        removeAllViews();
    }

    public void setVolumeVisible(int userId, boolean isMuted) {
        if (rostrumLayoutMap.containsKey(userId)) {
            RostrumLayout rostrumLayout = rostrumLayoutMap.get(userId);
            if (rostrumLayout != null) {
                rostrumLayout.setVolumeVisible(isMuted);
            }
        }
    }

    public void setRostrumText(UserModel student) {
        if (student == null) return;
        int userId = ObjectUtil.getIntValue(student.getId());
        if (rostrumLayoutMap.containsKey(userId)) {
            RostrumLayout rostrumLayout = rostrumLayoutMap.get(userId);
            if (rostrumLayout != null) {
                rostrumLayout.setTvDiamondCount(student.getDia());
                rostrumLayout.setTvStuName(student.getName());
                rostrumLayout.isTv(student.getChannel() == 1);
            }
        }
    }

    /**
     * 上台 老教室
     */
    public void doUpState(int userId, String value, float mScale, UpStageCallBack callBack) {
        if (TextUtils.isEmpty(value)) return;
        String[] points = value.split(",");

        RostrumLayout rostrumLayout;
        if (rostrumLayoutMap.containsKey(userId)) {
            rostrumLayout = rostrumLayoutMap.get(userId);
        } else {
            rostrumLayout = new RostrumLayout(getContext(), isNewRoom);
            UserModel student = getStudentById(userId);
            if (student != null) {
                rostrumLayout.setTvStuName(student.getName());
                rostrumLayout.setTvDiamondCount(student.getDia());
                rostrumLayout.isTv(student.getChannel() == 1);
            }
            addView(rostrumLayout);
            rostrumLayoutMap.put(userId, rostrumLayout);
        }

        if (rostrumLayout == null) return;
        FrameLayout flStuRostrum = rostrumLayout.getFlStuRostrum();
        ConstraintLayout clStuRostrum = rostrumLayout.getClStuRostrum();
        float x = Float.valueOf(points[0]) / mScale;
        float y = Float.valueOf(points[1]) / mScale;

        callBack.goUpStageCallBack(flStuRostrum, userId);

        LayoutParams layoutParams = (LayoutParams) clStuRostrum.getLayoutParams();
        layoutParams.leftMargin = (int) x;
        layoutParams.topMargin = (int) y;
        layoutParams.width = width;
        layoutParams.height = height;
        clStuRostrum.requestLayout();
    }

    /**
     * 上台
     */
    public void doNewUpState(int userId, RostrumModel value, float mScale, UpStageCallBack callBack) {
        float x = Float.valueOf(value.getX()) / mScale;
        float y = Float.valueOf(value.getY()) / mScale;
        doNewUpState(userId, x, y, TextUtils.equals(value.getType(), RoomConstant.START), callBack);
    }

    public void doNewUpState(int userId, float x, float y, boolean needCallBack, UpStageCallBack callBack) {
        RostrumLayout rostrumLayout;
        if (rostrumLayoutMap.containsKey(userId)) {
            rostrumLayout = rostrumLayoutMap.get(userId);
        } else {
            rostrumLayout = new RostrumLayout(getContext(), isNewRoom);
            UserModel student = getStudentById(userId);
            if (student != null) {
                rostrumLayout.setTvStuName(student.getName());
                rostrumLayout.setTvDiamondCount(student.getDia());
                rostrumLayout.isTv(student.getChannel() == 1);
                if (isNewRoom)
                    rostrumLayout.setCameraVisible(student.isCamera());
            }
            addView(rostrumLayout);
            rostrumLayoutMap.put(userId, rostrumLayout);
        }

        if (rostrumLayout == null) return;
        FrameLayout flStuRostrum = rostrumLayout.getFlStuRostrum();
        ConstraintLayout clStuRostrum = rostrumLayout.getClStuRostrum();

        if (needCallBack) {
            callBack.goUpStageCallBack(flStuRostrum, userId);
        }

        LayoutParams layoutParams = (LayoutParams) clStuRostrum.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.leftMargin = (int) x;
        layoutParams.topMargin = (int) y;
        clStuRostrum.requestLayout();
    }

    /**
     * 下台
     */
    public void doDownState(int userId, DownStageCallBack callBack) {
        if (rostrumLayoutMap.containsKey(userId)) {
            RostrumLayout rostrumLayout = rostrumLayoutMap.get(userId);
            if (rostrumLayout == null) return;
            rostrumLayout.removeAllViews();
            removeView(rostrumLayout);
            rostrumLayoutMap.remove(userId);
            callBack.goDownStageCallBack();
        }
    }

    /**
     * 用户退出
     */
    public void doQuitRostrum(int userId) {
        if (rostrumLayoutMap.containsKey(userId)) {
            RostrumLayout rostrumLayout = rostrumLayoutMap.get(userId);
            if (rostrumLayout == null) return;
            rostrumLayout.removeAllViews();
            removeView(rostrumLayout);
            rostrumLayoutMap.remove(userId);
        }
    }

    private UserModel getStudentById(int userId) {
        if (students == null) return null;
        for (UserModel student : students) {
            if (userId == student.getId()) {
                return student;
            }
        }
        return null;
    }

    public void setSignal(int userId, int signal) {
        if (rostrumLayoutMap.containsKey(userId)) {
            RostrumLayout rostrumLayout = rostrumLayoutMap.get(userId);
            if (rostrumLayout == null) return;
            rostrumLayout.setSignal(signal);
        }
    }

    public void setVolumeLevel(int userId, int volume) {
        if (rostrumLayoutMap.containsKey(userId)) {
            RostrumLayout rostrumLayout = rostrumLayoutMap.get(userId);
            if (rostrumLayout == null) return;
            rostrumLayout.setVolumeLevel(volume);
        }
    }

    public interface UpStageCallBack {
        void goUpStageCallBack(FrameLayout frameLayout, int userId);
    }

    public interface DownStageCallBack {
        void goDownStageCallBack();
    }
}
