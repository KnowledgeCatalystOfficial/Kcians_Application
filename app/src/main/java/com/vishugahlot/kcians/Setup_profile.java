package com.vishugahlot.kcians;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Setup_profile extends AppCompatActivity {

    TextView setup_text,dreams_text;
    EditText name,phone_no;
    ImageView profile_image;
    CardView submit,loading;
    //////

    int i=0,k=0;
    ///////
    String name_s,phone_no_s,uid_s,profile_url,e_mail;
    GoogleSignInAccount account;


    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        /////////////////////



        /////////////////////
        setup_text = findViewById(R.id.setup_text);
        name = findViewById(R.id.name);
        profile_image = findViewById(R.id.profile_image);
        phone_no = findViewById(R.id.phone_no);

        submit = findViewById(R.id.submit);
        account = GoogleSignIn.getLastSignedInAccount(Setup_profile.this);
        profile_url=String.valueOf(account.getPhotoUrl());
        setup_text.setText("Hello, " + account.getDisplayName() + " , some more information is needed to completely set you up");
        name.setText(account.getDisplayName());
        Picasso.get().load(String.valueOf(account.getPhotoUrl())).into(profile_image);
        ////
        ////
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!name.getText().toString().isEmpty())
                {
                    if(!phone_no.getText().toString().isEmpty())
                    {
                        if(phone_no.getText().toString().length()==10)
                        {
                            name_s=name.getText().toString();
                            phone_no_s=phone_no.getText().toString();
                            uid_s=FirebaseAuth.getInstance().getUid();
                            e_mail=account.getEmail();
                            saveuser(name_s,phone_no_s,uid_s,profile_url,e_mail);


                        }
                        else
                        {
                            phone_no.setError("Phone No should be of 10 digits");
                        }

                    }
                    else
                    {
                        phone_no.setError("Enter Phone No");
                    }
                }
                else
                {
                    name.setError("Please Enter Name");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }


    private void saveuser(String name_s, String phone_no_s, String uid_s, String profile_url, String e_mail) {

        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("UID").setValue(uid_s);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("Name").setValue(name_s);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("E-mail").setValue(e_mail);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("Profile_url").setValue(profile_url);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("Phone no").setValue(phone_no_s);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("Total Posts").setValue(0);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("Shares").setValue(0);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("Followers").setValue(0);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("Following").setValue(0);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("Likes").setValue(0);
        FirebaseDatabase.getInstance().getReference().child("users").child(uid_s).child("Interests").child(String.valueOf(0)).setValue("All");




        Intent intent=new Intent(Setup_profile.this,NavigtionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

}