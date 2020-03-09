package com.uuabc.classroomlib.common;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 通用ViewHolder
 */

public final class ViewHolder {

    private final SparseArray<View> mViews;
    int mPosition;
    private View mConvertView;

    private ViewHolder(int layoutId, ViewGroup parent) {
        mViews = new SparseArray<>();
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    static ViewHolder get(int layoutId, View convertView, ViewGroup parent) {
        if (convertView == null) {
            return new ViewHolder(layoutId, parent);
        }
        return (ViewHolder) convertView.getTag();
    }

    public <V extends View> V getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        //noinspection unchecked
        return (V) view;
    }

    public ViewHolder setText(int viewId, String value) {
        TextView textView = getView(viewId);
        textView.setText(value);
        return this;
    }

    View getConvertView() {
        return mConvertView;
    }

    public int getPosition() {
        return mPosition;
    }
}
