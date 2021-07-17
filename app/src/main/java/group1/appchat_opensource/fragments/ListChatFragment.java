package group1.appchat_opensource.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import group1.appchat_opensource.R;
import group1.appchat_opensource.adapters.ListChatsAdapter;
import group1.appchat_opensource.databinding.ListchatFragmentBinding;
import group1.appchat_opensource.events.IOnClickChatItem;
import group1.appchat_opensource.objects.Message;
import group1.appchat_opensource.objects.User;

import static group1.appchat_opensource.configs.Constant.IMG_LINK_FEMALE_DEFAULT;
import static group1.appchat_opensource.configs.Constant.IMG_LINK_MALE_DEFAULT;
import static group1.appchat_opensource.configs.Constant.STATUS_OFF;
import static group1.appchat_opensource.configs.Constant.STATUS_ON;

public class ListChatFragment extends Fragment {
    ListchatFragmentBinding binding;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    DatabaseReference data;
    String userId;
    List<User> listUser;
    //    List<Message> listMessage;
    List<String> chatList;
    User user, currentUser;
    boolean check = false;

    public static ListChatFragment newInstance() {

        Bundle args = new Bundle();

        ListChatFragment fragment = new ListChatFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.listchat_fragment, container, false);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        currentUser = new User();
        chatList = new ArrayList<>();
//        listMessage = new ArrayList<>();
        listUser = new ArrayList<>();
        data = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        getCurrentUserInfor();
        getAllMessage();
        getAllUser();
        binding.searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.btnCouple.setOnClickListener(v -> {
            Toast.makeText(getContext(), "This feature is under maintenance", Toast.LENGTH_SHORT).show();
        });

        return binding.getRoot();
    }

    private void filterUser(String s) {
        data = FirebaseDatabase.getInstance().getReference("Users");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (String id : chatList) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        User otherUser = data.getValue(User.class);
                        assert otherUser != null;
                        if (otherUser.getId().equals(id)&&otherUser.getUsername().toLowerCase().contains(s.toLowerCase())) {
                            listUser.add(0,otherUser);
                        }
                    }
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                ListChatsAdapter adapter = new ListChatsAdapter(listUser, getContext());
                adapter.setiOnClickChatItem(new IOnClickChatItem() {
                    @Override
                    public void IOnClickItem(User other_user) {
                        Fragment fragment = ChatFragment.newInstance(other_user, user.getImage_url());
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.layout_chat, fragment).addToBackStack(null).commit();
                        // Toast.makeText(getContext(), user.toString(), Toast.LENGTH_SHORT).show();
                    }

                });
                binding.rcvUsers.setLayoutManager(layoutManager);
                binding.rcvUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error load users", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCurrentUserInfor() {
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {

                    if (getContext() != null) {

                        Glide.with(getContext()).load(user.getImage_url()).into(binding.imgAvatar);
                    }
                    binding.tvUsername.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loaf Use Information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllUser() {
        data = FirebaseDatabase.getInstance().getReference("Users");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (String id : chatList) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        User otherUser = data.getValue(User.class);
                        assert otherUser != null;
                        if (otherUser.getId().equals(id)) {
                            listUser.add(0,otherUser);
                        }
                    }
                }

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
                ListChatsAdapter adapter = new ListChatsAdapter(listUser, getContext());
                adapter.setiOnClickChatItem(new IOnClickChatItem() {
                    @Override
                    public void IOnClickItem(User other_user) {
                        Fragment fragment = ChatFragment.newInstance(other_user, user.getImage_url());
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.layout_chat, fragment).addToBackStack(null).commit();
                        // Toast.makeText(getContext(), user.toString(), Toast.LENGTH_SHORT).show();
                    }

                });
                binding.rcvUsers.setLayoutManager(layoutManager);
                binding.rcvUsers.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error load users", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getAllMessage() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        data = FirebaseDatabase.getInstance().getReference("Chats");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getSenderId().equals(firebaseUser.getUid())) {
                        chatList.remove(message.getReceiverId());
                        chatList.add(message.getReceiverId());
                    } else if (message.getReceiverId().equals(firebaseUser.getUid())) {
                        chatList.remove(message.getSenderId());
                        chatList.add(message.getSenderId());
                    }
                }
                getAllUser();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
