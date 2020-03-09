package com.uuabc.classroomlib.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.NetworkUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomApplication;
import com.uuabc.classroomlib.databinding.ActivityBaseCommonBinding;
import com.uuabc.classroomlib.utils.TipUtils;


/**
 * activity 通用父类
 * Created by wb on 2017/4/11.
 */

@SuppressLint({"CheckResult", "Registered"})
public class BaseCommonActivity extends BaseAdapterScreenActivity {
    protected Handler mMainHandler = new Handler(Looper.getMainLooper());
    protected View mChildView;
    ActivityBaseCommonBinding mBaseBinding;
    protected RxPermissions mRxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base_common);
        mBaseBinding.setPresenter(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        RoomApplication.getInstance().registerEventBus(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        RoomApplication.getInstance().unregisterEventBus(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initIntent();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.fontScale != 1)//非默认值
            getResources();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        if (res.getConfiguration().fontScale != 1) {//非默认值
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置默认
            res.updateConfiguration(newConfig, res.getDisplayMetrics());
        }
        return res;
    }

    /**
     * 设置内容区域
     */
    public void setContentLayout(int resId) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            initAdapterScreen();
            mChildView = inflater.inflate(resId, null);
            initUI(mChildView);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mChildView.setLayoutParams(layoutParams);
            if (null != mBaseBinding.llContentView) {
                mBaseBinding.llContentView.addView(mChildView);
                mBaseBinding.llContentView.setBackgroundResource(android.R.color.transparent);
            }
        }
    }

    /**
     * 显示Progress
     */
    public void showProgress() {
        mBaseBinding.include.clProgress.setBackgroundResource(R.color.translucent);
        mBaseBinding.include.progressBar.setVisibility(View.VISIBLE);
        mBaseBinding.include.clProgress.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏Progress
     */
    public void dismissProgress() {
        mBaseBinding.include.clProgress.setVisibility(View.GONE);
    }

    /**
     * 隐藏ProgressBar
     */
    public void hideProgressBar() {
        mBaseBinding.include.progressBar.setVisibility(View.INVISIBLE);
        mBaseBinding.include.clProgress.setBackgroundResource(R.color.transparent);
    }

    /**
     * 显示ContentLayout
     */
    public void showContentLayout() {
        mBaseBinding.llContentView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏ContentLayout
     */
    public void dismissContentLayout() {
        mBaseBinding.llContentView.setVisibility(View.INVISIBLE);
    }

    /**
     * 显示ReloadLayout
     */
    public void showReloadLayout() {
        dismissProgress();
        mBaseBinding.reload.clReload.setVisibility(View.VISIBLE);
        mBaseBinding.reload.btnEmptyBg.setOnClickListener(v -> {
            hideReloadLayout();
            onReload();
        });
    }

    /**
     * 隐藏ReloadLayout
     */
    public void hideReloadLayout() {
        mBaseBinding.reload.clReload.setVisibility(View.GONE);
    }

    /**
     * 检查网络，并显示相应的提示
     */
    public boolean checkNetWork() {
        if (!NetworkUtils.isConnected()) {
            TipUtils.show(this, getString(R.string.common_network_connect_fail));
            return false;
        }
        return true;
    }

    /**
     * 检查网络，并显示相应的提示
     */
    public boolean checkInitNetWork() {
        if (!NetworkUtils.isConnected()) {
            TipUtils.show(this, getString(R.string.common_network_connect_fail));
            return false;
        } else if (!NetworkUtils.isWifiConnected()) {
            TipUtils.show(this, getString(R.string.common_network_connect_notwifi));
        }
        return true;
    }

    public void initUI(View view) {
    }

    public void onReload() {

    }

    public void initIntent() {

    }

    public void out() {
        TipUtils.show(this, getString(R.string.common_token_overdue));
        finish();
    }
}
