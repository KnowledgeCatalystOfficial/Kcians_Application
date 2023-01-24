package com.vishugahlot.kcians;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.firebase.auth.GoogleAuthProvider;

import soup.neumorphism.NeumorphButton;

public class SignupActivity extends AppCompatActivity {

    private LinearLayout layout_signup;
    private Animation Animation_fadein,  Animation_rotate;
    private View img_logo_signup;
    private NeumorphButton back_login;

    ////signup
    ConstraintLayout sign_in_google;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseAuth mauth;


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

        ////signup with google
        sign_in_google=findViewById(R.id.google_signin_btn);
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc= GoogleSignIn.getClient(SignupActivity.this,gso);
        mauth= FirebaseAuth.getInstance();
        sign_in_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginwithgoogle();
            }
        });

    }

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

    private void firebaseloginwithgoogle(String idToken) {
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        mauth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignupActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(SignupActivity.this);
                    Toast.makeText(SignupActivity.this,account.getDisplayName().toString() , Toast.LENGTH_SHORT).show();


                }
                else
                {
                    Toast.makeText(SignupActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}