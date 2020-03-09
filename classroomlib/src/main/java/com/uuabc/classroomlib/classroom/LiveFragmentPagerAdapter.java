package com.uuabc.classroomlib.classroom;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.uuabc.classroomlib.common.BaseCommonFragment;

import java.util.List;

public class LiveFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BaseCommonFragment> list;

    LiveFragmentPagerAdapter(FragmentManager fm, List<BaseCommonFragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
