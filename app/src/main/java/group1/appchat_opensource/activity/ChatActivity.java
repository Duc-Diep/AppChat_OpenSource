package group1.appchat_opensource.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import group1.appchat_opensource.R;
import group1.appchat_opensource.fragments.HomeFragment;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
        getFragment(HomeFragment.newInstance());

    }
    public void getFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.layout_chat, fragment).commit();
    }
}