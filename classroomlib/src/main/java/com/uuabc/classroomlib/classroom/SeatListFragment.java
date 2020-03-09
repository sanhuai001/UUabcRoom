package com.uuabc.classroomlib.classroom;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.common.BaseCommonFragment;
import com.uuabc.classroomlib.databinding.FragmentRoomSdkSeatListBinding;
import com.uuabc.classroomlib.model.SocketModel.UserModel;

import java.util.List;

@SuppressLint("StaticFieldLeak")
public class SeatListFragment extends BaseCommonFragment {
    private FragmentRoomSdkSeatListBinding binding;
    private static SeatListFragment fragment;
    private SeatListAdapter adapter;

    static SeatListFragment newInstant() {
        if (fragment == null) {
            fragment = new SeatListFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_room_sdk_seat_list, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(RecyclerView.VERTICAL);
        binding.rvSeatList.setLayoutManager(manager);
        binding.rvSeatList.setAdapter(adapter = new SeatListAdapter(getActivity()));
        binding.viewLine.setVisibility(View.GONE);
    }

    void setSeatList(List<UserModel> modelList) {
        if (!isAdded()) return;
        binding.ivChatNodata.setVisibility(modelList == null || modelList.size() == 0 ? View.VISIBLE : View.GONE);
        adapter.setDatas(modelList);
        binding.tvCount.setText(getString(R.string.fragment_seat_list_count_str, modelList == null ? 0 : modelList.size()));
    }
}
