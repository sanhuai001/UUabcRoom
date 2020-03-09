package com.uuabc.classroomlib.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

/**
 * CommonAdapter
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    private final int mLayoutId;
    private List<T> mDatas;
    private List<T> mSelectedList;
    private int mItemWidth;
    private int mItemHeight;
    private int mCheckBoxId, mClickableViewId;
    private boolean isSingleElection;
    private boolean canEnable = true;

    protected CommonAdapter(int itemLayoutId) {
        this.mLayoutId = itemLayoutId;
        mSelectedList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = ViewHolder.get(mLayoutId, convertView, parent);
        viewHolder.mPosition = position;
        if (mItemWidth > 0) {
            viewHolder.getConvertView().getLayoutParams().width = mItemWidth;
        }
        if (mItemHeight > 0) {
            viewHolder.getConvertView().getLayoutParams().height = mItemHeight;
        }

        T obj = getItem(position);

        if (mCheckBoxId != 0) {
            CheckBox cb = viewHolder.getView(mCheckBoxId);
            if (!canEnable) cb.setEnabled(false);
            cb.setChecked(mSelectedList.contains(obj));
            cb.setOnClickListener(v -> onItemClick(obj));
            cb.setChecked(mSelectedList.contains(obj));
        }

        if (mClickableViewId != 0) {
            viewHolder.getView(mClickableViewId).setOnClickListener(view -> onItemClick(obj));
        }

        convert(viewHolder, obj);
        return viewHolder.getConvertView();
    }

    private void onItemClick(T obj) {
        if (!canEnable) return;
        if (mCheckBoxId != 0) {
            if (mSelectedList.contains(obj)) {
                mSelectedList.remove(obj);
            } else {
                if (isSingleElection) {
                    mSelectedList.clear();
                }
                mSelectedList.add(obj);
            }
            checkedUpdate();
        }
    }

    public CommonAdapter setDatas(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
        return this;
    }

    public CommonAdapter setWidth(int mItemWidth) {
        this.mItemWidth = mItemWidth;
        return this;
    }

    public CommonAdapter setHeight(int mItemHeight) {
        this.mItemHeight = mItemHeight;
        return this;
    }

    public CommonAdapter setCheckBoxId(int mCheckBoxId) {
        this.mCheckBoxId = mCheckBoxId;
        return this;
    }

    public CommonAdapter setClickableViewId(int mClickableViewId) {
        this.mClickableViewId = mClickableViewId;
        return this;
    }

    public CommonAdapter setSingleElection(boolean isSingleElection) {
        this.isSingleElection = isSingleElection;
        return this;
    }

    public void setSelectedList(List<T> selectedList) {
        if (selectedList != null) {
            mSelectedList.clear();
            mSelectedList.addAll(selectedList);
        }
    }

    public CommonAdapter setCanEnable(boolean canEnable) {
        this.canEnable = canEnable;
        return this;
    }

    public List<T> getSelectedList() {
        return mSelectedList;
    }

    public abstract void convert(ViewHolder vh, T t);

    public void checkedUpdate() {
    }
}
