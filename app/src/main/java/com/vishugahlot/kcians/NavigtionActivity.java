package com.vishugahlot.kcians;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class NavigtionActivity extends AppCompatActivity {
    private View view;
    private BottomNavigationView bottomNavigationView;
    ImageView Profile_image;
    //ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView headerImage;
    TextView headertext;
    //drawer_layout
    FloatingActionButton fab;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigtion);
        Utils.blackIconStatusBar(NavigtionActivity.this, R.color.light_Background);

        fab=findViewById(R.id.fab_btn);
        fab.setImageResource(R.drawable.google);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.google));

        //drawer layout
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigation_view);
        navigationView.bringToFront();
        //headerImage=navigationView.getHeaderView(0).findViewById(R.id.imageViewofmenu);
        //headertext=navigationView.getHeaderView(0).findViewById(R.id.textviewofmenu);

        //finding the id of bottom navigation
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        Utils.blackIconStatusBar(NavigtionActivity.this , R.color.light_Background);
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

        Profile_image=findViewById(R.id.profile_image);
        Profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }




}
