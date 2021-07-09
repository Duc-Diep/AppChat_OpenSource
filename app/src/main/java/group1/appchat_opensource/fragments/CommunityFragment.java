package group1.appchat_opensource.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import group1.appchat_opensource.R;
import group1.appchat_opensource.databinding.CommunityFragmentBinding;

public class CommunityFragment extends Fragment {
    CommunityFragmentBinding binding;
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
        return binding.getRoot();
    }
}
