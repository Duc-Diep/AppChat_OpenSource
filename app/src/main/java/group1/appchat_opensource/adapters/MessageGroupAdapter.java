package group1.appchat_opensource.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import group1.appchat_opensource.R;
import group1.appchat_opensource.objects.Message;
import group1.appchat_opensource.objects.MessageGroup;
import group1.appchat_opensource.objects.User;

public class MessageGroupAdapter extends RecyclerView.Adapter<MessageGroupAdapter.ViewHolder> {
    public static final int MSG_LEFT_TYPE = 0;
    public static final int MSG_RIGHT_TYPE = 1;
    List<MessageGroup> list;
    Context context;
    FirebaseUser firebaseUser;
    String imgSender;

    public MessageGroupAdapter(List<MessageGroup> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageGroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_LEFT_TYPE) {
            View view = LayoutInflater.from( context ).inflate( R.layout.left_message_item, parent, false );
            return new MessageGroupAdapter.ViewHolder( view );
        } else {
            View view = LayoutInflater.from( context ).inflate( R.layout.right_message_item, parent, false );
            return new MessageGroupAdapter.ViewHolder( view );
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageGroupAdapter.ViewHolder holder, int position) {
        MessageGroup message = list.get( position );
        holder.tvMessage.setText( message.getContent() );
        if (holder.getItemViewType() == MSG_RIGHT_TYPE) {
            Glide.with( context ).load( imgSender ).into( holder.imgAvatar );
            holder.imgAvatarStatusSeen.setVisibility(View.INVISIBLE);
            if (message.getIsSeen().equalsIgnoreCase( "seen" )&&(position==list.size()-1)) {
                    getImageById(message.getSenderId(),holder.imgAvatar);
            } else {
                holder.imgAvatarStatusSeen.setVisibility( View.GONE );
            }
        } else {
            getImageById(message.getSenderId(),holder.imgAvatar);
        }
    }

    private void getImageById(String senderId,CircleImageView imageView) {
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("Users").child(senderId);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user!=null){
                    Glide.with( context ).load( user.getImage_url() ).into( imageView );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar,imgAvatarStatusSeen;
        TextView tvMessage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById( R.id.imgAvatarMess );
            tvMessage = itemView.findViewById( R.id.tvMessage );
            imgAvatarStatusSeen = itemView.findViewById( R.id.imgStatusSeenMessage );
        }
    }
    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get( position ).getSenderId().equals( firebaseUser.getUid() )) {
            return MSG_RIGHT_TYPE;
        } else {
            return MSG_LEFT_TYPE;
        }

    }
}
