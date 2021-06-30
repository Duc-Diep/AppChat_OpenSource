package group1.appchat_opensource.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

import group1.appchat_opensource.R;
import group1.appchat_opensource.databinding.UserInforFragmentBinding;

public class UserInforFragment extends Fragment {
    UserInforFragmentBinding binding;
    public static UserInforFragment newInstance() {

        Bundle args = new Bundle();

        UserInforFragment fragment = new UserInforFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.user_infor_fragment,container,false );
        return binding.getRoot();
    }
}
