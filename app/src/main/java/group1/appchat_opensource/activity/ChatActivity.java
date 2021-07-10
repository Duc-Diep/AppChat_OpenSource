package group1.appchat_opensource.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import group1.appchat_opensource.R;
import group1.appchat_opensource.fragments.HomeFragment;

import static group1.appchat_opensource.configs.Constant.STATUS_OFF;
import static group1.appchat_opensource.configs.Constant.STATUS_ON;

public class ChatActivity extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    DatabaseReference data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        getFragment(HomeFragment.newInstance());
        setStatus(STATUS_ON);
    }
    public void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_chat, fragment).commit();
    }
    public void setStatus(String status){
        try {
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            mAuth = FirebaseAuth.getInstance();
            data = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
            HashMap<String,Object> hm = new HashMap<>();
            hm.put("status",status);
            data.updateChildren(hm);
        }catch (Exception e){

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatus(STATUS_ON);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setStatus(STATUS_OFF);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setStatus(STATUS_OFF);
    }
}