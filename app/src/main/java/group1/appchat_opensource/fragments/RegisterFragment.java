package group1.appchat_opensource.fragments;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import group1.appchat_opensource.R;
import group1.appchat_opensource.configs.Constant;
import group1.appchat_opensource.databinding.RegisterFragmentBinding;
import group1.appchat_opensource.objects.User;

public class RegisterFragment extends Fragment {
    RegisterFragmentBinding binding;
    FirebaseAuth mAth;

    public static RegisterFragment newInstance() {

        Bundle args = new Bundle();

        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments( args );
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.register_fragment, container, false );
        mAth = FirebaseAuth.getInstance();
        binding.btnRegister.setOnClickListener( v -> registerUser() );
        binding.txtLogin.setOnClickListener( v -> {
            Fragment fragment = LoginFragment.newInstance();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations( R.anim.slide_in_right, R.anim.slide_out_right ).replace( R.id.layout_login, fragment ).addToBackStack( null ).commit();
        } );
        return binding.getRoot();
    }

    public void registerUser() {
        String email = binding.edtEmail.getText().toString().trim();
        String pass = binding.edtPass.getText().toString().trim();
        String repass = binding.edtRePass.getText().toString().trim();
        String username = binding.edtUsername.getText().toString().trim();
        String gender = binding.rdBoy.isChecked() ? "Nam" : "Nữ";
        String urlImageDefault = binding.rdBoy.isChecked() ? Constant.IMG_LINK_MALE_DEFAULT : Constant.IMG_LINK_FEMALE_DEFAULT;
        if (!Patterns.EMAIL_ADDRESS.matcher( email ).matches()) {
            binding.edtEmail.setError( "Email is not correct!" );
            binding.edtEmail.requestFocus();
            return;
        }
        if (pass.length() < 6) {
            binding.edtPass.setError( "Password length required more 6 characters" );
            binding.edtPass.requestFocus();
            return;
        }
        if (!repass.equals( pass )) {
            binding.edtRePass.setError( "Confirm Password is not equal password" );
            binding.edtRePass.requestFocus();
            return;
        }
        if (username.isEmpty()) {
            binding.edtUsername.setError( "User name is not empty!" );
            binding.edtUsername.requestFocus();
            return;
        }
        binding.progessBar.setVisibility( View.VISIBLE );
        mAth.createUserWithEmailAndPassword( email, pass ).addOnCompleteListener( task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mAth.getCurrentUser();
                User user = new User( gender, firebaseUser.getUid(), urlImageDefault, username, "Online" );
                FirebaseDatabase.getInstance().getReference( "Users" ).child( FirebaseAuth.getInstance().getCurrentUser().getUid() ).setValue( user ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText( getContext(), "User has been registerd successfully", Toast.LENGTH_SHORT ).show();
                            binding.progessBar.setVisibility( View.GONE );
                            Fragment fragment = LoginFragment.newInstance();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().replace( R.id.layout_login, fragment ).commit();
                        } else {
                            Toast.makeText( getContext(), "Failed to register! Try again", Toast.LENGTH_SHORT ).show();
                            binding.progessBar.setVisibility( View.GONE );
                        }
                    }
                } );
            } else {
                Toast.makeText( getContext(), "Failed to register", Toast.LENGTH_SHORT ).show();
                binding.progessBar.setVisibility( View.GONE );
            }
        } );
    }
}
