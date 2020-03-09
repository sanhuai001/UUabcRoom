package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.uuabc.classroomlib.R;

import java.math.BigDecimal;

/**
 * Created by user on 2018/4/16.
 */

public class GradeView extends LinearLayout {
    /**
     * 是否可点击
     */
    private boolean mClickable;
    /**
     * 总数
     */
    private int starCount;
    /**
     * 点击事件
     */
    private OnRatingChangeListener onRatingChangeListener;
    /**
     * 大小
     */
    private float starImageWidth;
    private float starImageHeight;
    /**
     * 间距
     */
    private float starPadding;
    /**
     * 显示数量，支持小数点
     */
    private float starStep;
    /**
     * 空白的图片
     */
    private Drawable starEmptyDrawable;
    /**
     * 选中后的填充图片
     */
    private Drawable starFillDrawable;
    /**
     * 半颗图片
     */
    private Drawable starHalfDrawable;

    /**
     * 每次点击所增加的量是整个还是半个
     */
    private StepSize stepSize;

    public GradeView(Context context) {
        this(context, null);
    }

    public GradeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.GradeView);
        starImageWidth = mTypedArray.getDimension(R.styleable.GradeView_starImageWidth, 20);
        starImageHeight = mTypedArray.getDimension(R.styleable.GradeView_starImageHeight, 20);
        starPadding = mTypedArray.getDimension(R.styleable.GradeView_starPadding, 10);
        starStep = mTypedArray.getFloat(R.styleable.GradeView_starStep, 1.0f);
        stepSize = StepSize.fromStep(mTypedArray.getInt(R.styleable.GradeView_stepSize, 1));
        starCount = mTypedArray.getInteger(R.styleable.GradeView_starCount, 5);
        starEmptyDrawable = mTypedArray.getDrawable(R.styleable.GradeView_starEmpty);
        starFillDrawable = mTypedArray.getDrawable(R.styleable.GradeView_starFill);
        starHalfDrawable = mTypedArray.getDrawable(R.styleable.GradeView_starHalf);
        mClickable = mTypedArray.getBoolean(R.styleable.GradeView_clickable, true);
        mTypedArray.recycle();
        for (int i = 0; i < starCount; ++i) {
            final ImageView imageView = getStarImageView();
            imageView.setImageDrawable(starEmptyDrawable);
            imageView.setOnClickListener(v -> {
                        if (mClickable) {
                            //浮点数的整数部分
                            int fint = (int) starStep;
                            BigDecimal b1 = new BigDecimal(Float.toString(starStep));
                            BigDecimal b2 = new BigDecimal(Integer.toString(fint));
                            //浮点数的小数部分
                            float fPoint = b1.subtract(b2).floatValue();
                            if (fPoint == 0) {
                                fint -= 1;
                            }

                            if (indexOfChild(v) > fint) {
                                setStar(indexOfChild(v) + 1);
                            } else if (indexOfChild(v) == fint) {
                                if (stepSize == StepSize.Full) {//如果是满星 就不考虑半颗了
                                    return;
                                }
                                //点击之后默认每次先增加一颗星，再次点击变为半颗
                                if (imageView.getDrawable().getCurrent().getConstantState().equals(starHalfDrawable.getConstantState())) {
                                    setStar(indexOfChild(v) + 1);
                                } else {
                                    setStar(indexOfChild(v) + 0.5f);
                                }
                            } else {
                                setStar(indexOfChild(v) + 1f);
                            }

                        }

                    }
            );
            addView(imageView);
        }
        setStar(starStep);
    }

    /**
     * 设置每颗的参数
     */
    private ImageView getStarImageView() {
        ImageView imageView = new ImageView(getContext());

        LayoutParams layout = new LayoutParams(
                Math.round(starImageWidth), Math.round(starImageHeight));//设置每颗在线性布局的大小
        layout.setMargins(0, 0, Math.round(starPadding), 0);//设置每颗在线性布局的间距
        imageView.setLayoutParams(layout);
        imageView.setAdjustViewBounds(true);
        imageView.setImageDrawable(starEmptyDrawable);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setMinimumWidth(10);
        imageView.setMaxHeight(10);
        return imageView;

    }

    /**
     * 设置个数
     */

    public void setStar(float rating) {

        if (onRatingChangeListener != null) {
            onRatingChangeListener.onRatingChange(rating);
        }
        this.starStep = rating;
        //浮点数的整数部分
        int fint = (int) rating;
        BigDecimal b1 = new BigDecimal(Float.toString(rating));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
        //浮点数的小数部分
        float fPoint = b1.subtract(b2).floatValue();

        //设置选中的钻石
        for (int i = 0; i < fint; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }
        //设置没有选中的钻石
        for (int i = fint; i < starCount; i++) {
            ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
        }
        //小数点默认增加半颗钻石
        if (fPoint > 0) {
            ((ImageView) getChildAt(fint)).setImageDrawable(starHalfDrawable);
        }
    }

    public int getStar() {
        return (int) starStep;
    }

    /**
     * 操作星星的点击事件
     */
    public interface OnRatingChangeListener {
        /**
         * 选中的星星的个数
         */
        void onRatingChange(float ratingCount);

    }

    /**
     * 星星每次增加的方式整星还是半星，枚举类型
     * 类似于View.GONE
     */
    public enum StepSize {
        Half(0), Full(1);
        int step;

        StepSize(int step) {
            this.step = step;
        }

        public static StepSize fromStep(int step) {
            for (StepSize f : values()) {
                if (f.step == step) {
                    return f;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
