package com.uuabc.classroomlib.classroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ObjectUtils;
import com.blankj.utilcode.util.SPUtils;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.SocketModel.ChartModel;
import com.uuabc.classroomlib.utils.CompatUtil;
import com.uuabc.classroomlib.widget.EmojiLottieView;

import java.util.ArrayList;
import java.util.List;

public class ChartListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_LEFT = 1;
    private final int TYPE_RIGHT = 2;
    private final int TYPE_TIPS = 3;
    private final int TYPE_MY_EMOJIS = 4;
    private final int TYPE_OTHER_EMOJIS = 5;

    private Context context;
    private List<ChartModel> chartModels = new ArrayList<>();
    private List<Integer> msgDrawableRes = new ArrayList<>();

    ChartListAdapter(Context context) {
        this.context = context;
        //自己
        msgDrawableRes.add(R.drawable.room_sdk_shape_msg_right_blue);
        //老师
        msgDrawableRes.add(R.drawable.room_sdk_shape_msg_left_deep_blue);
        //其他学生
        msgDrawableRes.add(R.drawable.room_sdk_shape_msg_left_blue);
    }

    public void addData(List<ChartModel> list) {
        chartModels.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(ChartModel chartModel) {
        chartModels.add(chartModel);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (chartModels != null) {
            if (chartModels.get(position).getType() == TYPE_MY_EMOJIS) {
                return TYPE_MY_EMOJIS;
            } else if (chartModels.get(position).getType() == TYPE_OTHER_EMOJIS) {
                return TYPE_OTHER_EMOJIS;
            } else if (ObjectUtils.isEmpty(chartModels.get(position).getSendId())) {
                return TYPE_TIPS;
            } else if (ObjectUtils.equals(String.valueOf(SPUtils.getInstance().getInt(RoomConstant.USER_ID)), chartModels.get(position).getSendId())) {
                return TYPE_RIGHT;
            } else if (!ObjectUtils.equals(String.valueOf(SPUtils.getInstance().getInt(RoomConstant.USER_ID)), chartModels.get(position).getSendId())) {
                return TYPE_LEFT;
            }
        }
        return TYPE_RIGHT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_LEFT) {
            View v = mInflater.inflate(R.layout.item_room_sdk_chart_other, parent, false);
            holder = new OtherHolder(v);
        } else if (viewType == TYPE_RIGHT) {
            View v = mInflater.inflate(R.layout.item_room_sdk_chart_me, parent, false);
            holder = new MeHolder(v);
        } else if (viewType == TYPE_TIPS) {
            View v = mInflater.inflate(R.layout.item_room_sdk_chart_tips, parent, false);
            holder = new TipsHolder(v);
        } else if (viewType == TYPE_MY_EMOJIS) {
            View v = mInflater.inflate(R.layout.item_room_sdk_chart_me_emoji, parent, false);
            holder = new MeEmojiHolder(v);
        } else if (viewType == TYPE_OTHER_EMOJIS) {
            View v = mInflater.inflate(R.layout.item_room_sdk_chart_other_emoji, parent, false);
            holder = new OtherEmojiHolder(v);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OtherHolder) {
            ((OtherHolder) holder).tvName.setTextColor(CompatUtil.getColor(context, R.color.white));
            ((OtherHolder) holder).tvName.setText(chartModels.get(position).getUserName());
            ((OtherHolder) holder).msg.setText(chartModels.get(position).getMsg());
            ((OtherHolder) holder).msg.setBackgroundResource(chartModels.get(position).getType() == 1 ? msgDrawableRes.get(2) : msgDrawableRes.get(1));
        } else if (holder instanceof MeHolder) {
            ((MeHolder) holder).msg.setTextColor(CompatUtil.getColor(context, R.color.white));
            ((MeHolder) holder).tvName.setText(chartModels.get(position).getUserName());
            ((MeHolder) holder).msg.setText(chartModels.get(position).getMsg());
            ((MeHolder) holder).msg.setBackgroundResource(msgDrawableRes.get(0));
        } else if (holder instanceof TipsHolder) {
            ((TipsHolder) holder).tvTips.setText(context.getResources().getString(R.string.live_class_room_send_star_tips_str, chartModels.get(position).getUserName()));
        } else if (holder instanceof MeEmojiHolder) {
            ((MeEmojiHolder) holder).tvName.setText(chartModels.get(position).getUserName());
            ((MeEmojiHolder) holder).emojiView.setBackgroundResource(msgDrawableRes.get(0));
            ((MeEmojiHolder) holder).emojiView.viewInSide(chartModels.get(position).getMsg());
        } else if (holder instanceof OtherEmojiHolder) {
            ((OtherEmojiHolder) holder).tvName.setText(chartModels.get(position).getUserName());
            ((OtherEmojiHolder) holder).emojiView.setBackgroundResource(msgDrawableRes.get(0));
            ((OtherEmojiHolder) holder).emojiView.viewInSide(chartModels.get(position).getMsg());
        }
    }

    @Override
    public int getItemCount() {
        return null == chartModels ? 0 : chartModels.size();
    }

    /**
     * 其他人的聊天布局
     */
    class OtherHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView msg;

        OtherHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            msg = itemView.findViewById(R.id.tv_msg_left);
        }
    }

    /**
     * 其他人的表情聊天布局
     */
    class OtherEmojiHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private EmojiLottieView emojiView;

        OtherEmojiHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            emojiView = itemView.findViewById(R.id.lavEmoji);
        }
    }

    /**
     * 自己的聊天布局
     */
    class MeHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView msg;

        MeHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            msg = itemView.findViewById(R.id.tv_msg_right);
        }
    }

    /**
     * 自己的表情聊天布局
     */
    class MeEmojiHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private EmojiLottieView emojiView;

        MeEmojiHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            emojiView = itemView.findViewById(R.id.lavRightEmoji);
        }
    }

    class TipsHolder extends RecyclerView.ViewHolder {
        private TextView tvTips;

        TipsHolder(View itemView) {
            super(itemView);
            tvTips = itemView.findViewById(R.id.tv_tips);
        }
    }
}
