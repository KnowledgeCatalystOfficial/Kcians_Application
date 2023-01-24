package com.vishugahlot.kcians;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private View img_logo, img_Botttom, tv_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.blackIconStatusBar(MainActivity.this , R.color.light_Background);

        tv_name = findViewById(R.id.tv_name);
        img_Botttom = findViewById(R.id.img_Bottom);
        img_logo = findViewById(R.id.img_logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                        Pair.create(img_logo, "logo"),
                        Pair.create(img_Botttom, "img_tree"),
                        Pair.create(tv_name, "logo_text"));
                startActivity(intent, options.toBundle());
            }
        }, 3000);
    }
   }