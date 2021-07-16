package group1.appchat_opensource.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import group1.appchat_opensource.R;
import group1.appchat_opensource.events.IOnClickChatItem;
import group1.appchat_opensource.objects.Message;
import group1.appchat_opensource.objects.User;

import static group1.appchat_opensource.configs.Constant.STATUS_ON;

public class ListChatsAdapter extends RecyclerView.Adapter<ListChatsAdapter.ViewHolder> {
    List<User> list;
    Context context;
    IOnClickChatItem iOnClickChatItem;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    List<Message> listMessage;
    String lastMessage;

    public ListChatsAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setiOnClickChatItem(IOnClickChatItem iOnClickChatItem) {
        this.iOnClickChatItem = iOnClickChatItem;
    }

    @NonNull
    @Override
    public ListChatsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listchat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChatsAdapter.ViewHolder holder, int position) {
        User user = list.get(position);
        holder.tvUsername.setText(user.getUsername().trim());
        Glide.with(context).load(user.getImage_url()).into(holder.imgAvatar);
        if (user.getStatus().equals(STATUS_ON)) {
            holder.imgStatus.setBackgroundResource(R.drawable.is_online);
        } else {
            holder.imgStatus.setBackgroundResource(R.drawable.is_offline);
        }
        holder.itemView.setOnClickListener(v -> iOnClickChatItem.IOnClickItem(user));
        lastMessage(user, holder.tvLastMessage, holder.imgStatusSeen);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar, imgStatusSeen;
        ImageView imgStatus;
        TextView tvUsername, tvLastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatarUserItem);
            imgStatus = itemView.findViewById(R.id.imgStatusItem);
            tvUsername = itemView.findViewById(R.id.tvUsernameItem);
            tvLastMessage = itemView.findViewById(R.id.tvLastMessage);
            imgStatusSeen = itemView.findViewById(R.id.imgStatusSeen);
        }
    }

    public void lastMessage(User user, TextView textView, CircleImageView circleImageView) {
        listMessage = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    listMessage.add(message);
                }
                Collections.reverse(listMessage);
                for (Message message : listMessage) {
//                    Log.d("TAG", "Message: "+message.getContent());
                    if ((message.getReceiverId().equals(firebaseUser.getUid()) && message.getSenderId().equals(user.getId())) || (message.getReceiverId().equals(user.getId()) && message.getSenderId().equals(firebaseUser.getUid()))) {
                        if (!message.getSenderId().equals(firebaseUser.getUid())) {
                            lastMessage = message.getContent();
                        } else {
                            lastMessage = "You : " + message.getContent();
                            if (message.getIsSeen().equalsIgnoreCase("seen") && message.getReceiverId().equals(user.getId()) && message.getSenderId().equals(firebaseUser.getUid())) {
                                Glide.with(context).load(user.getImage_url()).into(circleImageView);
                            } else {
                                circleImageView.setVisibility(View.INVISIBLE);
                            }
                        }
                        textView.setText(lastMessage);

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
