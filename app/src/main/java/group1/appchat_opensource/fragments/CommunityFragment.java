package group1.appchat_opensource.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class CommunityFragment extends Fragment {
    public static CommunityFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CommunityFragment fragment = new CommunityFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
