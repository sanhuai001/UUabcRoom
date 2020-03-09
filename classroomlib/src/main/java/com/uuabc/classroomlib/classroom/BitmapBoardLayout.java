package com.uuabc.classroomlib.classroom;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.SocketModel.DrawTextModel;
import com.uuabc.classroomlib.model.SocketModel.MoveValueModel;
import com.uuabc.classroomlib.model.SocketModel.OnlineUserModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.widget.BitmapBoardView;
import com.uuabc.classroomlib.widget.DrawTextImageView;

import java.util.ArrayList;
import java.util.List;

public class BitmapBoardLayout extends FrameLayout {
    protected List<BitmapBoardView> mBoardViewList;
    private List<DrawTextImageView> mMouseViewList;
    private List<LayoutParams> mLpPaintList;
    private Context context;
    private TextView textView;
    private List<TextView> textViews;

    public BitmapBoardLayout(Context context) {
        this(context, null);
    }

    public BitmapBoardLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapBoardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mBoardViewList = new ArrayList<>();
        mMouseViewList = new ArrayList<>();
        mLpPaintList = new ArrayList<>();
        textViews = new ArrayList<>();
        View root = LayoutInflater.from(context).inflate(R.layout.view_stub_room_sdk_board, this, true);
        initBoard(root);
    }

    /**
     * 初始化画板,画笔
     */
    private void initBoard(View root) {
        mBoardViewList.add(root.findViewById(R.id.bv_my_board));
        mBoardViewList.add(root.findViewById(R.id.bv_teacher_board));
        mBoardViewList.add(root.findViewById(R.id.bv_stu1_board));
        mBoardViewList.add(root.findViewById(R.id.bv_stu2_board));
        mBoardViewList.add(root.findViewById(R.id.bv_stu3_board));
        mBoardViewList.get(0).setUserId(SPUtils.getInstance().getInt(RoomConstant.USER_ID));

        DrawTextImageView ivMyselt = root.findViewById(R.id.iv_my_mouse);
        ivMyselt.setImage(R.drawable.ic_room_sdk_paint_stu);
        DrawTextImageView ivTeacher = root.findViewById(R.id.iv_teacher_mouse);
        ivTeacher.setImage(R.drawable.ic_room_sdk_paint_teacher);
        DrawTextImageView ivStu1 = root.findViewById(R.id.iv_stu1_mouse);
        ivStu1.setImage(R.drawable.ic_room_sdk_paint_stu);
        DrawTextImageView ivStu2 = root.findViewById(R.id.iv_stu2_mouse);
        ivStu2.setImage(R.drawable.ic_room_sdk_paint_stu);
        DrawTextImageView ivStu3 = root.findViewById(R.id.iv_stu3_mouse);
        ivStu3.setImage(R.drawable.ic_room_sdk_paint_stu);

        mMouseViewList.add(ivMyselt);
        mMouseViewList.add(ivTeacher);
        mMouseViewList.add(ivStu1);
        mMouseViewList.add(ivStu2);
        mMouseViewList.add(ivStu3);

        mLpPaintList.add((LayoutParams) ivMyselt.getLayoutParams());
        mLpPaintList.add((LayoutParams) ivTeacher.getLayoutParams());
        mLpPaintList.add((LayoutParams) ivStu1.getLayoutParams());
        mLpPaintList.add((LayoutParams) ivStu2.getLayoutParams());
        mLpPaintList.add((LayoutParams) ivStu3.getLayoutParams());
    }

    public void setMyselfViewTag() {
        mBoardViewList.get(0).setUserId(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
    }

    public void setLayoutParams(int width, int height, int containerWidth, int containerHeight) {
        for (int i = 0; i < mBoardViewList.size(); i++) {
            LayoutParams parentParams = (LayoutParams) mBoardViewList.get(i).getLayoutParams();
            parentParams.width = width;
            parentParams.height = height;
            mBoardViewList.get(i).requestLayout();
        }

        //设置画笔默认位置
        int paintTopMargin = 0, paintLeftMargin = 0;
        if (containerWidth == width) {
            paintTopMargin = (containerHeight - height) / 2;
        } else {
            paintLeftMargin = (containerWidth - width) / 2;
        }
        for (int i = 0; i < mMouseViewList.size(); i++) {
            mLpPaintList.get(i).topMargin = paintTopMargin;
            mLpPaintList.get(i).leftMargin = paintLeftMargin;
            mMouseViewList.get(i).requestLayout();
        }
    }

    public void setPaintColor(String colorStr) {
        mBoardViewList.get(0).setColor(colorStr);
    }

    public void setPaintSize(float size) {
        mBoardViewList.get(0).setSize(size);
    }

    public void drawText(DrawTextModel value, float mScale) {
        if (value == null) return;
//        if (value.getWidth() == 0 || value.getHeight() == 0) return;

        if (TextUtils.equals(value.getType(), RoomConstant.TEXT_START_TYPE)) {
            textView = new TextView(context);
            textView.setTextColor(Color.parseColor(value.getColor()));
            textView.setTextSize(SizeUtils.px2sp(value.getFontSize() / mScale));
            textViews.add(textView);
            addView(textView);
            LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
            layoutParams.width = (int) (value.getWidth() == 0 ? 310 : value.getWidth() / mScale);
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.leftMargin = (int) (value.getX() / mScale);
            layoutParams.topMargin = (int) (value.getY() / mScale);
            textView.requestLayout();
        } else if (TextUtils.equals(value.getType(), RoomConstant.TEXT_END_TYPE) && textView != null) {
            textView.setTextColor(Color.parseColor(value.getColor()));
            textView.setTextSize(SizeUtils.px2sp(value.getFontSize() / mScale));
            textView = null;
        }

        if (textView != null) {
            String textStr = value.getText();
            if (!TextUtils.isEmpty(textStr)) {
                textStr = textStr.replaceAll("<div>", "\n");
                textStr = textStr.replaceAll("<br>", "");
                textStr = textStr.replaceAll("</div>", "");
            }
            textView.setText(textStr);
        }

        mMouseViewList.get(1).setVisibility(View.GONE);
    }

    public void setCanAnimate(boolean canAnimate) {
        mBoardViewList.get(0).setCanAnimate(canAnimate);
    }

    public void setCanDraw(boolean canDraw) {
        mBoardViewList.get(0).setCanDraw(canDraw);
    }

    public void clearAllBoard() {
        for (int i = 0; i < mBoardViewList.size(); i++) {
            mBoardViewList.get(i).reset();
        }

        for (int i = 0; i < textViews.size(); i++) {
            removeView(textViews.get(i));
        }
        textViews.clear();

        clearAllMouseView();
    }

    public void clearAllMouseView() {
        for (int i = 0; i < mMouseViewList.size(); i++) {
            mMouseViewList.get(i).setVisibility(View.GONE);
        }
    }

    public void doPositionStart(int userId, boolean isMouseDown, MoveValueModel valueModel, float mScale) {
        for (int i = 0; i < mBoardViewList.size(); i++) {
            BitmapBoardView boardView = mBoardViewList.get(i);
            if (userId == boardView.getUserId()) {
                if (isMouseDown) {
                    boardView.setSize(valueModel.getWidth() / mScale);
                    boardView.setColor(valueModel.getColor());
                    boardView.setMove(valueModel.getX() / mScale, valueModel.getY() / mScale);
                }
                break;
            }
        }
    }

    public void doPositionMove(int userId, boolean isMouseDown, MoveValueModel valueModel, float mScale) {
        for (int i = 0; i < mBoardViewList.size(); i++) {
            BitmapBoardView boardView = mBoardViewList.get(i);
            if (userId == boardView.getUserId()) {
                float x = valueModel.getX() / mScale;
                float y = valueModel.getY() / mScale;
                if (isMouseDown) {
                    mBoardViewList.get(i).setLine(x, y);
                }

                //移动画笔
                if (isMouseDown) {
                    mMouseViewList.get(i).setVisibility(View.VISIBLE);
                }
                LayoutParams layoutParams = mLpPaintList.get(i);
                layoutParams.leftMargin = (int) x;
                layoutParams.topMargin = (int) y - layoutParams.height;
                mMouseViewList.get(i).requestLayout();
                break;
            }
        }
    }

    public void doTeaPositionMove(int userId, boolean isMouseDown, MoveValueModel valueModel, float mScale) {
        for (int i = 0; i < mBoardViewList.size(); i++) {
            BitmapBoardView boardView = mBoardViewList.get(i);
            if (userId == boardView.getUserId()) {
                float x = valueModel.getX() / mScale;
                float y = valueModel.getY() / mScale;
                if (isMouseDown) {
                    mBoardViewList.get(i).setLine(x, y);
                }

                LayoutParams layoutParams = mLpPaintList.get(i);
                layoutParams.leftMargin = (int) x;
                layoutParams.topMargin = (int) y - layoutParams.height;
                mMouseViewList.get(i).requestLayout();
                break;
            }
        }
    }

    public void doPositionEnd(int userId) {
        for (int i = 0; i < mBoardViewList.size(); i++) {
            BitmapBoardView boardView = mBoardViewList.get(i);
            if (userId == boardView.getUserId()) {
                mMouseViewList.get(i).setVisibility(View.GONE);
                break;
            }
        }
    }

    public void doDrawLine(int userId, MoveValueModel valueModel, float mScale) {
        for (int i = 0; i < mBoardViewList.size(); i++) {
            BitmapBoardView boardView = mBoardViewList.get(i);
            if (userId == boardView.getUserId()) {
                List<List<Object>> points = valueModel.getPoints();
                if (points != null && points.size() > 0) {
                    for (int j = 0; j < points.size(); j++) {
                        List<Object> point = points.get(j);
                        float x = ObjectUtil.getFloat(point.get(0)) / mScale;
                        float y = ObjectUtil.getFloat(point.get(1)) / mScale;
                        if (j == 0) {
                            boardView.setSize(valueModel.getWidth() / mScale);
                            boardView.setColor(valueModel.getColor());
                            boardView.setMove(x, y);
                        }
                        mBoardViewList.get(i).setLine(x, y);
                    }
                }
                break;
            }
        }
    }

    public boolean isStudentAdded(int studentId) {
        for (int i = 2; i < mBoardViewList.size(); i++) {
            if (mBoardViewList.get(i).getUserId() == studentId) {
                return true;
            }
        }
        return false;
    }

    public void studentInterRoom(UserModel student, StudentInterRoomCallBack callBack) {
        for (int i = 2; i < mBoardViewList.size(); i++) {
            if (mBoardViewList.get(i).getUserId() == 0) {
                mBoardViewList.get(i).setUserId(student.getId());
                mMouseViewList.get(i).setTag(student.getId());
                mMouseViewList.get(i).setUserName(student.getName());
                if (callBack != null)
                    callBack.onStudentInterRoom(student, i);
                break;
            }
        }
    }

    public void childInterRoom(UserModel userModel) {
        mBoardViewList.get(0).setUserId(userModel.getId());
        mMouseViewList.get(0).setTag(userModel.getId());
        mMouseViewList.get(0).setUserName(userModel.getName());
    }

    public void childInterRoom(String name, int userId) {
        mBoardViewList.get(0).setUserId(userId);
        mMouseViewList.get(0).setTag(userId);
        mMouseViewList.get(0).setUserName(name);
    }

    public void teacherInterRoom(UserModel userModel) {
        mBoardViewList.get(1).setUserId(userModel.getId());
        mMouseViewList.get(1).setTag(userModel.getId());
        mMouseViewList.get(1).setUserName(userModel.getName());
    }

    public void teacherInterRoom(String name, int userId) {
        mBoardViewList.get(1).setUserId(userId);
        mMouseViewList.get(1).setTag(userId);
        mMouseViewList.get(1).setUserName(name);
    }

    public void teacherInterRoom(OnlineUserModel userModel) {
        mBoardViewList.get(1).setUserId(userModel.getUser_id());
        mMouseViewList.get(1).setTag(userModel.getUser_id());
        mMouseViewList.get(1).setUserName(userModel.getInfo() != null ? userModel.getInfo().getName() : "");
    }

    public int getPosition(int userId) {
        for (int i = 2; i < mBoardViewList.size(); i++) {
            if (userId == mBoardViewList.get(i).getUserId()) {
                return i;
            }
        }
        return -1;
    }

    public int getBoardSize() {
        return mBoardViewList.size();
    }

    public BitmapBoardView getMyBoardView() {
        return mBoardViewList.get(0);
    }

    public void setTeacherInfo(int teacherId, String teacherName) {
        mBoardViewList.get(1).setUserId(teacherId);
        mMouseViewList.get(1).setUserName(teacherName);
    }

    public int getTeacherId() {
        return mBoardViewList.get(1).getUserId();
    }

    public void setLeftImage(boolean isText) {
        mMouseViewList.get(1).setImage(isText ? R.drawable.ic_room_sdk_text_teacher : R.drawable.ic_room_sdk_paint_teacher);
    }

    public interface StudentInterRoomCallBack {
        void onStudentInterRoom(UserModel student, int position);
    }
}
