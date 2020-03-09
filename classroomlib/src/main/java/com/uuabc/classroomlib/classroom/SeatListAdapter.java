package com.uuabc.classroomlib.classroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.uuabc.classroomlib.R;
import com.uuabc.classroomlib.RoomConstant;
import com.uuabc.classroomlib.model.SocketModel.UserModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SeatListAdapter extends RecyclerView.Adapter<SeatListAdapter.Holder> {

    private Context context;
    private List<UserModel> datas;
    private int[] positionNumbers = new int[]{R.drawable.ic_room_sdk_seat_level1, R.drawable.ic_room_sdk_seat_level2, R.drawable.ic_room_sdk_seat_level3, R.drawable.ic_room_sdk_seat_level4, R.drawable.ic_room_sdk_seat_level5};

    SeatListAdapter(Context context) {
        this.context = context;
    }

    void setDatas(List<UserModel> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.item_room_sdk_seat_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        switch (position) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
                /*
                 * 座位表前五的用户进行区别处理
                 */
                Glide.with(context).load(SPUtils.getInstance().getInt(RoomConstant.USER_ID) == datas.get(position).getId()
                        ? R.drawable.ic_room_sdk_seat_me : positionNumbers[position])
                        .into(holder.ivRank);

                holder.ivRank.setVisibility(View.VISIBLE);
                holder.rlAvatarBg.setBackgroundResource(R.drawable.room_sdk_shape_circle_orange2);

                holder.ivCrown.setVisibility(View.VISIBLE);
                holder.tvName.setTextColor(context.getResources().getColor(R.color.color_live_chart_count));
                break;
            default:
                if (SPUtils.getInstance().getInt(RoomConstant.USER_ID) == datas.get(position).getId()) {
                    holder.ivRank.setVisibility(View.VISIBLE);
                    Glide.with(context).load(R.drawable.ic_room_sdk_seat_me).into(holder.ivRank);
                    holder.ivRank.setBackgroundResource(R.drawable.ic_room_sdk_seat_me);
                } else {
                    holder.ivRank.setVisibility(View.GONE);
                }
                holder.ivCrown.setVisibility(View.GONE);
                holder.rlAvatarBg.setBackgroundResource(R.drawable.room_sdk_shape_circle_white);
                holder.tvName.setTextColor(context.getResources().getColor(R.color.color_live_chart_count));
                break;
        }
        holder.tvName.setText(datas.get(position).getName());
        Glide.with(context).load(datas.get(position).getPhoto())
                .apply(new RequestOptions().error(R.drawable.ic_room_sdk_boy_head))
                .into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return null == datas ? 0 : datas.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        private CircleImageView avatar;
        private TextView tvName;
        private ImageView ivCrown;
        private ConstraintLayout rlAvatarBg;
        private ImageView ivRank;

        Holder(View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            ivCrown = itemView.findViewById(R.id.iv_crown);
            rlAvatarBg = itemView.findViewById(R.id.rl_avatar_bg);
            ivRank = itemView.findViewById(R.id.iv_rank);
        }
    }
}
