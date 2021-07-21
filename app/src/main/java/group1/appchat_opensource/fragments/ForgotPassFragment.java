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

import org.jetbrains.annotations.NotNull;

import group1.appchat_opensource.R;
import group1.appchat_opensource.databinding.ForgotpassFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassFragment extends Fragment {
    ForgotpassFragmentBinding binding;
    FirebaseAuth mAthu;
    public static ForgotPassFragment newInstance() {

        Bundle args = new Bundle();

        ForgotPassFragment fragment = new ForgotPassFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate( inflater, R.layout.forgotpass_fragment,container,false );
	binding.btnResetPass.setOnClickListener(v->resetPassword());
        mAthu = FirebaseAuth.getInstance();
        return binding.getRoot();
    }
	public void resetPassword(){
        String email = binding.edtEmail.getText().toString().trim();
        if (email.isEmpty()){
            binding.edtEmail.setError("Email is required");
            binding.edtEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.edtEmail.setError("Incorrect format email");
            binding.edtEmail.requestFocus();
            return;
        }
        mAthu.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Please check your email to reset password", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Try again! Your email has some problem", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
