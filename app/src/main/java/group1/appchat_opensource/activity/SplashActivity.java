package group1.appchat_opensource.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import group1.appchat_opensource.R;
import group1.appchat_opensource.utils.AppUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        checkNetwork();
    }

    private void checkNetwork() {
        if (AppUtils.isAvailableNetwork(this)){
            new Handler().postDelayed( () -> {
                if (checkUser()){
                    Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }

            },2500);

        }else{
            Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean checkUser(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null){
            return true;
        }
        return false;
    }

}