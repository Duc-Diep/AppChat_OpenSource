package group1.appchat_opensource.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

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
        if (AppUtils.isAvailableNetword(this)){
            new Handler().postDelayed( () -> {
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            },2500);

        }else{
            Toast.makeText(this, "No network", Toast.LENGTH_SHORT).show();
        }
    }

}