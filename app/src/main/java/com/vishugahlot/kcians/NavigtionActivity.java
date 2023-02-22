package com.vishugahlot.kcians;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NavigtionActivity extends AppCompatActivity  implements  NavigationView.OnNavigationItemSelectedListener{
    private View view;
    private BottomNavigationView bottomNavigationView;
    ImageView Profile_image,addpost;

    //ActivityMainBinding binding;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView headerImage;
    TextView headertext,username,TextLogoName,following;
    //drawer_layout
    FloatingActionButton fab;

    private Animation Bounce;

    @Override
    protected void onStart() {
        super.onStart();
        addpost.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        addpost.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        addpost.setVisibility(View.VISIBLE);

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigtion);
        Utils.blackIconStatusBar(NavigtionActivity.this, R.color.light_Background);

        fab=findViewById(R.id.fab_btn);
        fab.setImageResource(R.drawable.google);
        fab.setImageDrawable(getResources().getDrawable(R.drawable.google));
        addpost=findViewById(R.id.search_frag);
        addpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NavigtionActivity.this,Add_post.class);
                startActivity(intent);
            }
        });

        //finding ID
        Bounce = AnimationUtils.loadAnimation(NavigtionActivity.this,R.anim.bounce);
        TextLogoName = findViewById(R.id.textView);

        //drawer layout
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigation_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(NavigtionActivity.this);


        /////setting up details
        headerImage=navigationView.getHeaderView(0).findViewById(R.id.profilepic);
        headertext=navigationView.getHeaderView(0).findViewById(R.id.name);
        username=navigationView.getHeaderView(0).findViewById(R.id.username);
        following=navigationView.getHeaderView(0).findViewById(R.id.following);

        //dekh liyo bhai                             !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        headertext.setText(account.getDisplayName());
        username.setText(account.getEmail());
        Picasso.get().load(account.getPhotoUrl()).into(headerImage);
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int len= (int) snapshot.child("Category").getChildrenCount();
                following.setText(String.valueOf(len));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



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
        Picasso.get().load(account.getPhotoUrl()).into(Profile_image);
        Profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

//        logo name animation
        TextLogoName.setAnimation(Bounce);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        navigationView.setSelected(true);
        navigationView.setCheckedItem(id);
        //Closing drawer on item click
        // drawerLayout.closeDrawers();

        if(id==R.id.nav_home)
        {



        }
        if(id==R.id.synch)
        {
            replaceFragment(new HomeFragment());

        }
        if(id==R.id.nav_login)
        {

            logout();
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.nav_share)
        {

        }
        if(id==R.id.settings)
        {

            Intent intent=new Intent(NavigtionActivity.this,SettingsActivity.class);
            startActivity(intent);
        }


        drawerLayout.closeDrawer(GravityCompat.START);


        return true;

    }
   private void logout() {
        try {
            GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(NavigtionActivity.this, MainActivity.class);
            startActivity(i);
        }
        catch (Exception e)
        {
            Toast.makeText(NavigtionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }





}
