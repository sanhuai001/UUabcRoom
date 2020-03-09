package com.uuabc.classroomlib.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ImageUtils;
import com.uuabc.classroomlib.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomCircleImageView extends CircleImageView {
    private boolean addTag = false;
    private Bitmap bitmap;
    private Paint paint;

    public CustomCircleImageView(Context context) {
        super(context);
    }

    public CustomCircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomCircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addTag(boolean scaleUp) {
        if (bitmap == null) {
            bitmap = ImageUtils.getBitmap(R.drawable.ic_room_sdk_tag_mine);
            paint = new Paint();
            paint.setColor(Color.parseColor("#00000000"));
            paint.setStyle(Paint.Style.FILL);
        }

        if (!scaleUp) {
            bitmap = ImageUtils.scale(bitmap, getWidth() / 2, getHeight() / 2);
        }
        addTag = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (addTag) {
            canvas.drawRect(canvas.getWidth() - bitmap.getWidth(), canvas.getHeight() - bitmap.getHeight(), canvas.getWidth(), canvas.getHeight(), paint);
            canvas.drawBitmap(bitmap, canvas.getWidth() - bitmap.getWidth(), canvas.getHeight() - bitmap.getHeight(), null);
        }
    }
}
