package com.vishugahlot.kcians;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import soup.neumorphism.NeumorphButton;

public class LoginActivity extends AppCompatActivity {

    private NeumorphButton signupButton;
    private LinearLayout layout_main;
    private Animation Animation_fadein,  Animation_rotate;

    private View img_logo_login, img_Botttom_login, text_logo_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        Utils.blackIconStatusBar(LoginActivity.this, R.color.light_Background);

        //declaring the animation effect
//        Animation effects are located in res > anim folder
        Animation_rotate= AnimationUtils.loadAnimation(LoginActivity.this, R.anim.rotate);
        Animation_fadein = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_in);

        layout_main= findViewById(R.id.layout_main);
        signupButton = findViewById(R.id.signup_button);
        img_logo_login= findViewById(R.id.img_logo_login);
        img_Botttom_login = findViewById(R.id.img_Bottom_login);
        text_logo_login = findViewById(R.id.text_logo_login);

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


//        signup button intent
        signupButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent sharedIntent = new Intent(LoginActivity.this, SignupActivity.class);
                ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this,
                        Pair.create(img_logo_login, "logo"),
                        Pair.create(img_Botttom_login, "img_tree"),
                        Pair.create(text_logo_login, "logo_text"));
                startActivity(sharedIntent);
            }
        });
    }
}