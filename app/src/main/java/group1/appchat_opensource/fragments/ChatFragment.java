package group1.appchat_opensource.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import group1.appchat_opensource.R;
import group1.appchat_opensource.adapters.MessageAdapter;
import group1.appchat_opensource.adapters.MessageGroupAdapter;
import group1.appchat_opensource.databinding.ChatFragmentBinding;
import group1.appchat_opensource.objects.Message;
import group1.appchat_opensource.objects.MessageGroup;
import group1.appchat_opensource.objects.User;

public class ChatFragment extends Fragment {
    ChatFragmentBinding binding;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    List<Message> list;
    List<MessageGroup> listGroup;
    MessageAdapter adapter;
    MessageGroupAdapter groupAdapter;
    ValueEventListener seenEventListener;
    User receiveUser;
    String imgUrlUser;
    Fragment me = this;
    public static ChatFragment newInstance(User user,String imgUrlUser) {

        Bundle args = new Bundle();
        args.putParcelable("user",user);
        args.putString("imgurl",imgUrlUser);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater,R.layout.chat_fragment,container,false );
        receiveUser = getArguments().getParcelable("user");
        imgUrlUser = getArguments().getString("imgurl");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        binding.tvUsername.setText(receiveUser.getUsername());
        if (!receiveUser.getUsername().equals("Community")){
            readMessage(firebaseUser.getUid(),receiveUser.getId());
            binding.btnSendMessage.setOnClickListener(v->{
                String message = binding.edtMessage.getText().toString().trim();
                if (!message.isEmpty()){
                    sendMessage(firebaseUser.getUid(),receiveUser.getId(),message);
                    binding.edtMessage.setText(null);
                    readMessage(firebaseUser.getUid(),receiveUser.getId());
                }
            });
            seenMessage(receiveUser.getId());

        }else{
            readMessageGroup();
            binding.btnSendMessage.setOnClickListener(v->{
                String message = binding.edtMessage.getText().toString().trim();
                if (!message.isEmpty()){
                    sendMessageGroup(firebaseUser.getUid(),message);
                    binding.edtMessage.setText(null);
                    readMessageGroup();
                }
            });
            seenMessageGroup(receiveUser.getId());
        }
        binding.btnBack.setOnClickListener(v->{
            FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStackImmediate();
        });

        return binding.getRoot();
    }
    public void sendMessage(String senderId,String receiveId,String message){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("senderId",senderId);
        hm.put("receiverId",receiveId);
        hm.put("content",message);
        hm.put("isSeen","unseen");
        DateFormat df = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        hm.put("time",date);
        databaseReference.child("Chats").push().setValue(hm);
    }
    public void readMessage(String myId,String receiveID){
        list = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Message messageSend = dataSnapshot.getValue(Message.class);
                    if (messageSend.getReceiverId().equals(myId)&&messageSend.getSenderId().equals(receiveID)||
                            messageSend.getReceiverId().equals(receiveID)&&messageSend.getSenderId().equals(myId)){
                        list.add(messageSend);
                    }
                }
                adapter = new MessageAdapter(list,getContext());
                adapter.setUserReceiver(receiveUser);
                adapter.setImgSender(imgUrlUser);
                binding.rcvChats.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void seenMessage(String senderId){
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        seenEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    Message messageSend = dataSnapshot.getValue(Message.class);
                    if (messageSend.getReceiverId().equals(firebaseUser.getUid())&&messageSend.getSenderId().equals(senderId)){
                        HashMap<String,Object> hm = new HashMap<>();
                        hm.put("isSeen","seen");
                        databaseReference.child(key).updateChildren(hm);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenEventListener);
    }

    public void readMessageGroup(){
        listGroup = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("GroupChat");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listGroup.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    MessageGroup messageGroup = dataSnapshot.getValue(MessageGroup.class);
                        listGroup.add(messageGroup);
                }
                groupAdapter = new MessageGroupAdapter(listGroup,getContext());
                binding.rcvChats.setAdapter(groupAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void sendMessageGroup(String senderId,String message){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hm = new HashMap<>();
        hm.put("senderId",senderId);
        hm.put("content",message);
        hm.put("isSeen","unseen");
        DateFormat df = new SimpleDateFormat("HH:mm, dd/MM/yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        hm.put("time",date);
        databaseReference.child("GroupChat").push().setValue(hm);
    }

    public void seenMessageGroup(String myID){
        databaseReference = FirebaseDatabase.getInstance().getReference("GroupChat");
        seenEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String key = dataSnapshot.getKey();
                    MessageGroup messageSend = dataSnapshot.getValue(MessageGroup.class);
                    if (!messageSend.getSenderId().equals(myID)){
                        HashMap<String,Object> hm = new HashMap<>();
                        hm.put("isSeen","seen");
                        databaseReference.child(key).updateChildren(hm);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
