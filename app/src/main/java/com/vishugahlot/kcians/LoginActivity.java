package com.vishugahlot.kcians;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;
import soup.neumorphism.NeumorphButton;

public class LoginActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    GifImageView SlideHint;
    ArrayList<ViewPagerItem> viewPagerItemArrayList;
    private NeumorphButton NextButton;
    private LinearLayout layout_main;
    ConstraintLayout sign_in_google;
    private Animation Animation_fadein,  Animation_rotate, Animation_fadeout;


    ////google sign in necessaties

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseAuth mauth;

    //normal login
    EditText email,password;
   ////////////////////

    @Override
    public void onStart() {
        super.onStart();
        try {
            GoogleSignIn.getClient(getApplicationContext(), GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
            FirebaseAuth.getInstance().signOut();
        }
        catch (Exception e){}
    }

    private View img_logo_login, img_Botttom_login, text_logo_login;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.blackIconStatusBar(LoginActivity.this, R.color.light_Background);


        //declaring the animation effect
//        Animation effects are located in res > anim folder
        Animation_rotate= AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotate);
        Animation_fadein = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_in);
        Animation_fadeout = AnimationUtils.loadAnimation(LoginActivity.this,R.anim.fade_out);


//        finding all IDs
        layout_main= findViewById(R.id.layout_main);
        NextButton = findViewById(R.id.next_but);
        img_logo_login= findViewById(R.id.img_logo_login);
        text_logo_login = findViewById(R.id.text_logo_login);
        SlideHint = findViewById(R.id.slideHint);

        ///sign in with google
        sign_in_google=findViewById(R.id.google_signin_btn);
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc= GoogleSignIn.getClient(LoginActivity.this,gso);
        mauth=FirebaseAuth.getInstance();
        sign_in_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginwithgoogle();
            }
        });



        //rotating the logo here
        img_logo_login.setAnimation(Animation_rotate);

//        layout visibility effect here with fadein animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_main.setVisibility(View.VISIBLE);
                layout_main.setAnimation(Animation_fadein);
            }
        }, 1000);


//        slide hint gif animation fade out effect

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SlideHint.setVisibility(View.INVISIBLE);
                SlideHint.setAnimation(Animation_fadeout);
            }
        }, 2500);

//        next button
//       !!!!!!!!!!!!!!!!!!!!!!!!!
//        next button ends


//        view pager function start

        viewPager2 = findViewById(R.id.viewpager);
        int[] images = {R.drawable.walkone,R.drawable.walktwo};

        viewPagerItemArrayList = new ArrayList<>();

        for (int image : images) {

            ViewPagerItem viewPagerItem = new ViewPagerItem(image);
            viewPagerItemArrayList.add(viewPagerItem);

        }

        VPAdapter vpAdapter = new VPAdapter(viewPagerItemArrayList);

        viewPager2.setAdapter(vpAdapter);
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(2);
        viewPager2.getChildAt(0).setOverScrollMode(View.OVER_SCROLL_NEVER);

//     view pager function end here

//        next button function
        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


//    login function
    private void loginwithgoogle() {
        Intent signinintent=gsc.getSignInIntent();
        startActivityForResult(signinintent,100);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseloginwithgoogle(account.getIdToken());

            }
            catch (ApiException e)
            {

            }
        }
    }

    //login google activity
    private void firebaseloginwithgoogle(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        mauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("users").child(mauth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);

                            } else {
                                Intent i = new Intent(LoginActivity.this, Setup_profile.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
                else
                {
                    Toast.makeText(LoginActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



}