package com.vishugahlot.kcians;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import soup.neumorphism.NeumorphButton;

public class LoginActivity extends AppCompatActivity {

    private NeumorphButton signupButton, loginButton;
    private LinearLayout layout_main;
    ConstraintLayout sign_in_google;
    private Animation Animation_fadein,  Animation_rotate;


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
        FirebaseUser currentuser=mauth.getCurrentUser();
        if(currentuser!=null)
        {
            Intent i=new Intent(LoginActivity.this, NavigtionActivity.class);
            startActivity(i);
        }

    }

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
        loginButton = findViewById(R.id.login_button);

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
        //normal login
        email=findViewById(R.id.et_email);
        password=findViewById(R.id.et_pass);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty())
                {

                    email.setError("Email Empty");
                }
                else
                {
                    if(password.getText().toString().isEmpty())
                    {
                        password.setError("Password Empty");

                    }
                    else
                    {
                        signinuser(email.getText().toString(),password.getText().toString());
                    }
                }
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

//        Login button onclick

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
                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                    GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                    Toast.makeText(LoginActivity.this,account.getDisplayName().toString() , Toast.LENGTH_SHORT).show();


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
    private void signinuser(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}