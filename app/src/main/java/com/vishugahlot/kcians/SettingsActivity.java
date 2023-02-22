package com.vishugahlot.kcians;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {
    ImageView goback,profile_image;
    TextView phone_no,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        goback=findViewById(R.id.goback);
        name=findViewById(R.id.name);
        phone_no=findViewById(R.id.phone_no);
        profile_image=findViewById(R.id.profile_image);
        GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(SettingsActivity.this);
        Picasso.get().load(account.getPhotoUrl()).into(profile_image);
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String names=snapshot.child("users").child(FirebaseAuth.getInstance().getUid()).child("Name").getValue(String.class);
               String phone_nos=snapshot.child("users").child(FirebaseAuth.getInstance().getUid()).child("Phone no").getValue(String.class);
               name.setText(names);
               phone_no.setText(phone_nos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SettingsActivity.this,NavigtionActivity.class);
                startActivity(intent);
            }
        });
    }
}