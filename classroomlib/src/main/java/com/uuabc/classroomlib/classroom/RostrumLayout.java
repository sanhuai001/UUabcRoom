package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.uuabc.classroomlib.R;

import io.agora.rtc.Constants;

public class RostrumLayout extends FrameLayout {
    private ConstraintLayout clStuRostrum;
    private FrameLayout flStuRostrum;
    private TextView tvStuName;
    private ImageView ivSignal;
    private ImageView ivTV;
    private TextView tvDiamondCount;
    private ImageView ivNoCamera;
    private ImageView ivVolume;
    private Drawable[] volumesList;
    private Drawable[] mSingalList;

    public RostrumLayout(Context context, boolean isNewRoom) {
        this(context, null);
        if (isNewRoom) {
            ivVolume.setVisibility(VISIBLE);
        }
    }

    public RostrumLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public RostrumLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View root = LayoutInflater.from(context).inflate(R.layout.room_sdk_rostrum, this, true);

        clStuRostrum = root.findViewById(R.id.cl_stu_rostrum);
        flStuRostrum = root.findViewById(R.id.fl_stu_rostrum);
        tvStuName = root.findViewById(R.id.tv_stu_name);
        ivSignal = root.findViewById(R.id.iv_signal);
        ivTV = root.findViewById(R.id.iv_tv);
        tvDiamondCount = root.findViewById(R.id.tv_stu_diamond_count);
        ivNoCamera = root.findViewById(R.id.iv_no_camera);
        ivVolume = root.findViewById(R.id.iv_volume);

        volumesList = new Drawable[]{
                getResources().getDrawable(R.drawable.icon_volume_level_0),
                getResources().getDrawable(R.drawable.icon_volume_level_1),
                getResources().getDrawable(R.drawable.icon_volume_level_2),
                getResources().getDrawable(R.drawable.icon_volume_level_3),
                getResources().getDrawable(R.drawable.icon_volume_level_4),
                getResources().getDrawable(R.drawable.icon_volume_level_5),
                getResources().getDrawable(R.drawable.icon_volume_level_6),
                getResources().getDrawable(R.drawable.icon_volume_level_7)
        };

        mSingalList = new Drawable[]{
                getResources().getDrawable(R.drawable.icon_signal_3),
                getResources().getDrawable(R.drawable.icon_signal_2),
                getResources().getDrawable(R.drawable.icon_signal_1),
        };
    }

    public void setTvStuName(String stuName) {
        tvStuName.setText(stuName);
    }

    public void setTvDiamondCount(int diamondCount) {
        tvDiamondCount.setText(String.valueOf(diamondCount));
    }

    public void setVolumeVisible(boolean isMuted) {
        ivVolume.setVisibility(isMuted ? GONE : VISIBLE);
    }

    public void setCameraVisible(boolean hasCamera) {
        ivNoCamera.setVisibility(hasCamera ? GONE : VISIBLE);
    }

    public void isTv(boolean isTv) {
//        if (isTv) {
//            setTvVisible();
//        } else {
//            ivTV.setVisibility(GONE);
//        }
    }

    public ConstraintLayout getClStuRostrum() {
        return clStuRostrum;
    }

    public FrameLayout getFlStuRostrum() {
        return flStuRostrum;
    }

    public void setVolumeLevel(Integer volume) {
        if (volume == 0) {
            ivVolume.setImageDrawable(volumesList[0]);
        } else if (volume <= 36) {
            ivVolume.setImageDrawable(volumesList[1]);
        } else if (volume <= 72) {
            ivVolume.setImageDrawable(volumesList[2]);
        } else if (volume <= 108) {
            ivVolume.setImageDrawable(volumesList[3]);
        } else if (volume <= 144) {
            ivVolume.setImageDrawable(volumesList[4]);
        } else if (volume <= 180) {
            ivVolume.setImageDrawable(volumesList[5]);
        } else if (volume <= 216) {
            ivVolume.setImageDrawable(volumesList[6]);
        } else {
            ivVolume.setImageDrawable(volumesList[7]);
        }
    }

    public void setSignal(int signal) {
        setSignalVisible();
        switch (signal) {
            case Constants.QUALITY_EXCELLENT:
                ivSignal.setImageDrawable(mSingalList[0]);
                break;
            case Constants.QUALITY_GOOD:
            case Constants.QUALITY_POOR:
                ivSignal.setImageDrawable(mSingalList[1]);
                break;
            case Constants.QUALITY_BAD:
            case Constants.QUALITY_VBAD:
                ivSignal.setImageDrawable(mSingalList[2]);
                break;
        }
    }

    public void setSignalVisible() {
        if (!ivSignal.isShown())
            ivSignal.setVisibility(VISIBLE);
    }

    public void setTvVisible() {
        if (!ivTV.isShown())
            ivTV.setVisibility(VISIBLE);
    }
}
