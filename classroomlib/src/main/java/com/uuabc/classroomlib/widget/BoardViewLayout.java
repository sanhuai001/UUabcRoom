package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.SocketModel.DrawTextModel;
import com.uuabc.classroomlib.model.SocketModel.MoveValueModel;
import com.uuabc.classroomlib.model.SocketModel.OnlineUserModel;
import com.uuabc.classroomlib.model.SocketModel.UserModel;
import com.uuabc.classroomlib.model.UserPathModel;
import com.uuabc.classroomlib.utils.ObjectUtil;
import com.uuabc.classroomlib.utils.UtilsBigDecimal;

import java.util.ArrayList;
import java.util.List;

public class BoardViewLayout extends FrameLayout {
    private ClassicsBoardView boardView;
    private Context context;
    private List<DrawTextImageView> mMouseViewList;
    private List<LayoutParams> mLpPaintList;
    private TextView textView;
    private List<TextView> textViews;

    public BoardViewLayout(Context context) {
        this(context, null);
    }

    public BoardViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BoardViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mMouseViewList = new ArrayList<>();
        mLpPaintList = new ArrayList<>();
        textViews = new ArrayList<>();
        View root = LayoutInflater.from(context).inflate(R.layout.view_board_layout, this, true);
        initBoard(root);
    }

    /**
     * 初始化画板,画笔
     */
    private void initBoard(View root) {
        boardView = root.findViewById(R.id.bv_my_board);

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
        boardView.getList().get(0).setUserId(SPUtils.getInstance().getInt(RoomConstant.USER_ID));
    }

    public void setPaintColor(String colorStr) {
        boardView.setMyColor(colorStr);
    }

    public void setPaintSize(float size) {
        boardView.setMySize(size);
    }

    public void drawText(DrawTextModel value, float mScale) {
        if (value == null) return;

        if (TextUtils.equals(value.getType(), RoomConstant.TEXT_START_TYPE)) {
            textView = new TextView(context);
            textViews.add(textView);
            addView(textView);
            drawEditBox(value, mScale);
        } else if (TextUtils.equals(value.getType(), RoomConstant.TEXT_END_TYPE) && textView != null) {
            drawEditBox(value, mScale);
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

    private void drawEditBox(DrawTextModel value, float mScale) {
        if (textView != null && textView.getLayoutParams() != null) {
            textView.setTextColor(Color.parseColor(value.getColor()));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, UtilsBigDecimal.getDivValue(value.getFontSize(), (float) (mScale * 0.92)));
            LayoutParams layoutParams = (LayoutParams) textView.getLayoutParams();
            layoutParams.width = (int) UtilsBigDecimal.getDivValue(value.getWidth() == 0 ? 300 : value.getWidth(), mScale);
            layoutParams.height = LayoutParams.WRAP_CONTENT;
            layoutParams.leftMargin = (int) (value.getX() / mScale);
            layoutParams.topMargin = (int) (value.getY() / mScale);
            textView.requestLayout();
        }
    }

    public void setCanAnimate(boolean canAnimate) {
        boardView.setCanAnimate(canAnimate);
    }

    public void setCanDraw(boolean canDraw) {
        boardView.setCanDraw(canDraw);
    }

    public void clearAllBoard() {
        boardView.clearBoard();
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
        for (UserPathModel userPathModel : boardView.getList()) {
            if (userId == userPathModel.getUserId()) {
                if (isMouseDown) {
                    if (!(userPathModel.getPathModel().getPaint().getColor() == Color.parseColor(valueModel.getColor()) && userPathModel.getPathModel().getPaint().getStrokeWidth() == valueModel.getWidth() / mScale)) {
                        userPathModel.setNewPath();
                        userPathModel.getPathModel().getPaint().setStrokeWidth(valueModel.getWidth() / mScale);
                        userPathModel.getPathModel().getPaint().setColor(Color.parseColor(valueModel.getColor()));
                    }
                    userPathModel.getPathModel().getPath().moveTo(valueModel.getX() / mScale, valueModel.getY() / mScale);
                }
                break;
            }
        }
    }

    public void doPositionMove(int userId, boolean isMouseDown, MoveValueModel valueModel, float mScale) {
        for (UserPathModel userPathModel : boardView.getList()) {
            if (userId == userPathModel.getUserId()) {
                float x = valueModel.getX() / mScale;
                float y = valueModel.getY() / mScale;
                if (isMouseDown) {
                    userPathModel.getPathModel().getPath().lineTo(x, y);
                    boardView.drawLine();
                    mMouseViewList.get(boardView.getList().indexOf(userPathModel)).setVisibility(View.VISIBLE);
                }
                LayoutParams layoutParams = mLpPaintList.get(boardView.getList().indexOf(userPathModel));
                layoutParams.leftMargin = (int) x;
                layoutParams.topMargin = (int) y - layoutParams.height;
                mMouseViewList.get(boardView.getList().indexOf(userPathModel)).requestLayout();
                break;
            }
        }
    }

    public void doPositionEnd(int userId) {
        for (UserPathModel userPathModel : boardView.getList()) {
            if (userId == userPathModel.getUserId()) {
                mMouseViewList.get(boardView.getList().indexOf(userPathModel)).setVisibility(View.GONE);
                break;
            }
        }
    }

    public void doDrawLine(int userId, MoveValueModel valueModel, float mScale) {
        for (UserPathModel userPathModel : boardView.getList()) {
            if (userId == userPathModel.getUserId()) {
                List<List<Object>> points = valueModel.getPoints();
                if (points != null && points.size() > 0) {
                    for (int j = 0; j < points.size(); j++) {
                        List<Object> point = points.get(j);
                        float x = ObjectUtil.getFloat(point.get(0)) / mScale;
                        float y = ObjectUtil.getFloat(point.get(1)) / mScale;
                        if (j == 0) {
                            userPathModel.setNewPath();
                            userPathModel.getPathModel().getPaint().setStrokeWidth(valueModel.getWidth() / mScale);
                            userPathModel.getPathModel().getPaint().setColor(Color.parseColor(valueModel.getColor()));
                            userPathModel.getPathModel().getPath().moveTo(x, y);
                        }
                        userPathModel.getPathModel().getPath().lineTo(x, y);
                        boardView.drawLine();
                    }
                }
                break;
            }
        }
    }

    public void studentInterRoom(UserModel student) {
        int position = boardView.createPathModel(student.getId());
        if (position != 0) {
            mMouseViewList.get(position).setTag(student.getId());
            mMouseViewList.get(position).setUserName(student.getName());
        }
    }

    public void teacherInterRoom(UserModel userModel) {
        boardView.getList().get(1).setUserId(userModel.getId());
        mMouseViewList.get(1).setTag(userModel.getId());
        mMouseViewList.get(1).setUserName(userModel.getName());
    }

    public void teacherInterRoom(OnlineUserModel userModel) {
        boardView.getList().get(1).setUserId(userModel.getUser_id());
        mMouseViewList.get(1).setTag(userModel.getUser_id());
        mMouseViewList.get(1).setUserName(userModel.getInfo() != null ? userModel.getInfo().getName() : "");
    }

    public int getPosition(int userId) {
        for (int i = 2; i < boardView.getList().size(); i++) {
            if (userId == boardView.getList().get(i).getUserId()) {
                return i;
            }
        }
        return -1;
    }

    public int getBoardSize() {
        return boardView.getList().size();
    }

    public ClassicsBoardView getMyBoardView() {
        return boardView;
    }

    public void setTeacherInfo(int teacherId, String teacherName) {
        boardView.getList().get(1).setUserId(teacherId);
        mMouseViewList.get(1).setUserName(teacherName);
    }

    public int getTeacherId() {
        return boardView.getList().get(1).getUserId();
    }

    public void setLeftImage(boolean isText) {
        mMouseViewList.get(1).setImage(isText ? R.drawable.ic_room_sdk_text_teacher : R.drawable.ic_room_sdk_paint_teacher);
    }
}
