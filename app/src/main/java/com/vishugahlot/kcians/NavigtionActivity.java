package com.vishugahlot.kcians;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishugahlot.kcians.databinding.ActivityMainBinding;

public class NavigtionActivity extends AppCompatActivity {
    private View view;
    private BottomNavigationView bottomNavigationView;
    //ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigtion);
        Utils.blackIconStatusBar(NavigtionActivity.this, R.color.light_Background);

        //finding the id of bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Utils.blackIconStatusBar(NavigtionActivity.this , R.color.bu);
        replaceFragment(new HomeFragment());
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.homebutton:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.cal:
                    replaceFragment(new CalenderFragment());
                    break;
                case R.id.notification:
                    replaceFragment(new NotificationFragment());
                    break;
                case R.id.community:
                    replaceFragment(new CommunityFragment());
                    break;
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}