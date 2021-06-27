package group1.appchat_opensource.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

import group1.appchat_opensource.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Button btnOK = findViewById( R.id.btnOK );
        btnOK.setOnClickListener( v -> {
            Toast.makeText( this, "Hello", Toast.LENGTH_SHORT ).show();
        } );
    }
}