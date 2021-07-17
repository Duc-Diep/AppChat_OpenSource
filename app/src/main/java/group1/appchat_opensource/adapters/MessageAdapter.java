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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import group1.appchat_opensource.R;
import group1.appchat_opensource.configs.Constant;
import group1.appchat_opensource.objects.Message;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_LEFT_TYPE = 0;
    public static final int MSG_RIGHT_TYPE = 1;
    List<Message> list;
    Context context;
    FirebaseUser firebaseUser;
    String imgSender, imgReceiver;

    public void setImgSender(String imgSender) {
        this.imgSender = imgSender;
    }

    public void setImgReceiver(String imgReceiver) {
        this.imgReceiver = imgReceiver;
    }


    public MessageAdapter(List<Message> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_LEFT_TYPE) {
            View view = LayoutInflater.from( context ).inflate( R.layout.left_message_item, parent, false );
            return new MessageAdapter.ViewHolder( view );
        } else {
            View view = LayoutInflater.from( context ).inflate( R.layout.right_message_item, parent, false );
            return new MessageAdapter.ViewHolder( view );
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message messageSend = list.get( position );
        holder.tvMessage.setText( messageSend.getContent() );
        if (holder.getItemViewType() == MSG_RIGHT_TYPE) {
            Glide.with( context ).load( imgSender ).into( holder.imgAvatar );
            if (messageSend.getIsSeen().equalsIgnoreCase( "seen" )&&(position==list.size()-1)) {
                Glide.with( context ).load( imgReceiver ).into( holder.imgAvatarStatusSeen );
            } else {
                holder.imgAvatarStatusSeen.setVisibility( View.GONE );
            }
        } else {
            Glide.with( context ).load( imgReceiver ).into( holder.imgAvatar );
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgAvatar,imgAvatarStatusSeen;
        TextView tvMessage, tvSeen;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
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
