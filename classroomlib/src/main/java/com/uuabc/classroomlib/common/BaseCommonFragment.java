package com.uuabc.classroomlib.common;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import me.jessyan.autosize.internal.CustomAdapt;

/**
 * fragment 通用父类
 * Created by wb on 2017/4/11.
 */

public class BaseCommonFragment extends Fragment implements CustomAdapt {
    public LinearLayout mLlContent;
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        ((BaseCommonActivity) mContext).initAdapterScreen();
    }

    /**
     * 显示Progress
     */
    public void showProgress() {
        if (isAdded()) {
            ((BaseCommonActivity) mContext).showProgress();
        }
    }

    /**
     * 隐藏Progress
     */
    public void dismissProgress() {
        if (isAdded()) {
            ((BaseCommonActivity) mContext).dismissProgress();
        }
    }

    /**
     * 显示ReloadLayout
     */
    public void showReloadLayout() {
        if (isAdded()) {
            ((BaseCommonActivity) mContext).showReloadLayout();
        }
    }

    /**
     * 隐藏ReloadLayout
     */
    public void hideReloadLayout() {
        if (isAdded()) {
            ((BaseCommonActivity) mContext).hideReloadLayout();
        }
    }

    /**
     * 显示ContentLayout
     */
    public void showContentLayout() {
        if (isAdded()) {
            if (mLlContent != null && !mLlContent.isShown()) {
                mLlContent.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 隐藏ContentLayout
     */
    public void hideContentLayout() {
        if (isAdded()) {
            if (mLlContent != null) {
                mLlContent.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 检测网络
     */
    public boolean checkNetWork() {
        return isAdded() && ((BaseCommonActivity) mContext).checkNetWork();
    }

    @Override
    public boolean isBaseOnWidth() {
        return false;
    }

    @Override
    public float getSizeInDp() {
        return 720;
    }
}
