package group1.appchat_opensource.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.jetbrains.annotations.NotNull;

import group1.appchat_opensource.R;
import group1.appchat_opensource.databinding.HomeFragmentBinding;

public class HomeFragment extends Fragment {
    HomeFragmentBinding binding;
    public static HomeFragment newInstance() {

        Bundle args = new Bundle();

        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.home_fragment,container,false );
        createNavigation();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_chat,ListChatFragment.newInstance());

        return binding.getRoot();
    }

    private void createNavigation() {
        //create item
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Chats", R.drawable.chat, R.color.black);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Community", R.drawable.user_group, R.color.black);
        //add
        binding.bottomNavigation.addItem(item1);
        binding.bottomNavigation.addItem(item2);
        //color
        binding.bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
        // Use colored navigation with circle reveal effect

        binding.bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);

        // Set current item programmatically
        binding.bottomNavigation.setCurrentItem(1);
        binding.bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        // Change colors
        binding.bottomNavigation.setAccentColor(Color.BLUE);
        binding.bottomNavigation.setInactiveColor(Color.GRAY);
        // Add or remove notification for each item
        binding.bottomNavigation.setNotification("1", 1);


        binding.bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            Toast.makeText(getContext(), position+"", Toast.LENGTH_SHORT).show();
            if (position==0){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_chat,ListChatFragment.newInstance());
            }else{
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layout_chat,CommunityFragment.newInstance());
            }


            return true;
        });
        binding.bottomNavigation.setOnNavigationPositionListener(y -> {
            // Manage the new y position
        });

    }
}
