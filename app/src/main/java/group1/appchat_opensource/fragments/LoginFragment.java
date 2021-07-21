package group1.appchat_opensource.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import group1.appchat_opensource.R;
import group1.appchat_opensource.activity.ChatActivity;
import group1.appchat_opensource.databinding.LoginFragmentBinding;

public class LoginFragment extends Fragment {
    LoginFragmentBinding binding;
    FirebaseAuth mAuth;

    public static LoginFragment newInstance() {

        Bundle args = new Bundle();

        LoginFragment fragment = new LoginFragment();
        fragment.setArguments( args );
        return fragment;
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.login_fragment, container, false );
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null)   binding.edtEmail.setText( user.getEmail() );
        binding.btnRegis.setOnClickListener( v -> {
            Fragment fragment = RegisterFragment.newInstance();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations( R.anim.slide_in_right, R.anim.slide_out_right ).replace( R.id.layout_login, fragment ).addToBackStack( null ).commit();
        } );
        mAuth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener( v -> signIn() );
        binding.txtForgot.setOnClickListener( v -> {
            Fragment fragment = ForgotPassFragment.newInstance();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations( R.anim.slide_in_right, R.anim.slide_out_right ).replace( R.id.layout_login, fragment ).addToBackStack( null ).commit();
        } );

        return binding.getRoot();
    }

    public void signIn() {
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPass.getText().toString().trim();
        if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches()) {
            binding.edtEmail.setError( "Email is not correct!" );
            binding.edtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.edtPass.setError( "Password is not empty!" );
            binding.edtPass.requestFocus();
            return;
        }
        binding.progressBar.setVisibility( View.VISIBLE );
        mAuth.signInWithEmailAndPassword( email, password ).addOnCompleteListener( task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText( getContext(), "Login success", Toast.LENGTH_SHORT ).show();
                if (binding.checkLogin.isChecked()){
                    FirebaseAuth.getInstance().updateCurrentUser( user );
                }
                binding.progressBar.setVisibility( View.GONE );
                startActivity( new Intent(getContext(),ChatActivity.class) );
            } else {
                Toast.makeText( getContext(), "Login failed!", Toast.LENGTH_SHORT ).show();
                binding.progressBar.setVisibility( View.GONE );
            }
        } );
    }
    @Override
    public void onStart() {
        super.onStart();
        //EventBus.getDefault().register(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        //EventBus.getDefault().unregister(getContext());
    }
}
