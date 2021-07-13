package group1.appchat_opensource.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import group1.appchat_opensource.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordDialog extends Dialog {
    TextInputEditText edtCurrentPass,edtNewPass,edtConfirmPass;
    Button btnChangePass,btnCancel;
    ProgressBar progressBar;
    FirebaseUser firebaseUser;
    public ChangePasswordDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.diaglog_change_pass);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mapping();
        btnChangePass.setOnClickListener(v -> {
            changePass();
        });
        btnCancel.setOnClickListener(v -> {
            dismiss();
        });
    }

    private void changePass(){
        String currentPass = edtCurrentPass.getText().toString().trim();
        String newPass = edtNewPass.getText().toString().trim();
        String confirmPass = edtConfirmPass.getText().toString().trim();

        if (currentPass.isEmpty()){
            edtCurrentPass.setError("Password must not empty");
            edtCurrentPass.requestFocus();
            return;
        }
        if (newPass.length()<6){
            edtNewPass.setError("Password length >6");
            edtNewPass.requestFocus();
            return;
        }
        if (!confirmPass.equals(newPass)){
            edtConfirmPass.setError("confirm password is not same as new password");
            edtConfirmPass.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential authCredential = EmailAuthProvider.getCredential(firebaseUser.getEmail(),currentPass);
        firebaseUser.reauthenticate(authCredential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                firebaseUser.updatePassword(newPass).addOnCompleteListener(task1->{
                    if (task1.isSuccessful()){
                        Toast.makeText(getContext(), "Change password success", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        cancel();
                    }else{
                        Toast.makeText(getContext(), "Change password unsuccess", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }else{
                Toast.makeText(getContext(), "Current password is uncorrect", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
//        firebaseUser.updatePassword("Hihi");
    }
    private void mapping() {
        edtCurrentPass = findViewById(R.id.edtCurrentPass);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtConfirmPass = findViewById(R.id.edtConfirmPass);
        btnChangePass = findViewById(R.id.btnChangePass);
        btnCancel = findViewById(R.id.btnCancel);
        progressBar = findViewById(R.id.processBar);
    }

}
