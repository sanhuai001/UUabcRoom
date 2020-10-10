package com.uuabc.classroomlib.classroom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.widget.FilterImageView;

public class NetworkTipsView extends LinearLayout {
    private TextView tvTipsNetWorkTitle;
    private TextView tvTipsSocketTitle;
    private ImageView ivRightArrow;
    private RelativeLayout rlTitle;
    private ConstraintLayout clInfo;
    private boolean netQualityGood;

    public NetworkTipsView(Context context) {
        this(context, null);
    }

    public NetworkTipsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NetworkTipsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View rootView = inflater.inflate(R.layout.view_room_sdk_common_network_tips, this);
            tvTipsNetWorkTitle = rootView.findViewById(R.id.tv_tips_network_title);
            tvTipsSocketTitle = rootView.findViewById(R.id.tv_tips_socket_title);
            ivRightArrow = rootView.findViewById(R.id.iv_right_arrow);
            rlTitle = rootView.findViewById(R.id.rl_tips_title);
            clInfo = rootView.findViewById(R.id.cl_tips_info);
            FilterImageView btnReConnect = rootView.findViewById(R.id.btn_connect);

            rlTitle.setOnClickListener(v -> {
                if (tvTipsSocketTitle.isShown()) return;

                if (clInfo.isShown()) {
                    ivRightArrow.animate().setDuration(150).rotation(0);
                    clInfo.setVisibility(GONE);
                } else {
                    ivRightArrow.animate().setDuration(150).rotation(180);
                    clInfo.setVisibility(VISIBLE);
                }
            });

            btnReConnect.setOnClickListener(v -> NetworkUtils.openWirelessSettings());
        }
    }

    public void setNetQuality(boolean netQualityGood) {
        this.netQualityGood = netQualityGood;
        if (netQualityGood) {
            doNetworkQualityGood();
        } else {
            doNetworkQualityBad();
        }
    }


    /**
     * 网络连接
     */
    public void doNetWorkConnect() {
        if (!RoomApplication.getInstance().socketConnected()) {
            doDisconnect(true);
            return;
        }

        if (!netQualityGood) {
            doDisconnect(false);
            return;
        }

        setVisibility(GONE);
    }

    /**
     * 网络断开
     */
    public void doNetWorkInconnect() {
        setVisibility(VISIBLE);
        ivRightArrow.setVisibility(VISIBLE);
        rlTitle.setBackgroundResource(R.color.color_network_tips_red);
        ivRightArrow.animate().setDuration(150).rotation(0);
        clInfo.setVisibility(GONE);
        tvTipsNetWorkTitle.setVisibility(VISIBLE);
        tvTipsSocketTitle.setVisibility(GONE);
    }

    /**
     * socket 连接
     */
    public void doSocketConnect() {
        if (!NetworkUtils.isConnected()) {
            doNetWorkInconnect();
            return;
        }

        if (!netQualityGood) {
            doDisconnect(false);
            return;
        }

        setVisibility(GONE);
    }

    /**
     * socket 断开
     */
    public void doSocketInConnect() {
        setVisibility(VISIBLE);
        if (!NetworkUtils.isConnected()) {
            doNetWorkInconnect();
            return;
        }

        doDisconnect(true);
    }

    /**
     * 网络质量好
     */
    public void doNetworkQualityGood() {
        LogUtils.i("networkQuality", "doNetworkQualityGood");
        if (!NetworkUtils.isConnected()) {
            doNetWorkInconnect();
            return;
        }

        if (!RoomApplication.getInstance().socketConnected()) {
            doDisconnect(true);
            return;
        }

        setVisibility(GONE);
    }

    /**
     * 网络质量差
     */
    public void doNetworkQualityBad() {
        LogUtils.i("networkQuality", "doNetworkQualityBad");
        setVisibility(VISIBLE);
        if (!NetworkUtils.isConnected()) {
            doNetWorkInconnect();
            return;
        }

        doDisconnect(!RoomApplication.getInstance().socketConnected());
    }

    private void doDisconnect(boolean isSocket) {
        ivRightArrow.setVisibility(GONE);
        rlTitle.setBackgroundResource(R.color.color_socket_tips_orange);
        tvTipsSocketTitle.setText(isSocket ? R.string.socket_connect_fail_title_tips_str : R.string.network_connect_bad_title_tips_str);
        tvTipsSocketTitle.setVisibility(VISIBLE);
        tvTipsNetWorkTitle.setVisibility(GONE);
        clInfo.setVisibility(GONE);
    }
}
