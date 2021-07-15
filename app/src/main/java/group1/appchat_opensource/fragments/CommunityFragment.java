package group1.appchat_opensource.fragments;

import android.os.Bundle;
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
import group1.appchat_opensource.adapters.CommunityAdapter;
import group1.appchat_opensource.adapters.ListChatsAdapter;
import group1.appchat_opensource.databinding.CommunityFragmentBinding;
import group1.appchat_opensource.events.IOnClickChatItem;
import group1.appchat_opensource.objects.User;

import static group1.appchat_opensource.configs.Constant.IMG_LINK_GROUP;
import static group1.appchat_opensource.configs.Constant.IMG_LINK_MALE_DEFAULT;
import static group1.appchat_opensource.configs.Constant.STATUS_ON;

public class CommunityFragment extends Fragment {
    CommunityFragmentBinding binding;
    FirebaseUser firebaseUser;
    DatabaseReference data;
    String userId;
    List<User> listUser;
    User user;
    public static CommunityFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CommunityFragment fragment = new CommunityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.community_fragment,container,false );
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        data = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error loaf Use Information", Toast.LENGTH_SHORT).show();
            }
        });
        getAllUser();

        return binding.getRoot();
    }
    public void getAllUser() {
        listUser = new ArrayList<>();
        data = FirebaseDatabase.getInstance().getReference("Users");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUser.clear();
                listUser.add(new User("","",IMG_LINK_GROUP,"Community","Online"));
                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if(user!=null&&!user.getId().equals(userId)){
                        listUser.add(user);
                    }
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);

                CommunityAdapter adapter = new CommunityAdapter(listUser,getContext());
                adapter.setiOnClickChatItem(new IOnClickChatItem() {
                    @Override
                    public void IOnClickItem(User user) {
                        Fragment fragment = ChatFragment.newInstance(user,user.getImage_url());
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_right).replace(R.id.layout_chat,fragment).addToBackStack(null).commit();
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
}
