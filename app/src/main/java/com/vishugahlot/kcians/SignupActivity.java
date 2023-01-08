package com.vishugahlot.kcians;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import soup.neumorphism.NeumorphButton;

public class SignupActivity extends AppCompatActivity {

    private LinearLayout layout_signup;
    private Animation Animation_fadein,  Animation_rotate;
    private View img_logo_signup;
    private NeumorphButton back_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Utils.blackIconStatusBar(SignupActivity.this, R.color.light_Background);

        img_logo_signup= findViewById(R.id.img_logo_signup);
        back_login=findViewById(R.id.back_login);
        layout_signup = findViewById(R.id.layout_signup);

        Animation_fadein = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.fade_in);
        Animation_rotate= AnimationUtils.loadAnimation(SignupActivity.this, R.anim.rotate);

        img_logo_signup.setAnimation(Animation_rotate);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                layout_signup.setVisibility(View.VISIBLE);
                layout_signup.setAnimation(Animation_fadein);
            }
        }, 1000);

        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}