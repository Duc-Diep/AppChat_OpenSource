package group1.appchat_opensource.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import group1.appchat_opensource.R;
import group1.appchat_opensource.events.IOnClickChatItem;
import group1.appchat_opensource.objects.User;

import static group1.appchat_opensource.configs.Constant.STATUS_ON;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    List<User> list;
    Context context;
    IOnClickChatItem iOnClickChatItem;

    public CommunityAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setiOnClickChatItem(IOnClickChatItem iOnClickChatItem) {
        this.iOnClickChatItem = iOnClickChatItem;
    }

    @NonNull
    @Override
    public CommunityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.community_user_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityAdapter.ViewHolder holder, int position) {
        User user = list.get(position);
        holder.tvUsername.setText(user.getUsername().trim());
        Glide.with(context).load(user.getImage_url()).into(holder.imgAvatar);
        if (user.getStatus().equals(STATUS_ON)){
            holder.imgStatus.setBackgroundResource(R.drawable.is_online);
        }else{
            holder.imgStatus.setBackgroundResource(R.drawable.is_offline);
        }
        holder.itemView.setOnClickListener(v-> iOnClickChatItem.IOnClickItem(user));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar;
        ImageView imgStatus;
        TextView tvUsername;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatarUserItem);
            imgStatus = itemView.findViewById(R.id.imgStatusItem);
            tvUsername = itemView.findViewById(R.id.tvUsernameItem);
        }
    }
}
