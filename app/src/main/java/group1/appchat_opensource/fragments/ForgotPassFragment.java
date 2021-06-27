package group1.appchat_opensource.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ForgotPassFragment extends Fragment {
    public static ForgotPassFragment newInstance() {

        Bundle args = new Bundle();

        ForgotPassFragment fragment = new ForgotPassFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
