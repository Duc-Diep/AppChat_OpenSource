package group1.appchat_opensource.fragments;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import group1.appchat_opensource.R;
import group1.appchat_opensource.databinding.LoginFragmentBinding;

public class LoginFragment extends Fragment {
    LoginFragmentBinding binding;
    FirebaseAuth mAuth;
    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.login_fragment,container,false );
        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }
    public void signIn(){
        String email = "";
        String password = "";
        if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches()){
            return;
        }
        if (password.isEmpty()){
            return;
        }

    }
}
