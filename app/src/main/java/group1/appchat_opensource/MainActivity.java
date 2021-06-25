package group1.appchat_opensource;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Button btnOK = findViewById( R.id.btnOK );
        btnOK.setOnClickListener( v -> {
            Toast.makeText( this, "Click OK", Toast.LENGTH_SHORT ).show();
        } );
    }
}