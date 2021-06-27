package group1.appchat_opensource.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserInforFragment extends Fragment {
    public static UserInforFragment newInstance() {

        Bundle args = new Bundle();

        UserInforFragment fragment = new UserInforFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
