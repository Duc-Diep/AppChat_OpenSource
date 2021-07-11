package group1.appchat_opensource.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import group1.appchat_opensource.R;
import group1.appchat_opensource.adapters.ListChatsAdapter;
import group1.appchat_opensource.databinding.ListchatFragmentBinding;
import group1.appchat_opensource.events.IOnClickChatItem;
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
    User user;

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
        data = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        getCurrentUserInfor();
        getAllUser();

//        binding.imgAvatar.setOnClickListener(v -> {
//            Fragment fragment = UserInforFragment.newInstance(user);
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.layout_chat, fragment).addToBackStack(null).commit();
//        });


        return binding.getRoot();
    }

    private void getCurrentUserInfor() {
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
                if (user != null) {
                    String userImagelink = user.getImage_url();
                    String username = user.getUsername();
                    if (getContext()!=null) {
                        if (userImagelink.equalsIgnoreCase("")){
                            Glide.with(getContext()).load(IMG_LINK_MALE_DEFAULT).into(binding.imgAvatar);
                        }else
                            Glide.with(getContext()).load(userImagelink).into(binding.imgAvatar);
                    }
                    binding.tvUsername.setText(username);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loaf Use Information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAllUser() {
        listUser = new ArrayList<>();
        data = FirebaseDatabase.getInstance().getReference("Users");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if(user!=null&&!user.getId().equals(userId)){
                        listUser.add(user);
                    }
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
                ListChatsAdapter adapter = new ListChatsAdapter(listUser,getContext());
                adapter.setiOnClickChatItem(new IOnClickChatItem() {
                    @Override
                    public void IOnClickItem(User user) {
//                        Fragment fragment = ChatFragment.newInstance(user,userImagelink);
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_right).replace(R.id.layout_chat,fragment).addToBackStack(null).commit();
                        Toast.makeText(getContext(), user.toString(), Toast.LENGTH_SHORT).show();
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

}
